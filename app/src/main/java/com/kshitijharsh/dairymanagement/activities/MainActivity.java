package com.kshitijharsh.dairymanagement.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.database.DBHelper;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
import com.kshitijharsh.dairymanagement.utils.FileUtils;
import com.kshitijharsh.dairymanagement.utils.SqliteExporter;
import com.kshitijharsh.dairymanagement.utils.SqliteImporter;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static com.kshitijharsh.dairymanagement.database.BaseContract.BaseEntry.TABLE_ITEM;
import static com.kshitijharsh.dairymanagement.database.BaseContract.BaseEntry.TABLE_MEMBER;
import static com.kshitijharsh.dairymanagement.database.BaseContract.BaseEntry.TABLE_RATEGRPMASTER;
import static com.kshitijharsh.dairymanagement.database.BaseContract.BaseEntry.TABLE_RATEMASTER;
import static com.kshitijharsh.dairymanagement.utils.Constants.CONST.BACKUP_DIRECTORY;
import static com.kshitijharsh.dairymanagement.utils.Constants.CONST.EXT_DIRECTORY;

public class MainActivity extends AppCompatActivity {
    public static final int ACCESS_EXTERNAL_STORAGE = 1;
    private static final String DB_FULL_PATH = Environment.getExternalStorageDirectory() + "/Winsoft/base.db";
    private boolean exit = false;
    DatabaseClass dc;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setContentView(R.layout.activity_main);
        checkForStoragePermission();
        dc = new DatabaseClass(this);
    }

    /**
     * to check directory 'Winsoft' and external storage permission
     */
    private void checkForStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission is not granted, ask for it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ACCESS_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // If request is cancelled, the result arrays are empty.
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            //Permission is denied
            Toast.makeText(this, "Storage permission was required, app will exit now...", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3 * 1000);
        } else if (!checkDataBase()) {
            Toast.makeText(this, "Database not found! App will exit now...", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3 * 1000);
        } else {
            FileUtils.createDirIfNotExist(FileUtils.getAppDir() + "/imports");
            dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
            Cursor member = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_MEMBER + "'", null);
            Cursor rateMaster = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_RATEMASTER + "'", null);
            Cursor item = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_ITEM + "'", null);
            Cursor rateGrpMaster = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_RATEGRPMASTER + "'", null);

            if (member.getCount() <= 0 || rateMaster.getCount() <= 0 || item.getCount() <= 0 || rateGrpMaster.getCount() <= 0) {
                Toast.makeText(this, "Required tables not found in database, app will exit now...", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3 * 1000);
            }
            member.close();
            rateMaster.close();
            item.close();
            rateGrpMaster.close();
            DBQuery query = new DBQuery(this);
            query.createDatabase();
            query.open();
            Log.d(getClass().getSimpleName(), "First Query Result: " + query.getMembercount());
            //Toast.makeText(this, DBHelper.DB_PATH, Toast.LENGTH_LONG).show();
            query.close();
//            db.endTransaction();
            db.close();
            dbHelper.close();
        }
    }

    public void exitApp() {
        if (exit) {
            finish();
        } else {
            exit = true;
//            Toast.makeText(this,"Press back again to exit",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitApp();
    }

    public void launchCollections(View view) {
        startActivity(new Intent(MainActivity.this, CollectionActivity.class));
    }

    public void launchSales(View view) {
        startActivity(new Intent(MainActivity.this, SaleActivity.class));
    }

    public void initiateExport(View view) {
        try {
            SqliteExporter.export(dc.getReadableDatabase());
            Toast.makeText(this, "Successfully exported to " + Environment.getExternalStorageDirectory()
                    + EXT_DIRECTORY
                    + File.separator + BACKUP_DIRECTORY, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void launchCattleFeed(View view) {
        startActivity(new Intent(MainActivity.this, CattleFeedActivity.class));
    }

//    public void launchDeduction(View view) {
//        startActivity(new Intent(MainActivity.this, DeductionActivity.class));
//    }

    public void launchMember(View view) {
        startActivity(new Intent(MainActivity.this, MemberActivity.class));
    }

    public void launchSettings(View view) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_main) {
            initiateImport();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initiateImport() {
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        try {

            int res = SqliteImporter.importData(sqLiteDatabase);
//            Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
            switch (res) {
                case 0:
                    Toast.makeText(this, "No CSV files to import", Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(this, "File names does not match!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(this, "Successfully Imported.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "Some problem occurred...", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error while importing, try again.", Toast.LENGTH_SHORT).show();
            Log.e("Import Exception: ", e.toString());
        }
//        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
        helper.close();
    }
}
