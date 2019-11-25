package com.example.needforbloodv1.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.needforbloodv1.sharedpref.NFBSharedPreference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class NFBLocation {

    private static FusedLocationProviderClient mFusedLocationClient;
    public static void getLocation(final Context c){
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(c);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) c, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("sooraz", location.getLatitude() + " " + location.getLongitude());
                            }
                        }
                    });
        }
        catch(SecurityException e){
            Log.d("sooraz","permission denied "+e);
        }
    }
}
