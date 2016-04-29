package com.jeffreyning.nhversion.common.version.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * @author ninghao
 * @param controllerName
 *            controller名称
 * @param funcName
 *            方法名称
 * @param funcVer
 *            方法版本 vxx_xx_xx x为数字 不能超过2位数 v10_20_30 => 102030
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerMappingAnno {
	String controllerName();

	String funcName();

	String funcVer();
}
