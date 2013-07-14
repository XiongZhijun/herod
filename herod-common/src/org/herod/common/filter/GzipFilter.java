/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.common.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Xiong Zhijun
 * 
 */
public class GzipFilter implements Filter {

	public void init(FilterConfig pConfig) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		req = getServletRequest(req);
		chain.doFilter(req, res);
	}

	private HttpServletRequest getServletRequest(
			final HttpServletRequest request) {
		if (!isUsingGzipEncoding(request.getHeader("Content-Encoding"))) {
			return request;
		}
		return new GZipHttpServletRequestWrapper(request);
	}

	private boolean isUsingGzipEncoding(String header) {
		if (header == null) {
			return false;
		}
		for (StringTokenizer st = new StringTokenizer(header, ","); st
				.hasMoreTokens();) {
			String encoding = st.nextToken();
			int offset = encoding.indexOf(';');
			if (offset >= 0) {
				encoding = encoding.substring(0, offset);
			}
			if ("gzip".equalsIgnoreCase(encoding.trim())) {
				return true;
			}
		}
		return false;
	}

	class GZipHttpServletRequestWrapper extends HttpServletRequestWrapper {

		public GZipHttpServletRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		public int getContentLength() {
			return -1;
		}

		public ServletInputStream getInputStream() throws IOException {
			final InputStream in = new GZIPInputStream(getRequest()
					.getInputStream());
			return new ServletInputStream() {
				public int read() throws IOException {
					return in.read();
				}

				public void close() throws IOException {
					in.close();
				}
			};
		}

		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(getInputStream(),
					getCharacterEncoding()));
		}
	}

}
