package com.focus.remote;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DBHandle  {
	
	private static final String DATABASE_NAME = "remote.db";
	private static final String TABLE_NAME = "ips";
	private static final int DATABASE_VERSION = 1;
	
	private Context context;
	private SQLiteDatabase db;
	
	private SQLiteStatement statement;
	private static final String INSERT = "insert into " + TABLE_NAME + " (ip) values (?)";

	public DBHandle(Context cont){
		context = cont;
		OpenHelper helper = new OpenHelper(context);
		db = helper.getWritableDatabase();
		statement = db.compileStatement(INSERT);
	}
	
	public long insert(String name){
		statement.bindString(1, name);
		return statement.executeInsert();
	}
	
	public void deleteAll(){
		db.delete(TABLE_NAME, null, null);
	}
	
	public String[] selectAll(){
		List<String> ret = new ArrayList<String>();
		Cursor c = db.query(TABLE_NAME, new String[]{"ip"}, null, null, null, null, null);
		if(c.moveToFirst()){
			do{
				ret.add(c.getString(0));
			}while(c.moveToNext());
		}
		if(!c.isClosed() && c != null)
			c.close();
		return (String[]) ret.toArray(new String[0]);
	}
	
	public void delete(String name){
		db.delete(TABLE_NAME, "ip='" + name + "'", null);
	}
	
	public boolean exists(String name){
		Cursor c = db.query(TABLE_NAME, null, "ip='"+name+"'", null, null, null, null);
		if(c.moveToLast()){
			if(c.getPosition() >= 0){
				if(!c.isClosed() && c != null)
					c.close();
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	private static class OpenHelper extends SQLiteOpenHelper{

		public OpenHelper(Context cont) {
			super(cont, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY, ip TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
		
	}

}
