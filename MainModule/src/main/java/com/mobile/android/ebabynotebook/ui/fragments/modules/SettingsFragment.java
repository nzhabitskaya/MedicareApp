package com.mobile.android.ebabynotebook.ui.fragments.modules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobile.android.ebabynotebook.R;
import com.mobile.android.ebabynotebook.ui.tabs.IconTabActivity;
import com.mobile.android.withings.ui.fragment.RequestWeightDataFragment;
import com.mobile.android.withings.service.connection.OAuth;

public class SettingsFragment extends RequestWeightDataFragment {
	private View view;
	private Button app_settings;
	private Button weight_settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tab_settings, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view){
		app_settings = (Button) view.findViewById(R.id.title_app_settings);
		weight_settings = (Button) view.findViewById(R.id.title_weight_settings);
		app_settings.setOnClickListener(listener);
		weight_settings.setOnClickListener(listener);
	}

	View.OnClickListener listener = new View.OnClickListener(){
		public void onClick(View view) {
			switch(view.getId()){
				case R.id.title_app_settings:
					switchFragment(IconTabActivity.APP_SETTINGS_FRAGMENT);
					break;
				case R.id.title_weight_settings:
					requestOauthToken(OAuth.KEY, OAuth.SECRET);
					break;
			}
		}
	};

	private void switchFragment(int fragmentId){
		IconTabActivity activity = (IconTabActivity) getActivity();
		activity.replaceFragment(IconTabActivity.SETTINGS_FRAGMENT, fragmentId);
	}
}