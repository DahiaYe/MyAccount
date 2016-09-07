package com.example.zs.dataBase;


import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wuqi on 2016/9/7 0007.
 */
public class PayOutContentDB extends SQLiteOpenHelper{
    public PayOutContentDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public PayOutContentDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table payouContent (id Integer primary key autoincrement,resourceID Integer，year Integer," +
                 "mouth Integer,day Integer,category var(5),money Integer,remarks var(20),photo var(20));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
