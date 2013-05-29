/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework.db;

import org.herod.framework.R;
import org.herod.framework.utils.ResourcesUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 继承至{@link SQLiteOpenHelper}，其目的主要是实现数据库结构自动升级的能力。
 * <p>
 * 其中不同版本数据库结构的定义语句又string资源定义，而资源的名称的命名规则为：
 * <p>
 * <b>{databaseName} + '_' + version</b>
 * <p>
 * 例如每一个数据库的第一个版本数据库定义语句存放在'Mobi_1'对应的字符串资源中，其中‘Mobi’就是数据库的名称。
 * 
 * @author Xiong Zhijun
 * 
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private Context context;
	private String databaseName;

	/**
	 * 创建一个{@link DatabaseOpenHelper}对象，采用默认的数据库名称，默认的数据库名称来自于
	 * {@link R.string#DatabaseName}定义的字符串资源。
	 * 
	 * @param context
	 */
	public DatabaseOpenHelper(Context context) {
		this(context, R.string.DatabaseName);
	}

	/**
	 * 根据databaseNameResId创建一个{@link DatabaseOpenHelper}对象。
	 * 
	 * @param context
	 * @param databaseNameResId
	 *            数据库名称的资源ID。
	 */
	public DatabaseOpenHelper(Context context, int databaseNameResId) {
		this(context, context.getString(databaseNameResId), null,
				getNewVersion(context, context.getString(databaseNameResId)));
	}

	public DatabaseOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
		this.databaseName = name;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		onUpgrade(db, 0, getNewVersion(context, databaseName));
	}

	/**
	 * 根据版本号自动完成数据库的自动升级能力。
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (int i = oldVersion + 1; i <= newVersion; i++) {
			String sqls = ResourcesUtils.getString(context,
					buildVersionResourceName(databaseName, i));
			if (sqls != null) {
				SqlTokenizer tokenizer = new SqlTokenizer(sqls);
				while (tokenizer.hasMoreStatements()) {
					String command = tokenizer.getNextStatement();
					command = command.trim();
					if (command.length() != 0) {
						db.execSQL(command);
					}
				}
			}
		}
	}

	public Context getContext() {
		return context;
	}

	private static int getNewVersion(Context context, String databaseName) {
		int resId;
		int version = 0;
		do {
			version++;
			String versionResouce = buildVersionResourceName(databaseName,
					version);
			resId = ResourcesUtils
					.getStringResourcesId(context, versionResouce);
		} while (resId != Integer.MIN_VALUE);
		return version - 1;
	}

	private static String buildVersionResourceName(String databaseName,
			int version) {
		StringBuilder versionResouce = new StringBuilder();
		versionResouce.append(databaseName).append("_").append(version);
		return versionResouce.toString();
	}
}
