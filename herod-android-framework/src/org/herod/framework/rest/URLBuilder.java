/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.rest;

/**
 * 定义了根据相对url构建完整的REST访问URL的方式。
 * 
 * @author Xiong Zhijun
 * 
 */
public interface URLBuilder {

	/**
	 * 根据相对url构建完整的REST访问URL。
	 * 
	 * @param relativeUrl
	 * @return
	 */
	String build(String relativeUrl);

}
