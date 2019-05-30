package com.kshitijharsh.dairymanagement.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.database.DBHelper;
import com.kshitijharsh.dairymanagement.database.DBQuery;

public class MemberActivity extends AppCompatActivity {

    DBQuery dbQuery;
    EditText name, zone, rateGrp;
    Button save, clear;
    Spinner type;
    RadioGroup radioGroup;
    DBHelper dbHelper;
    String cowBuf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        dbQuery = new DBQuery(this);
        dbQuery.open();
        dbHelper = new DBHelper(this);

        getSupportActionBar().setTitle("Add Members");

        name = findViewById(R.id.edt_memb_name);
        zone = findViewById(R.id.zoonCode);
        rateGrp = findViewById(R.id.rateGrNo);
        type = findViewById(R.id.spinnerItem);
        save = findViewById(R.id.save);
        clear = findViewById(R.id.clear);
        radioGroup = findViewById(R.id.cowBuff);
        radioGroup.clearCheck();

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
                rateGrp.setText("");
                radioGroup.clearCheck();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int zoonCode = 1, cb = -1, mType = -1, rateGrpNo = -1;
                String rateGrpName;
                if (name.getText().toString().equals("") || rateGrp.getText().toString().equals("") || radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MemberActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
                } else {
                    if (!zone.getText().toString().equals(""))
                        zoonCode = Integer.parseInt(zone.getText().toString());
                    rateGrpName = rateGrp.getText().toString();
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
                        zone.setText("");
                        rateGrp.setText("");
                        radioGroup.clearCheck();
                    }
                }

            }
        });
    }

    public int getRateGrpNoFromName(String name) {
        Cursor c = dbQuery.getRateGrpNo(name);
        int no;
        c.moveToFirst();
        if (c.getCount() > 0 && c != null) {
            no = c.getInt(c.getColumnIndex("RateGrno"));
            return no;
        } else {
            return -1;
        }
    }
}
