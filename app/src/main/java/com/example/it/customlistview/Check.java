package com.example.it.customlistview;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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

public class Check extends AppCompatActivity {
    private static String IP_ADDRESS = "165.229.90.38/";
    private static String TAG = "phptest";

    private EditText getId;
    Intent intent;
    static String get_id;
    private TextView mTextViewResult;
    private String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        intent = getIntent();
        get_id = intent.getStringExtra("id");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mTextViewResult = (TextView)findViewById(R.id.textView_Check_result);
        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        InsertData task = new InsertData();
        task.execute( "http://" + IP_ADDRESS + "check.php",get_id);                                 // 입력한 id값 db의 id와 비교
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Check.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){
                mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
                if(mTextViewResult.getText().toString().trim().equals("1565")) {
                    Toast.makeText(getApplicationContext(),"사용할 수 있는 아이디입니다.",Toast.LENGTH_SHORT).show();
                    intent.putExtra("id", get_id);
                    intent.putExtra("check_state", "check");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"중복된 아이디가 있습니다.",Toast.LENGTH_SHORT).show();
                    intent.putExtra("id", "");
                    intent.putExtra("check_state", "");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];

            String serverURL = (String)params[0];
            String postParameters = "id=" + id ;


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
                mTextViewResult.setText(personalData.getMember_id().toString());
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}