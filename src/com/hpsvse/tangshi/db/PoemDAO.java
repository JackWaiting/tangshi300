package com.hpsvse.tangshi.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hpsvse.tangshi.entity.Poem;
import com.hpsvse.tangshi.entity.Type;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PoemDAO {

	private SQLiteHelper helper;
	private SQLiteDatabase db;
	
	public PoemDAO(Context context) {
		helper = new SQLiteHelper(context);
	}
	
	Map<String, String> authMap = new HashMap<String, String>();
	Map<String, String> typeMap = new HashMap<String, String>();
	
	/**
	 * 获取标题信息
	 * @return
	 */
	public List<Poem> getGeneral(){
		List<Poem> list = new ArrayList<Poem>();
		Poem poem = null;
		db = helper.getReadableDatabase();
		Cursor cursor = db.query("poem", new String[]{"_id,title,authid,typeid"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			int id = Integer.parseInt(cursor.getString(0));
			String title = cursor.getString(1);
			String authid = cursor.getString(2);
			String typeid = cursor.getString(3);
			String auth = null;
			String type = null;
			if (authMap.get(authid) == null) {
				Cursor authCursor = db.query("auth", new String[]{"_id","auth"}, "_id = ?", new String[]{authid}, null, null, null);
				if (authCursor.moveToNext()) {
					auth = authCursor.getString(1);
					authMap.put(authid, auth);
				}
			}else{
				auth = authMap.get(authid);
			}
			
			// 查询缓存中是否有该类型
			if (typeMap.get(typeid) == null) {
				Cursor typeCursor = db.query("type", new String[]{"_id","type"}, "_id = ?", new String[]{typeid}, null, null, null);
				if (typeCursor.moveToNext()) {
					type = typeCursor.getString(1);
					typeMap.put(typeid, type);	// 没有改类型时，将本次查询的对应关系放入缓存
				}
			}else{
				type = typeMap.get(typeid);		// 有该类型是，直接从缓存中读取数据
			}
			
			poem = new Poem(id,title, auth, type, null, null);
			list.add(poem);
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 根据类型模糊查询
	 * @param typeSel	类型
	 * @param data	查询的数据
	 * @return
	 */
	public List<Poem> getGeneral(String typeSel , String data){
		List<Poem> list = new ArrayList<Poem>();
		Poem poem = null;
		db = helper.getReadableDatabase();
		Cursor cursor = null;
		if (typeSel.equals("title")) {	// 标题
			cursor = db.query("poem", new String[]{"_id,title,authid,typeid"}, "title like ?", new String[]{"%" + data + "%"}, null, null, null);
		}else if (typeSel.equals("author")) {	 // 作者
			cursor = db.query("poem", new String[]{"_id,title,authid,typeid"}, "authid = ?", new String[]{data}, null, null, null);
		}else if (typeSel.equals("content")) {	 // 诗歌
			cursor = db.query("poem", new String[]{"_id,title,authid,typeid"}, "content like ?", new String[]{"%" + data + "%"}, null, null, null);
		}else if (typeSel.equals("type")) {	 // 类型
			cursor = db.query("poem", new String[]{"_id,title,authid,typeid"}, "typeid = ?", new String[]{data}, null, null, null);
		}else if (typeSel.equals("desc")) {	 // 描述
			cursor = db.query("poem", new String[]{"_id,title,authid,typeid"}, "desc like ?", new String[]{"%" + data + "%"}, null, null, null);
		}
		while(cursor.moveToNext()){
			int id = Integer.parseInt(cursor.getString(0));
			String title = cursor.getString(1);
			String authid = cursor.getString(2);
			String typeid = cursor.getString(3);
			String auth = null;
			String type = null;
			if (authMap.get(authid) == null) {
				Cursor authCursor = db.query("auth", new String[]{"_id","auth"}, "_id = ?", new String[]{authid}, null, null, null);
				if (authCursor.moveToNext()) {
					auth = authCursor.getString(1);
					authMap.put(authid, auth);
				}
			}else{
				auth = authMap.get(authid);
			}
			
			// 查询缓存中是否有该类型
			if (typeMap.get(typeid) == null) {
				Cursor typeCursor = db.query("type", new String[]{"_id","type"}, "_id = ?", new String[]{typeid}, null, null, null);
				if (typeCursor.moveToNext()) {
					type = typeCursor.getString(1);
					typeMap.put(typeid, type);	// 没有改类型时，将本次查询的对应关系放入缓存
				}
			}else{
				type = typeMap.get(typeid);		// 有该类型是，直接从缓存中读取数据
			}
			
			poem = new Poem(id,title, auth, type, null, null);
			list.add(poem);
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 获取详细信息
	 * @return
	 */
	public Poem getDetail(String id){
		Poem poem = null;
		db = helper.getReadableDatabase();
		Cursor cursor = db.query("poem", new String[]{"title","content","desc"}, "_id = ?", new String[]{id}, null, null, null);
		if (cursor.moveToNext()) {
			poem = new Poem(0, cursor.getString(0), null, null, cursor.getString(1), cursor.getString(2));
		}
		cursor.close();
		return poem;
	}
	
	/**
	 * 获取 作者 信息
	 * @return
	 */
	public List<Type> getAuth(){
		List<Type> list = new ArrayList<Type>();
		Type auth = null;
		db = helper.getReadableDatabase();
		Cursor cursor = db.query("auth", null, null, null, null, null, null);
		while(cursor.moveToNext()){
			int id = Integer.parseInt(cursor.getString(0));
			String authStr = cursor.getString(1);
			auth = new Type(id, authStr);
			list.add(auth);
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 获取 类型 信息
	 * @return
	 */
	public List<Type> getType(){
		List<Type> list = new ArrayList<Type>();
		Type type = null;
		db = helper.getReadableDatabase();
		Cursor cursor = db.query("type", null, null, null, null, null, null);
		while(cursor.moveToNext()){
			int id = Integer.parseInt(cursor.getString(0));
			String typeStr = cursor.getString(1);
			type = new Type(id,typeStr);
			list.add(type);
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 插入诗词
	 * @param poem
	 */
	public void insert(Poem poem){
		db = helper.getWritableDatabase();
		int authid = -1;
		int typeid = -1;
		// --------- 向 auth 表插入数据 ----------
		String auth = poem.getAuth();
		Cursor authCursor = db.query("auth", null, "auth = ?", new String[]{auth}, null, null, null);
		if (!authCursor.moveToNext()) {
			ContentValues authValues = new ContentValues();
			authValues.put("auth", auth);
			authid = (int) db.insert("auth", null, authValues);
		}else{
			authid = Integer.parseInt(authCursor.getString(0));
		}
		// ---------- 向 type 表插入数据 -----------
		String type = poem.getType();
		Cursor typeCursor = db.query("type", null, "type = ?", new String[]{type}, null, null, null);
		if (!typeCursor.moveToNext()) {
			ContentValues typeValues = new ContentValues();
			typeValues.put("type", type);
			typeid = (int) db.insert("type", null, typeValues);
		}else{
			typeid = Integer.parseInt(typeCursor.getString(0));
		}
		// ---------- 向 poem 表插入数据 -----------
		ContentValues poemValues = new ContentValues();
		poemValues.put("title", poem.getTitle());
		poemValues.put("authid", authid);
		poemValues.put("typeid", typeid);
		poemValues.put("content", poem.getContent());
		poemValues.put("desc", poem.getDesc());
		db.insert("poem", null, poemValues);
	}
	
	/**
	 * 关闭数据
	 */
	public void close(){
		if (db != null) {
			db.close();
		}
	}
	
}
