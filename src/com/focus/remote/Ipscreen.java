package com.focus.remote;

import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Ipscreen extends Activity{

	private ListView lv;
	private ArrayAdapter<String> adapter;
	private DBHandle db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ipscreen);
		lv = (ListView)findViewById(R.id.iplist);
		db = new DBHandle(this);
		adapter = new ArrayAdapter<String>(this, R.layout.iplist_element, R.id.ipel, db.selectAllIps());
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(
				new OnItemClickListener(){
					public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
						connectByString((String) lv.getItemAtPosition(position));
					}
				}
				);
		lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			public void onCreateContextMenu(ContextMenu cm, View v, ContextMenuInfo cmi) {
				String[] menuitems = new String[]{"Connect", "Forget"};
				for (int i = 0; i<menuitems.length; i++) {
					cm.add(cm.NONE, i, i, menuitems[i]);
				}
			}	        	
		});

	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo minfo = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()){
		case 0:
			connectByString(adapter.getItem(minfo.position));
			break;
		case 1:
			db.deleteIp(adapter.getItem(minfo.position));
			adapter = new ArrayAdapter<String>(this, R.layout.iplist_element, R.id.ipel, db.selectAllIps());
			lv.setAdapter(adapter);
			lv.invalidateViews();
			break;
		}
		return super.onContextItemSelected(item);	 
	}
	public void ipExtract(View button){
		EditText ipe = (EditText) findViewById(R.id.EditTextIP); 
		String ip = ipe.getText().toString();
		ipe = (EditText) findViewById(R.id.EditTextPort);
		int port = 1987;
		try{
			port = Integer.parseInt(ipe.getText().toString());
		} catch(Exception e){
			port = 1987;
		}
		ProgressDialog dialog = ProgressDialog.show(this, "","Connecting to "+ip+":"+port+"...", true);
		TCPConnect app = (TCPConnect) getApplication();
		if(app.connect(ip, port)){
			dialog.dismiss();
			if(!db.existsIp(ip + ":" + port))
				db.insertIp(ip + ":" + port);
			goTo();
		}
		else
			new AlertDialog.Builder(this).setTitle("Error!").setMessage("Unable to connect. Are you sure the server is running on"+ip+":"+port +"?").show();
		dialog.dismiss();
	}
	public void connectByString(String in){
		int port = 1987;
		String ip = "";
		try{
			port = Integer.parseInt(in.substring(in.indexOf(":")+1));
			ip = in.substring(0,in.indexOf(":"));
		} catch(Exception e){
			port = 1987;
		}
		ProgressDialog dialog = ProgressDialog.show(this, "","Connecting to "+ip+":"+port+"...", true);
		TCPConnect app = (TCPConnect) getApplication();
		if(app.connect(ip, port)){
			dialog.dismiss();
			goTo();
			this.finish();
		}
		else
			new AlertDialog.Builder(this).setTitle("Error!").setMessage("Unable to connect. Are you sure the server is running on"+ip+":"+port +"?").show();
		dialog.dismiss();
	}

	private void goTo(){
		Bundle bundle = this.getIntent().getExtras();
		if(bundle.getInt("Direction") == 1)
			startActivity(new Intent(this, RemoteSelector.class));
		else
			startActivity(new Intent(this, MouseScreen.class));
	}

	@Override
	public void onResume(){
		super.onResume();
		adapter = new ArrayAdapter<String>(this, R.layout.iplist_element, R.id.ipel, db.selectAllIps());
		lv.setAdapter(adapter);
		lv.invalidateViews();

		TCPConnect app = (TCPConnect) getApplication();
		Socket sock = app.getSocket();
		try{
			if(sock.isConnected())
				goTo();
		}catch(Exception e){
		}
	}
}
