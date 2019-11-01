package com.kshitijharsh.dairymanagement.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class DatabaseClass extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "records.db";
    private static final int DATABASE_VERSION = 4;
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
        db.execSQL("CREATE TABLE saleTransactions (_id INTEGER PRIMARY KEY AUTOINCREMENT, trnDate TEXT, membCode INTEGER, memName TEXT, mornEve TEXT, cobf TEXT, liters FLOAT, fat FLOAT, rate FLOAT, amount FLOAT, cashCr TEXT);");
        db.execSQL("CREATE TABLE cattleTransactions (_id INTEGER PRIMARY KEY AUTOINCREMENT, trnDate TEXT, memId INTEGER, memName TEXT, itemName TEXT, quantity FLOAT, rate FLOAT, amount FLOAT, particulars TEXT, cashCr TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS member");
        db.execSQL("DROP TABLE IF EXISTS collectionTransactions");
        db.execSQL("DROP TABLE IF EXISTS saleTransactions");
        db.execSQL("DROP TABLE IF EXISTS cattleTransactions");
        onCreate(db);
    }

    //add new entries to database
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

    public void addSale(String date, int membCode, String name, String morEve, String cobf, float liters, float fat, float rate, float amount, String crdr) {
        ContentValues values = new ContentValues(10);
        values.put("trnDate", date);
        values.put("membCode", membCode);
        values.put("memName", name);
        values.put("mornEve", morEve);
        values.put("cobf", cobf);
        values.put("liters", liters);
        values.put("fat", fat);
        values.put("rate", rate);
        values.put("amount", amount);
        values.put("cashCr", crdr);
        getWritableDatabase().insert("saleTransactions", "trnDate", values);
    }

    public void addCattle(String date, int lgr, String name, String item, float qty, float rate, float amt, String part, String crdr) {
        ContentValues values = new ContentValues(9);
        values.put("trnDate", date);
        values.put("memId", lgr);
        values.put("memName", name);
        values.put("itemName", item);
        values.put("quantity", qty);
        values.put("rate", rate);
        values.put("amount", amt);
        values.put("particulars", part);
        values.put("cashCr", crdr);
        getWritableDatabase().insert("cattleTransactions", "trnDate", values);
    }

    //get all details queries
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

    //delete queries
    public Cursor deleteCollectionItem(String id) {
        String query = "DELETE FROM collectionTransactions WHERE _id='" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("collectionTransactions", "_id='" + id + "'", null);
        return db.rawQuery(query, null);
    }

    public Cursor deleteSaleItem(String id) {
        String query = "DELETE FROM saleTransactions WHERE _id='" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("saleTransactions", "_id='" + id + "'", null);
        return db.rawQuery(query, null);
    }

    public Cursor deleteCattleItem(String id) {
        String query = "DELETE FROM cattleTransactions WHERE _id='" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cattleTransactions", "_id='" + id + "'", null);
        return db.rawQuery(query, null);
    }

    //get amount and litres on a given date (For saleActivity)
    public float getMilkFromDate(String date) {
        float milkCount = 0;
        String query = "SELECT SUM(liters) as Total from saleTransactions WHERE trnDate='" + date + "'";
        Cursor c = getReadableDatabase().rawQuery(query, null);
        //c.moveToFirst();
        if (c.moveToFirst()) {
            milkCount = c.getFloat(c.getColumnIndex("Total"));
        }
        c.close();
        return milkCount;
    }

    public float getAmtFromDate(String date) {
        float amt = 0;
        String query = "SELECT SUM(amount) as Total from saleTransactions WHERE trnDate='" + date + "'";
        Cursor c = getReadableDatabase().rawQuery(query, null);
        //c.moveToFirst();
        if (c.moveToFirst()) {
            amt = c.getFloat(c.getColumnIndex("Total"));
        }
        c.close();
        return amt;
    }

    //get amount and litres on a given date (For collectionActivity)
    public float getCollecedMilkFromDate(String date) {
        float milkCount = 0;
        String query = "SELECT SUM(liters) as Total from collectionTransactions WHERE trnDate='" + date + "'";
        Cursor c = getReadableDatabase().rawQuery(query, null);
        //c.moveToFirst();
        if (c.moveToFirst()) {
            milkCount = c.getFloat(c.getColumnIndex("Total"));
        }
        c.close();
        return milkCount;
    }

    public float getCollecedAmtFromDate(String date) {
        float amt = 0;
        String query = "SELECT SUM(amount) as Total from collectionTransactions WHERE trnDate='" + date + "'";
        Cursor c = getReadableDatabase().rawQuery(query, null);
        //c.moveToFirst();
        if (c.moveToFirst()) {
            amt = c.getFloat(c.getColumnIndex("Total"));
        }
        c.close();
        return amt;
    }

    //get total cattle feed amount on a given date
    public float getCattleAmtFromDate(String date) {
        float amt = 0;
        String query = "SELECT SUM(amount) as Total from cattleTransactions WHERE trnDate='" + date + "'";
        Cursor c = getReadableDatabase().rawQuery(query, null);
        //c.moveToFirst();
        if (c.moveToFirst()) {
            amt = c.getFloat(c.getColumnIndex("Total"));
        }
        c.close();
        return amt;
    }

    //get member wise daily collection
    public float getMemberWiseDailyLitre(String date, String name) {
        float milkCount = 0;
        String query = "SELECT SUM(liters) as Total from collectionTransactions WHERE trnDate='" + date + "' AND memName='" + name + "'";
        Cursor c = getReadableDatabase().rawQuery(query, null);
        //c.moveToFirst();
        if (c.moveToFirst()) {
            milkCount = c.getFloat(c.getColumnIndex("Total"));
        }
        c.close();
        return milkCount;
    }

    public float getMemberWiseDailyAmt(String date, String name) {
        float amt = 0;
        String query = "SELECT SUM(amount) as Total from collectionTransactions WHERE trnDate='" + date + "' AND memName='" + name + "'";
        Cursor c = getReadableDatabase().rawQuery(query, null);
        //c.moveToFirst();
        if (c.moveToFirst()) {
            amt = c.getFloat(c.getColumnIndex("Total"));
        }
        c.close();
        return amt;
    }

    //Edit entries
    public void editColl(String id, String date, int membCode, String name, String cobf, String morEve, float degree, float liters, float fat, float rate, float amount) {
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

        SQLiteDatabase db = this.getWritableDatabase();
        db.update("collectionTransactions", values, "_id=" + id, null);
    }

    public void editSale(String id, String date, int membCode, String name, String morEve, String cobf, float liters, float fat, float rate, float amount, String crdr) {
        ContentValues values = new ContentValues(10);
        values.put("trnDate", date);
        values.put("membCode", membCode);
        values.put("memName", name);
        values.put("mornEve", morEve);
        values.put("cobf", cobf);
        values.put("liters", liters);
        values.put("fat", fat);
        values.put("rate", rate);
        values.put("amount", amount);
        values.put("cashCr", crdr);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update("saleTransactions", values, "_id=" + id, null);
    }

    public void editCattle(String id, String date, int lgr, String name, String item, float qty, float rate, float amt, String part, String crdr) {
        ContentValues values = new ContentValues(9);
        values.put("trnDate", date);
        values.put("memId", lgr);
        values.put("memName", name);
        values.put("itemName", item);
        values.put("quantity", qty);
        values.put("rate", rate);
        values.put("amount", amt);
        values.put("particulars", part);
        values.put("cashCr", crdr);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update("cattleTransactions", values, "_id=" + id, null);
    }
}
