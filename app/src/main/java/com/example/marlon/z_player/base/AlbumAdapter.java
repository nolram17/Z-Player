package com.example.marlon.z_player.base;





import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marlon.z_player.R;

public class AlbumAdapter extends CursorAdapter {
	static class ViewHolder{
		RelativeLayout art;
		TextView name;
    }

	
	public AlbumAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder= (ViewHolder) view.getTag();
		String text=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
		String coverPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
		Drawable img;
		if (coverPath!=null){
			img = Drawable.createFromPath(coverPath);
		}
		else{
		    img= context.getResources().getDrawable(R.drawable.download);
		 }
		holder.name.setText(text);
		holder.art.setBackground(img);

		
		
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		 LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		 View view = inflater.inflate(R.layout.album_layout, parent, false);

		 ViewHolder holder= new ViewHolder();
		 holder.art= (RelativeLayout) view.findViewById(R.id.art);
		 holder.name=(TextView)view.findViewById(R.id.name);
		 view.setTag(holder);
		 
		return view;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}
	
	
}
