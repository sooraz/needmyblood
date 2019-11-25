package com.example.needforbloodv1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

//        try {
//            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//            mFusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            if (location != null) {
//                                // Logic to handle location object
//                                Log.d("sooraz",location.getLatitude()+" "+location.getLongitude());
//
//                            }
//                        }
//                    });
//        }
//        catch(SecurityException e){
//            Log.d("sooraz","permission denied "+e);
//        }


        //}
        //catch (Exception e){

        //}
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

        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                Log.d("sooraz",location.getLatitude()+" "+location.getLongitude());
                                LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(newLatLng).title("my location")).showInfoWindow();
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
                                // Zoom in, animating the camera.
                                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                                hitForDiffLoc(location.getLatitude(),location.getLongitude());
                            }
                        }
                    });
        }
        catch(SecurityException e){
            Log.d("sooraz","permission denied "+e);
        }









//        Location location = null;
//        try {
//            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        } catch (SecurityException e) {
//           //// lets the user know there is a problem with the gps
//            Log.d("sooraz","locaton fasak");
//        }
//        if(location!=null) {
//            // Add a marker in Sydney and move the camera
//            LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(newLatLng).title("my location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
//        }
//        else{
//            Toast.makeText(this,"maps location null",Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(newLatLng).title("my location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void hitForDiffLoc(/* add km par*/double latitude, double longitude){
        final ProgressDialog pDialog = new ProgressDialog(this);
        String url = String.format("https://sooraz.000webhostapp.com/need_for_blood/getLocations.php"+"?lat=%1$s&lng=%2$s",latitude,longitude);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject temp = new JSONObject(response);
                            switch (temp.getInt("tol_users")) {
                                case 0:
//                                    no users
                                    Toast.makeText(MapsActivity.this, "sorry no users with your search request", Toast.LENGTH_LONG).show();
                                    break;
                                default:

                                    //final ArrayList<List<String>> list = new ArrayList<List<String>>();
                                    //list.add(Arrays.asList("Name","Location","Blood_group"));
                                    int size=temp.getInt("tol_users");
                                    for (int i = 0; i < size; ++i) {
                                        JSONObject looptemp = new JSONObject(temp.getString(Integer.toString(i)));
                                        //list.add(Arrays.asList(looptemp.getString("name"),looptemp.getString("bgroup"),looptemp.getString("longitude"),looptemp.getString("latitude")));
                                    if(mMap!=null){
                                        LatLng newLatLng = new LatLng(Double.parseDouble(looptemp.getString("latitude")), Double.parseDouble(looptemp.getString("longitude")));
                                        mMap.addMarker(new MarkerOptions().position(newLatLng).title(looptemp.getString("name")+" "+looptemp.getString("bgroup"))).showInfoWindow();
                                    }
                                    }
                                    break;

                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        pDialog.hide();
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                pDialog.dismiss();
            }

        }
        );

// Adding request to request queue
        AppContoller.getInstance().addToRequestQueue(jsonObjReq);
    }

}
