package com.example.nonequi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper {

    public static final String DBName = "nonequi.db";
    public static final String colNumber = "number";
    public static final String colName = "username";
    public static final String colCash = "cash";

    public static users usuarios = new users();


    public DBConnection(Context context)     {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(number INT primary key, username TEXT , password TEXT, email TEXT, cash INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists users");
    }

    public boolean insertData(String number,String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("number", number);
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);

        long result = db.insert("users", null, values);
        if(result == -1) return false;
        else return true;
    }

    public boolean checkIfEmailExists(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users where email = ?", new String[]{email});

        if(cursor.getCount() > 0) return true;
        else return false;

    }

    /*public boolean checkIfUserExists(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users where username = ?", new String[]{user});

        if(cursor.getCount() > 0) return true;
        else return false;

    }*/

    public boolean checkIfNumberExists(String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users where number = ?", new String[]{number});

        if(cursor.getCount() > 0) return true;
        else return false;
    }

    public boolean checkUserPassword(String number, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users where number = ? and password = ? ", new String[]{number, password});

        if(cursor.getCount() > 0) return true;
        else return false;
    }

    public void retrieveData(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username, cash FROM users where number = ?", new String[]{number});

        if(cursor.moveToFirst()) {
            String name = cursor.getString(0);
            String cash = cursor.getString(1);

            usuarios.setName(name);
            usuarios.setCash(cash);
        }

        cursor.close();
    }
}
