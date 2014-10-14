package com.example.marlon.z_player.support;

import android.database.Cursor;

import com.example.marlon.z_player.base.SongHolder;

public interface ActivityReceiver {
	
	public void select(String select, String tag);
	public void setPlaylist(Cursor cursor);
    public void setHolder(SongHolder holder);

}
