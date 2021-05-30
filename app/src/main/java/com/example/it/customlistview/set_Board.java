package com.example.it.customlistview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class set_Board extends AppCompatActivity {

    private static String IP_ADDRESS = "165.229.90.38";
    private static String TAG = "phptest";
    Button button;
    EditText editText;

    TextView titles, ids;
    String idss, title, content, index;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__board);

        intent = getIntent();
        idss = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        index = intent.getStringExtra("index");
        editText =(EditText)findViewById(R.id.content_set);

        titles = (TextView)findViewById(R.id.title_view);
        ids = (TextView)findViewById(R.id.id_view);
        button = (Button)findViewById(R.id.button_setboard);

        titles.setText(title);
        ids.setText(idss);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = editText.getText().toString();
                String id = index;

                InsertData task = new InsertData();
                task.execute( "http://" + IP_ADDRESS + "/setboard.php", id,content);
            }
        });
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(set_Board.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "POST response  - " + result);

            Toast.makeText(getApplicationContext(),"수정완료",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(set_Board.this, Board.class);
            startActivity(intent);
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String content = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "id=" + id + "&content=" + content;


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

    public void go_back_board(View v){
        Intent go_to_boards = new Intent(this, Board.class);
        startActivity(go_to_boards);
    }
}
