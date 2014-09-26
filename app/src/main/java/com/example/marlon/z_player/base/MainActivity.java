package com.example.marlon.z_player.base;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.example.marlon.z_player.R;
import com.example.marlon.z_player.framents.AlbumFrag;
import com.example.marlon.z_player.framents.ArtistFrag;
import com.example.marlon.z_player.framents.PlayerFrag;
import com.example.marlon.z_player.framents.SongFrag;
import com.example.marlon.z_player.support.ActivityReciever;
import com.example.marlon.z_player.support.FragmentReciever;


public class MainActivity extends Activity implements LoaderCallbacks<Cursor>,
        ActivityReciever,AdapterView.OnItemClickListener {


	ActionBar actionbar;
	FragmentManager fmanager;
    Cursor cursor;
	ViewFlipper viewfliper;
	public ViewSwitcher viewswitcher;

    TextView title;
    TextView artist;
    ImageView art;


	private static final int LOADER_songs = 1;
	private static final int LOADER_artist = 2;
	private static final int LOADER_albums = 3;

	private final String albumbTag = "Albums";
	private final String artsitTag = "Artists";
	private final String songTag = "Songs";
	private final String playerTag = "Player";
    DrawerLayout drawerLayout;
    ListView drawerList;
    String[] drawerOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection);

		getLoaderManager().initLoader(LOADER_songs, null, this);
		getLoaderManager().initLoader(LOADER_artist, null, this);
		getLoaderManager().initLoader(LOADER_albums, null, this);

		fmanager = getFragmentManager();
        actionbar=getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        title= (TextView)findViewById(R.id.title);
        title.setMovementMethod(new ScrollingMovementMethod());
        artist=(TextView)findViewById(R.id.name);
        art=(ImageView)findViewById(R.id.art);

		attachfrags();
        drawerOptions=getResources().getStringArray(R.array.options);
        drawerList=(ListView)findViewById(R.id.drawer_options);
        drawerList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,drawerOptions));
        drawerList.setOnItemClickListener(this);
		viewswitcher=(ViewSwitcher)findViewById(R.id.change);
		viewfliper = (ViewFlipper) findViewById(R.id.target);
		RelativeLayout miniPlayer= (RelativeLayout)findViewById(R.id.nowPlaying);
		miniPlayer.setOnClickListener(new OnClickListener() {
						
			@Override
			public void onClick(View v) {
				viewswitcher.showNext();
				
			}
		});
		
		viewfliper.setInAnimation(this,R.anim.push_right_in);
		viewfliper.setOutAnimation(this,R.anim.push_right_out );
        viewswitcher.setInAnimation(this,R.anim.fadein);
        viewswitcher.setOutAnimation(this, R.anim.fadeout);

	}


    private void attachfrags() {
		fmanager.beginTransaction()
				.add(R.id.mainPlayer, new PlayerFrag(), playerTag)
				.add(R.id.artist_Frame, new ArtistFrag(), artsitTag)
				.add(R.id.album_Frame, new AlbumFrag(), albumbTag)
				.add(R.id.song_Frame, new SongFrag(), songTag).commit();

	}

	@Override
	public void onBackPressed() {
       // switch ()
		if (viewfliper.indexOfChild(viewfliper.getCurrentView())>0 && viewswitcher.indexOfChild(viewswitcher.getCurrentView())<1){
			viewfliper.showPrevious();
		}
		else
			super.onBackPressed();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch (id) {

		case LOADER_songs:
			return new CursorLoader(this,
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Audio.Media.TITLE);
		case LOADER_artist:
			return new CursorLoader(this,
					MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Audio.Artists.ARTIST);
		case LOADER_albums:
			return new CursorLoader(this,
					MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Audio.Albums.ALBUM);
		default:
			return null;
		}

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		this.cursor = cursor;
		String tag;
		switch (loader.getId()) {
		case LOADER_songs:
			tag = songTag;
			break;
		case LOADER_artist:
			tag = artsitTag;

			break;
		case LOADER_albums:
			tag = albumbTag;
			break;
		default:
			tag = null;

		}

		if (tag != null) {
			((FragmentReciever) fmanager.findFragmentByTag(tag)).setCursor(cursor);
		}
		Toast.makeText(this, "Loading complete for " + tag, Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void select(String select, String tag) {
		viewfliper.showNext();

		if (tag == albumbTag ) {
			
			try {
				((FragmentReciever) fmanager.findFragmentByTag(songTag))
						.setSearch(select);
			} 
			catch (ClassCastException e) {

			}
		}
		if (tag==artsitTag){
			try {
				((FragmentReciever) fmanager.findFragmentByTag(albumbTag))
						.setSearch(select);
			} 
			catch (ClassCastException e) {

			}
			
		}
		
	}

	@Override
	public void setPlaylist(Cursor cursor) {
		viewswitcher.showNext();
		((FragmentReciever)fmanager.findFragmentByTag(playerTag)).setCursor(cursor);
	}

    @Override
    public void setHolder(SongHolder holder) {
        title.setText(holder.title.getText());
        artist.setText(holder.artist.getText());
        art.setImageDrawable(holder.img);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (viewswitcher.getCurrentView().getId()==R.id.mainPlayer){viewswitcher.setDisplayedChild(0);}
        viewfliper.setDisplayedChild(position);
        //drawerLayout.closeDrawer;

    }
}
