package com.hpsvse.tangshi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String FILE_NAME = "cache.db";
	private static final int VERSION = 1;
	private static final String CREATE_TABLE_POEM = "create table poem (_id integer primary key autoincrement, title varchar(20),authid int,typeid int,content varchar(200),desc text)";
	private static final String CREATE_TABLE_AUTH = "create table auth (_id integer primary key autoincrement, auth unique)";
	private static final String CREATE_TABLE_TYPE = "create table type (_id integer primary key autoincrement, type unique)";

	public SQLiteHelper(Context context) {
		super(context, FILE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_AUTH);	//创建作者表
		db.execSQL(CREATE_TABLE_TYPE);	//创建类型表
		db.execSQL(CREATE_TABLE_POEM);	//创建诗词表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
