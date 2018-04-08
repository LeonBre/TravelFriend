package de.brettin.leon.travelfriend;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.service.carrier.CarrierMessagingService;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.brettin.leon.travelfriend.mapping.TfMapCallbackHandler;
import de.brettin.leon.travelfriend.schedule.TfPositionUpdateScheduler;
import de.brettin.leon.travelfriend.schedule.TfScheduleManager;
import de.brettin.leon.travelfriend.view.TfMainViewPager;

public class MapsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Schedule all Tasks
        TfScheduleManager.schedule(this);

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
