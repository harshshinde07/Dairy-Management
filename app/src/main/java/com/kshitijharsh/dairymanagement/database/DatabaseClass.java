package com.kshitijharsh.dairymanagement.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class DatabaseClass extends SQLiteOpenHelper {

    public final static String DATABASE_NAME ="records.db";
    private static final int DATABASE_VERSION = 2;
    private SQLiteDatabase db;
    private File dbPath;

    public DatabaseClass(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbPath = context.getDatabasePath(DATABASE_NAME);
        db = SQLiteDatabase.openDatabase(dbPath.toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE member(memb_code INTEGER PRIMARY KEY, memb_name TEXT, zoon_code INTEGER, Cobf_type INTEGER, memb_type INTEGER, accno INTEGER, rategrno INTEGER, bank_code INTEGER, BankAcNo INTEGER, membNam_Eng TEXT, AcNo INTEGER);");
        db.execSQL("CREATE TABLE collectionTransactions (_id INTEGER PRIMARY KEY AUTOINCREMENT, trnDate TEXT, membCode INTEGER, memName TEXT, cobf TEXT, morEve TEXT, degree FLOAT, liters FLOAT, fat FLOAT, rate FLOAT, amount FLOAT);");
        db.execSQL("CREATE TABLE saleTransactions (_id INTEGER PRIMARY KEY AUTOINCREMENT, trnDate TEXT, brName TEXT, membCode INTEGER, memName TEXT, mornEve TEXT, cobf TEXT, liters FLOAT, fat FLOAT, rate FLOAT, amount FLOAT);");
        db.execSQL("CREATE TABLE cattleTransactions (_id INTEGER PRIMARY KEY AUTOINCREMENT, trnDate TEXT, memId INTEGER, memName TEXT, itemName TEXT, quantity FLOAT, rate FLOAT, amount FLOAT, particulars TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS member");
        db.execSQL("DROP TABLE IF EXISTS collectionTransactions");
        db.execSQL("DROP TABLE IF EXISTS saleTransactions");
        db.execSQL("DROP TABLE IF EXISTS cattleTransactions");
        onCreate(db);
    }

//    public void addMember(int memCode, String memName, int zoonCode, int cobfType, int memType, int accNo, int rategrNo, int bankCode, int bankacNo, String memnameEng, int acNo) {
//        ContentValues values = new ContentValues(11);
//        values.put("memb_code", memCode);
//        values.put("memb_name", memName);
//        values.put("zoon_code", zoonCode);
//        values.put("Cobf_type", cobfType);
//        values.put("memb_type", memType);
//        values.put("accno", accNo);
//        values.put("rategrno", rategrNo);
//        values.put("bank_code", bankCode);
//        values.put("BankAcNo", bankacNo);
//        values.put("membNam_Eng", memnameEng);
//        values.put("AcNo", acNo);
//        getWritableDatabase().insert("member", "memb_code", values);
//    }

    public void addColl(String date, int membCode, String name, String cobf, String morEve, float degree, float liters, float fat, float rate, float amount) {
        ContentValues values = new ContentValues(10);
        values.put("trnDate", date);
        values.put("membCode", membCode);
        values.put("memName", name);
        values.put("cobf", cobf);
        values.put("morEve", morEve);
        values.put("degree", degree);
        values.put("liters", liters);
        values.put("fat", fat);
        values.put("rate", rate);
        values.put("amount", amount);
        getWritableDatabase().insert("collectionTransactions", "trnDate", values);
    }

    public void addSale(String date, String brName, int membCode, String name, String morEve, String cobf, float liters, float fat, float rate, float amount) {
        ContentValues values = new ContentValues(10);
        values.put("trnDate", date);
        values.put("brName", brName);
        values.put("membCode", membCode);
        values.put("memName", name);
        values.put("mornEve", morEve);
        values.put("cobf", cobf);
        values.put("liters", liters);
        values.put("fat", fat);
        values.put("rate", rate);
        values.put("amount", amount);
        getWritableDatabase().insert("saleTransactions", "trnDate", values);
    }

    public void addCattle(String date, int lgr, String name, String item, float qty, float rate, float amt, String part) {
        ContentValues values = new ContentValues(9);
        values.put("trnDate", date);
        values.put("memId", lgr);
        values.put("memName", name);
        values.put("itemName", item);
        values.put("quantity", qty);
        values.put("rate", rate);
        values.put("amount", amt);
        values.put("particulars", part);
        getWritableDatabase().insert("cattleTransactions", "trnDate", values);
    }

    public float getMilkCount() {
        float milkCount = 0;
        String query = "SELECT SUM(liters) from TABLE_COLLECTION;";
        Cursor c = getReadableDatabase().rawQuery(query,null);
        //c.moveToFirst();
        if(c != null && c.getCount() >0)
            milkCount = c.getFloat(c.getColumnIndex("liters"));
        return milkCount;
    }

    public Cursor getAllCattle() {
        String query = "SELECT * FROM cattleTransactions";
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(query, null);
    }

    public Cursor getAllSale() {
        String query = "SELECT * FROM saleTransactions";
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(query, null);
    }

    public Cursor getAllCollection() {
        String query = "SELECT * FROM collectionTransactions";
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(query, null);
    }
}
