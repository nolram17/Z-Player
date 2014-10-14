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


public class ArtistAdapter extends CursorAdapter {

	public ArtistAdapter(Context context, Cursor c,int flags) {
		super(context, c, flags);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder=(ViewHolder)view.getTag();
        holder.artist.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		ViewHolder holder= new ViewHolder();
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.artist_layout, parent, false);
		holder.artist=(TextView)view.findViewById(R.id.name);
        view.setTag(holder);
		return view;
	}
	
	private class ViewHolder{
		TextView artist;
		TextView count;
	}
}
