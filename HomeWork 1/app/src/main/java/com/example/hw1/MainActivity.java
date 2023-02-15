package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 跳轉頁面到 大樂透
        Button DaReTouPageBtn = (Button)findViewById(R.id.DaReTou);
        DaReTouPageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , DaReTou.class);
                startActivity(intent);
            }
        });

        // 跳轉頁面到 威力彩
        Button WeiLiTsaiPageBtn = (Button)findViewById(R.id.WeiLiTsai);
        WeiLiTsaiPageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , WeiLiTsai.class);
                startActivity(intent);
            }
        });

    }




}