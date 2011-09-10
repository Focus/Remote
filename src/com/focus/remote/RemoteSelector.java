package com.focus.remote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RemoteSelector extends Activity{

	private ListView lv;
	private ArrayAdapter<String> adapter;
	private DBHandle db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_selector);
		lv = (ListView)findViewById(R.id.remotelist);
		db = new DBHandle(this);
		adapter = new ArrayAdapter<String>(this, R.layout.iplist_element, R.id.ipel, db.selectAllRemoteNames());
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(
				new OnItemClickListener(){
					public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
						displayRemote((String) lv.getItemAtPosition(position));
					}
				}
				);
		lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			public void onCreateContextMenu(ContextMenu cm, View v, ContextMenuInfo cmi) {
				String[] menuitems = new String[]{"Edit", "Forget"};
				for (int i = 0; i<menuitems.length; i++) {
					cm.add(cm.NONE, i, i, menuitems[i]);
				}
			}	        	
		});

	}
	
	public void displayRemote(String title){
		Intent intent = new Intent(this, RemoteScreen.class);
		intent.putExtra("Remote", title);
		startActivity(intent);
	}
	
	public void addNewRemote(View v){
		Intent intent = new Intent(this, RemoteEditor.class);
		intent.putExtra("Remote", "");
		startActivity(intent);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo minfo = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()){
		case 0:
			Intent intent = new Intent(this, RemoteEditor.class);
			intent.putExtra("Remote", adapter.getItem(minfo.position));
			startActivity(intent);
			break;
		case 1:
			db.deleteRemote(adapter.getItem(minfo.position));
			adapter = new ArrayAdapter<String>(this, R.layout.iplist_element, R.id.ipel, db.selectAllRemoteNames());
			lv.setAdapter(adapter);
			lv.invalidateViews();
			break;
		}
		return super.onContextItemSelected(item);	 
	}
	
	@Override
	public void onResume(){
		super.onResume();
		adapter = new ArrayAdapter<String>(this, R.layout.iplist_element, R.id.ipel, db.selectAllRemoteNames());
		lv.setAdapter(adapter);
		lv.invalidateViews();
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
