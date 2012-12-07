package com.focus.remote;




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
		title = "";
		search = "";
		play = "";
		previous = "";
		next = "";
		stop = "";
		fullscreen = "";
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
	 * The protocol is subject to change. Currently it is [modifiers]^[letter in unicode] > [search parameter].
	 * 
	 * Modifiers can be C for CTRL or A for ALT or possibly both. For example, sending CTRL+ALT+w to firefox would be
	 * <code>
	 * CA^119>firefox.
	 * </code>
	 */
	public String protocol(String action){
		int i = action.indexOf("^");
		if(action == null || action.length()<1)
			return null;
		if(i != -1){
			
			String uni = Integer.toString( action.charAt(i+1));
			return action.substring(0,i+1) + uni + ">" + this.search + ".";
		}
		else{
			String uni = Integer.toString( action.charAt(0) );
			return uni + ">" + this.search + ".";
		}
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
}
