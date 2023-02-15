package com.example.homework3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import android.content.Intent;


public class MainActivity extends AppCompatActivity{

    private MediaPlayer[] mp = new MediaPlayer[4]; // 音樂撥放器
    final String[] music = {"yuen_007", "yuen_008", "yuen_010", "yuen_sup"}; // 歌曲清單
    Spinner spinner; // 選擇音樂 － 下拉式選單
    int nowMusicPosition = 0;

    // 這個縮圖陣列是App專案的影像資源ID
    Integer[] avatorImgArr = {
            R.drawable.ehe, R.drawable.akuma, R.drawable.mujin,
            R.drawable.pikacap, R.drawable.ikemenkiara}; // 頭像陣列
    private ImageView avator; // 頭像圖片
    Integer avatorPosition = 0; // 目前頭像的位置（可能從avatorActivity傳過來），預設 0

    // GIF
    private ImageView mImgViewPikaRun;

    // Video
    Button btnVideo;
    final String[] video = {"皮卡丘偵探", "皮卡丘之歌"}; // 歌曲清單
    Spinner spinnerVideo; // 選擇音樂 － 下拉式選單
    Integer nowVideoPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO 頭像可以切換(相簿)
        if (getIntent().getExtras() != null) // 為了防止錯誤訊息：若沒有從AvatorActivity中傳來avatorPosition的話會錯誤
        {
            Bundle bundle = getIntent().getExtras();
            String bun_avatorPosition = bundle.getString("avatorPosition");
            if (!bun_avatorPosition.isEmpty())
            {
                avatorPosition = Integer.parseInt(bun_avatorPosition);
            }
        }


        // 頭貼圓形化，利用Bitmap完成
        avator = (ImageView) findViewById(R.id.view_avatar);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), avatorImgArr[avatorPosition]); // 接收資訊
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setAntiAlias(true); // 抗鋸齒，使圖片邊角更圓潤
        roundedBitmapDrawable.setCircular(true); // 圓形展示
        avator.setBackgroundDrawable(roundedBitmapDrawable);

        // 點擊頭像 切換頁面
        avator.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                Intent intent = new Intent(); // 新增Intent變數
                intent.setClass(MainActivity.this, AvatorAcitvity.class); // 設定目前Activity與目標Activity
                mp[nowMusicPosition].pause(); // 跳轉頁面音樂要暫停，不然會跟影片打架

                startActivity(intent); // 開始執行轉跳切換
            }
        });


        // 下面GIF可以跑動
        mImgViewPikaRun = findViewById(R.id.view_pika_run);
        Resources res = getResources(); // 從程式資源載入動畫，設定給ImageView物件，然後開始播放
        final AnimationDrawable animDraw =
                (AnimationDrawable) res.getDrawable(R.drawable.anim_pika_run);
        mImgViewPikaRun.setImageDrawable(animDraw);
        animDraw.start();

        // TODO Video
        btnVideo = (Button) findViewById(R.id.btn_play_video);
        btnVideo.setOnClickListener(new Button.OnClickListener() { // 按下按鈕 觸發事件
            public void onClick(View arg0) {
                Intent intent = new Intent(); // 新增Intent變數
                intent.setClass(MainActivity.this, VideoActivity.class); // 設定目前Activity與目標Activity
                Bundle bundle = new Bundle();
                bundle.putString("video", nowVideoPosition.toString());
                intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去
                mp[nowMusicPosition].pause();

                startActivity(intent); // 開始執行轉跳切換
            }
        });

        // 下拉式選單(選擇影片)
        spinnerVideo = (Spinner) findViewById(R.id.spinner_chose_video);
        ArrayAdapter<String> videoList = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                video);
        spinnerVideo.setAdapter(videoList);

        // 若下拉式選單有更動的話
        spinnerVideo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nowVideoPosition = position;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 音樂－導入音樂庫(四首) + 自動播放
        mp[0] = MediaPlayer.create(this, R.raw.yuen_007);
        mp[1] = MediaPlayer.create(this, R.raw.yuen_008);
        mp[2] = MediaPlayer.create(this, R.raw.yuen_010);
        mp[3] = MediaPlayer.create(this, R.raw.yuen_sup);
        mp[nowMusicPosition].start(); // 自動撥放第一首
        
        // 下拉式選單(選擇音樂)
        spinner = (Spinner) findViewById(R.id.spinner_chose_music);
        ArrayAdapter<String> musicList = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                music);
        spinner.setAdapter(musicList);

        // 若下拉式選單有更動的話
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 先將原本的音樂暫停 -> 啟動新選擇的音樂
                mp[nowMusicPosition].pause(); mp[position].start(); nowMusicPosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //重複播放
        mp[nowMusicPosition].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });

        //播放錯誤的處理
        mp[nowMusicPosition].setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //釋放資源
                mp.release();
                return false;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp[nowMusicPosition].release(); //停止並釋放資源
        mp[nowMusicPosition] = null;
    }




}