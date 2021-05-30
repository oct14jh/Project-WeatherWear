package com.example.it.customlistview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class mypage extends Activity {
    Button cancel, confirm;
    EditText editText;

    private static String TAG = "phptest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        cancel = (Button)findViewById(R.id.cancel_button);
        confirm = (Button)findViewById(R.id.confirm_button);
        editText = (EditText)findViewById(R.id.re_pass);
    }

    public void go_cancel(View v){
        Intent cancel_intent = new Intent(this, popup_setting_all.class);
        startActivity(cancel_intent);
    }

    public void go_confirm(View v) {
        if (editText.getText().toString().equals(state.PASSWORD)) {
            InsertData task = new InsertData();
            task.execute("http://165.229.90.38/withdraw.php", state.ID,state.PASSWORD);
            Intent confirm_intent = new Intent(this, MainActivity.class);
            startActivity(confirm_intent);
        }
        else{ Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인 해 주세요.", Toast.LENGTH_SHORT).show(); }
    }
    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(mypage.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);

            state.state_allset();

            Intent intent = new Intent(mypage.this, MainActivity.class);
            startActivity(intent);
        }
        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String password = params[2];
            String serverURL = (String)params[0];
            String postParameters = "id=" + id+"&password="+password;


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


}