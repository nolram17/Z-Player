package com.example.marlon.z_player.framents;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.marlon.z_player.R;
import com.example.marlon.z_player.base.MainActivity;
import com.example.marlon.z_player.base.SongAdapter;
import com.example.marlon.z_player.base.SongHolder;
import com.example.marlon.z_player.support.ActivityReciever;
import com.example.marlon.z_player.support.FragmentReciever;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class PlayerFrag extends Fragment implements FragmentReciever, OnClickListener ,CompoundButton.OnCheckedChangeListener,AudioManager.OnAudioFocusChangeListener {
	Button next,back, openList;
	MediaPlayer mediaPlayer;
	Cursor playlist;
	SongHolder holder;
    SeekBar seekBar;
    SeekBarTask seekTask;
    ToggleButton shuffle_toggle ,play;
    ArrayList<Integer>shuffle_indexes;
    int playlistIndex, delay = 1000;
    TextView duration, songprogress;
    private MainActivity parentActivity;
    AudioManager audioManager;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.player_frag,container,false);
		init_controls(view);
        audioManager= (AudioManager)parentActivity.getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(this,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);

		return view;
	}
	private void init_controls(View view) {
		play= (ToggleButton)view.findViewById(R.id.play_pause);
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

        songprogress =(TextView)view.findViewById(R.id.songprogress);
        duration=(TextView)view.findViewById(R.id.duration);
        shuffle_toggle =(ToggleButton)view.findViewById(R.id.shuffle);

		play.setOnCheckedChangeListener(this);
		next.setOnClickListener(this);
		back.setOnClickListener(this);
        openList.setOnClickListener(this);

        //Background activity for tracking the sound progress
        seekTask=new SeekBarTask();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean wasPlaying;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!= null && fromUser){
                    mediaPlayer.seekTo(progress);
                    songprogress.setText(getTimeString(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                wasPlaying=mediaPlayer.isPlaying();
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                 if(wasPlaying){mediaPlayer.start();}
            }
        });
	}

    private  void updateSeekbar(){
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        duration.setText(getTimeString(mediaPlayer.getDuration()));
        songprogress.setText(getTimeString(mediaPlayer.getCurrentPosition()));
    }

    @Override
	public void setCursor(Cursor cursor) {
		playlist=cursor;
		playlistIndex =playlist.getPosition();
		String path= playlist.getString(playlist.getColumnIndex(MediaStore.Audio.Media.DATA));
        shuffle_indexes= new ArrayList<Integer>();
        for (int i=0;i<playlist.getCount();i++)
        {
            shuffle_indexes.add(i);
        }
        Collections.shuffle(shuffle_indexes);
        holder.setMetaData(playlist);
        if(!play.isChecked()){play.toggle();}
		prepSource(path);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer.getCurrentPosition()>0&&mediaPlayer!=null){next();}
            }
        });
        shuffle_toggle.setOnCheckedChangeListener(this);
        ListView current= (ListView)parentActivity.findViewById(R.id.currentPlaylist);
        current.setAdapter(new SongAdapter(parentActivity,playlist,0));


    }

	private void play() {


        if (!play.isChecked()){
            if(mediaPlayer.isPlaying())
                mediaPlayer.pause();

        }
        else
            mediaPlayer.start();


	}
	@Override
	public void setSearch(String variable) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
        if (mediaPlayer!=null) {
            switch (v.getId()) {

                case R.id.next:
                    next();
                    break;
                case R.id.back:
                    back();
                    break;

                case R.id.openplaylist:
                    ((DrawerLayout)parentActivity.findViewById(R.id.drawer)).openDrawer(Gravity.RIGHT);
                    break;
            }
        }else{
            Toast.makeText(parentActivity,"No media selected",Toast.LENGTH_LONG).show();}
	}

	private void back() {
        playlistIndex = (playlistIndex - 1) % playlist.getCount();
        if (playlistIndex<0){playlistIndex=playlist.getCount()-1;}
        if(shuffle_toggle.isChecked()){
           playlist.moveToPosition(shuffle_indexes.get(playlistIndex));
        }
        else {
                playlist.moveToPosition(playlistIndex);
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
            seekTask.execute();
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
        playlistIndex =(playlistIndex +1)%(playlist.getCount());
		if(shuffle_toggle.isChecked()){
			playlist.moveToPosition(shuffle_indexes.get(playlistIndex));
		}else {
            playlist.moveToPosition(playlistIndex);
        }
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

    public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
        switch (compoundButton.getId()){

            case R.id.shuffle:
                if (on) {
                    Collections.shuffle(shuffle_indexes);
                } else {
                    playlistIndex = Integer.parseInt(holder.track.getText().toString());
                }
                break;
            case R.id.play_pause:
                play();
                break;
        }



    }

    @Override
    public void onAudioFocusChange(int focus) {
        boolean wasplaying=play.isChecked();
        if(focus<=0){
            if(play.isChecked())
                {play.toggle();}
        }
        else {
            if(wasplaying)
                {play.toggle();}
        }
    }

    class SeekBarTask extends AsyncTask<Void, Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            while(!this.isCancelled()) {
                publishProgress();
                try {
                    Thread.sleep(delay);
                }
                catch (InterruptedException e) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            updateSeekbar();
        }
    }

}
