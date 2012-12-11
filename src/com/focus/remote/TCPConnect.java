package com.focus.remote;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class TCPConnect extends Application {
	private Socket sock;
	private String ip;
	private PrintWriter out;
	private int sens, port;
	private boolean online;
	public void onCreate(){
		sock = null;
		out = null;
		ip = "";
		online = false;
		sync();
	}
	public boolean connect(String ip, int port){
		this.ip = ip;
		this.port = port;
		AsyncTask<Void,Void,Void> comms = new Comms();
		comms.execute();
		try {
			comms.get(3,TimeUnit.SECONDS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return online;
	}
	public Socket getSocket(){
		return sock;
	}
	public PrintWriter getPrintWriter(){
		return out;
	}
	public int getSens(){return sens;}

	public void sync(){
		SharedPreferences gprefs = PreferenceManager.getDefaultSharedPreferences(this);
		sens = gprefs.getInt("sensitivity", 35);
	}
	public void flush(){
		try {
			sock.close();
			sock = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		out.close();
	}
	class Comms extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try {
				sock = new Socket(ip,port);
			} catch (Exception e){
				Log.e("Socket",e.toString());
				return null;
			}

			try{
				out = new PrintWriter(sock.getOutputStream(), true);
			} catch (Exception e){
				Log.e("Socket",e.toString());
				return null;
			}
			online = true;
			return null;
		}
	}
}
