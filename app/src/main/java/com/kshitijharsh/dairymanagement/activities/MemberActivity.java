package com.kshitijharsh.dairymanagement.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.database.DBHelper;
import com.kshitijharsh.dairymanagement.database.DBQuery;

import java.util.ArrayList;
import java.util.Objects;

public class MemberActivity extends AppCompatActivity {

    DBQuery dbQuery;
    EditText name, zone;
    Button save, clear;
    Spinner type, rateGrp;
    RadioGroup radioGroup;
    DBHelper dbHelper;
    String cowBuf;
    ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        dbQuery = new DBQuery(this);
        dbQuery.open();
        dbHelper = new DBHelper(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Members");

        name = findViewById(R.id.edt_memb_name);
//        zone = findViewById(R.id.zoonCode);
        rateGrp = findViewById(R.id.rateGrNo);
        type = findViewById(R.id.spinnerItem);
        save = findViewById(R.id.save);
        clear = findViewById(R.id.clear);
        radioGroup = findViewById(R.id.cowBuff);
        radioGroup.clearCheck();

        initGroupNames();

//        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if(actionId== EditorInfo.IME_ACTION_DONE){
//                    //Clear focus here from edittext
//                    name.clearFocus();
//                }
//                return false;
//            }
//        });
//
////         Implement onFocusChanged to check for duplicate member names
//        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
////                    checkForDuplicateName(name.getText().toString());
//                    CheckDuplicateAsyncTask asyncTask = new CheckDuplicateAsyncTask();
//                    asyncTask.execute(name.getText().toString());
//                }
//            }
//        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (null != rb) {
                    //Toast.makeText(SaleActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                    cowBuf = rb.getText().toString();
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                zone.setText("");
                radioGroup.clearCheck();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int zoonCode = 1, cb = -1, mType = -1, rateGrpNo = -1;
                String rateGrpName;

//                CheckDuplicateAsyncTask asyncTask = new CheckDuplicateAsyncTask();
//                asyncTask.execute(name.getText().toString());

                if (name.getText().toString().equals("") || radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MemberActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
                } else {
//                    if(!zoon.getText().toString().equals(""))
//                        zoonCode = Integer.parseInt(zoon.getText().toString());
                    rateGrpName = rateGrp.getSelectedItem().toString();
                    rateGrpNo = getRateGrpNoFromName(rateGrpName);
                    if (rateGrpNo == -1) {
                        Toast.makeText(MemberActivity.this, "Invalid Rate group try again", Toast.LENGTH_SHORT).show();
                    } else {
                        if (cowBuf.equals("Cow"))
                            cb = 1;
                        if (cowBuf.equals("Buffalo"))
                            cb = 2;
                        if (cowBuf.equals("Both"))
                            cb = 3;
                        if (type.getSelectedItem().toString().equals("Member"))
                            mType = 1;
                        if (type.getSelectedItem().toString().equals("Contractor"))
                            mType = 2;
                        if (type.getSelectedItem().toString().equals("Labour Contractor"))
                            mType = 3;
                        dbQuery.addNewMem(name.getText().toString(), zoonCode, cb, mType, rateGrpNo);
                        Toast.makeText(MemberActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                        name.setText("");
//                        zone.setText("");
                        radioGroup.clearCheck();
                    }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_details) {
            startActivity(new Intent(this, MemberDetailActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int getRateGrpNoFromName(String name) {
        Cursor c = dbQuery.getRateGrpNo(name);
        int no;
        c.moveToFirst();
        if (c.getCount() > 0) {
            no = c.getInt(c.getColumnIndex("RateGrno"));
            return no;
        } else {
            return -1;
        }
    }

    private void initGroupNames() {
        Cursor cursor = dbQuery.getAllRateGroups();
        ArrayList<String> names = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            names.add(name);
            cursor.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rateGrp.setAdapter(adapter);

    }

    //TODO check for duplicates in member names
//    protected int checkForDuplicateName(String val) {
//        Cursor cursor = dbQuery.getAllMembers("");
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            if(cursor.getString(1).equals(val)) {
////                name.setText("");
////                name.setError("The name already exists! Try another.");
//                Toast.makeText(this, "True!", Toast.LENGTH_SHORT).show();
//                return 1;
//            }
//        }
//        return 0;
//    }
//
//    private class CheckDuplicateAsyncTask extends AsyncTask<String, Void, Integer> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            p = new ProgressDialog(MemberActivity.this);
//            p.setMessage("Please wait...");
//            p.setIndeterminate(false);
//            p.setCancelable(false);
//            p.show();
//        }
//
//        @Override
//        protected Integer doInBackground(String... strings) {
//            return checkForDuplicateName(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Integer i) {
//            super.onPostExecute(i);
//            p.dismiss();
//            if(i != 0) {
//                name.setText("");
//                name.setError("The name already exists! Try another.");
//            }
//        }
//    }
}
