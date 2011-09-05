package com.focus.remote;

import java.io.PrintWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

public class RemoteScreen extends Activity{
	
	private Remote remote;
	private PrintWriter out;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_screen);
		DBHandle db = new DBHandle(this);
		this.remote = db.getRemote(this.getIntent().getExtras().getString("Remote"));
		db.close();
		TCPConnect app = (TCPConnect) getApplication();
        out = app.getPrintWriter();
	}
	
	public void sendEvent(View button){
			try{
				out.println(remote.getProtocol(Remote.RemoteAction.PLAY));
			}catch(Exception e){
				new AlertDialog.Builder(this).setTitle("Error!").setMessage(e.getMessage()).show();
				TCPConnect app = (TCPConnect) getApplication();
				app.flush();
				this.finish();
			}
	}

}
