package com.kshitijharsh.dairymanagement.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.kshitijharsh.dairymanagement.database.BaseContract.BaseEntry.COLUMN_COWBF_TYPE;
import static com.kshitijharsh.dairymanagement.database.BaseContract.BaseEntry.COLUMN_MEMB_CODE;
import static com.kshitijharsh.dairymanagement.database.BaseContract.BaseEntry.COLUMN_MEMB_NAME;
import static com.kshitijharsh.dairymanagement.database.BaseContract.BaseEntry.COLUMN_MEMB_TYPE;
import static com.kshitijharsh.dairymanagement.database.BaseContract.BaseEntry.COLUMN_RATEGRP_NO;
import static com.kshitijharsh.dairymanagement.database.BaseContract.BaseEntry.TABLE_MEMBER;

public class DBQuery {

    private static final String TABLE_ITEMS = "item";
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBQuery(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void createDatabase() {
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open() {
        try {
            dbHelper.openDataBase();
            dbHelper.close();
            db = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        dbHelper.close();
    }

    //______________________________________________________________________________________________
    //__________________________________      QUERIES      ________________________________________
    //______________________________________________________________________________________________

    public int getMembercount() {
        String query = "SELECT * FROM member;";
        Cursor cursor = db.rawQuery(query,null);
        return cursor.getCount();
    }
    public Cursor getAllMembers() {
        String cols[] = {COLUMN_MEMB_CODE,COLUMN_MEMB_NAME,COLUMN_COWBF_TYPE,COLUMN_MEMB_TYPE, COLUMN_RATEGRP_NO};
        return db.query(
                TABLE_MEMBER,
                cols,
                null,
                null,
                null,
                null,
                COLUMN_MEMB_NAME
        );
    }

    public Cursor getRate(float degree, float fat, String cobf, int rateGrpNo) {
        String cb;
        if(cobf.equals("Buffalo"))
            cb = "B";
        else
            cb = "C";
        String query = "SELECT rate from ratemst where degree='"+degree+"' AND fat='"+fat+"' AND cobf='"+cb+"' AND rtgrno='"+rateGrpNo+"'";
        return db.rawQuery(query,null);
    }

    public Cursor getRateFromSNF(float snf, float fat, String cobf, int rateGrpNo) {
        String cb;
        if(cobf.equals("Buffalo"))
            cb = "B";
        else
            cb = "C";
        String query = "SELECT rate from ratemst where snf='"+snf+"' AND fat='"+fat+"' AND cobf='"+cb+"' AND rtgrno='"+rateGrpNo+"'";
        return db.rawQuery(query,null);
    }

    public void addNewMem(String name, int zone, int cowBuff, int memType, int rateGrp) {
        String query = "SELECT memb_code FROM member ORDER BY memb_code DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query,null);
        int val = 0;
        if (cursor.moveToFirst()) {
            val = cursor.getInt(cursor.getColumnIndex("memb_code"));
            val = val+1;
        }
        int memCode = val;
        Log.e("-----------------", String.valueOf(val));
        int acno = 1, bankcode = 1, bankAcNo = 1, acNo = 1;

        ContentValues values = new ContentValues(11);
        values.put("memb_code", memCode);
        values.put("memb_name", name);
        values.put("zoon_code", zone);
        values.put("cobf_type", cowBuff);
        values.put("memb_type", memType);
        values.put("accno", acno);
        values.put("rategrno", rateGrp);
        values.put("bank_code", bankcode);
        values.put("BankAcNo", bankAcNo);
        values.put("membNam_Eng", name);
        values.put("AcNo", acNo);
        dbHelper.getWritableDatabase().insert("member", "memb_code", values);
    }

    public List<String> getAllItems(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS;


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public Cursor getItemRate(String item) {
        String query = "SELECT rate from item where itname='"+item+"'";
        return db.rawQuery(query,null);
    }

    public Cursor getMemName(int id) {
        String query = "SELECT memb_name from member where memb_code='"+id+"'";
        return db.rawQuery(query,null);
    }

    public Cursor getRateGrpNo(String rateGrpName) {
        String query = "SELECT RateGrno from Rt_grmst where RateGrname='"+rateGrpName+"'";
        return db.rawQuery(query,null);
    }
}
