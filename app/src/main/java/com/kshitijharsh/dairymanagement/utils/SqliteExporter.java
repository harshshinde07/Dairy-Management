package com.kshitijharsh.dairymanagement.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqliteExporter {

    private static final String TAG = SqliteExporter.class.getSimpleName();

    public static void export(SQLiteDatabase db) throws IOException {
        if (!FileUtils.isExternalStorageWritable()) {
            throw new IOException("Cannot write to external storage");
        }
        File backupDir = FileUtils.createDirIfNotExist(FileUtils.getAppDir() + "/exports");
        List<String> tables = getTablesOnDataBase(db);

        org.apache.commons.io.FileUtils.cleanDirectory(backupDir);

        for (int i = 0; i < tables.size(); i++) {

            if (tables.get(i).equals("android_metadata") || tables.get(i).equals("sqlite_sequence"))
                continue;
            File backupFile = new File(backupDir, tables.get(i) + ".csv");

            boolean success = backupFile.createNewFile();
            if (!success) {
                throw new IOException("Failed to create the export file");
            }
            writeCsv(backupFile, db, tables.get(i));
        }
    }

    private static List<String> getTablesOnDataBase(SQLiteDatabase db) {
        List<String> tables = new ArrayList<>();
        try (Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    tables.add(c.getString(0));
                    c.moveToNext();
                }
            }
        } catch (Exception throwable) {
            Log.e(TAG, "Could not get the table names from db", throwable);
        }
        return tables;
    }

    private static void writeCsv(File backupFile, SQLiteDatabase db, String tableName) {
        CSVWriter csvWrite = null;
        Cursor curCSV = null;
        try {
            csvWrite = new CSVWriter(new FileWriter(backupFile, true));
            curCSV = db.rawQuery("SELECT * FROM " + tableName, null);
            while (curCSV.moveToNext()) {
                int columns = curCSV.getColumnCount();
                String[] columnArr = new String[columns];
                for (int i = 0; i < columns; i++) {
                    columnArr[i] = curCSV.getString(i);
                }
                csvWrite.writeNext(columnArr);
            }
        } catch (Exception sqlEx) {
            Log.e(TAG, sqlEx.getMessage(), sqlEx);
        } finally {
            if (csvWrite != null) {
                try {
                    csvWrite.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (curCSV != null) {
                curCSV.close();
            }
        }
    }
}
