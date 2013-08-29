/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class BaseFragment extends Fragment implements ViewFindable {

	@Override
	public View findViewById(int id) {
		return getView().findViewById(id);
	}

}
