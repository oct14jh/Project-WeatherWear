package com.example.it.customlistview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
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

public class Join extends AppCompatActivity {
    private static String IP_ADDRESS = "165.229.90.38";
    private static String TAG = "phptest";

    private EditText mEditTextid;
    private EditText mEditTextpw;
    private EditText mEditTextem;
    private TextView mTextViewResult;

    boolean check=false;

    public EditText m_id() { return (EditText)findViewById(R.id.editText_Join_id); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mEditTextid = (EditText)findViewById(R.id.editText_Join_id);
        mEditTextpw = (EditText)findViewById(R.id.editText_Join_pm);
        mEditTextem = (EditText)findViewById(R.id.editText_Join_em);
        mTextViewResult = (TextView)findViewById(R.id.textView_Join_result);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        Button buttonCheck = (Button)findViewById(R.id.button_Join_Check);
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check=true;
                String id = mEditTextid.getText().toString();

                Intent intent = new Intent(getApplicationContext(),Check.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, 3000);
            }
        });

        Button buttonInsert = (Button)findViewById(R.id.button_Join_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mEditTextid.getText().toString();
                String pw = mEditTextpw.getText().toString();
                String em = mEditTextem.getText().toString();
                if(check==true) {
                    if(pw != null && em != null) {
                        InsertData task = new InsertData();
                        task.execute("http://" + IP_ADDRESS + "/insert.php", id, pw, em);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"pw와 이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                    check = false;  // 내가 방금 추가해준거임... 중복체크 한번 하고 나면 join 되는경우잇어서
                }
                else if(check==false){
                    Toast.makeText(getApplicationContext(),"중복체크 먼저하세요",Toast.LENGTH_SHORT).show();
                    check=false;
                }
                mEditTextid.setText("");
                mEditTextpw.setText("");
                mEditTextem.setText("");
            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
// MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case 3000:
                    mEditTextid.setText(data.getStringExtra("id"));
                    break;
            }
        }
    }

    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Join.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);

            Intent intent = new Intent(Join.this, Login.class);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(String... params) {
            String id = (String)params[1];
            String pw = (String)params[2];
            String em = (String)params[3];
            String serverURL = (String)params[0];
            String postParameters = "id=" + id + "&password=" + pw + "&email=" + em;

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

    public void go_to_log(View v){
        Intent go_to_logs = new Intent(this, Login.class);
        startActivity(go_to_logs);
    }
}