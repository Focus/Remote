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
	public void tcp(View button){
		startActivity(new Intent(this, Ipscreen.class));
	}
	public void instructions(View button){
		startActivity(new Intent(this, Instruct.class));
	}
	public void pref(View button){
		startActivity(new Intent(this, Preferences.class));
	}
}