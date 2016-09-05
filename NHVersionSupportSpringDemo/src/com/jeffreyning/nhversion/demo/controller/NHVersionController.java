package com.jeffreyning.nhversion.demo.controller;

import java.util.HashMap;
import java.util.Map;

import com.jeffreyning.nhversion.common.version.mapping.CommonControllerMapping;

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
