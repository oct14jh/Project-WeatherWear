package com.example.it.customlistview;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class detail_weather extends AppCompatActivity {
    TextView t0,t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15;

    private static String IP_ADDRESS = "165.229.90.38/";
    private static String TAG = "phptest";
    PersonalData personalData;
    private String mJsonString;
    Handler handler = new Handler();  // 외부쓰레드 에서 메인 UI화면을 그릴때 사용
    String check_id, get_id;
    boolean image_select =false;
    boolean clickcnt = false;
    private GetWeather getWeather;
    ArrayList<HashMap<String, Object>> weatherinfo=new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_detail_weather);
        try {
            weatherinfo = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("날씨정보");
            t0 = findViewById(R.id.t1);
            t1 = findViewById(R.id.t2);
            t2 = findViewById(R.id.t3);
            t3 = findViewById(R.id.t4);
            t4 = findViewById(R.id.t5);
            t5 = findViewById(R.id.t6);
            t6 = findViewById(R.id.t7);
            t7 = findViewById(R.id.t8);
            t8 = findViewById(R.id.t9);
            t9 = findViewById(R.id.t10);
            t10 = findViewById(R.id.t11);
            t11 = findViewById(R.id.t12);
            t12 = findViewById(R.id.t13);
            t13 = findViewById(R.id.t14);
            t14 = findViewById(R.id.t15);
            t15 = findViewById(R.id.t16);

            setdetailWeather();
            Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), "다시 시도해보세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void setdetailWeather(){
        t0.setText("\n\n~ "+weatherinfo.get(0).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(0).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(0).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(0).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(0).get("강수확률").toString());

        t1.setText("\n\n~ "+weatherinfo.get(1).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(1).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(1).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(1).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(1).get("강수확률").toString());

        t2.setText("\n\n~ "+weatherinfo.get(2).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(2).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(2).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(2).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(2).get("강수확률").toString());

        t3.setText("\n\n~ "+weatherinfo.get(3).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(3).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(3).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(3).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(3).get("강수확률").toString());

        t4.setText("\n\n~ "+weatherinfo.get(4).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(4).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(4).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(4).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(4).get("강수확률").toString());

        t5.setText("\n\n~ "+weatherinfo.get(5).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(5).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(5).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(5).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(5).get("강수확률").toString());

        t6.setText("\n\n~ "+weatherinfo.get(6).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(6).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(6).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(6).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(6).get("강수확률").toString());

        t7.setText("\n\n~ "+weatherinfo.get(7).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(7).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(7).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(7).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(7).get("강수확률").toString());

        t8.setText("\n\n~ "+weatherinfo.get(8).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(8).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(8).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(8).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(8).get("강수확률").toString());

        t9.setText("\n\n~ "+weatherinfo.get(9).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(9).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(9).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(9).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(9).get("강수확률").toString());

        t10.setText("\n\n~ "+weatherinfo.get(10).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(10).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(10).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(10).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(10).get("강수확률").toString());

        t11.setText("\n\n~ "+weatherinfo.get(11).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(11).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(11).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(11).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(11).get("강수확률").toString());

        t12.setText("\n\n~ "+weatherinfo.get(12).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(12).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(12).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(12).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(12).get("강수확률").toString());

        t13.setText("\n\n~ "+weatherinfo.get(13).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(13).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(13).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(13).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(13).get("강수확률").toString());

        t14.setText("\n\n~ "+weatherinfo.get(14).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(14).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(14).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(14).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(14).get("강수확률").toString());

        t15.setText("\n\n~ "+weatherinfo.get(15).get("시간").toString()+"시" +
                "\n현재온도 : "+weatherinfo.get(15).get("현재온도").toString()+
                "\n날씨정보 : "+weatherinfo.get(15).get("날씨한국어").toString()+
                "\n습도(%) : "+weatherinfo.get(15).get("습도").toString()+
                "\n강수확률(%) : "+weatherinfo.get(15).get("강수확률").toString());
    }

    public void back_main(View v){
        Intent back_main = new Intent(this, MainActivity.class);
        startActivity(back_main);
    }
}
