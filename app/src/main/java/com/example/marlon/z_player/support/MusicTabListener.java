package com.example.marlon.z_player.support;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.example.marlon.z_player.R;


public class MusicTabListener implements TabListener {
	private final Fragment fragment;
	private String tag;
	private FragmentManager fm;
	public MusicTabListener(Fragment fragment ,String tag,Activity activity) {
		this.fragment=fragment;
		this.tag=tag;
		this.fm= activity.getFragmentManager();
		
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		
		
		ft.replace(R.id.target, fragment,tag);
		

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		 
		//ft.remove(fragment);
		
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
