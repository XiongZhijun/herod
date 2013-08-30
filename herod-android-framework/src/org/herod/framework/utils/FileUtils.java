/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved.
 */
package org.herod.framework.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 */
public class FileUtils {
	private static final String TAG = FileUtils.class.getSimpleName();

	public static void close(Closeable closeable) {
		if (closeable == null) {
			return;
		}
		try {
			closeable.close();
		} catch (IOException e) {
			Log.w(TAG, "close " + closeable.toString() + " failed.");
		}
	}

	public static File saveToFile(Context context, String type,
			String fileName, String text) {
		File file = createLocalFile(context, type, fileName, text.length());
		file.deleteOnExit();
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write(text);
			fileWriter.flush();
		} catch (IOException e) {
			Log.w(TAG, e.getMessage());
		} finally {
			close(fileWriter);
		}
		return file;
	}

	public static String readFromFile(Context context, String type,
			String fileName) {
		File fileDir = context.getExternalFilesDir(type);
		return readFromFile(context, new File(fileDir, fileName));
	}

	public static String readFromFile(Context context, File file) {
		if (!file.exists()) {
			return null;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			return sb.toString();
		} catch (IOException e) {
			Log.w(TAG, e.getMessage());
			return null;
		} finally {
			close(br);
		}
	}

	public static File createLocalFile(Context context, String type,
			String fileName, long fileSize) {
		if (StringUtils.isBlank(type)) {
			type = Environment.DIRECTORY_DOWNLOADS;
		}
		File fileDir;
		String sdStatus = Environment.getExternalStorageState();
		if (sdStatus.equals(Environment.MEDIA_MOUNTED)
				&& getSDCardFreeSize(context) >= fileSize * 2) {
			fileDir = context.getExternalFilesDir(type);
			return new File(fileDir, fileName);
		} else {
			return null;
		}
	}

	public static long getSDCardFreeSize(Context context) {
		String sDcString = android.os.Environment.getExternalStorageState();
		if (!sDcString.equals(android.os.Environment.MEDIA_MOUNTED)) {
			return 0;
		}
		File pathFile = android.os.Environment.getExternalStorageDirectory();
		android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
		// 获取SDCard上每个block的SIZE
		long nBlocSize = statfs.getBlockSize();
		// 获取可供程序使用的Block的数量
		long nAvailaBlock = statfs.getAvailableBlocks();
		// 计算 SDCard 剩余大小Byte
		return nAvailaBlock * nBlocSize;
	}

}
