/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.ci;

import java.lang.reflect.Field;
import java.util.List;

import org.herod.framework.ViewFindable;
import org.herod.framework.ViewFindable.ActivityWrapper;
import org.herod.framework.ViewFindable.DialogWrapper;
import org.herod.framework.ViewFindable.FragmentWrapper;
import org.herod.framework.ViewFindable.ViewWrapper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.framework.utils.BeanUtils;
import org.herod.framework.utils.ClassUtils;
import org.herod.framework.utils.ResourcesUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 可以用来直接注入view对象到activity、dialog、fragment中，避免很多类似findViewById然后类型转换的编码。
 * 
 * @author Xiong Zhijun
 * @since mobi-android-framework 2.0
 */
public class InjectViewHelper {

	/**
	 * 根据注解{@link InjectView}来注入View中的View类型的成员变量。
	 * 
	 * @param view
	 *            一般是自定义的View
	 */
	public void injectViews(View view) {
		injectViews(view.getContext(), new ViewWrapper(view), view);
	}

	/**
	 * 根据注解{@link InjectView}来注入Activity中的View类型的成员变量。
	 * 
	 * @param activity
	 */
	public void injectViews(Activity activity) {
		injectViews(activity, new ActivityWrapper(activity), activity);
	}

	/**
	 * 根据注解{@link InjectView}来注入Dialog中的View类型的成员变量。
	 * 
	 * @param dialog
	 */
	public void injectViews(Dialog dialog) {
		injectViews(dialog.getContext(), new DialogWrapper(dialog), dialog);
	}

	/**
	 * 根据注解{@link InjectView}来注入Fragment中的View类型的成员变量。
	 * 
	 * @param fragment
	 */
	public void injectViews(Fragment fragment) {
		injectViews(fragment.getActivity(), new FragmentWrapper(fragment),
				fragment);
	}

	/**
	 * 根据注解{@link InjectView}来注入实现了{@link ViewFindable}接口的类型的View类型的成员变量。
	 * 
	 * @param context
	 * @param viewFindable
	 */
	public void injectViews(Context context, ViewFindable viewFindable) {
		injectViews(context, viewFindable, viewFindable);
	}

	/**
	 * 根据注解{@link InjectView}将viewFinable对象中查找到的view注入到target对象的字段中。
	 * 
	 * @param context
	 * @param viewFindable
	 * @param target
	 */
	public void injectViews(Context context, ViewFindable viewFindable,
			Object target) {
		List<Field> fields = ClassUtils.getDeclaredFields(target.getClass(),
				ReflectionConfig.getIncludePackages());
		for (Field field : fields) {
			injectField(context, viewFindable, target, field);
		}
	}

	protected void injectField(Context context, ViewFindable viewFindable,
			Object target, Field field) {
		InjectView annotation = field.getAnnotation(InjectView.class);
		if (annotation == null) {
			return;
		}
		int resourcesId = annotation.value();
		if (resourcesId <= 0) {
			String resourceName = annotation.resourceName();
			resourcesId = ResourcesUtils.getIdResourcesId(resourceName);
		}
		View view = viewFindable.findViewById(resourcesId);
		if (view != null) {
			BeanUtils.setFieldValue(target, field, view);
		}
	}

}
