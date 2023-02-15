package com.example.hw1;

import java.util.Arrays;
import androidx.appcompat.app.AppCompatActivity;

import android.text.method.ScrollingMovementMethod;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import java.util.Arrays;

public class DaReTou extends AppCompatActivity {


    private EditText editInputNumber; // 輸入組數位置
    private TextView resultText; // 最終結果位置
    private Button ConfirmButton; // 確認按鈕

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_re_tou);

        // 返回到主頁面 START
        Button MainPageBtn = (Button)findViewById(R.id.DaReTouBackBtn);
        MainPageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(DaReTou.this , MainActivity.class);
                startActivity(intent);
            }
        });
        // 返回到主頁面 END

        // 滾動條件
        TextView tvAndroid123 = (TextView)findViewById(R.id.resultText);
        tvAndroid123.setMovementMethod(ScrollingMovementMethod.getInstance());

        editInputNumber = findViewById((R.id.editTextNumberSigned)); // 組數輸入位置
        ConfirmButton = findViewById(R.id.ConfirmBtn); // 確認按鈕
        resultText = findViewById(R.id.resultText); // 輸出結果

        ConfirmButton.setOnClickListener(ConfirmButtonListener); // 確認後的事件


    }

    final View.OnClickListener ConfirmButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int groupNumber = 0; // 初始化顯示組數
            String result;

            // 防呆，若未輸入則組數為 0
            if(!editInputNumber.getText().toString().equals(""))
                groupNumber = Integer.parseInt(editInputNumber.getText().toString());
            result = "大樂透共" + groupNumber + "組:\r\n";

            // 開始建立組數
            int DaReTouBiggest = 49;

            for(int row=0; row<groupNumber; row++) {
                String res = null;
                int[] temp = new int[6]; // 為了找尋是否重複的區域

                res = "第" + Align(row + 1) + "組";
                for (int col = 0; col < 6; ) {
                    int ran = (int) Math.floor((Math.random() * DaReTouBiggest) + 1);

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
                res += "\r\n";

                result += res;

                // 最後輸出
            }
            resultText.setText(result);
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