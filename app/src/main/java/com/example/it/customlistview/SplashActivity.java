package com.example.it.customlistview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedlnstanceState) {

        super.onCreate(savedlnstanceState);

        try{
            Thread.sleep(1200);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}