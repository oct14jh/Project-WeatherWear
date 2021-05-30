package com.example.it.customlistview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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



public class Login extends AppCompatActivity {

    private static String IP_ADDRESS = "165.229.90.38";
    private static String TAG = "phptest";

    Button Login,Register,Find;
    private EditText getId;
    private EditText getPw;

    private TextView mTextViewResult;
    private String mJsonString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Login = findViewById(R.id.btn_signin);
        Register  = findViewById(R.id.btn_signup);
        Find = findViewById(R.id.btn_find);
        getId = findViewById(R.id.user_id);
        getPw = findViewById(R.id.user_pw);

        mTextViewResult = findViewById(R.id.textView_main_result);
        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        ///////////로그인, 회원가입 버튼///////////////////////////
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = getId.getText().toString();
                state.ID = id;
                String passwd = getPw.getText().toString();
                state.PASSWORD=passwd;

                InsertData task = new InsertData();
                task.execute( "http://" + IP_ADDRESS + "/login.php",id,passwd);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Join.class);
                startActivity(intent);
            }
        });

        Find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),findselect.class);
                startActivity(intent);
            }
        });
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Login.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null)
                mTextViewResult.setText(errorString);

            else {
                mJsonString = result;
                showResult();
                if(mTextViewResult.getText().toString().trim().equals("success")) {
                    state.cc=true;
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[1];
            String password = params[2];
            String serverURL = params[0];
            String postParameters = "id=" + id + "&password=" + password;

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
                if(responseStatusCode == HttpURLConnection.HTTP_OK)
                    inputStream = httpURLConnection.getInputStream();

                else
                    inputStream = httpURLConnection.getErrorStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = bufferedReader.readLine()) != null)
                    sb.append(line);

                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }


    private void showResult(){
        String TAG_JSON="start";
        String TAG_ID = "code";
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                String id = item.getString(TAG_ID);
                PersonalData personalData = new PersonalData();
                personalData.setMember_id(id);
                mTextViewResult.setText(personalData.getMember_id());
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    public void go_back(View v){
        Intent go_backs = new Intent(this, popup_setting_all.class);
        startActivity(go_backs);
    }

}