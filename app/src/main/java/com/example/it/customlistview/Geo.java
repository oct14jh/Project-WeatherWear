package com.example.it.customlistview;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.io.IOException;
import java.util.List;

public class Geo extends AppCompatActivity {
    double lat,lon;
    Geocoder geocoder;

    public Geo(Geocoder geocoder,double lat,double lon){
        this.lat=lat;
        this.lon=lon;
        this.geocoder=geocoder;
    }

    public String convert(){
        List<Address> list;
        list = null;
        try {
            list = geocoder.getFromLocation(lat, lon,10);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (list != null) {
            if (list.size()==0) {
                String rt="해당되는 주소가 없습니다.";
                return rt;
            }
            else {
                return list.get(0).getAddressLine(0);
            }
        }
        String error="리스트를 받아오는데 문제가 생겼습니다.";
        return error;
    }
}