package com.example.marlon.z_player.framents;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


import com.example.marlon.z_player.R;
import com.example.marlon.z_player.base.SongAdapter;
import com.example.marlon.z_player.support.ActivityReceiver;
import com.example.marlon.z_player.support.FragmentReceiver;

public class SongFrag extends Fragment implements FragmentReceiver {


	ListView list;
	ActivityReceiver reciever;
	TextView title;
	
	SongAdapter songAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.song_frag,container,false);
	    list=(ListView)v.findViewById(R.id.artistlist);
	    reciever=(ActivityReceiver)getActivity();
	   
	    title= (TextView)v.findViewById(R.id.fragtitle);
	    title.setText(getTag());
	    list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor c= songAdapter.getCursor();
				c.moveToPosition(position);
				reciever.setPlaylist(c);
				
			}});
	    
	    return v;
	}

	@Override
	public void setCursor(Cursor cursor) {

	    songAdapter =new SongAdapter((Context) reciever, cursor, 0);
		list.setAdapter(songAdapter);
	}

	@Override
	public void setSearch(String variable) {
		if (variable!=null) {
			ContentResolver cr = ((ContextWrapper) reciever).getContentResolver();
			Cursor cursor = cr.query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
					MediaStore.Audio.Media.ALBUM + " = ? ",
					new String[] { variable },
					MediaStore.Audio.Media.TRACK);
			cursor.moveToFirst();
			songAdapter.changeCursor(cursor);
			
		}
	}
	
	
}