package com.javainstitute.tickethub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap map;
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        String latLang = getIntent().getStringExtra("latLang");
        data = latLang.split(",");


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        LatLng latLng = new LatLng(Double.parseDouble(data[0]), Double.parseDouble(data[1])); // location numbers from map
        map.addMarker(new MarkerOptions().position(latLng).title("Location")); // to add red marker
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15)); // to zoom  1 to 100

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.map);

            super.onBackPressed();
            Intent intent = new Intent(this, TicketDetailActivity.class);
            String originalTicketId = getIntent().getStringExtra("ticketId");
            intent.putExtra("ticketId", originalTicketId);
            startActivity(intent);
            finish();

    }
}

