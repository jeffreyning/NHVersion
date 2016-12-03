package com.jeffreyning.nhversion.demo.controller;

import java.util.HashMap;
import java.util.Map;

import com.jeffreyning.nhversion.common.version.mapping.CommonControllerMapping;
import com.jeffreyning.nhversion.common.version.util.VersionMappingUtil;

/*
 * 演示前端与后端有两套version，需要做映射
 * @author ninghao
 */
public class FrontVersionChangeController {
	public static int calcu(int param,String frontVer){
		String version=VersionMappingUtil.mappingVer(frontVer);
		System.out.println("frontVer is "+frontVer+" change to backVer is "+version);
		Map paramMap=new HashMap();
    	paramMap.put("version", version);
    	paramMap.put("param", param);
    	Map retMap=CommonControllerMapping.execVersionMapping("NHVersionController","calcu", version, paramMap);
    	Integer retInt=(Integer) retMap.get("retInt");
		return retInt;
	}
}
