package com.example.marlon.z_player.support;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.example.marlon.z_player.R;


public class FragmentTabListener implements TabListener {
	private final Fragment fragment;
	public FragmentTabListener(Fragment fragment, String tag, Activity activity) {
		this.fragment=fragment;

		
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		
		ft.show(fragment);

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		 ft.hide(fragment);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
