package com.example.it.customlistview;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class findselect extends AppCompatActivity {
    Button btnid, btnpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findselect);

        btnid = (Button)findViewById(R.id.button_findid);
        btnpw = (Button)findViewById(R.id.button_findpw);

        btnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_findid = new Intent(getApplicationContext(), find_id.class);
                startActivity(go_findid);
            }
        });

        btnpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_findpw = new Intent(getApplicationContext(), find_pw.class);
                startActivity(go_findpw);
            }
        });
    }

    public void go_back_log(View v){
        Intent go_back_log = new Intent(this, Login.class);
        startActivity(go_back_log);
    }
}
