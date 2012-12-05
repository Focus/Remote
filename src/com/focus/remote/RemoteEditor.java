package com.focus.remote;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RemoteEditor extends Activity{

	private Remote remote;
	String remoteName;
	@TargetApi(11)
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
		final FragmentManager fm = getFragmentManager();
        final RemoteEditFragment editNameDialog = new RemoteEditFragment();
		OnClickListener ocl = new View.OnClickListener() {
			public void onClick(View v) {
				//showDialog(v.getId());
				
		        editNameDialog.show(fm, "fragment_edit_name");
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
		final CheckBox ctrlCheck = new CheckBox(this);
		ctrlCheck.setText("CTRL");
		final CheckBox altCheck = new CheckBox(this);
		altCheck.setText("ALT");
		final LinearLayout ll = new LinearLayout(this);
		ll.addView(edit);
		if(id != R.id.edit_select_title && id != R.id.edit_select_search){
			ll.addView(ctrlCheck);
			ll.addView(altCheck);
		}
		return new AlertDialog.Builder(this)
		//.setIconAttribute(android.R.attr.alertDialogIcon)
		.setTitle("Generic")
		.setView(ll)
		.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if(edit.getText().length() <= 0)
					return;
				String mods = "";
				if(ctrlCheck.isChecked())
					mods += "C";
				if(altCheck.isChecked())
					mods += "A";
				if(mods != "")
					mods += "^";
				switch(id){
				case R.id.edit_select_title:
					remote.title = edit.getText().toString();
					break;
				case R.id.edit_select_search:
					remote.search = edit.getText().toString();
					break;
				case R.id.edit_select_play:
					remote.play = mods + String.valueOf( (int) edit.getText().toString().toCharArray()[0] );
					break;
				case R.id.edit_select_previous:
					remote.previous = mods + String.valueOf( (int) edit.getText().toString().toCharArray()[0] );
					break;
				case R.id.edit_select_stop:
					remote.stop = mods + String.valueOf( (int) edit.getText().toString().toCharArray()[0] );
					break;
				case R.id.edit_select_next:
					remote.next = mods + String.valueOf( (int) edit.getText().toString().toCharArray()[0] );
					break;
				case R.id.edit_select_fullscreen:
					remote.fullscreen = mods + String.valueOf( (int) edit.getText().toString().toCharArray()[0] );
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
		tv.setText(remote.modifierParser(remote.play)[0]+remote.modifierParser(remote.play)[1]);

		tv = (TextView) findViewById(R.id.edit_display_previous);
		tv.setText(remote.modifierParser(remote.previous)[0]+remote.modifierParser(remote.previous)[1]);

		tv = (TextView) findViewById(R.id.edit_display_next);
		tv.setText(remote.modifierParser(remote.next)[0]+remote.modifierParser(remote.next)[1]);

		tv = (TextView) findViewById(R.id.edit_display_stop);
		tv.setText(remote.modifierParser(remote.stop)[0]+remote.modifierParser(remote.stop)[1]);

		tv = (TextView) findViewById(R.id.edit_display_fullscreen);
		tv.setText(remote.modifierParser(remote.fullscreen)[0]+remote.modifierParser(remote.fullscreen)[1]);
	}
	public void onFinishEditDialog(String string) {
		// TODO Auto-generated method stub
		
	}

}
