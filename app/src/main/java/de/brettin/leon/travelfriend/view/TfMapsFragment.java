package de.brettin.leon.travelfriend.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import de.brettin.leon.travelfriend.R;

/**
 * Created by Leon on 08.04.18.
 */

public class TfMapsFragment extends Fragment {

    SupportMapFragment gMapView;
    GoogleMap gMap = null;

    OnMapReadyCallback mOnMapReadyListener;

    public static TfMapsFragment newInstance() {
        TfMapsFragment fragment = new TfMapsFragment();
        return fragment;
    }

    public void setOnMapReadyListener(OnMapReadyCallback onMapReadyListener) {
        mOnMapReadyListener = onMapReadyListener;
    }


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapsfragment, container, false);
        if (mOnMapReadyListener != null) {
            gMapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            gMapView.getMapAsync(mOnMapReadyListener);
        }
        return view;
    }

}
