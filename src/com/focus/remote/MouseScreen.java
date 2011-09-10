package com.focus.remote;


import java.io.PrintWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;

public class MouseScreen extends Activity implements OnGestureListener, OnKeyListener {
	
	private GestureDetector gesture;
	private float dX, dY;
	private PrintWriter out;
	private double sens;
	private boolean drag;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TCPConnect app = (TCPConnect) getApplication();
        out = app.getPrintWriter();
        sens = ((double) app.getSens())/10;
        gesture = new GestureDetector(this);
        setContentView(R.layout.mousescreen);
        drag = false;
    }
	public boolean onTouchEvent(MotionEvent em){
		if(em.getAction() == MotionEvent.ACTION_DOWN){
			dX = em.getX();
			dY = em.getY();
		}
		else if(em.getAction() == MotionEvent.ACTION_UP && drag){
			sendString("!!.");
			drag = false;
		}
		else if(dX != em.getX() || dY != em.getY()){
			sendString((int)((em.getX()-dX)*sens) + "," + (int)((em.getY()-dY)*sens) + ".");
			dX = em.getX();
			dY = em.getY();
		}
		gesture.onTouchEvent(em);
		return true;
	}
	public boolean onDown(MotionEvent e) {
		return false;
	}
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}
	public void onLongPress(MotionEvent e) {
		sendString("!!.");
		drag = true;
	}
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}
	public void onShowPress(MotionEvent e) {
	}
	public boolean onSingleTapUp(MotionEvent em) {
		sendString("!!.!!.");
		return true;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mouse_menu, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.keyboard:
	    	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    	imm.toggleSoftInput(0 ,InputMethodManager.SHOW_IMPLICIT);
	        return true;
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
	@Override
	public boolean onKeyUp(int keycode, KeyEvent kev){
		if(keycode == 67) //Backspace
			sendString(-1987+".");
		if(kev.getDisplayLabel() != '\u0000')
			sendString(kev.getUnicodeChar()+".");
		return super.onKeyUp(keycode, kev);
	}
	@Override
	public void onResume(){
		super.onResume();
		TCPConnect app = (TCPConnect) getApplication();
        app.sync();
		out = app.getPrintWriter();
		sens = ((double) app.getSens())/10;
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		return false;
	}
	
	void sendString(String s){
		try{
			out.println(s);
		}catch(Exception e){
			new AlertDialog.Builder(this).setTitle("Error!").setMessage(e.getMessage()).show();
			TCPConnect app = (TCPConnect) getApplication();
			app.flush();
			this.finish();
		}
	}
}