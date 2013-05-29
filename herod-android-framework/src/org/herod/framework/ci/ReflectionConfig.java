/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.ci;

/**
 * 对依赖注入时的反射进行配置，配置反射过程中只对哪些包进行反射，比如说默认只对“com.mobi”的包进行反射。
 * 
 * @author Xiong Zhijun
 * @see {@link InjectAttributeHelper}
 * @see {@link InjectFragmentHelper}
 * @see {@link InjectServiceHelper}
 * @see {@link InjectViewHelper}
 * 
 */
public abstract class ReflectionConfig {

	private static String[] DEFAULT_INCLUDE_PACKAGES = new String[] { "org.herod" };
	private static String[] includePackages = DEFAULT_INCLUDE_PACKAGES;

	public static String[] getIncludePackages() {
		return includePackages;
	}

	public static void setIncludePackages(String[] includePackages) {
		ReflectionConfig.includePackages = includePackages;
	}
}
