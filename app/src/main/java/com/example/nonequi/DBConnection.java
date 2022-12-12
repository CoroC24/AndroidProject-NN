package com.example.nonequi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Interfaces.SendMoney;
import Login.Users;

public class DBConnection extends SQLiteOpenHelper {

    public static final String DBName = "nonequi.db";
    public static final String colNumber = "number";
    public static final String colName = "username";
    public static final String colMoney = "money";

    public static Users users = new Users();
    SendMoney sendMoney = new SendMoney();



    public DBConnection(Context context)     {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(number INT primary key, username TEXT , password TEXT, email TEXT, money INT)");
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
        values.put("money", 0);

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
        Cursor cursor = db.rawQuery("SELECT number, username, money FROM users where number = ?", new String[]{number});

        if(cursor.moveToFirst()) {
            String numberPhone = cursor.getString(0);
            String name = cursor.getString(1);
            String money = cursor.getString(2);

            users.setNumber(numberPhone);
            users.setName(name);
            users.setMoney(money);
        }

        cursor.close();
    }

    public boolean setRemainingMoney(int money) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("UPDATE users SET money = '"+ money +"' where number = ?", new String[]{users.getNumber()});

        if(cursor.getCount() > 0 ) return true;
        else return false;
    }

    public void retrieveDataTransaction(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT money FROM users where number = ?", new String[]{number});

        if(cursor.moveToFirst()) {
            String money = cursor.getString(0);

            users.setMoneyTransaction(money);
        }

        cursor.close();
    }

    public boolean setIncomingMoney(int money) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("UPDATE users SET money = '"+ money +"' where number = ?", new String[]{users.getNumberToSend()});

        if(cursor.getCount() > 0 ) return true;
        else return false;
    }
}
