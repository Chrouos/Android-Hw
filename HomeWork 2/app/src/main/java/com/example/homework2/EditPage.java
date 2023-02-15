package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;

import android.text.method.ScrollingMovementMethod;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;


import android.database.Cursor;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.Menu;

import org.w3c.dom.Text;

import java.util.*;
import java.text.*;
import java.util.Date;
import java.util.Calendar;

public class EditPage extends AppCompatActivity {

    TextView startDate1, startDate2, startDate3, startDate4;
    TextView endDate1, endDate2, endDate3, endDate4;
    Button edit1Btn, edit2Btn, edit3Btn, edit4Btn;
    Button delete1Btn, delete2Btn, delete3Btn, delete4Btn;
    Button goToMainPage, goToAnalyzePage;

    private static final String DATABASE_NAME = "HomeWork2.db"; // 建立"資料庫名稱"常數
    private static final int DATABASE_VERSION = 1;  // 建立"資料庫版本"常數
    private static String DATABASE_TABLE1 = "mcCalendar"; //宣告"行事曆"常數
    private SQLiteDatabase db; //宣告資料庫變數
    private MyDBHelper dbHelper; //宣告資料庫幫助器變數
    Cursor cursor;

    TextView[] startText = new TextView[4], endText = new TextView[4];
    Button[] editBtn = new Button[4], deleteBtn = new Button[4];
    int[] nowIdList = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        /*-------------- 找ID區域 --------------*/
        startDate1 = (TextView) findViewById(R.id.start_date_1);
        startDate2 = (TextView) findViewById(R.id.start_date_2);
        startDate3 = (TextView) findViewById(R.id.start_date_3);
        startDate4 = (TextView) findViewById(R.id.start_date_4);

        endDate1 = (TextView) findViewById(R.id.end_date_1);
        endDate2 = (TextView) findViewById(R.id.end_date_2);
        endDate3 = (TextView) findViewById(R.id.end_date_3);
        endDate4 = (TextView) findViewById(R.id.end_date_4);

        edit1Btn = (Button) findViewById(R.id.edit_1);
        edit2Btn = (Button) findViewById(R.id.edit_2);
        edit3Btn = (Button) findViewById(R.id.edit_3);
        edit4Btn = (Button) findViewById(R.id.edit_4);

        delete1Btn = (Button) findViewById((R.id.delete_1));
        delete2Btn = (Button) findViewById((R.id.delete_2));
        delete3Btn = (Button) findViewById((R.id.delete_3));
        delete4Btn = (Button) findViewById((R.id.delete_4));

        startText[0] = startDate1; endText[0] = endDate1;
        startText[1] = startDate2; endText[1] = endDate2;
        startText[2] = startDate3; endText[2] = endDate3;
        startText[3] = startDate4; endText[3] = endDate4;

        editBtn[0] = edit1Btn; deleteBtn[0] = delete1Btn;
        editBtn[1] = edit2Btn; deleteBtn[1] = delete2Btn;
        editBtn[2] = edit3Btn; deleteBtn[2] = delete3Btn;
        editBtn[3] = edit4Btn; deleteBtn[3] = delete4Btn;

        goToAnalyzePage = (Button) findViewById(R.id.analyze_page);
        goToMainPage = (Button) findViewById(R.id.main_page);
        /*-------------- 找ID區域 --------------*/

