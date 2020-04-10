package com.mobile.android.ebabynotebook.ui.fragments.modules;

import com.mobile.android.ebabynotebook.R;
import com.mobile.android.ebabynotebook.utils.DialogUtil;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class CallDoctorFragment extends Fragment {
	private Button callDoctorBtn;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_call_doctor, container, false);
        
        callDoctorBtn = (Button) rootView.findViewById(
				R.id.call_btn);
        callDoctorBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone = getPhoneFromPreferences();
				if(phone.length() > 0)
					callDoctor(phone);
				else
					DialogUtil.showErrorDialog(getActivity(), R.string.msg_phone_is_empty);
			}
		});
        
        checkIsButtonEnabled();
        return rootView;
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	checkIsButtonEnabled();
    }
    
    private void checkIsButtonEnabled(){
    	if(!isPhoneEnabled()){
        	callDoctorBtn.setEnabled(false);
        	callDoctorBtn.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.button_shape_disabled));
        } else {
        	callDoctorBtn.setEnabled(true);
        	callDoctorBtn.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.button_shape));
        }
    }
 
    private void callDoctor(String phone){
    	Intent callIntent = new Intent(Intent.ACTION_CALL);
    	callIntent.setData(Uri.parse("tel:" + phone));
    	startActivity(callIntent);
    }
    
    private String getPhoneFromPreferences() {
    	SharedPreferences prefs = getActivity().getSharedPreferences("prefs_tempmeter", Context.MODE_PRIVATE);
    	return prefs.getString("phone", "");
    }
    
    private boolean isPhoneEnabled() {
    	SharedPreferences prefs = getActivity().getSharedPreferences("prefs_tempmeter", Context.MODE_PRIVATE);
    	return prefs.getBoolean("alarm_phone", true);
    }
}