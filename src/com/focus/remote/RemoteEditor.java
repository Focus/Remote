package com.focus.remote;


import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RemoteEditor extends Activity{

	private Remote remote;
	String remoteName;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_editor);

		//Check if we are editing an existing remote
		remoteName = this.getIntent().getExtras().getString("Remote");

		if(remoteName.length() > 0){
			DBHandle db = new DBHandle(this);
			remote = db.getRemote(remoteName);
			db.close();
		}
		else
			remote = new Remote();
		
		updateDisplay();

		OnClickListener ocl = new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(v.getId());
				//To change the colour when the list item is pressed.
				v.setBackgroundResource(android.R.drawable.list_selector_background);
			}
		};

		((LinearLayout) findViewById(R.id.edit_select_title)).setOnClickListener(ocl);
		((LinearLayout) findViewById(R.id.edit_select_search)).setOnClickListener(ocl);
		((LinearLayout) findViewById(R.id.edit_select_play)).setOnClickListener(ocl);
		((LinearLayout) findViewById(R.id.edit_select_previous)).setOnClickListener(ocl);
		((LinearLayout) findViewById(R.id.edit_select_next)).setOnClickListener(ocl);
		((LinearLayout) findViewById(R.id.edit_select_stop)).setOnClickListener(ocl);
		((LinearLayout) findViewById(R.id.edit_select_fullscreen)).setOnClickListener(ocl);
	}
	/*
	 * Called when the ocl triggers.
	 */
	@Override
	protected Dialog onCreateDialog(final int id){
		final EditText edit = new EditText(this);
		return new AlertDialog.Builder(this)
		//.setIconAttribute(android.R.attr.alertDialogIcon)
		.setTitle("Generic")
		.setView(edit)
		.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if(edit.getText().length() <= 0)
					return;
				switch(id){
				case R.id.edit_select_title:
					remote.title = edit.getText().toString();
					break;
				case R.id.edit_select_search:
					remote.search = edit.getText().toString();
					break;
				case R.id.edit_select_play:
					remote.play = (int) edit.getText().toString().toCharArray()[0];
					break;
				case R.id.edit_select_previous:
					remote.previous = (int) edit.getText().toString().toCharArray()[0];
					break;
				case R.id.edit_select_stop:
					remote.stop = (int) edit.getText().toString().toCharArray()[0];
					break;
				case R.id.edit_select_next:
					remote.next = (int) edit.getText().toString().toCharArray()[0];
					break;
				case R.id.edit_select_fullscreen:
					remote.fullscreen = (int) edit.getText().toString().toCharArray()[0];
					break;
				}
				updateDisplay();
			}
		})
		.setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		})
		.create();		
	}

	/*
	 * When the user presses the Save/Disacard button.
	 */
	public void exitButtonPress(View button){
		if(button.getId() == R.id.remote_save){
			if(remote.title == null || remote.search == null){
				Toast.makeText(this, "You haven't finished yet!", Toast.LENGTH_LONG).show();
				return;
			}
			DBHandle db = new DBHandle(this);
			if(remoteName.length() > 0)
				db.deleteRemote(remoteName);
			db.insertRemote(remote);
			db.close();
			this.finish();
		}
		else{
			this.finish();
		}
	}
	/**
	 * @brief Updates the editor screen display.
	 */
	private void updateDisplay(){
		TextView tv;
		
		tv = (TextView) findViewById(R.id.edit_display_title);
		tv.setText(remote.title);
		
		tv = (TextView) findViewById(R.id.edit_display_search);
		tv.setText(remote.search);
		
		tv = (TextView) findViewById(R.id.edit_display_play);
		tv.setText(Arrays.toString(new char[]{(char) remote.play}));

		tv = (TextView) findViewById(R.id.edit_display_previous);
		tv.setText(Arrays.toString(new char[]{(char) remote.previous}));

		tv = (TextView) findViewById(R.id.edit_display_next);
		tv.setText(Arrays.toString(new char[]{(char) remote.next}));

		tv = (TextView) findViewById(R.id.edit_display_stop);
		tv.setText(Arrays.toString(new char[]{(char) remote.stop}));

		tv = (TextView) findViewById(R.id.edit_display_fullscreen);
		tv.setText(Arrays.toString(new char[]{(char) remote.fullscreen}));
	}

}
