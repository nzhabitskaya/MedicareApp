package com.mobile.android.ebabynotebook.ui.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobile.android.ebabynotebook.R;
import com.mobile.android.ebabynotebook.ui.tabs.IconTabActivity;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends Activity {
    private List<Boolean> activeModules;
    private List<Integer> slotIds;
    private List<Integer> moduleResourcesIds;
    private List<View.OnClickListener> listeners;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);
        activeModules = new ArrayList<>();

        initSlotIds();
        initModuleResources();
        initListeners();

        getActiveModulesFromSharedPrefs();
        fillLayoutWithActiveModules();
    }

    @Override
    public void onResume(){
        super.onResume();
        clearAllSlots();
        getActiveModulesFromSharedPrefs();
        fillLayoutWithActiveModules();
    }

    private void initSlotIds(){
        slotIds = new ArrayList<>();
        slotIds.add(R.id.slot1);
        slotIds.add(R.id.slot2);
        slotIds.add(R.id.slot3);
        slotIds.add(R.id.slot4);
        slotIds.add(R.id.slot5);
        slotIds.add(R.id.slot6);
        slotIds.add(R.id.slot7);
        slotIds.add(R.id.slot8);
        slotIds.add(R.id.slot9);
        slotIds.add(R.id.slot10);
        slotIds.add(R.id.slot11);
        slotIds.add(R.id.slot12);
    }

    private void initModuleResources(){
        moduleResourcesIds = new ArrayList<>();
        moduleResourcesIds.add(R.drawable.ic_baby_icon);
        moduleResourcesIds.add(R.drawable.ic_waage_icon);
        moduleResourcesIds.add(R.drawable.ic_thermometer_icon);
        moduleResourcesIds.add(R.drawable.ic_medikament);
        moduleResourcesIds.add(R.drawable.ic_babyflasche_2);
        moduleResourcesIds.add(R.drawable.ic_spritze);
        moduleResourcesIds.add(R.drawable.ic_schlafmuetze);
        moduleResourcesIds.add(R.drawable.ic_foto_icon);
        moduleResourcesIds.add(R.drawable.ic_planning);
        moduleResourcesIds.add(R.drawable.ic_setting);
        moduleResourcesIds.add(R.drawable.ic_exit);
    }

    private void getActiveModulesFromSharedPrefs(){
        SharedPreferences prefs = getSharedPreferences("com.mobile.android.ebabynotebook", Context.MODE_PRIVATE);
        activeModules.clear();
        activeModules.add(true);
        activeModules.add(prefs.getBoolean("module1", false));
        activeModules.add(prefs.getBoolean("module2", false));
        activeModules.add(prefs.getBoolean("module3", false));
        activeModules.add(prefs.getBoolean("module4", false));
        activeModules.add(prefs.getBoolean("module5", false));
        activeModules.add(prefs.getBoolean("module6", false));
        activeModules.add(prefs.getBoolean("module7", false));
        activeModules.add(prefs.getBoolean("module8", false));
        activeModules.add(true);
        activeModules.add(true);
    }

    private void clearAllSlots(){
        for(int i = 0; i < activeModules.size(); i++){
            int slotId = slotIds.get(i);
            FrameLayout slot = (FrameLayout) findViewById(slotId);
            slot.removeAllViews();
        }
    }

    private void fillLayoutWithActiveModules(){
        int j = 0;
        for(int i = 0; i < activeModules.size(); i++){
            if(activeModules.get(i)){
                int slotId = slotIds.get(j);
                FrameLayout slot = (FrameLayout) findViewById(slotId);
                int resId = moduleResourcesIds.get(i);
                ImageButton button = createImageButton(i, resId);
                slot.addView(button);
                j++;
            }
        }
    }

    private ImageButton createImageButton(int pos, int resId){
        ImageButton button = new ImageButton(this);
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setId(resId);
        button.setImageResource(resId);
        button.setOnClickListener(listeners.get(pos));
        button.setAdjustViewBounds(true);
        button.setPadding(25, 25, 25, 25);
        button.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return button;
    }

    private void initListeners() {
        listeners = new ArrayList<>();
        listeners.add(KinderInfoListener);
        listeners.add(WaageListener);
        listeners.add(TermometerListener);
        listeners.add(MedikamentListener);
        listeners.add(FlascheListener);
        listeners.add(SpritzeListener);
        listeners.add(SchlafmuetzeListener);
        listeners.add(PlanningListener);
        listeners.add(FotoListener);
        listeners.add(SettingsListener);
        listeners.add(ExitListener);
    }

    View.OnClickListener KinderInfoListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MenuActivity.this, IconTabActivity.class);
            intent.putExtra("tabId", IconTabActivity.KINDER_INFO_FRAGMENT);
            startActivity(intent);
        }
    };

    View.OnClickListener WaageListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MenuActivity.this, IconTabActivity.class);
            intent.putExtra("tabId", IconTabActivity.WEIGHT_FRAGMENT);
            startActivity(intent);
        }
    };

    View.OnClickListener TermometerListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MenuActivity.this, IconTabActivity.class);
            intent.putExtra("tabId", IconTabActivity.TEMPERATURE_FRAGMENT);
            startActivity(intent);
        }
    };

    View.OnClickListener MedikamentListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MenuActivity.this, IconTabActivity.class);
            intent.putExtra("tabId", IconTabActivity.MEDIKAMENTEN_FRAGMENT);
            startActivity(intent);
        }
    };

    View.OnClickListener FlascheListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MenuActivity.this, IconTabActivity.class);
            intent.putExtra("tabId", IconTabActivity.FLASCHE_FRAGMENT);
            startActivity(intent);
        }
    };

    View.OnClickListener SpritzeListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MenuActivity.this, IconTabActivity.class);
            intent.putExtra("tabId", IconTabActivity.SPRITZE_FRAGMENT);
            startActivity(intent);
        }
    };

    View.OnClickListener SchlafmuetzeListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MenuActivity.this, IconTabActivity.class);
            intent.putExtra("tabId", IconTabActivity.SCHLAFMUETZE_FRAGMENT);
            startActivity(intent);
        }
    };

    View.OnClickListener PlanningListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MenuActivity.this, IconTabActivity.class);
            intent.putExtra("tabId", IconTabActivity.PLANNING_FRAGMENT);
            startActivity(intent);
        }
    };

    View.OnClickListener FotoListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MenuActivity.this, IconTabActivity.class);
            intent.putExtra("tabId", IconTabActivity.FOTO_FRAGMENT);
            startActivity(intent);
        }
    };

    View.OnClickListener SettingsListener = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(MenuActivity.this, IconTabActivity.class);
            intent.putExtra("tabId", IconTabActivity.SETTINGS_FRAGMENT);
            startActivity(intent);
        }
    };

    View.OnClickListener ExitListener = new View.OnClickListener() {
        public void onClick(View view) {
            finish();
        }
    };
}
