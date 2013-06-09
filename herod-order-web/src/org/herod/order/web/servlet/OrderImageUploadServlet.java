/*
 * Copyright 2010 FPI,Inc. All rights reserved.
 * http://www.fpi-inc.com
 */
package org.herod.order.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.Gson;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * 
 * @author Xiong Zhijun
 * @since sword_fileserver
 * @create 2010-10-27
 * 
 */
public class OrderImageUploadServlet extends HttpServlet {
	/**  */
	private static final int _1M = 1 * 1024 * 1024;
	private static final long serialVersionUID = 1971771252526988106L;
	private static final String UTF_8 = "UTF-8";
	protected WebApplicationContext context;

	@Override
	public void init() throws ServletException {
		super.init();
		context = WebApplicationContextUtils.getWebApplicationContext(this
				.getServletContext());
	}

	@Override
	protected void doPost(final HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		final List<String> files = new LinkedList<String>();
		String realPath = getServletConfig().getServletContext().getRealPath(
				getStoreDir());
		new MultipartRequest(req, realPath, _1M, UTF_8,
				new CustomerFileRenamePolicy(files));
		resp.setCharacterEncoding(UTF_8);
		resp.setContentType("text/html;charset=UTF-8");

		resp.getWriter().write(new Gson().toJson(new Result(true, files)));

	}

	protected String getStoreDir() {
		return "/OrderImages";
	}

	class CustomerFileRenamePolicy implements FileRenamePolicy {
		List<String> files;

		public CustomerFileRenamePolicy(List<String> files) {
			super();
			this.files = files;
		}

		@Override
		public File rename(File originalFile) {
			String uuid = FileUtils.buildUUID();
			String newFileName = FileUtils.rename(originalFile.getName(), uuid);
			String dir = originalFile.getParent();
			File newFile = new File(dir, newFileName);
			files.add(getStoreDir() + "/" + newFileName);
			return newFile;
		}
	}

	public static class Result {
		private boolean success = true;
		private List<String> images = new ArrayList<String>();

		public Result(boolean success, List<String> images) {
			super();
			this.success = success;
			this.images = images;
		}

		public boolean isSuccess() {
			return success;
		}

		public List<String> getImages() {
			return images;
		}

	}

}
