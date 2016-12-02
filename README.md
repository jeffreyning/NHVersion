# NHVersion
NHVersion类库

api接口需要分版本同时提出给使用方，通常移动端app的后台接口有这种版本化需求。如果每个接口都重新改类名将造成大量重复代码。
使用NHVersion类库可以解决代码重复问题，同时在没有完全一致的版本映射时，还可以根据提供的版本号找到最近似的版本进行调用。
NHVersion类库的开源代码请访问https://github.com/jeffreyning/NHVersion
下载jar请访问nhversion-1.0.jar
下载jar(支持spring)请访问nhversion-1.1.jar

在没有NHVersion类库的情况下，一般会用ifelse处理多版本判断，例如以下的代码
名为Calcu的接口通过ifelse判断v1_0_1 v_1_0_5 v1_1_0这3个版本如果需要增加新的版本，
则需要修改Calcu类内部ifelse逻辑，这样容易出错，而且每个版本都要重新编写。
public class NoNHVersionController {
	public static int calcu(int param,String version){
		if(version.equals("v1_0_1")){
			System.out.println("this is nonhversioin v1_0_1");
			return param+1;
		}else if(version.equals("v1_0_5")){
			System.out.println("this is nonhversion v1_0_5");
			return param+5;
		}else if(version.equals("v1_1_0")){
			System.out.println("this is nonhversion v1_1_0");
			return param+10;
		}
		System.out.println("this is nonhversion not catch");
		return 0;
	}
}

NHVersion类库将处理逻辑抽象成handler，通过map维护handler与version的映射关系。
增加新版本时只需实现新的handler对象并注入map中，不必修改接口主类的代码。而且可以支持版本号自动降级匹配。
比如V1_0_6没有完全相等handler，则会自动降级匹配V1_0_5进行调用。



每个不同版本需要编写handler
@ControllerMappingAnno(controllerName="NHVersionController",funcName="calcu",funcVer="v1_0_0")
注解的作用是配置handler与version的映射关系。如NHVersionController接口类，calcu方法对应，v1_0_0版本的handler对象CalcuCmd1
支持按照springbeanid加载cmd对象@ControllerMappingAnno(controllerName="NHVersionController",funcName="calcu",funcVer="v1_0_0" beanId="cmd1")

package com.jeffreyning.nhversion.demo.controller.cmd;
import java.util.HashMap;
import java.util.Map;

import com.jeffreyning.nhversion.common.version.cmd.IControllerCommand;
import com.jeffreyning.nhversion.common.version.mapping.ControllerMappingAnno;
//@Component("cmd1")
//@ControllerMappingAnno(controllerName="NHVersionController",funcName="calcu",funcVer="v1_0_0" beanId="cmd1")
@ControllerMappingAnno(controllerName="NHVersionController",funcName="calcu",funcVer="v1_0_0")
public class CalcuCmd1 extends IControllerCommand{

	@Override
	public Map execute(Map paramMap) {
		System.out.println("this is nhversion v1_0_0");
		int param=(Integer) paramMap.get("param");
		int ret=param+1;
		Map retMap=new HashMap();
		retMap.put("retInt", ret);
		return retMap;
	}
}


使用NHVersion类库后接口层改写为
CommonControllerMapping.execVersionMapping触发版本映射并调用找出的handler对象。
参数为接口类标识、方法标识，版本号，参数（map）
public class NHVersionController {
	public static int calcu(int param,String version){
		Map paramMap=new HashMap();
    	paramMap.put("version", version);
    	paramMap.put("param", param);
    	Map retMap=CommonControllerMapping.execVersionMapping("NHVersionController","calcu", version, paramMap);
    	Integer retInt=(Integer) retMap.get("retInt");
		return retInt;
	}
}

@ControllerMappingAnno维护的映射关系，通过自动扫描注入到map中
CommonControllerMapping.batchRegistryByPackage("com.jeffreyning.nhversion.demo.controller.cmd");
package com.jeffreyning.nhversion.demo;

import com.jeffreyning.nhversion.common.version.mapping.CommonControllerMapping;
import com.jeffreyning.nhversion.demo.controller.NoNHVersionController;
import com.jeffreyning.nhversion.demo.controller.NHVersionController;

public class Demo {

	public static void main(String[] args) throws Exception {
		CommonControllerMapping.batchRegistryByPackage("com.jeffreyning.nhversion.demo.controller.cmd");
		NoNHVersionController.calcu(1, "v1_0_1");
		NHVersionController.calcu(1,"v1_0_1");
		NoNHVersionController.calcu(1, "v1_0_6");
		NHVersionController.calcu(1,"v1_0_6");
	}

}

注意：版本号格式为vxx_xx_xx xx代表2位数字 如v01_0_10 v1_1_6都是正确的格式
如果需要加载spring中的cmd对象，需要配置在spring配置文件中添加<bean class="com.jeffreyning.nhversion.common.version.util.NHBeanUtil"></bean>
　　
