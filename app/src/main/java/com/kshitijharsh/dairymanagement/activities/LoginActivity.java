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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.database.DBHelper;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
import com.kshitijharsh.dairymanagement.model.Customer;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.kshitijharsh.dairymanagement.database.BaseContract.BaseEntry.TABLE_CUSTOMER;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login;

    DatabaseClass dc;
    DBHelper dbHelper;
    SQLiteDatabase db;
    DBQuery query;

    public static final int ACCESS_EXTERNAL_STORAGE = 1;
    private static final String DB_FULL_PATH = Environment.getExternalStorageDirectory() + "/Winsoft/base.db";

    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAILURE = 1;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        username.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        username = findViewById(R.id.user_id);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        // Migrated from @MainActivity
        checkForStoragePermission();
        dc = new DatabaseClass(this);

        query = new DBQuery(this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(password.getText())) {
                    Toast.makeText(LoginActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
                } else {
                    int res = checkLogin(username.getText().toString(), password.getText().toString(), query);
                    if (res == LOGIN_SUCCESS) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        username.setText("");
                        password.setText("");
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private int checkLogin(String id, String password, DBQuery query) {
        int res = 1;
        query.open();
        Customer c = query.getCustomerDetails();

        // ID
        byte[] bytesID = new byte[0];
        try {
            bytesID = id.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        MessageDigest mdID = null;

        // Password
        byte[] bytesPassword = new byte[0];
        try {
            bytesPassword = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        MessageDigest mdPassword = null;

        try {
            mdID = MessageDigest.getInstance("MD5");
            mdPassword = MessageDigest.getInstance("MD5");
            byte[] encID = mdID.digest(bytesID);
            byte[] encPassword = mdPassword.digest(bytesPassword);

            if (Arrays.equals(encID, c.getId()) && Arrays.equals(encPassword, c.getPassword())) {
                Log.e("mainActivity", "login: TRUE");
                res = LOGIN_SUCCESS;
            } else {
                Log.e("mainActivity", "login: FALSE");
                res = LOGIN_FAILURE;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void checkForStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission is not granted, ask for it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ACCESS_EXTERNAL_STORAGE);
        } else {
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
                Cursor customer = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_CUSTOMER + "'", null);

                if (customer.getCount() <= 0) {
                    Toast.makeText(this, "Required tables not found in database, app will exit now...", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 3 * 1000);
                }
                customer.close();
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
            Cursor customer = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_CUSTOMER + "'", null);

            if (customer.getCount() <= 0) {
                Toast.makeText(this, "Required tables not found in database, app will exit now...", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3 * 1000);
            }
            customer.close();
            db.close();
            dbHelper.close();
        }
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

}
