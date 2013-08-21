/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework.form;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.herod.framework.ViewFindable;
import org.herod.framework.utils.BeanUtils;
import org.herod.framework.utils.ClassUtils;
import org.herod.framework.utils.ResourcesUtils;
import org.herod.framework.utils.StringUtils;
import org.herod.framework.utils.TextViewUtils;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class FormHelper {
	private Map<Class<?>, Serializer> serializerMap = new HashMap<Class<?>, Serializer>();
	private Map<Class<?>, Deserializer> deserializerMap = new HashMap<Class<?>, Deserializer>();
	private String[] from;
	private int[] to;
	private ViewFindable viewFindable;
	private Map<String, Method> getterMap = new HashMap<String, Method>();

	public FormHelper(String[] from, int[] to, Class<?> clazz) {
		this(from, to, clazz, null);
	}

	public FormHelper(String[] from, int[] to, Class<?> clazz,
			ViewFindable viewFindable) {
		super();
		this.from = from;
		this.to = to;
		this.viewFindable = viewFindable;
		initGetterMap(from, clazz);
	}

	protected void initGetterMap(String[] from, Class<?> clazz) {
		for (String property : from) {
			Method getter = ClassUtils.findGetter(clazz, property);
			getterMap.put(property, getter);
		}
	}

	public void setValues(Object target) {
		setValues(target, viewFindable);
	}

	public void setValues(Object target, ViewFindable viewFindable) {
		for (int i = 0; i < to.length; i++) {
			Object value = BeanUtils
					.getProperty(target, getterMap.get(from[i]));
			Serializer serializer = findSerializer(value);
			TextViewUtils.setText(viewFindable, to[i],
					serializer.serialize(value));
		}
	}

	protected Serializer findSerializer(Object value) {
		if (value == null) {
			return DEFAULT_SERIALIZER;
		}
		for (Entry<Class<?>, Serializer> entry : serializerMap.entrySet()) {
			if (entry.getKey().isAssignableFrom(value.getClass())) {
				return entry.getValue();
			}
		}
		return DEFAULT_SERIALIZER;
	}

	public void addSerializer(Class<?> clazz, Serializer serializer) {
		serializerMap.put(clazz, serializer);
	}

	public void addDeserializer(Class<?> clazz, Deserializer deserializer) {
		deserializerMap.put(clazz, deserializer);
	}

	public void setViewFindable(ViewFindable viewFindable) {
		this.viewFindable = viewFindable;
	}

	private static final Serializer DEFAULT_SERIALIZER = new Serializer() {
		public String serialize(Object value) {
			return value != null ? value.toString() : StringUtils.EMPTY;
		}
	};

	public static interface Serializer {
		String serialize(Object value);
	}

	public static interface Deserializer {
		Object serialize(String value);
	}

	public static class FormHelperBuilder {
		private FormHelper formHelper;

		public FormHelperBuilder(String[] from, int[] to, Class<?> clazz) {
			this(from, to, clazz, null);
		}

		public FormHelperBuilder(String[] from, int[] to, Class<?> clazz,
				ViewFindable viewFindable) {
			formHelper = new FormHelper(from, to, clazz, viewFindable);
			addEnumSerializer();
		}

		public FormHelperBuilder addSerializer(Class<?> clazz,
				Serializer serializer) {
			formHelper.addSerializer(clazz, serializer);
			return this;
		}

		public FormHelperBuilder addDateSerializer(final String pattern) {
			formHelper.addSerializer(Date.class, new Serializer() {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						pattern);

				public String serialize(Object value) {
					if (value == null) {
						return StringUtils.EMPTY;
					}
					return simpleDateFormat.format(value);
				}
			});
			return this;
		}

		public FormHelperBuilder addEnumSerializer() {
			formHelper.addSerializer(Enum.class, new Serializer() {
				public String serialize(Object value) {
					return ResourcesUtils.getEnumShowName((Enum<?>) value);
				}
			});
			return this;
		}

		public FormHelper build() {
			return formHelper;
		}
	}
}
