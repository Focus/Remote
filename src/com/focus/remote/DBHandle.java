package com.focus.remote;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
/**
 * @brief A handle for DB (SQLite) operations
 */
public class DBHandle  {
	
	private static final String DATABASE_NAME = "remote.db";
	private static final String TABLE_NAME_IP = "ips";
	private static final String TABLE_NAME_REMOTE = "remotes";
	private static final int DATABASE_VERSION = 3;
	
	private Context context;
	private SQLiteDatabase db;
	private OpenHelper helper;
	
	private SQLiteStatement ipstatement;
	private SQLiteStatement remotestatement;
	private static final String INSERT_IP = "insert into " + TABLE_NAME_IP + " (ip) values (?)";
	private static final String INSERT_REMOTE = "insert into " + TABLE_NAME_REMOTE + " (title, search, play, stop, fullscreen, previous, next) values (?,?,?,?,?,?,?)";

	public DBHandle(Context cont){
		context = cont;
		helper = new OpenHelper(context);
		db = helper.getWritableDatabase();
		ipstatement = db.compileStatement(INSERT_IP);
		remotestatement = db.compileStatement(INSERT_REMOTE);
	}
	/**
	 * @brief Inserts a remote into the database.
	 * @return The executing insert result.
	 */
	public long insertRemote(Remote remote){
		remotestatement.bindString(1, remote.title);
		remotestatement.bindString(2, remote.search);
		remotestatement.bindString(3, remote.play);
		remotestatement.bindString(4, remote.stop);
		remotestatement.bindString(5, remote.fullscreen);
		remotestatement.bindString(6, remote.previous);
		remotestatement.bindString(7, remote.next);
		return remotestatement.executeInsert();
	}
	
	public long insertIp(String name){
		ipstatement.bindString(1, name);
		return ipstatement.executeInsert();
	}
	
	public void deleteAll(){
		db.delete(TABLE_NAME_IP, null, null);
	}
	
	public Remote getRemote(String title){
		Remote ret = new Remote();
		Cursor c = db.query(TABLE_NAME_REMOTE, null, "title='" + title + "'", null, null, null, null);
		if(c.moveToFirst()){
			ret.title = c.getString(1);
			ret.search = c.getString(2);
			ret.play = c.getString(3);
			ret.stop = c.getString(4);
			ret.fullscreen = c.getString(5);
			ret.previous = c.getString(6);
			ret.next = c.getString(7);
		}
		c.close();
		return ret;
	}
	
	public String[] selectAllIps(){
		List<String> ret = new ArrayList<String>();
		Cursor c = db.query(TABLE_NAME_IP, new String[]{"ip"}, null, null, null, null, null);
		if(c.moveToFirst()){
			do{
				ret.add(c.getString(0));
			}while(c.moveToNext());
		}
		//if(!c.isClosed() && c != null)
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
		//if(!c.isClosed() && c != null)
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
				//if(!c.isClosed() && c != null)
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
				//if(!c.isClosed() && c != null)
					c.close();
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	public void close(){
		if(helper != null)
			helper.close();
		db.close();
	}
	
	private static class OpenHelper extends SQLiteOpenHelper{

		public OpenHelper(Context cont) {
			super(cont, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME_IP + " (id INTEGER PRIMARY KEY, ip TEXT)");
			db.execSQL("CREATE TABLE " + TABLE_NAME_REMOTE + " (id INTEGER PRIMARY KEY, title TEXT, search TEXT, play TEXT, stop TEXT, fullscreen TEXT, previous TEXT, next TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_IP);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_REMOTE);
			onCreate(db);
		}
		
	}

}
