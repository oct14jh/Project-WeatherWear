package com.example.it.customlistview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class inquryboard extends AppCompatActivity {

    TextView titles, ids, contents;
    String idss, titless, contentss, indexss;
    Intent intent3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquryboard);

        intent3 = getIntent();
        idss = intent3.getStringExtra("idss");
        titless = intent3.getStringExtra("titless");
        contentss = intent3.getStringExtra("contentss");
        indexss = intent3.getStringExtra("indexss");

        titles = (TextView)findViewById(R.id.title_view);
        ids = (TextView)findViewById(R.id.id_view);
        contents = (TextView)findViewById(R.id.content_view);

        titles.setText(titless);
        ids.setText(idss);
        contents.setText(contentss);

        Button Setboard = (Button)findViewById(R.id.button_setboard);
        Button Delete = (Button)findViewById(R.id.button_delete);

        Setboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idss.equals(state.ID)||state.ID.equals("root")) {
                    Intent go_setboard = new Intent(getApplicationContext(), set_Board.class);
                    go_setboard.putExtra("index", indexss);
                    go_setboard.putExtra("title",titless);
                    go_setboard.putExtra("id",idss);
                    startActivity(go_setboard);

                } else{
                    Toast.makeText(getApplicationContext(),"작성자의 ID와 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idss.equals(state.ID)||state.ID.equals("root")) {
                    Intent go_deleteboard = new Intent(getApplicationContext(), delete_board.class);
                    go_deleteboard.putExtra("index", indexss);
                    startActivity(go_deleteboard);
                }else {
                    Toast.makeText(getApplicationContext(),"작성자의 ID와 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void go_back_board(View v){
        Intent go_to_boards = new Intent(this, Board.class);
        startActivity(go_to_boards);
    }
}