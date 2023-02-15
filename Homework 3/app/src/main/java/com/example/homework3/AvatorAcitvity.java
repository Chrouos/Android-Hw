package com.example.homework3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class AvatorAcitvity extends AppCompatActivity implements ViewSwitcher.ViewFactory{

    private GridView mGridView;
    private ImageSwitcher mImgSwitcher;

    Integer[] avatorImgArr = {
            R.drawable.ehe, R.drawable.akuma, R.drawable.mujin,
            R.drawable.pikacap, R.drawable.ikemenkiara};

    Integer[] avatorImgArrTh = {
            R.drawable.ehe_th, R.drawable.akuma_th, R.drawable.mujin_th,
            R.drawable.pikacap_th, R.drawable.ikemenkiara_th};

    Integer avatorPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avator_acitvity);

        // 建立一個ImageAdapter型態的物件
        ImageAdapter imgAdap = new ImageAdapter(getApplicationContext(), avatorImgArrTh);

        mGridView = findViewById(R.id.gridView);
        mGridView.setAdapter(imgAdap);

        // 設定GridView物件的OnItemClickListener
        mGridView.setOnItemClickListener(gridViewOnItemClick);

        mImgSwitcher = findViewById(R.id.imgSwitcher);

        mImgSwitcher.setFactory(this);	// 主程式類別必須implements
        //  ViewSwitcher.ViewFactory
        mImgSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                android.R.anim.fade_in));
        mImgSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                android.R.anim.fade_out));

        mImgSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(); // 新增Intent變數
                intent.setClass(AvatorAcitvity.this, MainActivity.class); // 設定目前Activity與目標Activity

                Bundle bundle = new Bundle();
                bundle.putString("avatorPosition", avatorPosition.toString());
                intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去

                startActivity(intent); // 開始執行轉跳切換
            }
        });


    }

    private AdapterView.OnItemClickListener gridViewOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mImgSwitcher.setImageResource(avatorImgArr[i]);
            avatorPosition = i;
        }
    };

    @Override
    public View makeView() {
        ImageView v = new ImageView(this);
        v.setBackgroundColor(0xFF000000);
        v.setScaleType(ImageView.ScaleType.FIT_CENTER);
        v.setLayoutParams(new ImageSwitcher.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        v.setBackgroundColor(Color.WHITE);
        return v;
    }
}