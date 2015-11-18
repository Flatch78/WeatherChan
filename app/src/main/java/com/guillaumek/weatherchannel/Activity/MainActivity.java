package com.guillaumek.weatherchannel.Activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.guillaumek.weatherchannel.Fragment.InformationFragment;
import com.guillaumek.weatherchannel.Fragment.MainFragment;
import com.guillaumek.weatherchannel.Fragment.SettingFragment;
import com.guillaumek.weatherchannel.Global.AppWeatherChan;
import com.guillaumek.weatherchannel.Network.Object.CityInfoObject;
import com.guillaumek.weatherchannel.R;
import com.guillaumek.weatherchannel.Tools.SQLiteDB.SQLiteWeatherChan;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MainFragment mMainFragment;
    private SettingFragment mSettingFragment;
    private InformationFragment mInformationFragment;

    private SQLiteWeatherChan mSQLiteWeatherChan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        {
            if (((AppWeatherChan) getApplication()).getSQLiteWeatherChan() == null) {
                mSQLiteWeatherChan = new SQLiteWeatherChan(this);
                if (mSQLiteWeatherChan.getCityCount() == 0) {
                    CityInfoObject cityInfoObject = new CityInfoObject();
                    cityInfoObject.setName("Paris");
                    cityInfoObject.setLatitude(48.853);
                    cityInfoObject.setLongitude(2.35);
                    cityInfoObject.setFavorite(1);
                    mSQLiteWeatherChan.addCity(cityInfoObject);
                    Log.e("PARIS", "paris added");
                }
                ((AppWeatherChan) getApplication()).setSQLiteWeatherChan(mSQLiteWeatherChan);
            }
        }


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        {
            mMainFragment = MainFragment.newInstance(this);
            mSettingFragment = SettingFragment.newInstance(this);
            mInformationFragment = InformationFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, mMainFragment).commit();
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_main) {
            fTransaction.replace(R.id.contentMain, mMainFragment);
        } else if (id == R.id.nav_setting) {
            fTransaction.replace(R.id.contentMain, mSettingFragment);
        } else if (id == R.id.nav_information) {
            fTransaction.replace(R.id.contentMain, mInformationFragment);
        }

        fTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
