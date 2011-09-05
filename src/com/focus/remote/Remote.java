package com.focus.remote;

import android.content.Context;


/**
 * @brief Holds information about a remote, e.g. which button to press for play.
 * 
 * A fairly basic class that holds information about which buttons correspond to which actions.
 * 
 * @todo Extend functionality to special keys, e.g. CTRL ALT etc.
 */
public class Remote {
	public String title, search;
	public int play, stop, fullscreen, next, previous;
	
	public enum RemoteAction{
		PLAY,
		STOP,
		FULLSCREEN,
		NEXT,
		PREVIOUS;
	}
	
	public Remote(){
	}
	
	public Remote(String title, String search, int play, int stop, int fullscreen, int next, int previous){
		this.title = title;
		this.search = search;
		this.play = play;
		this.stop = stop;
		this.fullscreen = fullscreen;
		this.next = next;
		this.previous = previous;
	}
	/**
	 * @brief Returns the current protocol.
	 * 
	 * The protocol is subject to change.
	 */
	private String protocol(int action){
		return action + ">" + this.search + ".";
	}
	
	public String getProtocol(RemoteAction action){
		switch(action){
		case PLAY:
			return protocol(this.play);
		case STOP:
			return protocol(this.stop);
		default:
			return ".";
		}
	}
}
