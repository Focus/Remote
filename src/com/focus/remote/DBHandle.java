package com.focus.remote;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DBHandle  {
	
	private static final String DATABASE_NAME = "remote.db";
	private static final String TABLE_NAME_IP = "ips";
	private static final String TABLE_NAME_REMOTE = "remotes";
	private static final int DATABASE_VERSION = 1;
	
	private Context context;
	private SQLiteDatabase db;
	
	private SQLiteStatement ipstatement;
	private SQLiteStatement remotestatement;
	private static final String INSERT_IP = "insert into " + TABLE_NAME_IP + " (ip) values (?)";
	private static final String INSERT_REMOTE = "insert into " + TABLE_NAME_REMOTE + " (title, search, play, stop, fullscreen, next, previous) values (?,?,?,?,?,?,?)";

	public DBHandle(Context cont){
		context = cont;
		OpenHelper helper = new OpenHelper(context);
		db = helper.getWritableDatabase();
		ipstatement = db.compileStatement(INSERT_IP);
		//remotestatement = db.compileStatement(INSERT_REMOTE);  //Crashes!
	}
	
	public long insertRemote(String title, String search, int play, int stop, int fullscreen, int next, int previous){
		remotestatement.bindString(1, title);
		remotestatement.bindString(2, search);
		remotestatement.bindLong(3, play);
		remotestatement.bindLong(4, stop);
		remotestatement.bindLong(5, fullscreen);
		remotestatement.bindLong(6, next);
		remotestatement.bindLong(7, previous);
		return remotestatement.executeInsert();
	}
	
	public long insertIp(String name){
		ipstatement.bindString(1, name);
		return ipstatement.executeInsert();
	}
	
	public void deleteAll(){
		db.delete(TABLE_NAME_IP, null, null);
	}
	
	public String[] selectAllIps(){
		List<String> ret = new ArrayList<String>();
		Cursor c = db.query(TABLE_NAME_IP, new String[]{"ip"}, null, null, null, null, null);
		if(c.moveToFirst()){
			do{
				ret.add(c.getString(0));
			}while(c.moveToNext());
		}
		if(!c.isClosed() && c != null)
			c.close();
		return (String[]) ret.toArray(new String[0]);
	}
	
	public String[] selectAllRemoteNames(){
		List<String> ret = new ArrayList<String>();
		Cursor c = db.query(TABLE_NAME_REMOTE, new String[]{"title"}, null, null, null, null, null);
		if(c.moveToFirst()){
			do{
				ret.add(c.getString(0));
			}while(c.moveToNext());
		}
		if(!c.isClosed() && c != null)
			c.close();
		return (String[]) ret.toArray(new String[0]);
	}
	
	public void deleteIp(String name){
		db.delete(TABLE_NAME_IP, "ip='" + name + "'", null);
	}
	
	public void deleteRemote(String name){
		db.delete(TABLE_NAME_REMOTE, "title='" + name + "'", null);
	}
	
	public boolean existsIp(String name){
		Cursor c = db.query(TABLE_NAME_IP, null, "ip='"+name+"'", null, null, null, null);
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
	
	public boolean existsRemote(String name){
		Cursor c = db.query(TABLE_NAME_REMOTE, null, "title='"+name+"'", null, null, null, null);
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
			db.execSQL("CREATE TABLE " + TABLE_NAME_IP + "(id INTEGER PRIMARY KEY, ip TEXT)");
			db.execSQL("CREATE TABLE " + TABLE_NAME_REMOTE + "(id INTEGER PRIMARY KEY, title TEXT, search TEXT, play INTEGER, stop INTEGER, fullscreen INTEGER, previous INTEGER, next INTEGER)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_IP);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_REMOTE);
			onCreate(db);
		}
		
	}

}
