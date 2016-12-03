package com.jeffreyning.nhversion.common.version.util;

import java.util.Map;
import java.util.TreeMap;

public class VersionMappingUtil {
public static TreeMap versionMap=new TreeMap();
public static String defaultVer="";
public static TreeMap getVersionMap() {
	return versionMap;
}

public void setVersionMap(TreeMap versionMap) {
	VersionMappingUtil.versionMap = versionMap;
}

public void setVerProp(Map verProp){

	versionMap.putAll(verProp);
}

public static String getDefaultVer() {
	return defaultVer;
}

public void setDefaultVer(String defaultVer) {
	VersionMappingUtil.defaultVer = defaultVer;
}

public static String mappingVer(String frontVer){
	String frontKey = (String) versionMap.floorKey(frontVer);
	String backVer=defaultVer;
	if (frontKey != null) {
		backVer=(String) versionMap.get(frontKey);
	}
	return backVer;
}

}
