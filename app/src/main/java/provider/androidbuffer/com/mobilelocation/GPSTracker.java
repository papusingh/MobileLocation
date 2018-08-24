package provider.androidbuffer.com.mobilelocation;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by incred-dev on 22/8/18.
 */

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    //Todo flag for GPS status
    boolean isGPSEnabled = false;

    //Todo flag for network status
    boolean isNetworkEnabled = false;

    //Todo flag for location status
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    //todo the minimum distance to change update in meters
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATE = 10;

    //todo the minimum time between updates in minutes
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000 * 60 * 1;

    //todo declear a location manager
    LocationManager locationManager;


    public GPSTracker(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            //getting gps status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                //no network provider is enabled
            } else {
                //todo get location from network provider
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        this.canGetLocation = true;

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
                                MINIMUM_DISTANCE_CHANGE_FOR_UPDATE, this);

                        Log.d("location","getting through network provider");

                        if (locationManager != null){
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                                    || !locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).isFromMockProvider()){

                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null){
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    } else {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},0);
                    }
                }

                //todo get location from gps service
                if (isGPSEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        this.canGetLocation = true;

                        if (location == null){
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
                                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATE, this);

                            Log.d("location","getting through gps service");

                            if (locationManager != null){
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                                        || !locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).isFromMockProvider()){

                                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (location != null){
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                    }
                                }
                            }
                        }
                    }else {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},0);
                    }
                }
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }

        return location;
    }

    public double getLatitude(){
        if (location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if (location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public void stopUsingGPS(){
        if (location != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
