package com.example.homework3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.TextView;

public class VideoActivity extends AppCompatActivity  implements MediaPlayer.OnErrorListener,
                                                                    MediaPlayer.OnCompletionListener{
    // Video
    Button btnVideo;
    Integer a = 0;

    private VideoView mVideoView;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Bundle bundle = getIntent().getExtras();
        String vide = bundle.getString("video");

        test = (TextView) findViewById(R.id.test);

        // TODO 喜歡的影片片段
        mVideoView = findViewById(R.id.view_video);

        MediaController mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        Uri uri = Uri.parse("android.resource://" +
                getPackageName() + "/" + R.raw.pikachu_song);

        if(vide.equals("0")){
            uri = Uri.parse("android.resource://" +
                getPackageName() + "/" + R.raw.detective_pikachu);
            test.setText("皮卡丘偵探╰(*°▽°*)╯");
        }
        else{
            uri = Uri.parse("android.resource://" +
                    getPackageName() + "/" + R.raw.pikachu_song);
            test.setText("皮卡丘之歌(❁´◡`❁)");
        }

        mVideoView.setVideoURI(uri);

        btnVideo = (Button) findViewById(R.id.btn_return_main);
        btnVideo.setOnClickListener(new Button.OnClickListener() { // 按下按鈕 觸發事件
            public void onClick(View arg0) {
                Intent intent = new Intent(); // 新增Intent變數
                intent.setClass(VideoActivity.this, MainActivity.class); // 設定目前Activity與目標Activity
                Bundle bundle = new Bundle();
                bundle.putString("video:", a.toString());
                intent.putExtras(bundle);   // 記得put進去，不然資料不會帶過去

                startActivity(intent); // 開始執行轉跳切換
            }
        });
    }

    @Override
    protected void onResume() {
        mVideoView.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mVideoView.stopPlayback();
        super.onPause();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Toast.makeText(VideoActivity.this, "播放完畢！", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Toast.makeText(VideoActivity.this, "發生錯誤！", Toast.LENGTH_LONG)
                .show();
        return true;
    }
}
