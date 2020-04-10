package com.mobile.android.ebabynotebook.ui.tabs;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;

import com.mobile.android.ebabynotebook.R;


public class TabListener implements ActionBar.TabListener {

	Fragment fragment;

	public TabListener(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.fragment_container, fragment);
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}
}
