package com.kshitijharsh.dairymanagement.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.database.DBHelper;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
import com.kshitijharsh.dairymanagement.model.Customer;
import com.kshitijharsh.dairymanagement.utils.FileUtils;
import com.kshitijharsh.dairymanagement.utils.SqliteExporter;
import com.kshitijharsh.dairymanagement.utils.SqliteImporter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import static com.kshitijharsh.dairymanagement.database.BaseContract.BaseEntry.TABLE_CUSTOMER;
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
    DBQuery query;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    TextView licensee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setContentView(R.layout.activity_main);
        licensee = findViewById(R.id.licensee);
        checkForStoragePermission();
        dc = new DatabaseClass(this);

        query = new DBQuery(this);
        builder = new AlertDialog.Builder(this);
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
        } else {
            query = new DBQuery(this);
            dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();

            if (!checkDataBase()) {
                Toast.makeText(this, "Database not found! App will exit now...", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3 * 1000);
            } else {
                FileUtils.createDirIfNotExist(FileUtils.getAppDir() + "/imports");
                query.open();
                Cursor member = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_MEMBER + "'", null);
                Cursor rateMaster = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_RATEMASTER + "'", null);
                Cursor item = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_ITEM + "'", null);
                Cursor rateGrpMaster = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_RATEGRPMASTER + "'", null);
                Cursor customer = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_CUSTOMER + "'", null);

                if (member.getCount() <= 0 || rateMaster.getCount() <= 0 || item.getCount() <= 0 || rateGrpMaster.getCount() <= 0 || customer.getCount() <= 0) {
                    Toast.makeText(this, "Required tables not found in database, app will exit now...", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 3 * 1000);
                } else if (!query.validateIfTableHasData(db)) {
                    Toast.makeText(this, "Empty tables found, Importing data...", Toast.LENGTH_SHORT).show();
                    ImportTask task = new ImportTask();
                    task.execute();

                    Customer c = query.getCustomerDetails();
                    licensee.setText(c.getName());

                    // TODO Replace later when login is implemented
                    byte[] bytesID = new byte[0];
                    bytesID = "8005".getBytes(StandardCharsets.UTF_8);

                    MessageDigest mdID = null;
                    try {
                        mdID = MessageDigest.getInstance("MD5");
                        byte[] encID = mdID.digest(bytesID);

                        if (Arrays.equals(encID, c.getId())) {
                            Log.e("mainActivity", "checkForStoragePermission: TRUE");
                        } else {
                            Log.e("mainActivity", "checkForStoragePermission: FALSE");
                        }

                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.d(getClass().getSimpleName(), "First Query Result: " + query.getMembercount());

                    Customer c = query.getCustomerDetails();
                    licensee.setText(c.getName());

                    // TODO Replace later when login is implemented
                    byte[] bytesID = new byte[0];
                    bytesID = "8005".getBytes(StandardCharsets.UTF_8);

                    MessageDigest mdID = null;
                    try {
                        mdID = MessageDigest.getInstance("MD5");
                        byte[] encID = mdID.digest(bytesID);

                        if (Arrays.equals(encID, c.getId())) {
                            Log.e("mainActivity", "checkForStoragePermission: TRUE");
                        } else {
                            Log.e("mainActivity", "checkForStoragePermission: FALSE");
                        }

                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
                member.close();
                rateMaster.close();
                item.close();
                rateGrpMaster.close();
                customer.close();
                query.close();
                db.close();
                dbHelper.close();
            }

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
            dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
            FileUtils.createDirIfNotExist(FileUtils.getAppDir() + "/imports");
            query.open();
            Cursor member = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_MEMBER + "'", null);
            Cursor rateMaster = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_RATEMASTER + "'", null);
            Cursor item = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_ITEM + "'", null);
            Cursor rateGrpMaster = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_RATEGRPMASTER + "'", null);
            Cursor customer = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_CUSTOMER + "'", null);

            if (member.getCount() <= 0 || rateMaster.getCount() <= 0 || item.getCount() <= 0 || rateGrpMaster.getCount() <= 0 || customer.getCount() <= 0) {
                Toast.makeText(this, "Required tables not found in database, app will exit now...", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3 * 1000);
            } else if (!query.validateIfTableHasData(db)) {
                Toast.makeText(this, "Empty tables found, Importing data...", Toast.LENGTH_SHORT).show();
//                initiateImport();
                ImportTask task = new ImportTask();
                task.execute();

            } else {
                Log.d(getClass().getSimpleName(), "First Query Result: " + query.getMembercount());
            }
            member.close();
            rateMaster.close();
            item.close();
            rateGrpMaster.close();
            customer.close();
            query.close();
            db.close();
            dbHelper.close();
        }
    }

    public void exitApp() {
        if (exit) {
            finish();
        } else {
            exit = true;
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
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

    public void launchCollections(View view) {
        startActivity(new Intent(MainActivity.this, CollectionActivity.class));
    }

    public void launchSales(View view) {
        startActivity(new Intent(MainActivity.this, SaleActivity.class));
    }

    public void initiateExport(View view) {

        builder.setMessage("Are you sure you want to export data to CSV file ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ExportTask task = new ExportTask();
                        task.execute();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Export to CSV");
        alert.show();

    }

    public void launchCattleFeed(View view) {
        startActivity(new Intent(MainActivity.this, CattleFeedActivity.class));
    }

//    public void launchDeduction(View view) {
//        startActivity(new Intent(MainActivity.this, DeductionActivity.class));
//    }

    public void launchMember(View view) {
        startActivity(new Intent(MainActivity.this, MemberDetailActivity.class));
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

            builder.setMessage("Are you sure you want to import data from CSV file ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ImportTask task = new ImportTask();
                            task.execute();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();

                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Import from CSV");
            alert.show();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int initiateImport() {
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        int retval = -1;
        try {

            int res = SqliteImporter.importData(sqLiteDatabase);
            if (res == 1) {
                retval = 0;
            }
        } catch (Exception e) {
            Log.e("Import Exception: ", e.toString());
        }
        sqLiteDatabase.close();
        helper.close();
        return retval;
    }

    @SuppressLint("StaticFieldLeak")
    private class ImportTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            return initiateImport();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Importing data..."); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            progressDialog.dismiss();
            switch (integer) {
                case -1:
                    Toast.makeText(MainActivity.this, "No CSV files to import", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(MainActivity.this, "Successfully Imported.", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    Toast.makeText(MainActivity.this, "Some problem occurred...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public int exportTables() {
        try {
            SqliteExporter.export(dc.getReadableDatabase());
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ExportTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            return exportTables();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Exporting data..."); // Setting Message
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            progressDialog.dismiss();
            switch (integer) {
                case 1:
                    Toast.makeText(MainActivity.this, "Error while exporting, try again", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(MainActivity.this, "Successfully exported to " + Environment.getExternalStorageDirectory()
                            + EXT_DIRECTORY
                            + File.separator + BACKUP_DIRECTORY, Toast.LENGTH_SHORT).show();

                    break;
                default:
                    Toast.makeText(MainActivity.this, "Some problem occurred...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
