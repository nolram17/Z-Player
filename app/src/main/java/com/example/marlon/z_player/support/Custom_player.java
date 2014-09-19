package com.example.marlon.z_player.support;

import android.database.Cursor;
import android.media.MediaPlayer;

public class Custom_player extends MediaPlayer {
	Cursor cursor;
	public Custom_player(Cursor c) {
		super();
		this.cursor=c;
	}  

}
