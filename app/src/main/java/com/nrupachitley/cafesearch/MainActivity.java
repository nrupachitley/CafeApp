package com.nrupachitley.cafesearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity implements MapsFragment.SendData{

    private static final String TAG = "MainActivity";
    private  int NETWORK_CONNECTIVITY_STATUS = 0;
    private  TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.map_logo,
            R.drawable.cafe_logo
    };

    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TAG", "onCreate : called. Checking for Network and GooglePlay service");
        if(isNetworkAvailable() != 0 && checkGooglePlayServices()) {
            tabLayout = (TabLayout) findViewById(R.id.tablayout);
            tabLayout.addTab(tabLayout.newTab().setText("Map").setIcon(tabIcons[1]));
            tabLayout.addTab((tabLayout.newTab().setText("Cafes")));
            viewPager = (ViewPager) findViewById(R.id.viewPager);

            PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(pageAdapter);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();
        }
    }

    private int isNetworkAvailable() {
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
        if (networkCapabilities != null) {
            if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                NETWORK_CONNECTIVITY_STATUS = 1;
            }
            else if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                NETWORK_CONNECTIVITY_STATUS = 2;
            }
        }
        return NETWORK_CONNECTIVITY_STATUS;
    }

    public boolean checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }


    @Override
    public void sendData(String arg1, String arg2) {
        String tag = "android:switcher:" + R.id.viewPager + ":" + 1;
        CafeFragment cafeFragment = (CafeFragment) getSupportFragmentManager().findFragmentByTag(tag);
        cafeFragment.displayReceivedData(arg1, arg2);
    }
}
