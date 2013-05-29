/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.ci.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.herod.framework.ci.InjectViewHelper;

/**
 * 标记一个字段可以注入一个id为{@link #value()}的View。这个注入的View是通过{@link ViewFindable}
 * 的实例对象查找到的。
 * 
 * <pre>
 * {@code
 * public class MobiListFragment<T extends ListAdapter> extends BasicFragment {
 * 	{@literal @}InjectView(resourceName = "listView")
 * 	protected ListView listView;
 * 
 * 	public void onActivityCreated(Bundle savedInstanceState) {
 * 		super.onActivityCreated(savedInstanceState);
 * 		new InjectViewHelper().injectViews(this);
 * 	}
 * }
 * 
 * </pre>
 * 
 * @author Xiong Zhijun
 * @since mobi-android-framework 2.0
 * @see {@link InjectViewHelper}
 * @see {@link ViewFindable}
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface InjectView {

	/**
	 * 待注入的View的Id，一个是定义在R.java文件中。
	 * 
	 * @return
	 */
	int value() default 0;

	/**
	 * 待注入的View的Id的resource name，当{@link #value()}的值无效（小于等于0）时，通过该Resouce
	 * Name查找到View的ID，这个过程会利用
	 * {@link ResourcesUtils#getIdResourcesId(android.content.Context, String)}
	 * 来反射获取Resource Id。
	 * 
	 * @return
	 * @see {@link ResourcesUtils#getResourcesIdByType(android.content.Context, String, String)}
	 */
	String resourceName() default "";
}
