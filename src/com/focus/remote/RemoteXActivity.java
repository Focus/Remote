package com.focus.remote;

import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class RemoteXActivity extends Activity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainscreen);
	}
	public void buttonClick(View button){
		switch(button.getId()){
		case R.id.mouse:
			TCPConnect app = (TCPConnect) getApplication();
			Socket sock = app.getSocket();
			try{
				if(sock.isConnected()){
					startActivity(new Intent(this, MouseScreen.class));
					break;
				}
			}catch(Exception e){
			}
			Intent intent = new Intent(this, Ipscreen.class);
			intent.putExtra("Direction", 0);
			startActivity(intent);
			break;
		case R.id.remote:
			TCPConnect app1 = (TCPConnect) getApplication();
			Socket sock1 = app1.getSocket();
			try{
				if(sock1.isConnected()){
					startActivity(new Intent(this, RemoteSelector.class));
					break;
				}
			}catch(Exception e){
			}
			Intent intent1 = new Intent(this, Ipscreen.class);
			intent1.putExtra("Direction", 1);
			startActivity(intent1);
			break;
		case R.id.settings:
			startActivity(new Intent(this, Preferences.class));
			break;
		case R.id.instruct:
			startActivity(new Intent(this, Instruct.class));
			break;
		}
	}
}