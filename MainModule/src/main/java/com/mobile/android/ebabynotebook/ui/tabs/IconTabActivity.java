package com.mobile.android.ebabynotebook.ui.tabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.mobile.android.ebabynotebook.R;
import com.mobile.android.ebabynotebook.ui.fragments.modules.FlascheFragment;
import com.mobile.android.ebabynotebook.ui.fragments.modules.PlanningFragment;
import com.mobile.android.ebabynotebook.ui.fragments.modules.KinderInfoFragment;
import com.mobile.android.ebabynotebook.ui.fragments.modules.MedikamentFragment;
import com.mobile.android.ebabynotebook.ui.fragments.modules.SchlafmuetzeFragment;
import com.mobile.android.ebabynotebook.ui.fragments.modules.FotoFragment;
import com.mobile.android.ebabynotebook.ui.fragments.modules.SettingsFragment;
import com.mobile.android.ebabynotebook.ui.fragments.modules.SpritzeFragment;
import com.mobile.android.ebabynotebook.ui.fragments.modules.TemperatureFragment;
import com.mobile.android.ebabynotebook.ui.fragments.modules.WeightFragment;
import com.mobile.android.ebabynotebook.ui.fragments.nodes.temperatur.TemperaturChartFragment;
import com.mobile.android.ebabynotebook.ui.fragments.nodes.temperatur.ManuellFragment;
import com.mobile.android.ebabynotebook.ui.fragments.nodes.settings.AppSettingsFragment;
import com.mobile.android.ebabynotebook.ui.fragments.nodes.settings.MedicamentsSettingsFragment;
import com.mobile.android.ebabynotebook.ui.fragments.nodes.settings.TemperatureSettingsFragment;
import com.mobile.android.ebabynotebook.ui.fragments.nodes.weight.GraphischeDarstellungFragment;
import com.mobile.android.ebabynotebook.ui.fragments.nodes.weight.ManuellErfassenFragment;
import com.mobile.android.ebabynotebook.ui.fragments.nodes.weight.WeightChartFragment;

