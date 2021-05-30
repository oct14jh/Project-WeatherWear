package com.example.it.customlistview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class popup_setting_all extends AppCompatActivity {
    Button button;
    Button button2;
    Button button3;
    Button button4;
//    public void board_button(View v){
//        Intent intent_setting_local_in_main = new Intent(getApplicationContext(), Board.class);
//        startActivity(intent_setting_local_in_main);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_setting_all);

        button = findViewById(R.id.board_button);
        button2 = findViewById(R.id.mypages_button);
        button3 = findViewById(R.id.logs_button);
        button4 = findViewById(R.id.logout_button);

        check_log_state(state.cc);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.cc==true) {
                    Intent intent = new Intent(getApplicationContext(), Board.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.cc==true) {
                    Intent intent = new Intent(getApplicationContext(), mypage.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.cc = false;
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("state.cc",state.cc);
                startActivity(intent);
            }
        });
    }

    public void check_log_state(boolean value){
        if(value==true){
            button4.setVisibility(View.VISIBLE);
            button3.setVisibility(View.INVISIBLE);
        }
        else{
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
    }

    public void I_M_B(View v){
        Intent go_to_main = new Intent(this, MainActivity.class);
        startActivity(go_to_main);
    }

}
