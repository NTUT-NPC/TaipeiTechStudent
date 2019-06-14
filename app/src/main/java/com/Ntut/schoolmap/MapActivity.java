package com.Ntut.schoolmap;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.Ntut.R;
import com.Ntut.utility.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar mToolbar;
    private HashMap<String, Marker> locationList = new HashMap<>();
    private DatabaseReference ref;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mToolbar = findViewById(R.id.main_toolbar);
        fab = findViewById(R.id.fab_mapsearch);
        setSupportActionBar(mToolbar);
        setActionBar();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("locations");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LatLng latLng = new LatLng((Double) ds.child("latitude").getValue(), (Double) ds.child("longitude").getValue());
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(ds.child("title").getValue().toString())
                            .snippet(ds.child("subTitle").getValue().toString()));
                    locationList.put(ds.child("title").getValue().toString(), marker);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fab.setOnClickListener(v -> showChooser());
    }

    public void showChooser() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_location);
        final String[] locations = new String[locationList.size()];
        locationList.keySet().toArray(locations);
        // add a list
        builder.setItems(locations, (dialog, which) -> {
            String location = locations[which];
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationList.get(location).getPosition(), 17));
            locationList.get(location).showInfoWindow();
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng init = new LatLng(25.042848, 121.534447);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(init));

    }

    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(v -> finish());
            actionBar.setTitle(R.string.school_map_text);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.map_color)));
        }
        Utility.setStatusBarColor(this, getResources().getColor(R.color.map_color));
    }
}