import java.util.ArrayList;
import java.util.List;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class IconTabActivity extends ActionBarActivity implements MaterialTabListener {
    public static final int KINDER_INFO_FRAGMENT = 0;
    public static final int WEIGHT_FRAGMENT = 1;
    public static final int TEMPERATURE_FRAGMENT = 2;
    public static final int MEDIKAMENTEN_FRAGMENT = 3;
    public static final int FLASCHE_FRAGMENT = 4;
    public static final int SPRITZE_FRAGMENT = 5;
    public static final int SCHLAFMUETZE_FRAGMENT = 6;
    public static final int PLANNING_FRAGMENT = 7;
    public static final int FOTO_FRAGMENT = 8;
    public static final int SETTINGS_FRAGMENT = 9;
    private int currentTabNode;

    // weight fragment subnodes
    public static final int MANUELL_ERFASSEN_FRAGMENT = 10;
    public static final int GRAPHISCHE_DARTSTELLUNG_FRAGMENT = 11;
    public static final int WEIGHT_CHART_FRAGMENT = 12;

    // settings fragment subnodes
    public static final int APP_SETTINGS_FRAGMENT = 13;
    public static final int TEMPERATURE_SETTINGS_FRAGMENT = 14;
    public static final int MEDIKAMENT_SETTINGS_FRAGMENT = 15;

    // Temperatur fragment subnodes
    public static final int MANUELL_FRAGMENT = 16;
    public static final int TEMPERATUR_CHART_FRAGMENT = 17;

    private List<Boolean> activeModules;
    private List<Integer> activeModulesIds;
    private final int TABS_COUNT = 10;

    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    private MaterialTabHost tabHost;
    private Resources res;

    private List<Fragment> fragmentTabs;
    // Fragment tabs
    private KinderInfoFragment kinderInfoFragment;
    private TemperatureFragment temperatureFragment;
    private MedikamentFragment medikamentFragment;
    private WeightFragment weightFragment;
    private FlascheFragment flascheFragment;
    private SpritzeFragment spritzeFragment;
    private SchlafmuetzeFragment schlafmuetzeFragment;
    private PlanningFragment planningFragment;
    private FotoFragment fotoFragment;
    private SettingsFragment settingsFragment;

    // Temperatur fragment nodes
    private ManuellFragment manuellFragment;
    private TemperaturChartFragment temperaturChartFragment;

    // Weight fragment nodes
    private ManuellErfassenFragment manuellErfassenFragment;
    private GraphischeDarstellungFragment graphischeDarstellungFragment;
    private WeightChartFragment weightChartFragment;

    // Settings fragment nodes
    private AppSettingsFragment appSettingsFragment;
    private MedicamentsSettingsFragment medikamentsSettingsFragment;
    private TemperatureSettingsFragment temperatureSettingsFragment;

    private boolean isNode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icons);
        res = this.getResources();

        activeModulesIds = new ArrayList<>();
        activeModules = new ArrayList<>();
        getActiveModulesFromSharedPrefs();
        initFragmentTabs();

        /*Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        this.setSupportActionBar(toolbar);*/

        tabHost = (MaterialTabHost) this.findViewById(R.id.tabHost);
        pager = (ViewPager) this.findViewById(R.id.pager);
        // init view pager
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int pos = findModulePosition(position);
                tabHost.setSelectedNavigationItem(pos);
            }
        });

        activeModulesIds.clear();
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            if(activeModules.get(i)) {
                tabHost.addTab(tabHost.newTab().setIcon(getIcon(i)).setTabListener(this));
                activeModulesIds.add(i);
            }
        }

        addFragments();
        selectTab(getIntent());
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

    private void initFragmentTabs(){
        kinderInfoFragment = new KinderInfoFragment();
        temperatureFragment = new TemperatureFragment();
        manuellFragment = new ManuellFragment();
        medikamentFragment = new MedikamentFragment();
        weightFragment = new WeightFragment();
        flascheFragment = new FlascheFragment();
        spritzeFragment = new SpritzeFragment();
        schlafmuetzeFragment = new SchlafmuetzeFragment();
        planningFragment = new PlanningFragment();
        fotoFragment = new FotoFragment();
        settingsFragment = new SettingsFragment();

        manuellErfassenFragment = new ManuellErfassenFragment();
        graphischeDarstellungFragment = new GraphischeDarstellungFragment();
        appSettingsFragment = new AppSettingsFragment();
        medikamentsSettingsFragment = new MedicamentsSettingsFragment();
        temperatureSettingsFragment = new TemperatureSettingsFragment();
        temperaturChartFragment = new TemperaturChartFragment();
        weightChartFragment = new WeightChartFragment();

        fragmentTabs = new ArrayList<Fragment>();
    }

    private void addFragments(){
        fragmentTabs.clear();
        for(int i = 0; i < activeModulesIds.size(); i++){
            if(activeModulesIds.get(i) == KINDER_INFO_FRAGMENT){
                fragmentTabs.add(kinderInfoFragment);
            } else if(activeModulesIds.get(i) == WEIGHT_FRAGMENT){
                fragmentTabs.add(weightFragment);
            } else if(activeModulesIds.get(i) == TEMPERATURE_FRAGMENT){
                fragmentTabs.add(temperatureFragment);
            } else if(activeModulesIds.get(i) == MEDIKAMENTEN_FRAGMENT){
                fragmentTabs.add(medikamentFragment);
            } else if(activeModulesIds.get(i) == FLASCHE_FRAGMENT){
                fragmentTabs.add(flascheFragment);
            } else if(activeModulesIds.get(i) == SPRITZE_FRAGMENT){
                fragmentTabs.add(spritzeFragment);
            } else if(activeModulesIds.get(i) == SCHLAFMUETZE_FRAGMENT){
                fragmentTabs.add(schlafmuetzeFragment);
            } else if(activeModulesIds.get(i) == PLANNING_FRAGMENT){
                fragmentTabs.add(planningFragment);
            } else if(activeModulesIds.get(i) == FOTO_FRAGMENT){
                fragmentTabs.add(fotoFragment);
            } else if(activeModulesIds.get(i) == SETTINGS_FRAGMENT){
                fragmentTabs.add(settingsFragment);
            }
        }
        for(int i = activeModulesIds.size(); i < TABS_COUNT; i++){
            fragmentTabs.add(new Fragment());
        }
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {
    }

    /**
     * Opens selected tab from menu
     * @param intent
     */
    private void selectTab(Intent intent){
        int tabId = intent.getIntExtra("tabId", KINDER_INFO_FRAGMENT);
        int position = findModulePosition(tabId);
        pager.setCurrentItem(position);
        tabHost.setSelectedNavigationItem(position);
    }

    private int findModulePosition(int tabId){
        for(int i = 0; i < activeModulesIds.size(); i++){
            if(activeModulesIds.get(i) == tabId)
                return i;
        }
        return 0;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            return fragmentTabs.get(position);
        }

        @Override
        public int getCount() {
           return TABS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
               default: return null;
           }
        }
    }

    public void replaceFragment(int currentFragmentId, int newFragmentId){
        switch(newFragmentId) {
            // weight fragment subnodes cases
            case MANUELL_ERFASSEN_FRAGMENT:
                fragmentTabs.set(currentFragmentId, manuellErfassenFragment);
                isNode = true;
                currentTabNode = WEIGHT_FRAGMENT;
                break;
            case GRAPHISCHE_DARTSTELLUNG_FRAGMENT:
                fragmentTabs.set(currentFragmentId, graphischeDarstellungFragment);
                isNode = true;
                currentTabNode = WEIGHT_FRAGMENT;
                break;
            case WEIGHT_CHART_FRAGMENT:
                fragmentTabs.set(currentFragmentId, weightChartFragment);
                isNode = true;
                currentTabNode = WEIGHT_FRAGMENT;
                break;
            case WEIGHT_FRAGMENT:
                fragmentTabs.set(currentFragmentId, weightFragment);
                isNode = false;
                break;
            case TEMPERATURE_FRAGMENT:
                fragmentTabs.set(currentFragmentId, temperatureFragment);
                isNode = false;
                break;
            //Temperature fragment subnodes cases
            case MANUELL_FRAGMENT:
                fragmentTabs.set(currentFragmentId, manuellFragment);
                isNode = true;
                currentTabNode = TEMPERATURE_FRAGMENT;
                break;
            case TEMPERATUR_CHART_FRAGMENT:
                fragmentTabs.set(currentFragmentId, temperaturChartFragment);
                isNode = true;
                currentTabNode = TEMPERATURE_FRAGMENT;
                break;
            // Settings fragment subnodes cases
            case APP_SETTINGS_FRAGMENT:
                fragmentTabs.set(currentFragmentId, appSettingsFragment);
                isNode = true;
                currentTabNode = SETTINGS_FRAGMENT;
                break;
            case TEMPERATURE_SETTINGS_FRAGMENT:
                fragmentTabs.set(currentFragmentId, temperatureSettingsFragment);
                isNode = true;
                currentTabNode = SETTINGS_FRAGMENT;
                break;
            case MEDIKAMENT_SETTINGS_FRAGMENT:
                fragmentTabs.set(currentFragmentId, medikamentsSettingsFragment);
                isNode = true;
                currentTabNode = SETTINGS_FRAGMENT;
                break;
            case SETTINGS_FRAGMENT:
                fragmentTabs.set(currentFragmentId, settingsFragment);
                isNode = false;
                break;
        }
        pagerAdapter.notifyDataSetChanged();
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(currentFragmentId);
    }

    /*
    * Switch tab icons
    */
    private Drawable getIcon(int position) {
        switch(position) {
            case KINDER_INFO_FRAGMENT:
                return res.getDrawable(R.drawable.ic_baby_icon);
            case TEMPERATURE_FRAGMENT:
                return res.getDrawable(R.drawable.ic_thermometer_icon);
            case MEDIKAMENTEN_FRAGMENT:
                return res.getDrawable(R.drawable.ic_medikament);
            case WEIGHT_FRAGMENT:
                return res.getDrawable(R.drawable.ic_waage_icon);
            case FLASCHE_FRAGMENT:
                return res.getDrawable(R.drawable.ic_babyflasche_2);
            case SPRITZE_FRAGMENT:
                return res.getDrawable(R.drawable.ic_spritze);
            case SCHLAFMUETZE_FRAGMENT:
                return res.getDrawable(R.drawable.ic_schlafmuetze);
            case PLANNING_FRAGMENT:
                return res.getDrawable(R.drawable.ic_planning);
            case FOTO_FRAGMENT:
                return res.getDrawable(R.drawable.ic_foto_icon);
            case SETTINGS_FRAGMENT:
                return res.getDrawable(R.drawable.ic_setting);
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if(isNode) {
            int tabId = pager.getCurrentItem();
            replaceFragment(tabId, currentTabNode);
        } else{
            super.onBackPressed();
        }
    }
}