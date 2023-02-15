package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;

import android.text.method.ScrollingMovementMethod;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.Menu;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import java.util.*;
import java.text.*;
import java.util.Calendar;

public class AnalyzePage extends AppCompatActivity {

    TextView lastDate, analyzeNext, howLongDay, cycle;
    Button goToMainPage, goToEditPage;

    private static final String DATABASE_NAME = "HomeWork2.db"; // 建立"資料庫名稱"常數
    private static final int DATABASE_VERSION = 1;  // 建立"資料庫版本"常數
    private static String DATABASE_TABLE1 = "mcCalendar"; //宣告"行事曆"常數
    private SQLiteDatabase db; //宣告資料庫變數
    private MyDBHelper dbHelper; //宣告資料庫幫助器變數
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_page);

        /*-------------------- 找 ID 區域 Start --------------------*/
        lastDate = (TextView) findViewById(R.id.last_date);
        analyzeNext = (TextView) findViewById(R.id.analyze_next);
        howLongDay = (TextView) findViewById(R.id.how_long_day);
        cycle = (TextView) findViewById(R.id.cycle);
        goToMainPage = (Button) findViewById(R.id.main_page);
        goToEditPage = (Button) findViewById(R.id.edit_page);
        /*-------------------- 找 ID 區域 End --------------------*/

        /*-------------------- SQLite Start --------------------*/
        try{
            dbHelper = new MyDBHelper(getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION ); //建立資料庫輔助類別物件
            db = dbHelper.getWritableDatabase(); // 透過資料庫輔助類別物件建立一個可以讀寫的SQLite資料庫 (HomeWork2.db)
//            db.execSQL("DROP TABLE mcCalendar"); // 每次開啟都會Drop 方便測試用

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

        /*-------------------- 分析 Start --------------------*/
        String commandString = "SELECT * FROM " + DATABASE_TABLE1 + " order by id desc " ;
        cursor = db.rawQuery(commandString, null); //執行資料表查詢命令
        if(cursor != null){ // 若有回傳消費紀錄，則進行以下處理
            int n = cursor.getCount(); //取得資料筆數
            cursor.moveToFirst();  // 將指標移到第1筆紀錄
            int totalHowLongDay = 0;
            int totalCycle = 0;
            int countH = 0, countC = 0;

            String preEndD = null, lastEndD = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); //  準備輸出的格式，如：2009/01/01
            DateFormat df = DateFormat.getDateInstance(); //  利用 DateFormat 來parse 日期的字串
            for (int i = 0; i < n; i++){ // 利用迴圈逐一讀取每一筆紀錄
                String startD = cursor.getString(2), endD = cursor.getString(3);

                // 第一筆: last_date
                String firstStr = "最近一筆: \n" + cursor.getString(2) + "   " + cursor.getString(3);
                if(i == 0) {
                    lastDate.setText(firstStr);
                    lastEndD = endD;
                }

                // 第二筆: 先把所有時間相加(startDate - endDate)，在除與總比數
                try {
                    if(startD != null && endD != null){

                        Date sDate = sdf.parse(startD);
                        Date eDate = sdf.parse(endD);
                        long diff = eDate.getTime() - sDate.getTime();

                        TimeUnit time = TimeUnit.DAYS;
                        long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                        System.out.println("The difference in days is : " + diffrence);
                        totalHowLongDay += diffrence;
                        countH += 1;
                    }
                } catch (ParseException e) {
                    System.out.println(e);
                }

                // 第三筆: 平均幾天一次
                try {
                    if(preEndD != null && endD != null){
                        System.out.println("preEndD: " + preEndD + " endD: " + endD);
                        Date preDate = sdf.parse(preEndD);
                        Date nowDate = sdf.parse(endD);
                        long diff = preDate.getTime() - nowDate.getTime();

                        TimeUnit time = TimeUnit.DAYS;
                        long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                        System.out.println("The difference in days is : " + diffrence);
                        totalCycle += diffrence;
                        countC += 1;

                    }
                } catch (ParseException e) {
                    System.out.println(e);
                } finally {
                    preEndD = endD;
                }


                cursor.moveToNext();
            } // for end

            System.out.println("COUNTH!!: " + countH);
            if (countH != 0) {
                int averageH = totalHowLongDay / countH;
                String howLongDayStr = "平均持續時間: " + averageH + "天";
                howLongDay.setText(howLongDayStr);
            }
            int averageC = 0;
            if(countC != 0) {
                System.out.println("COUNTC!!: " + countC);
                averageC = totalCycle / countC;
                String cycleStr = "平均幾天一次: " + averageC + "天";
                cycle.setText(cycleStr);
            }

            // 第四筆: 預測下一次
            if(lastEndD != null){
                try {
                    Date lastD = sdf.parse(lastEndD);
                    Date newDate = addDate(lastD, averageC);
                    String dateToStr = sdf.format(newDate);
                    String nextStr = "預測下次日期: " + dateToStr;
                    analyzeNext.setText(nextStr);

                } catch (ParseException e) {
                    System.out.println(e);
                    analyzeNext.setText("無法預測，沒有最後一筆資料");
                }
            }
        }
        /*-------------------- 分析 End --------------------*/






        /*-------------------- 切換頁面至 goToEditPage Start --------------------*/
        goToEditPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditPageIntent = new Intent(); // 新增Intent變數
                EditPageIntent.setClass(AnalyzePage.this, EditPage.class); // 設定目前Activity與目標Activity
                startActivity(EditPageIntent); // 開始執行轉跳切換
            }
        });
        /*-------------------- 切換頁面至 goToEditPage End --------------------*/

        /*-------------------- 切換頁面至 goToMainPage Start --------------------*/
        goToMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditPageIntent = new Intent(); // 新增Intent變數
                EditPageIntent.setClass(AnalyzePage.this, MainActivity.class); // 設定目前Activity與目標Activity
                startActivity(EditPageIntent); // 開始執行轉跳切換
            }
        });
        /*-------------------- 切換頁面至 goToMainPage End --------------------*/


    }

    public static Date addDate(Date date, long day) throws ParseException {
        long time = date.getTime(); // 得到指定日期的毫秒數
        day = day * 24 * 60 * 60 * 1000; // 要加上的天數轉換成毫秒數
        time += day; // 相加得到新的毫秒數
        return new Date(time); // 將毫秒數轉換成日期
    }
}