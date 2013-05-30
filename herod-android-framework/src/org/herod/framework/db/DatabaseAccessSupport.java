/*
 * Copyright © 2013-2014 Xiong Zhijun, All Rights Reserved. 
 */
package org.herod.framework.db;

import java.util.Arrays;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 
 * @author Xiong Zhijun
 * @email hust.xzj@gmail.com
 * 
 */
public class DatabaseAccessSupport {
	protected SQLiteOpenHelper openHelper;

	public DatabaseAccessSupport(SQLiteOpenHelper openHelper) {
		super();
		this.openHelper = openHelper;
	}

	public void executeWrite(Writer writer) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			writer.write(db);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public <T> T executeRead(Reader<T> reader) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		db.beginTransaction();
		try {
			T result = reader.read(db);
			db.setTransactionSuccessful();
			return result;
		} finally {
			db.endTransaction();
		}
	}

	protected List<String> getAllColumnsByDatabase(SQLiteDatabase db,
			String table) {
		Cursor cursor = db.query(table, null, null, null, null, null, null);
		return Arrays.asList(cursor.getColumnNames());
	}

	public static interface Writer {
		void write(SQLiteDatabase db);
	}

	public static interface Reader<T> {
		T read(SQLiteDatabase db);
	}
}
