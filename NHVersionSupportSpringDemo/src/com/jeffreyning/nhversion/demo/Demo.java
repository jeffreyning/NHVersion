package com.jeffreyning.nhversion.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jeffreyning.nhversion.common.version.mapping.CommonControllerMapping;
import com.jeffreyning.nhversion.demo.controller.FrontVersionChangeController;
import com.jeffreyning.nhversion.demo.controller.NoNHVersionController;
import com.jeffreyning.nhversion.demo.controller.NHVersionController;

public class Demo {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new ClassPathXmlApplicationContext("applicationContext.xml");
		CommonControllerMapping.batchRegistryByPackage("com.jeffreyning.nhversion.demo.controller.cmd");
		NoNHVersionController.calcu(1, "v1_0_1");
		NHVersionController.calcu(1,"v1_0_1");
		NoNHVersionController.calcu(1, "v1_0_6");//传统ifelse无自动降级功能
		NHVersionController.calcu(1,"v1_0_6");//不存在106的实例，则自动降级为105的cmd实例
		
		//使用nhversion-1.3.0-RELEASE.jar时支持
		FrontVersionChangeController.calcu(1,"v3_1_0");//前台version 310映射为后台version 105
		FrontVersionChangeController.calcu(1,"v3_1_1");//前台version 311映射为后台version 105
		FrontVersionChangeController.calcu(1,"v3_2_0");//前台version 320映射为后台version 110
	}

}
