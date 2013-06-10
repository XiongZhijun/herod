/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.order.web.pms;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class MD5UserPasswordEncryptor implements UserPasswordEncryptor {

	@Override
	public String encrypt(String originalText) {
		return new Md5Hash(originalText).toHex().toUpperCase();
	}

}
