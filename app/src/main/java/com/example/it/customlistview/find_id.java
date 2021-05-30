package com.example.it.customlistview;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class find_id extends AppCompatActivity {
    private static String IP_ADDRESS = "165.229.90.38/";
    private static String TAG = "phptest";

    Button Find, Backlog;
    EditText selectid;
    private TextView mTextViewResult;
    private String mJsonString;
    String id;
    PersonalData personalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        selectid = (EditText)findViewById(R.id.editText_find_id);
        Find = (Button)findViewById(R.id.button_find);
        Backlog = (Button)findViewById(R.id.button_backlogin);

        mTextViewResult = (TextView)findViewById(R.id.textView_find_result);
        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        Find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String em = selectid.getText().toString();
                InsertData task = new InsertData();
                task.execute( "http://" + IP_ADDRESS + "/findid.php",em);
            }
        });

        Backlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
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
            progressDialog = ProgressDialog.show(find_id.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);
            mJsonString = result;
            showResult();
            mTextViewResult.setText("\nID : "+ personalData.getMember_id());
        }

        @Override
        protected String doInBackground(String... params) {
            String em = (String)params[1];
            String serverURL = (String)params[0];
            String postParameters = "email=" + em ;
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
        String TAG_JSON="find";
        String TAG_ID = "id";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                String id = item.getString(TAG_ID);
                personalData = new PersonalData();
                personalData.setMember_id(id);
                mTextViewResult.setText(personalData.getMember_id());
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
