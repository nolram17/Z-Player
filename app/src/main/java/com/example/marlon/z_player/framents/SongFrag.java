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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.example.marlon.z_player.R;
import com.example.marlon.z_player.base.SongAdapter;
import com.example.marlon.z_player.support.ActivityReciever;
import com.example.marlon.z_player.support.FragmentReciever;

import java.util.ArrayList;
import java.util.HashMap;

public class SongFrag extends Fragment implements FragmentReciever {

	HashMap<String, String> songlist;
	ArrayList<String> songs;
	ArrayAdapter<String> adapter;
	ArrayList<String> paths;
	ListView list;
	ActivityReciever reciever;
	TextView title;
	
	SongAdapter dapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.song_frag,container,false);
	    list=(ListView)v.findViewById(R.id.artistlist);
	    reciever=(ActivityReciever)getActivity();
	   
	    title= (TextView)v.findViewById(R.id.fragtitle);
	    title.setText(getTag());
	    list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor c=dapter.getCursor();
				c.moveToPosition(position);
				reciever.setPlaylist(c);
				
			}});
	    
	    return v;
	}

	@Override
	public void setCursor(Cursor cursor) {
		songs=new ArrayList<String>();
		while(cursor.moveToNext()){
			songs.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
		}
		adapter= new ArrayAdapter<String>((Context) reciever, android.R.layout.simple_list_item_1,songs);
	    dapter=new SongAdapter((Context) reciever, cursor, 0);
		list.setAdapter(dapter);
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
			ArrayList<String> temp = new ArrayList<String>();
			while (cursor.moveToNext()) {
				temp.add(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.TITLE)));
			}
			songs.clear();
			songs.addAll(temp);
			adapter.notifyDataSetChanged();
			dapter.changeCursor(cursor);
			
		}
	}
	
	
}