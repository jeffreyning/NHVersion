package com.jeffreyning.nhversion.demo.controller;

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
