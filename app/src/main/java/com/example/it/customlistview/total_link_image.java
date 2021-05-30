package com.example.it.customlistview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class total_link_image extends AppCompatActivity {

    Button top_name,bottom_name;
    private static String IP_ADDRESS = "165.229.90.38/";
    private static String TAG = "phptest";

    PersonalData personalData;
    private String mJsonString;
    Handler handler = new Handler();  // 외부쓰레드 에서 메인 UI화면을 그릴때 사용
    String check_img, get_img;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_link_image);

        top_name = findViewById(R.id.top_name);
        bottom_name = findViewById(R.id.bottom_name);

        intent = getIntent();
        String id = intent.getStringExtra("id");
        get_img = intent.getStringExtra("path");

        InsertData task = new InsertData();
        task.execute( "http://" + IP_ADDRESS + "img.php",id);

    }

    public void one_click_top(View v){
        top_name.setVisibility(View.VISIBLE);
        top_name.setText(personalData.getMember_top().toString());
    }

    public void one_click_bottom(View v){
        bottom_name.setVisibility(View.VISIBLE);
        bottom_name.setText(personalData.getMember_bottom().toString());
    }

    public void two_click_top(View v){
        Intent go_to_link_top = new Intent(Intent.ACTION_VIEW, Uri.parse("https://msearch.shopping.naver.com/search/all.nhn?origQuery="+ personalData.getMember_top().toString() +
                "&pagingIndex=1&viewType=lst&sort=rel&showFilter=true&frm=NVSHSRC&selectedFilterTab=price&query="+ personalData.getMember_top().toString()));
        startActivity(go_to_link_top);
    }
    public void two_click_bottom(View v){
        Intent go_to_link_bottom = new Intent(Intent.ACTION_VIEW, Uri.parse("https://msearch.shopping.naver.com/search/all.nhn?origQuery="+ personalData.getMember_bottom().toString() +
                "&pagingIndex=1&viewType=lst&sort=rel&showFilter=true&frm=NVSHSRC&selectedFilterTab=price&query="+ personalData.getMember_bottom().toString()));
        startActivity(go_to_link_bottom);
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(total_link_image.this,
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

                if(check_img.equals(get_img)) {
                    //Intent intent = new Intent(Login.this, image.class);
                    //startActivity(intent);
                    //Thread t = new Thread(Runnable 객체를 만든다);
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {    // 오래 거릴 작업을 구현한다
                            // TODO Auto-generated method stub
                            try{
                                // 걍 외우는게 좋다 -_-
                                final ImageView iv = (ImageView) findViewById(R.id.imageView3);

                                URL url = new URL("http://" + IP_ADDRESS + personalData.getMember_image().toString());

                                InputStream is = url.openStream();
                                final Bitmap bm = BitmapFactory.decodeStream(is);

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

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


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
        String TAG_image = "image";
        String TAG_top = "top";
        String TAG_bottom = "bottom";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String image = item.getString(TAG_image);
                String top = item.getString(TAG_top);
                String bottom = item.getString(TAG_bottom);
                check_img = image;

                personalData = new PersonalData();

                personalData.setMember_image(image);
                personalData.setMember_top(top);
                personalData.setMember_bottom(bottom);

            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

}
