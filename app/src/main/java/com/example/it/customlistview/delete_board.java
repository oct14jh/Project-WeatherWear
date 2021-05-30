package com.example.it.customlistview;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class delete_board extends AppCompatActivity {
    private static String IP_ADDRESS = "165.229.90.38";
    private static String TAG = "phptest";
    Button delbtn, backbtn;
    Intent intent;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_board);

        intent = getIntent();
        id = intent.getStringExtra("index");
        delbtn = (Button)findViewById(R.id.button_del);
        backbtn = (Button)findViewById(R.id.button_inquryback);

        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData task = new InsertData();
                task.execute( "http://" + IP_ADDRESS + "/delboard.php", id);
            }
        });
        /*
        backbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent backintent = new Intent(getApplicationContext(), inquryboard.class);
                startActivity(backintent);
            }
        });
        */
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(delete_board.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
            Toast.makeText(getApplicationContext(),"삭제완료",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(delete_board.this, Board.class);
            startActivity(intent);
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
                return sb.toString();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    public void go_back_no(View v){
        Intent go_backs = new Intent(this, Board.class);
        Toast.makeText(this,"삭제 취소",Toast.LENGTH_SHORT).show();
        startActivity(go_backs);
    }
}
