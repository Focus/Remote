package com.focus.remote;

import android.content.Context;


/**
 * @brief Holds information about a remote, e.g. which button to press for play.
 * 
 * A fairly basic class that holds information about which buttons correspond to which actions.
 * 
 * 
 */
public class Remote {
	public String title, search;
	public String play, stop, fullscreen, next, previous;
	
	public enum RemoteAction{
		PLAY,
		STOP,
		FULLSCREEN,
		NEXT,
		PREVIOUS;
	}
	
	public Remote(){
	}
	
	public Remote(String title, String search, String play, String stop, String fullscreen, String next, String previous){
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
	private String protocol(String action){
		return action + ">" + this.search + ".";
	}
	
	public String getProtocol(RemoteAction action){
		switch(action){
		case PLAY:
			return protocol(this.play);
		case STOP:
			return protocol(this.stop);
		case NEXT:
			return protocol(this.next);
		case FULLSCREEN:
			return protocol(this.fullscreen);
		case PREVIOUS:
			return protocol(this.previous);
		default:
			return ".";
		}
	}
	
	public String[] modifierParser(String item){
		if(item == null)
			return new String[]{"",""};
		String[] ret = new String[2];
		int i;
		if( (i = item.indexOf("^")) != -1){
			String mods = item.substring(0,i);
			ret[0] = "";
			for(int j = 0; j < mods.length(); j++){
				if(mods.charAt(j) == 'C')
					ret[0] += "CTRL ";
				else if(mods.charAt(j) == 'A')
					ret[0] += "ALT ";
			}
			int num = Integer.parseInt(item.substring(i+1));
			ret[1] = String.valueOf( ((char) num) );
		}
		else{
			ret[0] = "";
			int num = Integer.parseInt(item);
			ret[1] = "" + (char)num;
		}
		return ret;
	}
}
