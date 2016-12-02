package com.jeffreyning.nhversion.common.version.cmd;

import java.util.HashMap;
import java.util.Map;

public abstract class IControllerCommand {
	public final static String RETOBJ = "retObj";
	public final static String EXECSTATUS = "execStatus";

	public Map createRetMapSimple(Object retObj) {
		Map retMap = new HashMap();
		retMap.put(RETOBJ, retObj);
		return retMap;
	}

	public abstract Map execute(Map paramMap);
}
