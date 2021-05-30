package com.example.it.customlistview;

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
import android.support.v4.content.ContextCompat;

public class GPSinfo extends Service implements LocationListener {
    private final Context mContext;
    boolean isGpsEnabled=false;
    boolean isNetworkEnable=false;
    boolean isGetLocation=false;
    Location location;

    double lat;
    double lon;

    private static final long MIN_DISTANCE=10;
    private static final long MIN_TIME= 1000 * 60;

    protected LocationManager locationManager;

    public GPSinfo(Context context){
        this.mContext=context;
        getLocation();
    }

    public Location getLocation() {
        if ((Build.VERSION.SDK_INT >= 23) &&
                (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(
                        mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {

            return null;
        }

        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            isGpsEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGpsEnabled&&!isNetworkEnable){
                return null;
            }
            else{
                this.isGetLocation=true;
                if(isNetworkEnable){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME,MIN_DISTANCE,this);

                    if(locationManager!=null){
                        location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location!=null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }
                if(isGpsEnabled){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE,this);
                    if(locationManager!=null){
                        location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location!=null){
                            lat=location.getLatitude();
                            lon=location.getLongitude();
                        }
                    }

                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return location;

    }

    public void stopUsingGPS(){
        if(locationManager!=null){
            locationManager.removeUpdates(GPSinfo.this);
        }
    }

    public double getLatitude(){
        if(location!=null){
            lat=location.getLatitude();
        }
        return lat;
    }

    public double getLongtitue(){
        if(location!=null){
            lon=location.getLongitude();
        }
        return lon;
    }

    public boolean isGetLocation(){
        return this.isGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 세팅 오류가 있습니다. \n 설정을 확인해주시길바랍니다.");
        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}