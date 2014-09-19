package com.example.marlon.z_player.base;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.marlon.z_player.R;

/**
 * Created by Marlon on 9/18/2014.
 */
public class SongHolder{
    public TextView artist, title ,track, album;
    public FrameLayout art;
    public String path;
    private Activity activity;

    public SongHolder(Activity activity){
        this.activity=activity;
    }
    public void setMetaData(Cursor c){
        artist.setText(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
        title.setText(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        album.setText(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
        track.setText(c.getPosition()+"");
        String art_path= getAlbumArt(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY)));
        Drawable img;
        if ( art_path !=null) {
            img = Drawable.createFromPath(art_path);
        }else { img= activity.getResources().getDrawable(R.drawable.download);}
        art.setBackground(img);
    }

    private  String getAlbumArt(String id){
        ContentResolver cr=(activity).getContentResolver();
        Cursor temp=cr.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums.ALBUM_KEY+" = ?",
                new String[]{id},
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        temp.moveToFirst();
        return temp.getString(0);
    }
}
