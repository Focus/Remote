package com.focus.remote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
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
		db.deleteAll();
		db.insertRemote(new Remote("test","spotify", 32, 32, 32, 32, 32));
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
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo minfo = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()){
		case 0:
			displayRemote(adapter.getItem(minfo.position));
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
	
}
