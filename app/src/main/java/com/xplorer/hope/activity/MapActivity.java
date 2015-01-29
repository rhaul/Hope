package com.xplorer.hope.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xplorer.hope.R;
import com.xplorer.hope.config.HopeApp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    @InjectView(R.id.b_map_setMyWL)
    Button b_setMyWL;

    AlertDialog dialog;

    private GoogleMap mMap;
    private Marker marker;
    private Circle circle;
    private Geocoder geocoder;
    LocationManager manager;
    LatLng latLng = new LatLng(28.63821, 77.2047405);
    float radius = 200;
    String address = "";
    GetAddressTask getAddressTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.inject(this);


        getActionBar().setTitle("Set Location");
        Integer colorVal = HopeApp.CategoryColor.get(HopeApp.TITLES[3]);

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorVal)));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (Geocoder.isPresent()) {
            geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
        }
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapFragment.getMapAsync(this);
        b_setMyWL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAddressTask != null) {
                    getAddressTask.cancel(true);
                }
                getAddressTask = new GetAddressTask(MapActivity.this, true);
                getAddressTask.execute(latLng.latitude, latLng.longitude);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (getIntent().getDoubleExtra("lat", 0) != 0) {
            latLng = new LatLng(getIntent().getDoubleExtra("lat", 0), getIntent().getDoubleExtra("lng", 0));
        } else if (manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            Location lastLoc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
        } else if (manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
            Location lastLoc = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            latLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
        } else if (manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) != null) {
            Location lastLoc = manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            latLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(MapActivity.this, "Please enable your GPS", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                showLocationOnMap();
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                marker.hideInfoWindow();
            }

            @Override
            public void onMarkerDragEnd(Marker TempMarker) {
                latLng = new LatLng(TempMarker.getPosition().latitude, TempMarker.getPosition().longitude);
                showLocationOnMap();
            }
        });
        showLocationOnMap();
    }

    public void showLocationOnMap() {
        if (marker != null) {
            marker.remove();
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Work Location")
                    .draggable(true));

            if (getAddressTask != null) {
                getAddressTask.cancel(true);
            }
            getAddressTask = new GetAddressTask(MapActivity.this, false);
            getAddressTask.execute(latLng.latitude, latLng.longitude);
            
        } else {
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Long press to drag me to your Work Location")
                    .draggable(true));
        }
        marker.showInfoWindow();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);

    }

    private class GetAddressTask extends
            AsyncTask<Double, Void, Address> {
        Context mContext;
        boolean setFinalAddress = false;

        public GetAddressTask(Context context, boolean value) {
            super();
            mContext = context;
            setFinalAddress = value;
        }

        @Override
        protected Address doInBackground(Double... params) {
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(params[0], params[1], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                //loading.setVisibility(View.VISIBLE);
                return addresses.get(0);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Address addr) {
            //pb_loading.setVisibility(View.GONE);
            if (addr != null) {
                latLng = new LatLng(addr.getLatitude(), addr.getLongitude());
                address = String.format(
                        "%s, %s",
                        // If there's a street address, add it
                        addr.getMaxAddressLineIndex() > 0 ?
                                addr.getAddressLine(0) : "",
                        // Locality is usually a city
                        addr.getLocality());
                marker.setTitle(address);
                marker.showInfoWindow();
            } else {
                Toast.makeText(mContext, "No Address found", Toast.LENGTH_SHORT).show();
            }
            if (setFinalAddress) {
                setResults();
            }
        }
    }

    private void setResults() {
        Intent intent = new Intent();
        intent.putExtra("address", address);
        intent.putExtra("lat", latLng.latitude);
        intent.putExtra("lng", latLng.longitude);
        setResult(1, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
