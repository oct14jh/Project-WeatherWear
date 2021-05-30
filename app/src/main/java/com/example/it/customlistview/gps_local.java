package com.example.it.customlistview;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class gps_local extends Activity {
    // private Context main_Context;
    private Geocoder main_Geocoder;
    private Button btnShowLocation;
    private TextView locality;
    private TextView weatherinfotxt;
    private boolean isPermission=true;
    private GPSinfo gps;
    private Geo convertgps;
    private Gpstoxy gpstoxy;
    private GetWeather getWeather;
    private Serializable weatherInfo=new ArrayList<HashMap<String,Object>>();
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    public class weather_value{}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_local);
        btnShowLocation = findViewById(R.id.gps_button);
        locality = findViewById(R.id.locality);
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            final Geocoder geocoder=new Geocoder(gps_local.this);
            @Override
            public void onClick(View view) {
                if (!isPermission) {
                    callPermission();
                    return;
                }
                gps = new GPSinfo(gps_local.this);

                if (gps.isGetLocation()) {
                    double lat = gps.getLatitude();
                    double lon = gps.getLongtitue();

                    Toast.makeText(getApplicationContext(), "당신의 위치 - 위도 : " + lat + "경도 : " + lon, Toast.LENGTH_LONG).show();
                    gpstoxy = new Gpstoxy(lat, lon);
                    gpstoxy.cal();
                    XYpoint temp = gpstoxy.getPoint();

                    try {
                        /////////////////////////////////////////////
                        getWeather = new GetWeather(temp.x, temp.y);
                        getWeather.start();
                        getWeather.join();
                        weatherInfo=getWeather.returnWeather();
                     } catch (Exception e) {
                        locality.setText(getWeather.errorMsg);
                    }

                    convertgps = new Geo(geocoder, lat, lon);
                    String city = convertgps.convert();
                    locality.setText(city);

                } else
                    gps.showSettingsAlert();
            }
        });
        callPermission();
    }
    private void callPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) { requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }

    public void onBackPressed() { return; }

    public void mOnClose(View v){
        Intent intent_popup_exit = new Intent();
        intent_popup_exit.putExtra("weather",weatherInfo);
        setResult(RESULT_OK, intent_popup_exit);   //데이터 전달하기
        //액티비티(팝업) 닫기
        finish();
    }
}