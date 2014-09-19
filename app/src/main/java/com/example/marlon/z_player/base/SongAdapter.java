package com.example.marlon.z_player.base;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.marlon.z_player.R;

public class SongAdapter extends CursorAdapter {
	
	class ViewHolder{
		TextView name;
		TextView artist;
		TextView album;
	}
	 
	public SongAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view= inflater.inflate(R.layout.song_layout, parent,false);
        ViewHolder holder=new ViewHolder();
        holder.name=(TextView) view.findViewById(R.id.title);
        holder.artist=(TextView)view.findViewById(R.id.artist);
        holder.album=(TextView)view.findViewById(R.id.album);
        view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder= (ViewHolder) view.getTag();
		String name=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
		String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
		String album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
		holder.name.setText(name);
		holder.artist.setText(artist);
		holder.album.setText(album);
	}

}
