package com.example.nonequi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Login.Users;

public class DBConnection extends SQLiteOpenHelper {

    public static final String DBName = "nonequi.db";
    public static Users users = new Users();
    public static HistoryTransactionList history = new HistoryTransactionList();
    public final int moneyTemp = 0;


    public DBConnection(Context context)     {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(number INT primary key, username TEXT , password TEXT, email TEXT, money INT)");
        db.execSQL("create table history(numberSender INT, sender TEXT, numberReceiver INT, receiver TEXT, money INT, date DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists history");
    }

    public boolean insertData(String number,String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("number", number);
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        values.put("money", moneyTemp);

        long result = db.insert("users", null, values);
        if(result == -1) return false;
        else return true;
    }

    public void insertDataHistory(String numberSender, String sender, String numberReceiver, String receiver, String money) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("numberSender", numberSender);
        values.put("sender", sender);
        values.put("numberReceiver", numberReceiver);
        values.put("receiver", receiver);
        values.put("money", money);

        db.insert("history", null, values);
    }

    public boolean checkIfEmailExists(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users where email = ?", new String[]{email});

        if(cursor.getCount() > 0) return true;
        else return false;

    }

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

        if(cursor.isAfterLast()) return true;
        else return false;
    }

    public void retrieveDataTransaction(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username, money FROM users where number = ?", new String[]{number});

        if(cursor.moveToFirst()) {
            String money = cursor.getString(1);
            String name = cursor.getString(0);

            users.setMoneyTransaction(money);
            users.setNameToReceiver(name);
        }

        cursor.close();
    }

    public boolean setIncomingMoney(int money) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("UPDATE users SET money = '"+ money +"' where number = ?", new String[]{users.getNumberToReceiver()});

        if(cursor.isAfterLast()) return true;
        else return false;
    }


    //Method to retrieve data to recyclerview history

    public void retrieveDataToHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT numberSender, sender, numberReceiver, receiver, money, date FROM history where number = ?", new String[]{users.getNumber()});

        if(cursor.moveToFirst()) {
            String numberSender = cursor.getString(0);
            String nameSender = cursor.getString(1);
            String numberReceiver = cursor.getString(2);
            String nameReceiver = cursor.getString(3);
            String money = cursor.getString(4);
            String date = cursor.getString(5);

            history.setNumberSender(numberSender);
            history.setNumberReceiver(numberReceiver);
            history.setNameReceiver(nameReceiver);
            history.setNameSender(nameSender);
            history.setMoneyHTL(money);
            history.setDate(date);
        }

        cursor.close();
    }
}
