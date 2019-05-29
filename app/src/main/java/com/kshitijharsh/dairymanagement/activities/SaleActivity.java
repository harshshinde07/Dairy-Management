package com.kshitijharsh.dairymanagement.activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.database.DBHelper;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
import com.kshitijharsh.dairymanagement.model.Member;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SaleActivity extends AppCompatActivity {

    DBQuery dbQuery;
    AutoCompleteTextView edtName;
    TextView cowBuf;
    ArrayList<String> names;
    TextView amt;
    EditText txtCode;
    HashMap<String, Member> members;
    EditText branch, fat, quantity, date, rate;
    Button save, clear;
    DBHelper dbHelper;
    DatabaseClass dbClass;
    RadioGroup radioGroup, radioGroupCB;
    LinearLayout swapCB, swapBoth;
    String cowBuff;
    String mornEve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        dbQuery = new DBQuery(this);
        dbQuery.open();
        dbHelper = new DBHelper(this);
        dbClass = new DatabaseClass(this);
        edtName = findViewById(R.id.edt_memb_name);
        cowBuf = findViewById(R.id.cow_buf);
        txtCode = findViewById(R.id.edt_memb_id);
        rate = findViewById(R.id.rate);
        amt = findViewById(R.id.amt);
        branch = findViewById(R.id.branch);
        fat = findViewById(R.id.fat);
        quantity = findViewById(R.id.qty);
        date = findViewById(R.id.date);
        save = findViewById(R.id.save);
        clear = findViewById(R.id.clear);
        radioGroup = findViewById(R.id.morEve);
        initNames();
        radioGroup.clearCheck();

        rate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                float ra = 0, qt = 0;
                if(!cowBuf.getText().toString().equals("")) {
                    if (swapBoth.getVisibility() == View.VISIBLE) {
                        if (cowBuff.equals("Cow"))
                            cowBuf.setText("Cow");
                        if (cowBuff.equals("Buffalo"))
                            cowBuf.setText("Buffalo");
                    }
                    if(!s.toString().equals(""))
                        ra = Float.parseFloat(s.toString());
                    if(!quantity.getText().toString().equals(""))
                        qt = Float.parseFloat(quantity.getText().toString());
                    float a = qt * ra;
                    amt.setText(String.valueOf(a));
                } else {
                    Toast.makeText(SaleActivity.this, "Please choose values first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        quantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                float ra = 0, qt = 0;
                if(!cowBuf.getText().toString().equals("")) {
                    if (swapBoth.getVisibility() == View.VISIBLE) {
                        if (cowBuff.equals("Cow"))
                            cowBuf.setText("Cow");
                        if (cowBuff.equals("Buffalo"))
                            cowBuf.setText("Buffalo");
                    }
                    if(!s.toString().equals(""))
                        qt = Float.parseFloat(s.toString());
                    if(!rate.getText().toString().equals(""))
                        ra = Float.parseFloat(rate.getText().toString());
                    float a = qt * ra;
                    amt.setText(String.valueOf(a));
                } else {
                    Toast.makeText(SaleActivity.this, "Please choose values first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (null != rb) {
                    //Toast.makeText(SaleActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                    mornEve = rb.getText().toString();
                }

            }
        });

        //float count = dbClass.getMilkCount();
        //Toast.makeText(this, String.valueOf(count), Toast.LENGTH_SHORT).show();

        radioGroupCB = findViewById(R.id.cowBuff);
        radioGroupCB.clearCheck();
        swapBoth = findViewById(R.id.swapBoth);
        swapCB = findViewById(R.id.swapCB);

        radioGroupCB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (null != rb) {
                    //Toast.makeText(SaleActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                    cowBuff = rb.getText().toString();
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtName.setText("");
                branch.setText("");
                cowBuf.setText("");
                txtCode.setText("");
                rate.setText("");
                amt.setText("");
                fat.setText("");
                quantity.setText("");
                date.setText("");
                radioGroup.clearCheck();
                radioGroupCB.clearCheck();
            }
        });

        final DatePickerDialog datePickerDialog;
        Calendar mcurrentDate = Calendar.getInstance();
        final int mYear = mcurrentDate.get(Calendar.YEAR);
        final int mMonth = mcurrentDate.get(Calendar.MONTH);
        final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(SaleActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int m = month + 1;
                date.setText(dayOfMonth + "-" + m + "-" + year);

            }
        }, mYear, mMonth, mDay);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (date.getText().toString().equals("") || edtName.getText().toString().equals("") || branch.getText().toString().equals("") || txtCode.getText().toString().equals("") || fat.getText().toString().equals("") || quantity.getText().toString().equals("") || radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SaleActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
                } else {
                    int memCode = Integer.parseInt(txtCode.getText().toString());
                    float lit = Float.parseFloat(quantity.getText().toString());
                    float f = Float.parseFloat(fat.getText().toString());
                    float r = Float.parseFloat(rate.getText().toString());
//                    float a = lit * r;
//                    amt.setText(String.valueOf(a));
                    if (swapBoth.getVisibility() == View.VISIBLE) {
                        if (cowBuff.equals("Cow"))
                            cowBuf.setText("Cow");
                        if (cowBuff.equals("Buffalo"))
                            cowBuf.setText("Buffalo");
                    }
                    if (!cowBuf.getText().toString().equals("")) {
                        dbClass.addSale(date.getText().toString(), branch.getText().toString(), memCode, edtName.getText().toString(), mornEve, cowBuf.getText().toString(), lit, f, r, Float.parseFloat(amt.getText().toString()));
                        Toast.makeText(SaleActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                        edtName.setText("");
                        branch.setText("");
                        cowBuf.setText("");
                        txtCode.setText("");
                        rate.setText("");
                        fat.setText("");
                        quantity.setText("");
                        date.setText("");
                        radioGroup.clearCheck();
                        radioGroupCB.clearCheck();
                        swapBoth.setVisibility(View.GONE);
                        swapCB.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(SaleActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        txtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int val;
                if (!charSequence.toString().equals("")) {
                    val = Integer.parseInt(charSequence.toString());
                    getMemNameFromID(val);
                } else {
                    edtName.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                TextView txtName = (TextView) view;
                Member member = members.get(txtName.getText().toString());
                int cb = Integer.parseInt(member.getCowbfType());

                String cbText = "";
                if (cb == 1) {
                    cbText = "Cow";
                    cowBuf.setText(cbText);
                } else if (cb == 2) {
                    cbText = "Buffalo";
                    cowBuf.setText(cbText);
                } else if (cb == 3) {
                    cbText = "Both";
                    swapBoth.setVisibility(View.VISIBLE);
                    swapCB.setVisibility(View.GONE);
                } else {
                    cowBuf.setText(cbText);
                }
                txtCode.setText(member.getCode());

            }
        });
    }

    private void initNames() {
        Cursor cursor = dbQuery.getAllMembers();
        names = new ArrayList<>();
        members = new HashMap<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(1);
            Member mem = new Member(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
            names.add(name);
            members.put(name, mem);
            cursor.moveToNext();
        }
        System.out.println("Names added: " + members.size());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_name_list,
                names);

        edtName.setAdapter(adapter);
        edtName.setThreshold(1);
    }

    public void getMemNameFromID(int id) {
        Cursor c = dbQuery.getMemName(id);
        String name;
        c.moveToFirst();
        edtName.setText("");
        if (c.getCount() > 0 && c != null) {
            name = c.getString(c.getColumnIndex("memb_name"));
            edtName.setText(name);
            Member member = members.get(name);
            int cb = Integer.parseInt(member.getCowbfType());

            String cbText = "";
            if (cb == 1) {
                cbText = "Cow";
                cowBuf.setText(cbText);
            } else if (cb == 2) {
                cbText = "Buffalo";
                cowBuf.setText(cbText);
            } else if (cb == 3) {
                cbText = "Both";
                swapBoth.setVisibility(View.VISIBLE);
                swapCB.setVisibility(View.GONE);
            } else {
                cowBuf.setText(cbText);
            }
        } else {
            Toast.makeText(this, "Member not found!", Toast.LENGTH_SHORT).show();
        }
    }
}
