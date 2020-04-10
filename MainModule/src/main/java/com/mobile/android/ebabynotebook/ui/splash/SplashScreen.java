package com.mobile.android.ebabynotebook.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mobile.android.ebabynotebook.R;
import com.mobile.android.ebabynotebook.ui.menu.MenuActivity;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(1000);

                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(), MenuActivity.class);
                    startActivity(i);

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}
