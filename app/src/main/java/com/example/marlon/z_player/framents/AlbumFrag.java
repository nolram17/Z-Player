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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.marlon.z_player.R;
import com.example.marlon.z_player.base.AlbumAdapter;
import com.example.marlon.z_player.support.ActivityReceiver;
import com.example.marlon.z_player.support.FragmentReceiver;

import java.util.ArrayList;


public class AlbumFrag extends Fragment implements FragmentReceiver {
	GridView covers;
	ActivityReceiver parentActivity;
	TextView title;
	ArrayList<String> albums;
	AlbumAdapter adapter;

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		  View view= inflater.inflate( R.layout.album_frag, container,false);
		  title= (TextView)view.findViewById(R.id.fragtitle);
		  covers= (GridView)view.findViewById(R.id.album_art);
		  parentActivity = (ActivityReceiver)getActivity();
		  title.setText(getTag());
		  covers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				Cursor temp= adapter.getCursor();
						temp.moveToPosition(position);
				parentActivity.select(temp.getString(temp.getColumnIndex(MediaStore.Audio.Albums.ALBUM)), getTag());
				
			}
		});

		  
		return  view;
	}

	@Override
	public void setCursor(Cursor cursor) {

		adapter = new AlbumAdapter((Context) parentActivity, cursor, 0);
		covers.setAdapter(adapter);
	}
	
	@Override
	public void setSearch(String variable) {

		if (variable!=null) {
			ContentResolver cr = ((ContextWrapper) parentActivity).getContentResolver();
			Cursor cursor = cr.query(
					MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
					MediaStore.Audio.Artists.ARTIST + " = ? ",
					new String[] { variable }, MediaStore.Audio.Albums.ALBUM);


			adapter.changeCursor(cursor);
			adapter.notifyDataSetChanged();
			
		}
		else

			Toast.makeText((Context) parentActivity, "null value", Toast.LENGTH_LONG);
		
		
	}
	
	

}
