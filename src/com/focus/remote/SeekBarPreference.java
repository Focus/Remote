package com.focus.remote;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.preference.DialogPreference;
import android.widget.SeekBar;
import android.widget.TextView;


public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener
{
	private static final String androidns="http://schemas.android.com/apk/res/android";

	private SeekBar mSeekBar;
	private TextView mValueText;
	
	private int mDefault, mMax, mValue = 0;

	public SeekBarPreference(Context context, AttributeSet attrs) { 
		super(context,attrs); 

		mDefault = attrs.getAttributeIntValue(androidns,"defaultValue", 0);
		mMax = attrs.getAttributeIntValue(androidns,"max", 100);
		

	}
	@Override 
	protected View onCreateDialogView() {
		
		mValue = getPersistedInt(mDefault);
		
		LayoutInflater inflater = 
				(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pref_slider, null);

		
		mValueText = (TextView) view.findViewById(R.id.slider_numeric);
		mValueText.setText(Integer.toString(mValue));
		
		
		mSeekBar = (SeekBar) view.findViewById(R.id.slider);
		mSeekBar.setOnSeekBarChangeListener(this);
		mSeekBar.setMax(mMax);
		mSeekBar.setProgress(mValue);
		
		return view;
	}

	public void onProgressChanged(SeekBar seek, int value, boolean fromUser)
	{
		if(fromUser){
			mValue = value;
			mValueText.setText(Integer.toString(value));
		}
	}
	public void onStartTrackingTouch(SeekBar seek) {}
	public void onStopTrackingTouch(SeekBar seek) {}

	public int getProgress() { return mValue; }

	@Override
	protected void onDialogClosed (boolean res){
		super.onDialogClosed(res);
		if(!res){ //Cancel button
			return;
		}
		if(shouldPersist()){
			persistInt(mValue);
		}
		notifyChanged();
	}
	
	@Override
	public CharSequence getSummary() {
	    String summary = super.getSummary().toString();
	    int value = getPersistedInt(mDefault);
	    return String.format(summary, value);
	}

}