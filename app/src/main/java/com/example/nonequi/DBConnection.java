package com.example.nonequi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import java.time.LocalDate;

import Interfaces.Home.Home;
import Interfaces.ShowCard.CardCredentials;

public class DBConnection extends SQLiteOpenHelper {

    public static final String DBName = "nonequi.db";
    public static Users users = new Users();
    public static CardCredentials cardCredentials = new CardCredentials();
    public static UpdateMoney updateMoney = new UpdateMoney();
//    public static HistoryTransactionList history = new HistoryTransactionList();
    public final int moneyTemp = 1000000;


    public DBConnection(Context context)     {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(number INT primary key, username TEXT , password TEXT, email TEXT, money INT, date DATE)");
        db.execSQL("create table history(numberSender INT, sender TEXT, numberReceiver INT, receiver TEXT, money INT, date DATE)");
        db.execSQL("create table creditCard(number INT, name TEXT, serial INT, cvc INT, month INT, year INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists history");
        db.execSQL("drop table if exists creditCard");
    }


    //Methods to insert data into database

    public boolean insertData(String number,String username, String password, String email, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("number", number);
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        values.put("money", moneyTemp);
        values.put("date", date);

        long result = db.insert("users", null, values);
        if(result == -1) return false;
        else return true;
    }

    public void insertDataHistory(String numberSender, String sender, String numberReceiver, String receiver, String money, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("numberSender", numberSender);
        values.put("sender", sender);
        values.put("numberReceiver", numberReceiver);
        values.put("receiver", receiver);
        values.put("money", money);
        values.put("date", date);

        db.insert("history", null, values);
    }

    public void insertDataCreditCard(String number, String name, String serial, String cvc, String month, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("number", number);
        values.put("name", name);
        values.put("serial", serial);
        values.put("cvc", cvc);
        values.put("month", month);
        values.put("year", year);

        db.insert("creditCard", null, values);
    }

    //Methods to check credentials

    public boolean checkIfEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users where email = ?", new String[]{email});

        if(cursor.getCount() > 0) return true;
        else return false;

    }

    public boolean checkIfNumberExists(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users where number = ?", new String[]{number});

        if(cursor.getCount() > 0) return true;
        else return false;
    }

    public boolean checkUserPassword(String number, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users where number = ? and password = ? ", new String[]{number, password});

        if(cursor.getCount() > 0) return true;
        else return false;
    }

    public boolean checkIfUserHaveCreditCard(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM creditCard where number = ?", new String[]{number});

        if(cursor.getCount() > 0) return true;
        else return false;
    }

    //Methods to retrieve data

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


    public void retrieveDataOfCreditCard(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM creditCard where number = ?", new String[]{number});

        if(cursor.moveToFirst()) {
            cardCredentials.setNumber(cursor.getString(0));
            cardCredentials.setName(cursor.getString(1));
            cardCredentials.setSerial(cursor.getString(2));
            cardCredentials.setCvc(cursor.getString(3));
            cardCredentials.setMonth(cursor.getString(4));
            cardCredentials.setYear(cursor.getString(5));
        }
    }

    //Methods to set data of transactions

    public boolean setRemainingMoney(int money) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("UPDATE users SET money = '"+ money +"' where number = ?", new String[]{users.getNumber()});

        if(cursor.isAfterLast()) return true;
        else return false;
    }

    public boolean setIncomingMoney(int money) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("UPDATE users SET money = '"+ money +"' where number = ?", new String[]{users.getNumberToReceiver()});

        if(cursor.isAfterLast()) return true;
        else return false;
    }


    //Method to retrieve data to recyclerview history

    public Cursor retrieveDataToHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM history where numberSender = ? or numberReceiver = ?", new String[]{users.getNumber(), users.getNumber()});

        return cursor;
    }

    //Methods to update money every 24 hours

    public void retrieveDate(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.
                rawQuery("SELECT date FROM users where number = ?", new String[]{number});

        if(cursor.moveToFirst()) {
            updateMoney.setDateDB(cursor.getString(0));
        }
    }

    public void updateMoney() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("money", moneyTemp);

        db.update("users", values, "number=?", new String[]{users.getNumber()});
    }
}
