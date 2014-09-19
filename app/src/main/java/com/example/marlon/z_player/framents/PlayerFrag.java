package com.example.marlon.z_player.framents;

import android.app.Dialog;
import android.app.Fragment;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marlon.z_player.R;
import com.example.marlon.z_player.base.MainActivity;
import com.example.marlon.z_player.base.SongHolder;
import com.example.marlon.z_player.support.ActivityReciever;
import com.example.marlon.z_player.support.FragmentReciever;

import java.io.IOException;

public class PlayerFrag extends Fragment implements FragmentReciever, OnClickListener {
	Button next,back, play,openList;
	MediaPlayer mediaPlayer;
	Cursor playlist;
	SongHolder holder;
    SeekBar seekBar;
    Handler seekHandler = new Handler();
    Thread seekThread;
    int pos, delay = 1000;
    TextView duration,songprogess;
    private MainActivity parentActivity;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.player_frag,container,false);
		init_controls(view);

		return view;
	}
	private void init_controls(View view) {
		play= (Button)view.findViewById(R.id.play_pause);
		next=(Button)view.findViewById(R.id.next);
		back=(Button)view.findViewById(R.id.back);
        openList=(Button)view.findViewById(R.id.openplaylist);
		mediaPlayer= new MediaPlayer();
        parentActivity=(MainActivity)getActivity();
        seekBar=(SeekBar)view.findViewById(R.id.seekBar);

		holder= new SongHolder(parentActivity);
		holder.title=(TextView)view.findViewById(R.id.title);
		holder.artist=(TextView)view.findViewById(R.id.artist);
		holder.album=(TextView)view.findViewById(R.id.album);
		holder.track=(TextView)view.findViewById(R.id.track);
        holder.art=(FrameLayout)view.findViewById(R.id.art);

        songprogess=(TextView)view.findViewById(R.id.songprogress);
        duration=(TextView)view.findViewById(R.id.duration);

		play.setOnClickListener(this);
		next.setOnClickListener(this);
		back.setOnClickListener(this);
        openList.setOnClickListener(this);

        /*seekThread= new Thread( new Runnable() {
            @Override
            public void run() {
                while(mediaPlayer!=null &&seekBar.getProgress()!=seekBar.getMax());{
                    try{
                        seekThread.sleep(delay);
                        updateSeekbar();
                    }
                    catch (InterruptedException e){
                        return;
                    }


                }
            }
        });*/




        seekHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    if(mediaPlayer.isPlaying()){
                        updateSeekbar();
                    }
                    seekHandler.postDelayed(this, delay);
                }
            }
        }, delay);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!= null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();

            }
        });
	}

    private  void updateSeekbar(){
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        duration.setText(getTimeString(mediaPlayer.getDuration()));
        songprogess.setText(getTimeString(mediaPlayer.getCurrentPosition()));
    }

    @Override
	public void setCursor(Cursor cursor) {
		playlist=cursor;

		pos=playlist.getPosition();
		//trackCount=playlist.getCount();
		String path= playlist.getString(playlist.getColumnIndex(MediaStore.Audio.Media.DATA));

		holder.setMetaData(playlist);
		prepSource(path);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer!=null){next();}
            }
        });




	}

	private void play() {
        Drawable img;
		if(mediaPlayer.isPlaying()){
           img=parentActivity.getResources().getDrawable(R.drawable.ic_play);
            mediaPlayer.pause();

        }
		else{
            img= parentActivity.getResources().getDrawable(R.drawable.ic_pause);
            mediaPlayer.start();

        }
        play.setBackground(img);
	}
	@Override
	public void setSearch(String variable) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
        if (mediaPlayer!=null) {
            switch (v.getId()) {

                case R.id.play_pause:
                    play();
                    break;
                case R.id.next:
                    next();
                    break;
                case R.id.back:
                    back();
                    break;

                case R.id.openplaylist:
                    parentActivity.viewswitcher.showPrevious();
                    // showList();
                    break;
            }
        }else{
            Toast.makeText(parentActivity,"No media selected",Toast.LENGTH_LONG);}
	}
    private  void showList(){
        Dialog dialog= new Dialog(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View list= inflater.inflate(R.layout.now_playing_layout,null,false);

        dialog.setContentView(list);
        dialog.show();

    }
	private void back() {
        try {
            pos = (pos - 1) % playlist.getCount();
            playlist.moveToPosition(pos);
        }
        catch (CursorIndexOutOfBoundsException e) {
            pos = playlist.getCount() - 1;
            playlist.moveToPosition(pos);
        }
        String path = playlist.getString(playlist.getColumnIndex(MediaStore.Audio.Media.DATA));
        holder.setMetaData(playlist);
        prepSource(path);
	}
	private void prepSource(String path) {
		
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
            ActivityReciever reciever= (ActivityReciever)getActivity();
            reciever.setHolder(holder);
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		play();
		
	}
	private void next() {
        pos=(pos+1)%(playlist.getCount());
		playlist.moveToPosition(pos);
		/*if(playlist.isAfterLast()){
			playlist.moveToFirst();
		}*/
		String path= playlist.getString(playlist.getColumnIndex(MediaStore.Audio.Media.DATA));
		holder.setMetaData(playlist);
		prepSource(path);
	}

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf
                .append(String.format("%02d", hours))
                .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }


}
