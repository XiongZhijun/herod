/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.herod.framework.Constants;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.util.FileCopyUtils;

/**
 * 自定义字符串解析方式，目的是解决默认实现导致的乱码的问题。
 * 
 * @author Xiong Zhijun
 * 
 */
public class StringHttpMessageConverter extends
		AbstractHttpMessageConverter<String> {
	private static final String PLAIN = "plain";
	private static final String TEXT = "text";

	public static final Charset DEFAULT_CHARSET = Charset
			.forName(Constants.UTF8);

	private final List<Charset> availableCharsets;

	private boolean writeAcceptCharset = true;

	public StringHttpMessageConverter() {
		super(new MediaType(TEXT, PLAIN, DEFAULT_CHARSET), MediaType.ALL);
		this.availableCharsets = new ArrayList<Charset>(Charset
				.availableCharsets().values());
	}

	public void setWriteAcceptCharset(boolean writeAcceptCharset) {
		this.writeAcceptCharset = writeAcceptCharset;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return String.class.equals(clazz);
	}

	@Override
	protected String readInternal(@SuppressWarnings("rawtypes") Class clazz,
			HttpInputMessage inputMessage) throws IOException {
		MediaType contentType = inputMessage.getHeaders().getContentType();
		Charset charset = contentType.getCharSet() != null ? contentType
				.getCharSet() : DEFAULT_CHARSET;
		return FileCopyUtils.copyToString(new InputStreamReader(inputMessage
				.getBody(), charset));
	}

	@Override
	protected Long getContentLength(String s, MediaType contentType) {
		if (contentType != null && contentType.getCharSet() != null) {
			Charset charset = contentType.getCharSet();
			try {
				return (long) s.getBytes(charset.name()).length;
			} catch (UnsupportedEncodingException ex) {
				throw new InternalError(ex.getMessage());
			}
		} else {
			return null;
		}
	}

	@Override
	protected void writeInternal(String s, HttpOutputMessage outputMessage)
			throws IOException {
		if (writeAcceptCharset) {
			outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
		}
		MediaType contentType = outputMessage.getHeaders().getContentType();
		Charset charset = contentType.getCharSet() != null ? contentType
				.getCharSet() : DEFAULT_CHARSET;
		FileCopyUtils.copy(s, new OutputStreamWriter(outputMessage.getBody(),
				charset));
	}

	protected List<Charset> getAcceptedCharsets() {
		return this.availableCharsets;
	}
}
