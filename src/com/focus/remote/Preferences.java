package com.focus.remote;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class Preferences extends PreferenceActivity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFrag()).commit();
	}
	@Override
	public void onStop(){
		super.onStop();
	}
	
	private class PrefFrag extends PreferenceFragment{
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}		
	}
}
