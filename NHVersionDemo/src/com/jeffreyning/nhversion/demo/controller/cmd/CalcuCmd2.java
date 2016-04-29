package com.jeffreyning.nhversion.demo.controller.cmd;

import java.util.HashMap;
import java.util.Map;

import com.jeffreyning.nhversion.common.version.cmd.IControllerCommand;
import com.jeffreyning.nhversion.common.version.mapping.ControllerMappingAnno;

@ControllerMappingAnno(controllerName="NHVersionController",funcName="calcu",funcVer="v1_0_5")
public class CalcuCmd2 extends IControllerCommand{

	@Override
	public Map execute(Map paramMap) {
		System.out.println("this is nhversion v1_0_5");
		int param=(Integer) paramMap.get("param");
		int ret=param+5;
		Map retMap=new HashMap();
		retMap.put("retInt", ret);
		return retMap;
	}

}
