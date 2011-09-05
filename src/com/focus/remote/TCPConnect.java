package com.focus.remote;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TCPConnect extends Application {
	private Socket sock;
	private PrintWriter out;
	private int sens;
	public void onCreate(){
		sock = null;
		out = null;
		sync();
	}
	public boolean connect(String ip, int port){
		try {
			sock = new Socket(ip,port);
			out = new PrintWriter(sock.getOutputStream(), true);
			return true;
		} catch (UnknownHostException e){
			return false;
		}
		catch (IOException e){
			return false;
		}

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
}
