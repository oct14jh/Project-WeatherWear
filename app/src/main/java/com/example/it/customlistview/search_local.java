package com.example.it.customlistview;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class search_local extends Activity {
    private Button jeju;
    private Button seoul;
    private Button incheon;
    private Button kangwon;
    private Button daegu;
    private Button busan;
    private Button ulsan;
    private Button gwangju;
    private Button jeonnam;
    private Button gyungnam;
    private Button jeonbyuk;
    private Button kyungpook;
    private Button chungbuk;
    private Button chungnam;
    private Button gyeonggi;
    private Button sejong;
    private Button daejeon;
    private TextView city;
    private Button confirm;

    private Gpstoxy gpstoxy;
    private GetWeather getWeather;
    private Serializable weatherInfo=new ArrayList<LinkedHashMap<String,Object>>();
    double lat=0,lon=0;



    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);          // 코드에서 바로 타이틀바 없애기
            setContentView(R.layout.activity_search_local);
            jeju=findViewById(R.id.jeju_button);
            seoul=findViewById(R.id.seoul_button);
            incheon=findViewById(R.id.incheon_button);
            kangwon=findViewById(R.id.kangwon_button);
            daegu=findViewById(R.id.daegu_button);
            busan=findViewById(R.id.busan_button);
            ulsan=findViewById(R.id.ulsan_button);
            gwangju=findViewById(R.id.gwangju_button);
            jeonnam=findViewById(R.id.jeonnam_button);
            gyungnam=findViewById(R.id.gyungnam_button);
            jeonbyuk=findViewById(R.id.jeonbyuk_button);
            kyungpook=findViewById(R.id.kyungpook_button);
            chungbuk=findViewById(R.id.Chungbuk_button);
            chungnam=findViewById(R.id.chungnam_button);
            gyeonggi=findViewById(R.id.Gyeonggi_button);
            sejong=findViewById(R.id.sejong_button);
            daejeon=findViewById(R.id.daejeon_button);
            city=findViewById(R.id.city);
            confirm=findViewById(R.id.search_local_confirm_button);
    }
    public void onjeju(View v){
        Toast.makeText(getApplicationContext(), "제주", Toast.LENGTH_LONG).show();
        lat=33.387279;
        lon=126.539132;
        city.setText("제주");
    }
    public void onseoul(View v){
        Toast.makeText(getApplicationContext(), "서울", Toast.LENGTH_LONG).show();
        lat=37.549442;
        lon=126.998368;
        city.setText("서울");
    }
    public void onincheon(View v){
        Toast.makeText(getApplicationContext(), "인천", Toast.LENGTH_LONG).show();
        lat=37.453263;
        lon=126.716697;
        city.setText("인천");
    }
    public void onkangwon(View v){
        Toast.makeText(getApplicationContext(), "강원", Toast.LENGTH_LONG).show();
        lat=37.830010;
        lon=128.162934;
        city.setText("강원");
    }
    public void ondaegu(View v){
        Toast.makeText(getApplicationContext(), "대구", Toast.LENGTH_LONG).show();
        lat=35.821081;
        lon=128.567037;
        city.setText("대구");
    }
    public void onbusan(View v){
        Toast.makeText(getApplicationContext(), "부산", Toast.LENGTH_LONG).show();
        lat=35.168485;
        lon=129.046529;
        city.setText("부산");
    }
    public void onulsan(View v){
        Toast.makeText(getApplicationContext(), "울산", Toast.LENGTH_LONG).show();
        lat=35.547797;
        lon=129.247547;
        city.setText("울산");
    }
    public void ongwangju(View v){
        Toast.makeText(getApplicationContext(), "광주", Toast.LENGTH_LONG).show();
        lat=35.159260;
        lon=126.836096;
        city.setText("광주");
    }
    public void onjeonnam(View v){
        Toast.makeText(getApplicationContext(), "전남", Toast.LENGTH_LONG).show();
        lat=34.879096;
        lon=126.993956;
        city.setText("전남");
    }
    public void ongyungnam(View v){
        Toast.makeText(getApplicationContext(), "경남", Toast.LENGTH_LONG).show();
        lat=35.460476;
        lon=128.210807;
        city.setText("경남");
    }
    public void onjeonbyuk(View v){
        Toast.makeText(getApplicationContext(), "전북", Toast.LENGTH_LONG).show();
        lat=35.715151;
        lon=127.152083;
        city.setText("전북");
    }
    public void onkyungpook(View v){
        Toast.makeText(getApplicationContext(), "경북", Toast.LENGTH_LONG).show();
        lat=36.279008;
        lon=128.887189;
        city.setText("경북");
    }
    public void onchungbuk(View v){
        Toast.makeText(getApplicationContext(), "충복", Toast.LENGTH_LONG).show();
        lat=37.008536;
        lon=127.718689;
        city.setText("충북");
    }
    public void onchungnam(View v){
        Toast.makeText(getApplicationContext(), "충남", Toast.LENGTH_LONG).show();
        lat=36.719300;
        lon=126.795913;
        city.setText("충남");
    }
    public void ongyeonggi(View v){
        Toast.makeText(getApplicationContext(), "경기", Toast.LENGTH_LONG).show();
        lat=37.410629;
        lon=127.505888;
        city.setText("경기");
    }
    public void onsejong(View v){
        Toast.makeText(getApplicationContext(), "세종", Toast.LENGTH_LONG).show();
        lat=36.561417;
        lon=127.260782;
        city.setText("세종");
    }
    public void ondaejeon(View v){
        Toast.makeText(getApplicationContext(), "대전", Toast.LENGTH_LONG).show();
        lat=36.347767;
        lon=127.397212;
        city.setText("대전");
    }
    public void mOnClose(View v){
        if(lat==0&&lon==0)
            finish();
        getweather_city(lat,lon);
        finish();
    }


    public void getweather_city(double lat,double lon){

        XYpoint temp;
        gpstoxy=new Gpstoxy(lat,lon);
        gpstoxy.cal();
        temp=gpstoxy.getPoint();

        try{
            getWeather = new GetWeather(temp.x, temp.y);
            getWeather.start();
            getWeather.join();
            weatherInfo=getWeather.returnWeather();
            Intent wi=new Intent(search_local.this,popup_setting_local.class);
            wi.putExtra("weather", weatherInfo);
            setResult(RESULT_OK,wi);
        }catch (Exception e){

        }

    }
}