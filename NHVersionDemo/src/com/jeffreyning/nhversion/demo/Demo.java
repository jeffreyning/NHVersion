package com.jeffreyning.nhversion.demo;

import com.jeffreyning.nhversion.common.version.mapping.CommonControllerMapping;
import com.jeffreyning.nhversion.demo.controller.NoNHVersionController;
import com.jeffreyning.nhversion.demo.controller.NHVersionController;

public class Demo {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		CommonControllerMapping.batchRegistryByPackage("com.jeffreyning.nhversion.demo.controller.cmd");
		NoNHVersionController.calcu(1, "v1_0_1");
		NHVersionController.calcu(1,"v1_0_1");
		NoNHVersionController.calcu(1, "v1_0_6");
		NHVersionController.calcu(1,"v1_0_6");
	}

}
