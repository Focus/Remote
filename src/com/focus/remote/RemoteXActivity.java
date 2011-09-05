package com.focus.remote;

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
			Intent intent =new Intent(this, Ipscreen.class);
			intent.putExtra("Direction", 0);
			startActivity(intent);
			break;
		case R.id.remote:
			Intent intent1 =new Intent(this, Ipscreen.class);
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