package com.kshitijharsh.dairymanagement.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqliteExporter {

    private static final String TAG = SqliteExporter.class.getSimpleName();

    private static final String DB_BACKUP_DB_VERSION_KEY = "dbVersion";
    private static final String DB_BACKUP_TABLE_NAME = "table";

    public static void export(SQLiteDatabase db) throws IOException {
        if (!FileUtils.isExternalStorageWritable()) {
            throw new IOException("Cannot write to external storage");
        }
        File backupDir = FileUtils.createDirIfNotExist(FileUtils.getAppDir() + "/exports");
        List<String> tables = getTablesOnDataBase(db);

//        Log.d(TAG, "Started to fill the export file in " + backupFile.getAbsolutePath());
//        long starTime = System.currentTimeMillis();
        org.apache.commons.io.FileUtils.cleanDirectory(backupDir);

        for (int i = 2; i < tables.size(); i++) {

//            String fileName = createBackupFileName(tables.get(i));
//            File backupFile = new File(backupDir, fileName);

            File backupFile = new File(backupDir, tables.get(i) + ".csv");

//            if(!backupFile.exists()) {
            boolean success = backupFile.createNewFile();
            if (!success) {
                throw new IOException("Failed to create the export file");
            }
//            }
            writeCsv(backupFile, db, tables.get(i));
        }

//        long endTime = System.currentTimeMillis();
//        Log.d(TAG, "Creating export took " + (endTime - starTime) + "ms.");

//        return backupFile.getAbsolutePath();
    }

    private static String createBackupFileName(String tableName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmm");
        return tableName + sdf.format(new Date()) + ".csv";
    }

    private static List<String> getTablesOnDataBase(SQLiteDatabase db) {
        Cursor c = null;
        List<String> tables = new ArrayList<>();
        try {
            c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    tables.add(c.getString(0));
                    c.moveToNext();
                }
            }
        } catch (Exception throwable) {
            Log.e(TAG, "Could not get the table names from db", throwable);
        } finally {
            if (c != null)
                c.close();
        }
        return tables;
    }

    private static void writeCsv(File backupFile, SQLiteDatabase db, String tableName) {
        CSVWriter csvWrite = null;
        Cursor curCSV = null;
        try {
            csvWrite = new CSVWriter(new FileWriter(backupFile, true));
//            writeSingleValue(csvWrite, DB_BACKUP_DB_VERSION_KEY + "=" + db.getVersion());
//            for (int i1 = 2; i1 < tables.size(); i1++) {
//                String table = tables.get(i1);
//                writeSingleValue(csvWrite, DB_BACKUP_TABLE_NAME + "=" + table);
            curCSV = db.rawQuery("SELECT * FROM " + tableName, null);
//                csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                int columns = curCSV.getColumnCount();
                String[] columnArr = new String[columns];
                for (int i = 0; i < columns; i++) {
                    columnArr[i] = curCSV.getString(i);
                }
                csvWrite.writeNext(columnArr);
            }
//            }
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

    private static void writeSingleValue(CSVWriter writer, String value) {
        writer.writeNext(new String[]{value});
    }

}
