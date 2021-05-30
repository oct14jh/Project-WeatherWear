package com.example.it.customlistview;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class popup_setting_local extends Activity {
    ArrayList<HashMap<String,Object>> weatherinfo=new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);          // 코드에서 바로 타이틀바 없애기
        setContentView(R.layout.activity_popup_setting_local);
    }

    //팝업 나가기 버튼 클릭
    public void mOnClose(View v){
        finish();
    }

    public void search_button(View v){
        Intent intent_go_search = new Intent(this,search_local.class);
        startActivityForResult(intent_go_search,3001);
    }

    public void gps_button(View v){
        Intent intent_go_gps = new Intent(this, gps_local.class);
        startActivityForResult(intent_go_gps,3000);
    }

    // 바깥 레이아웃 클릭해도 안닫히게 하기 위함
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    //안드로이드 자체 백버튼 막기
    public void onBackPressed() { return; }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent temp=new Intent(this,MainActivity.class);
        if(requestCode==3000){
            if(resultCode==RESULT_OK){
              temp=data;
            }
        }
        if(requestCode==3001){
            if(resultCode==RESULT_OK){
                temp=data;
            }
        }
        setResult(RESULT_OK, temp);
        finish();
    }
}

