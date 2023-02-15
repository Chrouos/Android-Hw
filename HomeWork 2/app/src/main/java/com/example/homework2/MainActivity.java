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

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.Menu;

import java.util.*;
import java.text.*;
import java.util.Date;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    TextView todayDate, dateEdit, commentEdit, testView; // 今日日期, 填寫日期, 備註撰寫
    DatePickerDialog.OnDateSetListener DatePicker; // 日曆的監聽器，用於獲取被選擇的日期
    Button dateBtn, submitBtn, goToEditPage, goToAnalyzePage; // 日期選擇器, 送出按鈕, 編輯換頁按鈕, 分析換頁按鈕
    Calendar calendar = Calendar.getInstance(); // 日期的格式
    RadioGroup radioTypeGroup;
    RadioButton startDateRb, endDateRb;

    private static final String DATABASE_NAME = "HomeWork2.db"; // 建立"資料庫名稱"常數
    private static final int DATABASE_VERSION = 1;  // 建立"資料庫版本"常數
    private static String DATABASE_TABLE1 = "mcCalendar"; //宣告"行事曆"常數
    private SQLiteDatabase db; //宣告資料庫變數
    private MyDBHelper dbHelper; //宣告資料庫幫助器變數
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*-------------------- 找 ID 區域 Start --------------------*/
        todayDate = (TextView) findViewById(R.id.today_date);
        dateEdit = (TextView) findViewById(R.id.date_edit);
        dateBtn = (Button) findViewById(R.id.date_btn);
        radioTypeGroup = (RadioGroup) findViewById(R.id.radio_type_choice);
        commentEdit = (TextView) findViewById(R.id.comment_edit);
        testView = (TextView) findViewById(R.id.testView);
        testView.setMovementMethod(ScrollingMovementMethod.getInstance());
        submitBtn = (Button) findViewById(R.id.submit_btn);
        goToAnalyzePage = (Button) findViewById(R.id.analyze_page);
        goToEditPage = (Button) findViewById(R.id.edit_page);
        /*-------------------- 找 ID 區域 End --------------------*/

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


        /*-------------------- 找尋今日日期 Start --------------------*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //先行定義時間格式
        Date nowDate = new Date(); //取得現在時間
        String sdfNowDate = sdf.format(nowDate); //透過SimpleDateFormat的format方法將Date轉為字串
        todayDate.setText(sdfNowDate);
        /*-------------------- 找尋今日日期 End --------------------*/

        /*-------------------- 日曆的選擇器 Start --------------------*/
        DatePicker = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
                dateEdit.setText(sdf.format(calendar.getTime()));
            }
        };
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        DatePicker,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        /*-------------------- 日曆的選擇器 End --------------------*/

        /*-------------------- 資料送出 btn Start --------------------*/
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                RadioGroup radioTypeGroup = (RadioGroup) findViewById(R.id.radio_type_choice);
                int selectID = radioTypeGroup.getCheckedRadioButtonId();

                switch(selectID){
                    case R.id.start_date_rb: // Start Time Radio
//                        db.execSQL("INSERT INTO " + DATABASE_TABLE1 + " (comment, startDate) VALUES ('123', '2022/05/04')");
                        db.execSQL("INSERT INTO " + DATABASE_TABLE1 + " (comment, startDate) " + " VALUES ("
                                + "'" + commentEdit.getText() + "'"
                                + ",'" + dateEdit.getText() +"'"
                                + ")");
                        break;
                    case R.id.end_date_rb: // End Time Radio
                        db.execSQL("INSERT INTO " + DATABASE_TABLE1 + " (comment, endDate) " + " VALUES ("
                                + "'" + commentEdit.getText() + "'"
                                + ",'" + dateEdit.getText() +"'"
                                + ")");
                        break;
                }
                querySelect();
            }
        });
        /*-------------------- 資料送出 btn End --------------------*/

        /*-------------------- 切換頁面至 goToAnalyzePage Start --------------------*/
        goToAnalyzePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AnalyzePageIntent = new Intent(); // 新增Intent變數
                AnalyzePageIntent.setClass(MainActivity.this, AnalyzePage.class); // 設定目前Activity與目標Activity
                startActivity(AnalyzePageIntent); // 開始執行轉跳切換
            }
        });
        /*-------------------- 切換頁面至 goToAnalyzePage End --------------------*/

        /*-------------------- 切換頁面至 goToEditPage Start --------------------*/
        goToEditPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditPageIntent = new Intent(); // 新增Intent變數
                EditPageIntent.setClass(MainActivity.this, EditPage.class); // 設定目前Activity與目標Activity
                startActivity(EditPageIntent); // 開始執行轉跳切換
            }
        });
        /*-------------------- 切換頁面至 goToEditPage End --------------------*/

        querySelect();

    }

    void querySelect(){
        String commandString = "SELECT * FROM " + DATABASE_TABLE1 ;
        cursor = db.rawQuery(commandString, null); //執行資料表查詢命令
        if(cursor != null){ // 若有回傳消費紀錄，則進行以下處理
            int n = cursor.getCount(); //取得資料筆數
            String str = "";
            cursor.moveToFirst();  // 將指標移到第1筆紀錄
            for (int i = 0; i < n; i++){ // 利用迴圈逐一讀取每一筆紀錄
                str += cursor.getString(1)  + " "
                        + " " + cursor.getString(2) + " " + cursor.getString(3) + "\n";
                cursor.moveToNext();
            }
            testView.setText(str);
        }
    }





}