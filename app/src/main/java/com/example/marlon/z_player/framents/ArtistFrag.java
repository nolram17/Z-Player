package com.example.marlon.z_player.framents;


import android.app.Fragment;
import android.content.Context;
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
import com.example.marlon.z_player.base.ArtistAdapter;
import com.example.marlon.z_player.support.ActivityReciever;
import com.example.marlon.z_player.support.FragmentReciever;

import java.util.ArrayList;


public class ArtistFrag extends Fragment implements FragmentReciever {
	
	ArrayList<String> artist;
	ArrayAdapter<String> adapter;
	ListView list;
	ActivityReciever parent;
	TextView title;
    ArtistAdapter dapter;

	
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		    View view=inflater.inflate(R.layout.artist_frag,container,false);
		    list=(ListView)view.findViewById(R.id.artistlist);
		    title= (TextView)view.findViewById(R.id.fragtitle);
		    parent=(ActivityReciever)getActivity();
		    title.setText(getTag());
		    list.setOnItemClickListener(new OnItemClickListener() {
		    	
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
                    Cursor temp=dapter.getCursor();
                    temp.moveToPosition(position);
                    parent.select(temp.getString(temp.getColumnIndex(MediaStore.Audio.Artists.ARTIST)),getTag());


				}
			});
		return view;
	}
	

	@Override
	public void setCursor(Cursor cursor) {
		artist=new ArrayList<String>();
		while(cursor.moveToNext()){
			artist.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
		}
	    adapter= new ArrayAdapter<String>((Context) parent, android.R.layout.simple_list_item_1,artist);
        dapter= new ArtistAdapter((Context)parent,cursor,0);
	    list.setAdapter(dapter);
	}

	@Override
	public void setSearch(String variable) {
		// TODO Auto-generated method stub
		
	}
	

	
	
	
	
}
