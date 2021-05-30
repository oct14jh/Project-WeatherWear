package com.example.it.customlistview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import android.os.Handler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView text_comment, textview1,textview2,textview3,textview4,textview5,textview6, jh, js;
    Document doc = null;
    //LinearLayout layout = null;
    ImageView iv1;
    Random rnd = new Random();

    int p = rnd.nextInt(2);
    ArrayList<HashMap<String, Object>> weatherinfo=new ArrayList<HashMap<String, Object>>();
    double kkk;
    boolean check_log = false;

    private static String IP_ADDRESS = "165.229.90.38/";
    private static String TAG = "phptest";
    private EditText getId;

    PersonalData personalData;
    private String mJsonString;
    Handler handler = new Handler();  // 외부쓰레드 에서 메인 UI화면을 그릴때 사용
    String check_id, get_id;
    boolean image_select =false;
    boolean clickcnt = false;
    private GetWeather getWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_comment = findViewById(R.id.weather_comment);
        textview1 = findViewById(R.id.now_temperature);
        textview2 = findViewById(R.id.sense_temperature);
        textview3 = findViewById(R.id.high_temperature);
        textview4 = findViewById(R.id.low_temperature);
        textview5 = findViewById(R.id.simple_weather);
        textview6 = findViewById(R.id.rainfall_text);

        // GetXMLTask task = new GetXMLTask();
        //  task.execute("http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=4729053000");

        String item="서울 기준 날씨입니다. 원할한 사용을 위해 지역 설정을 해주세요";
        Toast.makeText(this, item, Toast.LENGTH_LONG).show();
        try {
            getWeather = new GetWeather(59, 125);
            getWeather.start();
            getWeather.join();
            weatherinfo = (ArrayList<HashMap<String,Object>>)(getWeather.returnWeather());
            setWeather(null);
            setcomment(text_comment);
        }catch(Exception e){
            item="날씨를 받아오는데 오류가 발생했습니다.";
            Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
        }


    }

    //메인화면에서의 back 버튼 눌렀을 때의 토스트 팝업

    public void button_back_in_main(View v){
        Toast.makeText(getApplicationContext(), "더이상 뒤로가기가 불가능합니다", Toast.LENGTH_SHORT).show();
    }

    // 데이터 받아서 popup_setting_local 호출

    public void button_setting_local_in_main(View v){
        Intent intent_setting_local_in_main = new Intent(this, popup_setting_local.class);
        intent_setting_local_in_main.putExtra("data", "Test Popup");
        startActivityForResult(intent_setting_local_in_main, 1);
    }
    public void setWeather(View v){
        double high_temp,low_temp;
        try {
            high_temp = Double.parseDouble(weatherinfo.get(0).get("최고온도").toString());
            low_temp = Double.parseDouble(weatherinfo.get(0).get("최저온도").toString());
            kkk = Double.parseDouble(weatherinfo.get(0).get("체감온도").toString());
            textview1.setText("현재온도 : " + weatherinfo.get(0).get("현재온도").toString());
            textview5.setText(weatherinfo.get(0).get("날씨한국어").toString());

            textview2.setText("체감온도 : " + weatherinfo.get(0).get("체감온도").toString());
            if (high_temp < -990) {
                textview3.setText("최고온도 : -");
                for(int i=1;i<16;i++){
                    high_temp=Double.parseDouble(weatherinfo.get(i).get("최고온도").toString());
                    if(high_temp>-990){
                        textview3.setText("최고온도 : " + weatherinfo.get(i).get("최고온도").toString());
                        break;
                    }
                }
            }
            else
                textview3.setText("최고온도 : " + weatherinfo.get(0).get("최고온도").toString());
            if (low_temp < -990) {
                textview4.setText("최저온도 : -");
                for(int i=1;i<16;i++){
                    low_temp=Double.parseDouble(weatherinfo.get(i).get("최저온도").toString());
                    if(low_temp>-990){
                        textview4.setText("최저온도 : " + weatherinfo.get(i).get("최저온도").toString());
                        break;
                    }
                }
            }
            else
                textview4.setText("최저온도 : " + weatherinfo.get(0).get("최저온도").toString());

            textview6.setText("강수확률 : " + weatherinfo.get(0).get("강수확률").toString());

        }catch(Exception e){
            //textview1.setText("오류");
        }
    }

    public void setcomment(View v){
        try {
            if (kkk <= 2.0)
                text_comment.setText("날씨가 매우 춥습니다. 패딩 필수!!!");
            else if (kkk > 2.0 && kkk <= 8.0)
                text_comment.setText("날씨가 춥습니다. 감기 걸릴바에 따뜻한 옷 입으세요!!");
            else if (kkk > 8.0 && kkk <= 14.0)
                text_comment.setText("패딩까진 아니더라도 외투는 챙기세요. 방심은 금물!");
            else if (kkk > 14.0 && kkk <= 20.0)
                text_comment.setText("선선하면서 시원한 느낌의 날씨입니다. 얇은 가디건 챙기면 센스쟁이");
            else if (kkk > 20.0 && kkk <= 26.0)
                text_comment.setText("사람들이 대체로 좋아하는 체감온도입니다. 선선과 따뜻함이 공존하는 날씨입니다.");
            else
                text_comment.setText("26도 이상입니다. 햇빛 세기에 따라 따뜻함과 더움으로 갈릴 날씨입니다.");
        }catch(Exception e){
            Toast.makeText(this,"오류",Toast.LENGTH_SHORT).show();
        }
    }
    //데이터받기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                weatherinfo= (ArrayList<HashMap<String, Object>>)data.getSerializableExtra("weather");
                setWeather(textview1);
                setcomment(text_comment);
            }
        }
    }

    public void onPostExecute(Document doc){
        Double Rtemp = 0.0; //체감온도
        String s = "";
        NodeList nodeList = doc.getElementsByTagName("data");
        int cnt=0;
        for(int i = cnt; i < nodeList.getLength(); i++){

        }
    }

    public void button_setting_all_in_main(View v){
        Intent intent_setting_all_in_main = new Intent(this, popup_setting_all.class);
        intent_setting_all_in_main.putExtra("data", "Test Popup");
        startActivityForResult(intent_setting_all_in_main, 1);
    }

    public void detail_weather_button_click(View v){
        if (state.cc == true) {
            clickcnt = true;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //mArrayList = new ArrayList<>();
            //mArrayList.clear();
            try {
                InsertData task = new InsertData();
                Intent intent_detail_weather = new Intent(this, detail_weather.class);
                intent_detail_weather.putExtra("날씨정보", (Serializable) weatherinfo);
                startActivity(intent_detail_weather);
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "이게에러",Toast.LENGTH_LONG);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "로그인 해주세요", Toast.LENGTH_SHORT).show();
        }
    }


    public void man_button_click(View v) {
        if (state.cc == true) {
            clickcnt = true;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String a = null;
            //mArrayList = new ArrayList<>();
            //mArrayList.clear();


            if(kkk <=16) {
                int random_down = (int)(Math.random()*10)+1;
                a = String.valueOf(random_down);
            }
            else if(kkk > 16) {
                int random_down = (int)(Math.random()*10)+21;
                a = String.valueOf(random_down);
            }

            get_id = a;
            InsertData task = new InsertData();
            task.execute("http://" + IP_ADDRESS + "json_img.php", a);
        } else {
            Toast.makeText(getApplicationContext(), "로그인 해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void woman_button_click(View v) {
        if (state.cc == true) {
            clickcnt = true;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String aa = null;

            /*
            if(kkk<=14){
                int random_down = (int)(Math.random()*10)+11;
                if(random_down>=16){
                    for(;;){
                        random_down = (int)(Math.random()*10)+11;
                        if(random_down<16)
                            break;
                    }
                }
                aa = String.valueOf(random_down);
            }
            else{
                int random_up = (int)(Math.random()*10)+16;
                if(random_up>=21){
                    for(;;){
                        random_up = (int)(Math.random()*10)+16;
                        if(random_up<21)
                            break;
                    }
                }
                aa = String.valueOf(random_up);
            }*/

            if(kkk <=16) {
                int random_down = (int)(Math.random()*10)+11;
                aa = String.valueOf(random_down);
            }
            else if(kkk > 16) {
                int random_down = (int)(Math.random()*10)+31;
                aa = String.valueOf(random_down);
            }
            get_id = aa;
            InsertData task = new InsertData();
            task.execute("http://" + IP_ADDRESS + "json_img.php", aa);
        } else {
            Toast.makeText(getApplicationContext(), "로그인 해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void total_button_click_link(View v){
        if(clickcnt == true) {
            if(state.cc == true) {
                try {
                    Intent go_to_m_link = new Intent(this, total_link_image.class);
                    go_to_m_link.putExtra("id", check_id);
                    go_to_m_link.putExtra("path", personalData.getMember_image().toString());
                    startActivity(go_to_m_link);
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "여기가 오류입니다", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "비회원은 이용불가", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "MAN OR WOMAN 버튼을 누르세요", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finishAffinity();
//    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);

            Log.d(TAG, "response - " + result);

            if (result == null){

                //mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();

                if(check_id.equals(get_id)) {
                    //Intent intent = new Intent(Login.this, image.class);
                    //startActivity(intent);
                    //Thread t = new Thread(Runnable 객체를 만든다);
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {    // 오래 거릴 작업을 구현한다
                            // TODO Auto-generated method stub
                            try{
                                // 걍 외우는게 좋다 -_-
                                int resizeWidth = 272;
                                int resizeHeight = 339;
                                final ImageButton iv = (ImageButton) findViewById(R.id.image);

                                URL url = new URL("http://" + IP_ADDRESS + personalData.getMember_image().toString());

                                InputStream is = url.openStream();
                                final Bitmap bm = BitmapFactory.decodeStream(is);
                                bm.getScaledWidth(resizeWidth);
                                bm.getScaledHeight(resizeHeight);

                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {  // 화면에 그려줄 작업
                                        iv.setImageBitmap(bm);

                                    }
                                });
                                iv.setImageBitmap(bm); //비트맵 객체로 보여주기
                            } catch(Exception e){

                            }

                        }
                    });
                    t.start();
                }
                else{
                    Toast.makeText(getApplicationContext(),"이미지 불러오기 실패",Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String serverURL = (String)params[0];
            String postParameters = "id=" + id;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = bufferedReader.readLine()) != null){ sb.append(line); }
                bufferedReader.close();
                return sb.toString();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    private void showResult(){

        String TAG_JSON="img";
        String TAG_ID = "id";
        String TAG_image = "image";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);
                String image = item.getString(TAG_image);
                check_id = id;

                personalData = new PersonalData();

                personalData.setMember_id(id);
                personalData.setMember_image(image);

                //mTextViewResult.setText(personalData.getMember_id().toString());
                //mArrayList.add(personalData);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

}
