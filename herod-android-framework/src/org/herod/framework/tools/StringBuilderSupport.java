/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.tools;

/**
 * @author Xiong Zhijun
 * 
 */
public class StringBuilderSupport {

	private static ThreadLocal<StringBuilder> stringBuilderThreadLocal = new StringBuilderThreadLocal();

	public void newStringBuilder() {
		stringBuilderThreadLocal.remove();
	}

	public StringBuilderSupport append(String s) {
		StringBuilder sb = stringBuilderThreadLocal.get();
		sb.append(s);
		return this;
	}

	public StringBuilderSupport append(int i) {
		StringBuilder sb = stringBuilderThreadLocal.get();
		sb.append(i);
		return this;
	}

	public StringBuilderSupport append(Object s) {
		if (s != null) {
			StringBuilder sb = stringBuilderThreadLocal.get();
			sb.append(s.toString());
		}
		return this;
	}

	public String getString() {
		return stringBuilderThreadLocal.get().toString();
	}

	public void clean() {
		stringBuilderThreadLocal.remove();
	}

	static class StringBuilderThreadLocal extends ThreadLocal<StringBuilder> {
		@Override
		protected StringBuilder initialValue() {
			return new StringBuilder();
		}
	}
}
