package de.brettin.leon.travelfriend;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;

import de.brettin.leon.travelfriend.mapping.TfMapCallbackHandler;
import de.brettin.leon.travelfriend.schedule.TfScheduleManager;
import de.brettin.leon.travelfriend.view.TfMainViewPager;

public class MapsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Schedule all Tasks
        TfScheduleManager.schedule(this);

        // Check for Permission of the user
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.ACCESS_COARSE_LOCATION}, 14141);
        } else {
            initiate();
        }
    }

    /**
     * Wait until permission is granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        initiate();
    }

    /**
     * Initate view
     */
    private void initiate() {
        // Callbackhandler when the map has finished loading
        TfMapCallbackHandler callbackHandler = new TfMapCallbackHandler(this);

        // Set up viewpager
        final ViewPager viewPager = this.findViewById(R.id.viewPager);
        final TfMainViewPager adapter = new TfMainViewPager(getSupportFragmentManager(), callbackHandler);
        viewPager.setAdapter(adapter);

        // Set Listener for State Changes on Bottom Navigation
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getTitle().toString()) {
                    case "Account":
                        viewPager.setCurrentItem(1);
                        break;
                    case "Karte":
                    default:
                        viewPager.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });
    }
}
