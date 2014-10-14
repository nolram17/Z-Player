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
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.marlon.z_player.R;
import com.example.marlon.z_player.base.AlbumAdapter;
import com.example.marlon.z_player.base.ArtistAdapter;
import com.example.marlon.z_player.base.SongAdapter;
import com.example.marlon.z_player.base.SongHolder;
import com.example.marlon.z_player.support.ActivityReceiver;
import com.example.marlon.z_player.support.FragmentReceiver;

import java.util.ArrayList;
import java.util.MissingResourceException;


public class ArtistFrag extends Fragment implements FragmentReceiver ,OnItemClickListener{
	

	ListView artistlist,songlist;
    GridView albumlist;
	ActivityReceiver parent;
	TextView title;
    ArtistAdapter artistAdapter;
    SongAdapter songAdapter;
    AlbumAdapter albumAdapter;
    ViewFlipper viewFlipper;


	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		    View view=inflater.inflate(R.layout.artist_frag,container,false);

		    artistlist =(ListView)view.findViewById(R.id.artistlist);
            albumlist=(GridView)view.findViewById(R.id.albumList);
            songlist=(ListView)view.findViewById(R.id.songlist);
		    parent=(ActivityReceiver)getActivity();
            viewFlipper=(ViewFlipper)view.findViewById(R.id.viewFlipper);
            artistlist.setOnItemClickListener(this);
            albumlist.setOnItemClickListener(this);
            songlist.setOnItemClickListener(this);

		return view;
	}



    @Override
	public void setCursor(Cursor cursor) {

        artistAdapter = new ArtistAdapter((Context)parent,cursor,0);
	    artistlist.setAdapter(artistAdapter);
	}

	@Override
	public void setSearch(String variable) {
		// TODO Auto-generated method stub
		
	}
    private  void getSelection(String variable,int id) {
        String WHERE;
        String[] selection;

        if (variable != null) {
            ContentResolver cr = ((Context) parent).getContentResolver();
            Cursor cursor;
            Context context= (Context)parent;
            selection=new String[]{variable};
            switch (id){
                case R.id.artistlist:

                    cursor = cr.query(
                            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null,
                            MediaStore.Audio.Artists.ARTIST + " = ? ",
                            selection, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
                    albumAdapter= new AlbumAdapter(context,cursor,0);
                    albumlist.setAdapter(albumAdapter);
                    viewFlipper.showNext();

                    break;
                case R.id.albumList:
                    cursor= cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                            MediaStore.Audio.Media.ALBUM+" = ? ",
                            selection,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                    songAdapter=new SongAdapter(context,cursor,0);
                    songlist.setAdapter(songAdapter);
                    viewFlipper.showNext();
                    break;
                case R.id.songlist:
                    parent.setPlaylist(songAdapter.getCursor());
                    break;

            }


        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Cursor temp=((CursorAdapter)adapterView.getAdapter()).getCursor();
        temp.moveToPosition(position);
        String colmn;
        switch (adapterView.getId()){
            case R.id.artistlist:
                colmn=MediaStore.Audio.Artists.ARTIST;
                break;
            case R.id.albumList:
                colmn=MediaStore.Audio.Albums.ALBUM;
                break;

            default:
                colmn= MediaStore.Audio.Media.TITLE;
        }



        String item=temp.getString(temp.getColumnIndex(colmn));
        //getSelection(item,adapterView.getId());
        parent.select(temp.getString(temp.getColumnIndex(MediaStore.Audio.Artists.ARTIST)), getTag());
    }
}
