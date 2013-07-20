/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.worker.phone;

import org.herod.worker.phone.model.Token;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public interface WorkerService {

	Token login(String phone, String password);
}
