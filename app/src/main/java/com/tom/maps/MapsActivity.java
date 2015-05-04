package com.tom.maps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LatLng sevenEleven = new LatLng(25.02567019, 121.5387252);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        setUpMapIfNeeded();
        mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap!=null){
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMyLocationEnabled(true);
/*            mMap.setOnMyLocationButtonClickListener(
                    new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    //LocationProvider provider = manager.getProvider("gps");
                    Location loc = manager.getLastKnownLocation("gps");
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(
                            new LatLng(loc.getLatitude(), loc.getLongitude())
                    ));
                    return false;
                }
            });*/
//            mMap.setOnMyLocationChangeListener(this);


            mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(25.025797, 121.537819), 18)
                    , 2000, null);
            mMap.addMarker(new MarkerOptions()
                    .title("SCE")
                    .position(new LatLng(25.025797, 121.537819)
                    )
            );
            //台北市大安區和平東路二段63號, 02 2702 7770
            mMap.addMarker(new MarkerOptions()
                    .position(sevenEleven)
                    .title("7-11")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.seven11))
                    .snippet("02 2702 7770")
            );
            mMap.setOnMarkerClickListener(this);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onMyLocationChange(Location location) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 18)
        );
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        new AlertDialog.Builder(this)
                .setTitle(marker.getTitle())
                .setMessage(marker.getSnippet())
                .setPositiveButton("OK", null)
                .setNeutralButton("Dial", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent dial = new Intent(Intent.ACTION_CALL);
                        Uri number = Uri.parse("tel:"+marker.getSnippet());
                        dial.setData(number);
                        startActivity(dial);
                    }
                })
                .show();
        return false;
    }
}
