package com.kshitijharsh.dairymanagement.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.kshitijharsh.dairymanagement.utils.Constants.CONST.EXT_DIRECTORY;

public class DBHelper extends SQLiteOpenHelper {

    //[bug_: there is issue creating your own database]
    //[Solution1_: http://stackoverflow.com/a/29281714/4754141]
    //[Prerequisite : android_metadata table required]
    //[Solution2_: better edit DB_CATEGORY.sqlite in assets folder, if you want multiple database to copy]

    private static String DB_NAME = "base.db";
    public static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDataBase;
    private File DbFile;

    public DBHelper(Context context) {
        super(context, Environment.getExternalStorageDirectory()
                + EXT_DIRECTORY
                + File.separator + DB_NAME, null, DB_VERSION);
        DbFile = new File(new File(Environment.getExternalStorageDirectory(), EXT_DIRECTORY),
                "/" + DB_NAME);
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
    }

    void createDataBase() throws IOException {
        boolean exist = checkDataBase();
        Log.d(getClass().getSimpleName(), "createDataBase: " + exist);
        if (!exist) {
            //this.getReadableDatabase();
            copyDataBase();
            //this.close();
        }
    }

    private boolean checkDataBase() {
        File db = new File(DB_PATH);
        return db.exists();
    }

    boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        SQLiteDatabase.releaseMemory();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }



    //_______________________________________________________________________________________________
    private void copyDataBase() throws IOException {
        InputStream mInput = new FileInputStream(DbFile);
        String outfileName = DB_PATH;
        OutputStream mOutput = new FileOutputStream(outfileName);
        byte[] buffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(buffer)) > 0) {
            mOutput.write(buffer, 0, mLength);
        }
        Log.d(getClass().getSimpleName(), "Database copied successfully...");
        mOutput.flush();
        mInput.close();
        mOutput.close();
    }

}
