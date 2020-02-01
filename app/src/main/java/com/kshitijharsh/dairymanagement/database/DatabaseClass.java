package com.kshitijharsh.dairymanagement.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatabaseClass extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "records.db";
    private static final int DATABASE_VERSION = 8; // Add lineNo field in collectionTransactions table

    public DatabaseClass(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        File dbPath = context.getDatabasePath(DATABASE_NAME);
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath.toString(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE collectionTransactions (_id INTEGER PRIMARY KEY AUTOINCREMENT, trnDate TEXT, membCode INTEGER, memName TEXT, cobf TEXT, morEve TEXT, degree FLOAT, liters FLOAT, fat FLOAT, rate FLOAT, amount FLOAT, zoonCode INTEGER, snf FLOAT, lineNo INTEEGR);");
        db.execSQL("CREATE TABLE saleTransactions (_id INTEGER PRIMARY KEY AUTOINCREMENT, trnDate TEXT, membCode INTEGER, memName TEXT, mornEve TEXT, cobf TEXT, liters FLOAT, fat FLOAT, rate FLOAT, amount FLOAT, cashCr TEXT, zoonCode INTEGER);");
        db.execSQL("CREATE TABLE cattleTransactions (_id INTEGER PRIMARY KEY AUTOINCREMENT, trnDate TEXT, memId INTEGER, memName TEXT, itemId INTEGER, quantity FLOAT, rate FLOAT, amount FLOAT, particulars TEXT, cashCr TEXT, zoonCode INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS collectionTransactions");
        db.execSQL("DROP TABLE IF EXISTS saleTransactions");
        db.execSQL("DROP TABLE IF EXISTS cattleTransactions");
        onCreate(db);
    }

    //add new entries to database
    public void addColl(String date, int membCode, String name, String cobf, String morEve, float degree, float liters, float fat, float rate, float amount, int zoonCode, float snf) {

        int line = getLineNo(date, membCode, morEve, cobf);

        String formattedDate = convertDateToDB(date);

        ContentValues values = new ContentValues(13);
        values.put("trnDate", formattedDate);
        values.put("membCode", membCode);
        values.put("memName", name);
        values.put("cobf", cobf);
        values.put("morEve", morEve);
        values.put("degree", degree);
        values.put("liters", liters);
        values.put("fat", fat);
        values.put("rate", rate);
        values.put("amount", amount);
        values.put("zoonCode", zoonCode);
        values.put("snf", snf);
        values.put("lineNo", line);
        getWritableDatabase().insert("collectionTransactions", "trnDate", values);
    }

    public void addSale(String date, int membCode, String name, String morEve, String cobf, float liters, float fat, float rate, float amount, String crdr, int zoonCode) {
        ContentValues values = new ContentValues(10);

        String formattedDate = convertDateToDB(date);

        values.put("trnDate", formattedDate);
        values.put("membCode", membCode);
        values.put("memName", name);
        values.put("mornEve", morEve);
        values.put("cobf", cobf);
        values.put("liters", liters);
        values.put("fat", fat);
        values.put("rate", rate);
        values.put("amount", amount);
        values.put("cashCr", crdr);
        values.put("zoonCode", zoonCode);
        getWritableDatabase().insert("saleTransactions", "trnDate", values);
    }

    public void addCattle(String date, int lgr, String name, int itemId, float qty, float rate, float amt, String part, String crdr, int zoonCode) {
        ContentValues values = new ContentValues(9);

        String formattedDate = convertDateToDB(date);

        values.put("trnDate", formattedDate);
        values.put("memId", lgr);
        values.put("memName", name);
        values.put("itemId", itemId);
        values.put("quantity", qty);
        values.put("rate", rate);
        values.put("amount", amt);
        values.put("particulars", part);
        values.put("cashCr", crdr);
        values.put("zoonCode", zoonCode);
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
        if (c.moveToFirst()) {
            amt = c.getFloat(c.getColumnIndex("Total"));
        }
        c.close();
        return amt;
    }

    //Edit entries
    public void editColl(String id, String date, int membCode, String name, String cobf, String morEve, float degree, float liters, float fat, float rate, float amount, int zoonCode, float snf) {
        ContentValues values = new ContentValues(12);

        String formattedDate = convertDateToDB(date);

        values.put("trnDate", formattedDate);
        values.put("membCode", membCode);
        values.put("memName", name);
        values.put("cobf", cobf);
        values.put("morEve", morEve);
        values.put("degree", degree);
        values.put("liters", liters);
        values.put("fat", fat);
        values.put("rate", rate);
        values.put("amount", amount);
        values.put("zoonCode", zoonCode);
        values.put("snf", snf);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("collectionTransactions", values, "_id=" + id, null);
    }

    public void editSale(String id, String date, int membCode, String name, String morEve, String cobf, float liters, float fat, float rate, float amount, String crdr, int zoonCode) {
        ContentValues values = new ContentValues(10);

        String formattedDate = convertDateToDB(date);

        values.put("trnDate", formattedDate);
        values.put("membCode", membCode);
        values.put("memName", name);
        values.put("mornEve", morEve);
        values.put("cobf", cobf);
        values.put("liters", liters);
        values.put("fat", fat);
        values.put("rate", rate);
        values.put("amount", amount);
        values.put("cashCr", crdr);
        values.put("zoonCode", zoonCode);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update("saleTransactions", values, "_id=" + id, null);
    }

    public void editCattle(String id, String date, int lgr, String name, int itemId, float qty, float rate, float amt, String part, String crdr, int zoonCode) {
        ContentValues values = new ContentValues(9);

        String formattedDate = convertDateToDB(date);

        values.put("trnDate", formattedDate);
        values.put("memId", lgr);
        values.put("memName", name);
        values.put("itemId", itemId);
        values.put("quantity", qty);
        values.put("rate", rate);
        values.put("amount", amt);
        values.put("particulars", part);
        values.put("cashCr", crdr);
        values.put("zoonCode", zoonCode);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update("cattleTransactions", values, "_id=" + id, null);
    }

    // Add line number as 1 plus the previous line number available
    private int getLineNo(String date, int membCode, String morEve, String cobf) {
        int lineNo = 0;
        String query = "SELECT COUNT(*) as line from collectionTransactions where membCode='" + membCode + "' AND trnDate='" + date + "' AND morEve='" + morEve + "' AND cobf='" + cobf + "'";
        Cursor c = getReadableDatabase().rawQuery(query, null);
        if (c.moveToFirst()) {
            lineNo = c.getInt(c.getColumnIndex("line"));
        }
        c.close();
        return ++lineNo;
    }

    /**
     * this function converts date from the format dd-MM-yyyy to MM/dd/yyyy
     *
     * @param date String of the form dd-MM-yyyy
     * @return String of the form MM/dd/yyyy
     */
    private static String convertDateToDB(String date) {

        String[] array = date.split("-");
        int day = Integer.valueOf(array[0]);
        int month = Integer.valueOf(array[1]);
        int year = Integer.valueOf(array[2]);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month - 1, day);
        Date formattedDate = cal.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        return sdf.format(formattedDate);
    }
}