        /*-------------------- SQLite Start --------------------*/
        try{
            dbHelper = new MyDBHelper(getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION ); //建立資料庫輔助類別物件
            db = dbHelper.getWritableDatabase(); // 透過資料庫輔助類別物件建立一個可以讀寫的SQLite資料庫 (HomeWork2.db)
            // db.execSQL("DROP TABLE mcCalendar"); // 每次開啟都會Drop 方便測試用

            // 檢查資料表DATABASE_TABLE1是否已經存在，如果不存在，就建立一個。
            cursor = db.rawQuery(
                    "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                            DATABASE_TABLE1 + "'", null);
            if(cursor != null) {
                if(cursor.getCount() == 0){	// 沒有資料表，要建立一個資料表。

                    // 執行SQL建立資料表指令， 建立記帳簿資料表(calendar)，此資料表有以下5個欄位:
                    // id(編號): 整數，為主鍵，數值會自動增加
                    // event(事件): 字串，not null
                    // type(事件類別):整數，not null，(ex. 0:代辦事項, 1:行事曆)
                    // startDate(開始日期):date型別(只包含年月日資訊)，不可為null(空白)
                    // endDate(結束日期):date型別(只包含年月日資訊)，不可為null(空白)
                    db.execSQL("CREATE TABLE mcCalendar ("
                            + "id integer primary key autoincrement, "
                            + "comment nvarchar(255), " // 備註
                            + "startDate date, " // 開始時間
                            + "endDate date, " // 結束時間
                            + "duringDay integer," // 持續時間
                            + "preDate)"); // 間隔上一次的時間
                }
            }
        }
        catch(Exception ex){}
        /*-------------------- SQLite End --------------------*/

        /*-------------------- 切換頁面至 goToAnalyzePage Start --------------------*/
        goToAnalyzePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AnalyzePageIntent = new Intent(); // 新增Intent變數
                AnalyzePageIntent.setClass(EditPage.this, AnalyzePage.class); // 設定目前Activity與目標Activity
                startActivity(AnalyzePageIntent); // 開始執行轉跳切換
            }
        });
        /*-------------------- 切換頁面至 goToAnalyzePage End --------------------*/

        /*-------------------- 切換頁面至 goToMainPage Start --------------------*/
        goToMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditPageIntent = new Intent(); // 新增Intent變數
                EditPageIntent.setClass(EditPage.this, MainActivity.class); // 設定目前Activity與目標Activity
                startActivity(EditPageIntent); // 開始執行轉跳切換
            }
        });
        /*-------------------- 切換頁面至 goToMainPage End --------------------*/

        /*-------------------- 抓取資訊到 編輯頁面上 Start --------------------*/
        querySelect_edit();
        /*-------------------- 抓取資訊到 編輯頁面上 End --------------------*/

        /*-------------------- 修改 Btn 1 Start --------------------*/
        edit1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitUpdate(nowIdList[0], startText[0].getText().toString(), endText[0].getText().toString());
            }
        });
        /*-------------------- 修改 Btn 1 End --------------------*/

        /*-------------------- 修改 Btn 2 Start --------------------*/
        edit2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitUpdate(nowIdList[1], startText[1].getText().toString(), endText[1].getText().toString());
            }
        });
        /*-------------------- 修改 Btn 2 End --------------------*/

        /*-------------------- 修改 Btn 3 Start --------------------*/
        edit3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitUpdate(nowIdList[2], startText[2].getText().toString(), endText[2].getText().toString());
            }
        });
        /*-------------------- 修改 Btn 3 End --------------------*/

        /*-------------------- 修改 Btn 4 Start --------------------*/
        edit4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitUpdate(nowIdList[3], startText[3].getText().toString(), endText[3].getText().toString());
            }
        });
        /*-------------------- 修改 Btn 4 End --------------------*/

        /*-------------------- 刪除 Btn 1 Start --------------------*/
        delete1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDelete(nowIdList[0]);
            }
        });
        /*-------------------- 刪除 Btn 1 End --------------------*/

        /*-------------------- 刪除 Btn 2 Start --------------------*/
        delete2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDelete(nowIdList[1]);
            }
        });
        /*-------------------- 刪除 Btn 2 End --------------------*/

        /*-------------------- 刪除 Btn 3 Start --------------------*/
        delete3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDelete(nowIdList[2]);
            }
        });
        /*-------------------- 刪除 Btn 3 End --------------------*/

        /*-------------------- 刪除 Btn 4 Start --------------------*/
        delete4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDelete(nowIdList[3]);
            }
        });
        /*-------------------- 刪除 Btn 4 End --------------------*/





    }

    // 利用 id 改變 startDate, endDate
    public void submitUpdate(int inedxId, String startDate, String endDate) {
        try{
            ContentValues contentvalues = new ContentValues();
            String where = "id = " + inedxId;
            contentvalues.put("startDate", startDate);
            contentvalues.put("endDate", endDate);
            db.update(DATABASE_TABLE1, contentvalues, where,null);
        }
        catch(Exception ex){
        }
        finally{
            querySelect_edit();
        }
    }

    // 利用 id 刪除
    public void submitDelete(int inedxId) {
        try{
            String where = "id = " + inedxId;
            db.delete(DATABASE_TABLE1, where,null);
        }
        catch(Exception ex){
        }
        finally{
            querySelect_edit();
        }
    }



    public void querySelect_edit(){
        String commandString = "SELECT * FROM " + DATABASE_TABLE1 + " order by id desc " ;
        cursor = db.rawQuery(commandString, null); //執行資料表查詢命令
        if(cursor != null){ // 若有回傳消費紀錄，則進行以下處理
            int n = cursor.getCount(); //取得資料筆數
            cursor.moveToFirst();  // 將指標移到第1筆紀錄
            for (int i = 0; i < 4; i++){ // 利用迴圈逐一讀取每一筆紀錄
                if(i < n){
                    String ans = "";
                    nowIdList[i] = cursor.getInt(0);
                    startText[i].setText(cursor.getString(2));
                    endText[i].setText(cursor.getString(3));
                    cursor.moveToNext();
                }
                else{
                    nowIdList[i] = 0;
                    startText[i].setText("");
                    endText[i].setText("");
                }
            }
        }
    }


}