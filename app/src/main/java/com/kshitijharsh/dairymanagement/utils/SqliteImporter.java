package com.kshitijharsh.dairymanagement.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;

public class SqliteImporter {

    private static final String IMPORT_PATH = FileUtils.getAppDir() + "/imports";

    private static FilenameFilter csvFilter = new FilenameFilter() {
        File f;

        public boolean accept(File dir, String name) {
            if (name.endsWith(".csv") || name.endsWith(".CSV")) {
                return true;
            }
            f = new File(dir.getAbsolutePath() + "/" + name);

            return f.isDirectory();
        }
    };

    // return 1 = success, -1 = bad file name, 0 = no csv files found
    // FileNotFound and IO Exceptions need to be handled
    public static int importData(SQLiteDatabase db) throws Exception {
        File directory = new File(IMPORT_PATH);
        String[] files = directory.list(csvFilter);

        if (files.length > 0) {
            for (String fileName : files) {
                Log.e("Files", fileName);
                FileReader file = new FileReader(IMPORT_PATH + File.separator + fileName);
                BufferedReader buffer = new BufferedReader(file);
                String line = "";

                if (fileName.equalsIgnoreCase("member.csv")) {
                    String tableName = "member";
//                    String columns = "memb_code, memb_name, zoon_code, Cobf_type, memb_type, accno, rategrno, bank_code, BankAcNo, membName_Eng, AcNo";
//                    String str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
//                    String str2 = ");";

                    Log.e("member", "IN");
                    db.beginTransaction();
                    while ((line = buffer.readLine()) != null) {
                        Log.e("Lines:", line);
//                        StringBuilder sb = new StringBuilder(str1);
                        String[] str = line.split(",");
//                    if (str.length != 11) {
//                        Log.d("CSVParser", "Skipping Bad CSV Row");
//                        continue;
//                    }
//                        sb.append("'" + str[0] + "',");
//                        sb.append(str[1] + "',");
//                        sb.append(str[2] + "',");
//                        sb.append(str[3] + "',");
//                        sb.append(str[4] + "',");
//                        sb.append(str[5] + "',");
//                        sb.append(str[6] + "',");
//                        sb.append(str[7] + "',");
//                        sb.append(str[8] + "',");
//                        sb.append(str[9] + "',");
//                        sb.append(str[10] + "'");
//                        sb.append(str2);
//                        db.execSQL(sb.toString());
                        ContentValues cv = new ContentValues(11);
                        cv.put("memb_code", str[0].trim());
                        cv.put("memb_name", str[1].trim());
                        cv.put("zoon_code", str[2].trim());
                        cv.put("Cobf_type", str[3].trim());
                        cv.put("memb_type", str[4].trim());
                        cv.put("accno", str[5].trim());
                        cv.put("rategrno", str[6].trim());
                        cv.put("bank_code", str[7].trim());
                        cv.put("BankAcNo", str[8].trim());
                        cv.put("membNam_Eng", str[9].trim());
                        cv.put("AcNo", str[10].trim());
                        db.insertWithOnConflict(tableName, "memb_code", cv, SQLiteDatabase.CONFLICT_IGNORE);
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();


                }
                if (fileName.equalsIgnoreCase("item.csv")) {
                    String tableName = "item";
//                    String columns = "itcode, itname, opqty, oprate, opbal, issue, rece, clqty, rate, purchAc, saleAc, saleCr";
//                    String str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
//                    String str2 = ");";

                    Log.e("item", "IN");
                    db.beginTransaction();
                    while ((line = buffer.readLine()) != null) {
                        Log.e("Lines:", line);
//                        StringBuilder sb = new StringBuilder(str1);
                        String[] str = line.split(",");
//                    if (str.length != 12) {
//                        Log.d("CSVParser", "Skipping Bad CSV Row");
//                        continue;
//                    }
//                        sb.append("'" + str[0] + "',");
//                        sb.append(str[1] + "',");
//                        sb.append(str[2] + "',");
//                        sb.append(str[3] + "',");
//                        sb.append(str[4] + "',");
//                        sb.append(str[5] + "',");
//                        sb.append(str[6] + "',");
//                        sb.append(str[7] + "',");
//                        sb.append(str[8] + "',");
//                        sb.append(str[9] + "',");
//                        sb.append(str[10] + "',");
//                        sb.append(str[11] + "'");
//                        sb.append(str2);
//                        db.execSQL(sb.toString());
                        ContentValues cv = new ContentValues(12);
                        cv.put("itcode", str[0].trim());
                        cv.put("itname", str[1].trim());
                        cv.put("opqty", str[2].trim());
                        cv.put("oprate", str[3].trim());
                        cv.put("opbal", str[4].trim());
                        cv.put("issue", str[5].trim());
                        cv.put("rece", str[6].trim());
                        cv.put("clqty", str[7].trim());
                        cv.put("rate", str[8].trim());
                        cv.put("purchAc", str[9].trim());
                        cv.put("saleAc", str[10].trim());
                        cv.put("saleCr", str[11].trim());
                        db.insertWithOnConflict(tableName, "itcode", cv, SQLiteDatabase.CONFLICT_IGNORE);
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();


                }
                if (fileName.equalsIgnoreCase("ratemst.csv")) {
                    String tableName = "ratemst";
//                    String columns = "rtgrno, RtDate, cobf, rtno, fat, rate, degree, snf";
//                    String str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
//                    String str2 = ");";

                    Log.e("ratemaster", "IN");
                    db.beginTransaction();
                    while ((line = buffer.readLine()) != null) {
                        Log.e("Lines:", line);
//                        StringBuilder sb = new StringBuilder(str1);
                        String[] str = line.split(",");
//                    if (str.length != 8) {
//                        Log.d("CSVParser", "Skipping Bad CSV Row");
//                        continue;
//                    }
//                        sb.append("'" + str[0] + "',");
//                        sb.append(str[1] + "',");
//                        sb.append(str[2] + "',");
//                        sb.append(str[3] + "',");
//                        sb.append(str[4] + "',");
//                        sb.append(str[5] + "',");
//                        sb.append(str[6] + "',");
//                        sb.append(str[7] + "'");
//                        sb.append(str2);
//                        db.execSQL(sb.toString());
                        ContentValues cv = new ContentValues(8);
                        cv.put("rtgrno", str[0].trim());
                        cv.put("RtDate", str[1].trim());
                        cv.put("cobf", str[2].trim());
                        cv.put("rtno", str[3].trim());
                        cv.put("fat", str[4].trim());
                        cv.put("rate", str[5].trim());
                        cv.put("degree", str[6].trim());
                        cv.put("snf", str[7].trim());
                        db.insertWithOnConflict(tableName, "rtno", cv, SQLiteDatabase.CONFLICT_IGNORE);
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();


                }
                if (fileName.equalsIgnoreCase("Rt_grmst.csv")) {
                    String tableName = "Rt_grmst";
//                    String columns = "RateGrno, RateGrname, RateTyp, CowRate, BufRate";
//                    String str1 = "INSERT INTO " + tableName + " (" + columns + ") values(";
//                    String str2 = ");";

                    Log.e("Rt_grmst", "IN");
                    db.beginTransaction();
                    while ((line = buffer.readLine()) != null) {
                        Log.e("Lines:", line);
//                        StringBuilder sb = new StringBuilder(str1);
                        String[] str = line.split(",");
                        if (str.length != 5) {
                            Log.e("CSVParser", "Skipping Bad CSV Row");
                            continue;
                        }
//                        sb.append("'" + str[0] + "',");
//                        sb.append(str[1] + "',");
//                        sb.append(str[2] + "',");
//                        sb.append(str[3] + "'");
//                        sb.append(str[4] + "'");
//                        sb.append(str2);
//                        db.execSQL(sb.toString());
                        ContentValues cv = new ContentValues(5);
                        cv.put("RateGrno", str[0].trim());
                        cv.put("RateGrname", str[1].trim());
                        cv.put("RateTyp", str[2].trim());
                        cv.put("CowRate", str[3].trim());
                        cv.put("BufRate", str[4].trim());
                        db.insertWithOnConflict(tableName, "RateGrno", cv, SQLiteDatabase.CONFLICT_IGNORE);

                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();


                }

            }
            return 1;
        }
        Log.e("Return:", "0 No files");
        return 0;
    }
}
