package com.focus.remote;

import java.io.PrintWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	
	private void sendAction(Remote.RemoteAction act){
		try{
			out.println(remote.getProtocol(act));
		}catch(Exception e){
			new AlertDialog.Builder(this).setTitle("Error!").setMessage(e.getMessage()).show();
			TCPConnect app = (TCPConnect) getApplication();
			app.flush();
			this.finish();
		}
	}
	
	public void sendEvent(View button){
		
		switch(button.getId()){
		case R.id.remote_play:
			sendAction(Remote.RemoteAction.PLAY);
			break;
		case R.id.remote_pause:
			sendAction(Remote.RemoteAction.PLAY);
			break;
		case R.id.remote_seek_back:
			sendAction(Remote.RemoteAction.PREVIOUS);
			break;
		case R.id.remote_seek_forward:
			sendAction(Remote.RemoteAction.NEXT);
			break;
		case R.id.remote_stop:
			sendAction(Remote.RemoteAction.STOP);
			break;
		case R.id.remote_fullscreen:
			sendAction(Remote.RemoteAction.FULLSCREEN);
			break;
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.remote_menu, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.pref:
	    	startActivity(new Intent(this, Preferences.class));
	    	return true;
	    case R.id.close:
	    	TCPConnect app = (TCPConnect) getApplication();
	    	app.flush();
	    	this.finish();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
