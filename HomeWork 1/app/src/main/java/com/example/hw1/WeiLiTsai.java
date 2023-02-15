package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import java.util.Arrays;

public class WeiLiTsai extends AppCompatActivity {

    private EditText WeiLiTsaiEditInputNumber; // 輸入組數位置
    private TextView WeiLiTsaiResultText; // 最終結果位置
    private Button WeiLiTsaiConfirmButton; // 確認按鈕

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_li_tsai);

        // 返回到主頁面
        Button MainPageBtn = (Button)findViewById(R.id.WeiLeTsaiBackBtn);
        MainPageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WeiLiTsai.this , MainActivity.class);
                startActivity(intent);
            }
        });

        // 滾動條件
        TextView tvAndroid123 = (TextView)findViewById(R.id.WeiLiTsaiResultText);
        tvAndroid123.setMovementMethod(ScrollingMovementMethod.getInstance());


        WeiLiTsaiEditInputNumber = findViewById((R.id.WeiLiTsaiEditText)); // 組數輸入位置
        WeiLiTsaiConfirmButton = findViewById(R.id.WeiLiTsaiConfirmBtn); // 確認按鈕
        WeiLiTsaiResultText = findViewById(R.id.WeiLiTsaiResultText); // 輸出結果

        WeiLiTsaiConfirmButton.setOnClickListener(WeiLiTsaiConfirmButtonListener); // 確認後的事件
    }

    final View.OnClickListener WeiLiTsaiConfirmButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int groupNumber = 0; // 初始化顯示組數
            String result;

            // 防呆，若未輸入則組數為 0
            if(!WeiLiTsaiEditInputNumber.getText().toString().equals(""))
                groupNumber = Integer.parseInt(WeiLiTsaiEditInputNumber.getText().toString());
            result = "威力彩共" + groupNumber + "組:\r\n";
            result += "                    第一組                   第二組\r\n";

            // 開始建立組數
            int WeiLiTsaiBiggest_1 = 38;

            for(int row=0; row<groupNumber; row++) {
                String res = null;
                int[] temp = new int[6]; // 為了找尋是否重複的區域

                res = "第" + Align(row + 1) + "組";
                for (int col = 0; col < 6; ) {
                    int ran = (int) Math.floor((Math.random() * WeiLiTsaiBiggest_1) + 1);

                    if(isInTheList(temp, col, ran)){
                        temp[col] = ran;
                        col++;
//                        res += Align((int) ran);
                    }
                }

                Arrays.sort(temp); // 排序 temp
                for(int i=0; i<6; i++){
                    res += " " + Align((int) temp[i]);
                }

                int WeiLiTsaiBiggest_2 = 8;
                int ran = (int) Math.floor((Math.random() * WeiLiTsaiBiggest_2) + 1);
                res += "          " + ran;


                res += "\r\n";
                result += res;

                // 最後輸出
            }
            WeiLiTsaiResultText.setText(result);
        }
    };

    // 對齊用
    String Align(int ran){

        String re = "";
        if(ran / 10 >= 1)
            re = "" + ran;
        else
            re = "0" + ran;

        return re;
    }

    // 判斷是否 重複
    boolean isInTheList(int[] Num, int index, int ran){

        boolean status = true;
        for(int i=0; i<index; i++){
            if(Num[i] == ran) status = false;
        }

        return status;
    }
}