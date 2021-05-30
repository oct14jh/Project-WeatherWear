package com.example.it.customlistview;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;

public class Board extends AppCompatActivity {
    private static String IP_ADDRESS = "165.229.90.38/";
    private static String TAG = "phptest";
    private String mJsonString;
    private ListView listView;
    ArrayList<HashMap<String, String>> contactList;
    HashMap<String, String> contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
        Button buttonwrite = (Button)findViewById(R.id.button_board_wirte);
        GetData task = new GetData();
        task.execute( "http://" + IP_ADDRESS + "/board_json.php", "");

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent go_to_detail_board = new Intent(getApplicationContext(), inquryboard.class);

                        String item = String.valueOf(parent.getItemAtPosition(position));
                        String s1 = contactList.get(position).get("id");
                        String s2 = contactList.get(position).get("title");
                        String s3 = contactList.get(position).get("content");
                        String s4 = contactList.get(position).get("index");

                        go_to_detail_board.putExtra("idss", s1);
                        go_to_detail_board.putExtra("titless",s2);
                        go_to_detail_board.putExtra("contentss",s3);
                        go_to_detail_board.putExtra("indexss",s4);
                        startActivity(go_to_detail_board);
                    }
                }
        );

        buttonwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_wirte = new Intent(getApplicationContext(), write_board.class);
                startActivity(go_wirte);
            }
        });
    }

    public void go_back_main(View v){
        Intent go_to_mains = new Intent(this, MainActivity.class);
        startActivity(go_to_mains);
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Board.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null)
                Toast.makeText(getApplicationContext(),"ErrorString",Toast.LENGTH_SHORT).show();
            else {
                mJsonString = result;
                showResult();

                ListAdapter adapter = new SimpleAdapter(
                        Board.this, contactList,
                        R.layout.list_item, new String[]{"id", "title",
                        }, new int[]{R.id.userid,
                        R.id.title});

                listView.setAdapter(adapter);
                listView.setSelection(adapter.getCount() - 1);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String postParameters = params[1];
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK)
                    inputStream = httpURLConnection.getInputStream();
                else
                    inputStream = httpURLConnection.getErrorStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null)
                    sb.append(line);

                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }

    private void showResult(){
        String TAG_JSON="board";
        String TAG_index = "index";
        String TAG_title = "title";
        String TAG_content = "content";
        String TAG_ID = "id";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                String index = item.getString(TAG_index);
                String title = item.getString(TAG_title);
                String content = item.getString(TAG_content);
                String id = item.getString(TAG_ID);

                contact = new HashMap<>();
                contact.put("index", index);
                contact.put("title", title);
                contact.put("content", content);
                contact.put("id", id);
                contactList.add(contact);
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
