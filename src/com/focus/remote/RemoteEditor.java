package com.focus.remote;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
		
	}

	/*
	 * When the user presses the Save/Disacard button.
	 */
	public void exitButtonPress(View button){
		if(button.getId() == R.id.remote_save){
			EditText et;
			CheckBox cb;
			
			et = (EditText) findViewById(R.id.remote_edit_title);
			remote.title = et.getText().toString();
			
			et = (EditText) findViewById(R.id.remote_edit_search);
			remote.search = et.getText().toString();
			
			if(remote.title == null || remote.search == null){
				Toast.makeText(this, "You haven't finished yet!", Toast.LENGTH_LONG).show();
				return;
			}
			
			if(remote.title.length() <= 0 || remote.search.length() <= 0){
				Toast.makeText(this, "You haven't finished yet!", Toast.LENGTH_LONG).show();
				return;
			}
			
			cb = (CheckBox) findViewById(R.id.remote_edit_play_ctrl);
			remote.play="";
			if(cb.isChecked())
				remote.play += "C";
			cb = (CheckBox) findViewById(R.id.remote_edit_play_alt);
			if(cb.isChecked())
				remote.play += "A";
			if(remote.play.length()>0)
				remote.play += "^";
			et = (EditText) findViewById(R.id.remote_edit_play);
			remote.play += et.getText().toString();
			
			cb = (CheckBox) findViewById(R.id.remote_edit_previous_ctrl);
			remote.previous="";
			if(cb.isChecked())
				remote.previous += "C";
			cb = (CheckBox) findViewById(R.id.remote_edit_previous_alt);
			if(cb.isChecked())
				remote.previous += "A";
			if(remote.previous.length()>0)
				remote.previous += "^";
			et = (EditText) findViewById(R.id.remote_edit_previous);
			remote.previous += et.getText().toString();
			
			cb = (CheckBox) findViewById(R.id.remote_edit_next_ctrl);
			remote.next="";
			if(cb.isChecked())
				remote.next += "C";
			cb = (CheckBox) findViewById(R.id.remote_edit_next_alt);
			if(cb.isChecked())
				remote.next += "A";
			if(remote.next.length()>0)
				remote.next += "^";
			et = (EditText) findViewById(R.id.remote_edit_next);
			remote.next += et.getText().toString();
			
			cb = (CheckBox) findViewById(R.id.remote_edit_stop_ctrl);
			remote.stop="";
			if(cb.isChecked())
				remote.stop += "C";
			cb = (CheckBox) findViewById(R.id.remote_edit_stop_alt);
			if(cb.isChecked())
				remote.stop += "A";
			if(remote.stop.length()>0)
				remote.stop += "^";
			et = (EditText) findViewById(R.id.remote_edit_stop);
			remote.stop += et.getText().toString();
			
			cb = (CheckBox) findViewById(R.id.remote_edit_fs_ctrl);
			remote.fullscreen="";
			if(cb.isChecked())
				remote.fullscreen += "C";
			cb = (CheckBox) findViewById(R.id.remote_edit_fs_alt);
			if(cb.isChecked())
				remote.fullscreen += "A";
			if(remote.fullscreen.length()>0)
				remote.fullscreen += "^";
			et = (EditText) findViewById(R.id.remote_edit_fs);
			remote.fullscreen += et.getText().toString();
			
			
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
		EditText et;
		CheckBox cb;
		int i;
		
		et = (EditText) findViewById(R.id.remote_edit_title);
		et.setText(remote.title);
		
		et = (EditText) findViewById(R.id.remote_edit_search);
		et.setText(remote.search);
		
		et = (EditText) findViewById(R.id.remote_edit_play);
		i = remote.play.indexOf("^");
		if(i != -1){
			if(remote.play.substring(0, i).contains("C")){
				cb = (CheckBox) findViewById(R.id.remote_edit_play_ctrl);
				cb.setChecked(true);
			}
			if(remote.play.substring(0, i).contains("A")){
				cb = (CheckBox) findViewById(R.id.remote_edit_play_alt);
				cb.setChecked(true);
			}
			et.setText(remote.play.substring(i+1));
		}
		else
			et.setText(remote.play);
		
		et = (EditText) findViewById(R.id.remote_edit_previous);
		i = remote.previous.indexOf("^");
		if(i != -1){
			if(remote.previous.substring(0, i).contains("C")){
				cb = (CheckBox) findViewById(R.id.remote_edit_previous_ctrl);
				cb.setChecked(true);
			}
			if(remote.previous.substring(0, i).contains("A")){
				cb = (CheckBox) findViewById(R.id.remote_edit_previous_alt);
				cb.setChecked(true);
			}
			et.setText(remote.previous.substring(i+1));
		}
		else
			et.setText(remote.previous);
		
		et = (EditText) findViewById(R.id.remote_edit_next);
		i = remote.next.indexOf("^");
		if(i != -1){
			if(remote.next.substring(0, i).contains("C")){
				cb = (CheckBox) findViewById(R.id.remote_edit_next_ctrl);
				cb.setChecked(true);
			}
			if(remote.next.substring(0, i).contains("A")){
				cb = (CheckBox) findViewById(R.id.remote_edit_next_alt);
				cb.setChecked(true);
			}
			et.setText(remote.next.substring(i+1));
		}
		else
			et.setText(remote.next);
		
		et = (EditText) findViewById(R.id.remote_edit_stop);
		i = remote.stop.indexOf("^");
		if(i != -1){
			if(remote.stop.substring(0, i).contains("C")){
				cb = (CheckBox) findViewById(R.id.remote_edit_stop_ctrl);
				cb.setChecked(true);
			}
			if(remote.stop.substring(0, i).contains("A")){
				cb = (CheckBox) findViewById(R.id.remote_edit_stop_alt);
				cb.setChecked(true);
			}
			et.setText(remote.stop.substring(i+1));
		}
		else
			et.setText(remote.stop);
		
		et = (EditText) findViewById(R.id.remote_edit_fs);
		i = remote.fullscreen.indexOf("^");
		if(i != -1){
			if(remote.fullscreen.substring(0, i).contains("C")){
				cb = (CheckBox) findViewById(R.id.remote_edit_fs_ctrl);
				cb.setChecked(true);
			}
			if(remote.fullscreen.substring(0, i).contains("A")){
				cb = (CheckBox) findViewById(R.id.remote_edit_fs_alt);
				cb.setChecked(true);
			}
			et.setText(remote.fullscreen.substring(i+1));
		}
		else
			et.setText(remote.fullscreen);
	}
	public void onFinishEditDialog(String string) {
		// TODO Auto-generated method stub
		
	}

}
