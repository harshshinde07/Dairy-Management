package com.kshitijharsh.dairymanagement.activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.kshitijharsh.dairymanagement.SettingsActivity;
import com.kshitijharsh.dairymanagement.database.DBHelper;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
import com.kshitijharsh.dairymanagement.model.Member;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CollectionActivity extends AppCompatActivity {
    DBQuery dbQuery;
    AutoCompleteTextView edtName;
    TextView membType, cowBuf;
    ArrayList<String> names;
    TextView rate, amt;
    EditText txtCode;
    HashMap<String, Member> members;
    EditText degree, fat, quantity, date, snf;
    Button save, clear;
    float f, q, d, a;
    DBHelper dbHelper;
    DatabaseClass dbClass;
    RadioGroup radioGroup, radioGroupMorEve;
    LinearLayout swapCB, swapBoth, addSNF;
    String cowBuff;
    String mornEve;
    String[] memb_type = {"Member", "Contractor", "Labour Contractor"};
    String settingsPrefs = "empty";
    int rateGroupNo;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        dbQuery = new DBQuery(this);
        dbQuery.open();
        dbHelper = new DBHelper(this);
        dbClass = new DatabaseClass(this);
        edtName = findViewById(R.id.edt_memb_name);
        membType = findViewById(R.id.mem_type);
        cowBuf = findViewById(R.id.cow_buf);
        txtCode = findViewById(R.id.edt_memb_id);
        save = findViewById(R.id.save);
        clear = findViewById(R.id.clear);
        rate = findViewById(R.id.rate);
        amt = findViewById(R.id.amt);
        degree = findViewById(R.id.degree);
        fat = findViewById(R.id.fat);
        quantity = findViewById(R.id.qty);
        date = findViewById(R.id.date);
        degree.setVisibility(View.GONE);
        addSNF = findViewById(R.id.linearAdd);

        getSupportActionBar().setTitle("Milk Collection");

        settingsPrefs = SettingsActivity.MainPreferenceFragment.CALCULATE_PREF;

        if (settingsPrefs.equals("false")) {
            degree.setVisibility(View.VISIBLE);
            degree.setHint("SNF");
        }
        if (settingsPrefs.equals("true")) {
            snf = new EditText(this);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            snf.setLayoutParams(p);
            snf.setEnabled(false);
            snf.setSingleLine();
            degree.setVisibility(View.VISIBLE);
            snf.setHint("SNF");
            snf.setInputType(InputType.TYPE_CLASS_PHONE);
            //snf.setText("Text");
            //snf.setId(R.id.snf);
            addSNF.addView(snf);
        }

        degree.addTextChangedListener(new TextWatcher() {

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
                float fa = 0, de = 0, qt = 0;
                if (!cowBuf.getText().toString().equals("")) {
                    if (swapBoth.getVisibility() == View.VISIBLE) {
                        if (cowBuff.equals("Cow"))
                            cowBuf.setText("Cow");
                        if (cowBuff.equals("Buffalo"))
                            cowBuf.setText("Buffalo");
                    }
                    if (!fat.getText().toString().equals(""))
                        fa = Float.parseFloat(fat.getText().toString());
                    if (!s.toString().equals(""))
                        de = Float.parseFloat(s.toString());
                    if (!quantity.getText().toString().equals(""))
                        qt = Float.parseFloat(quantity.getText().toString());
                    if (settingsPrefs.equals("true")) {
                        calculateSNF(de, fa);
                    }
                    getRateAmt(de, fa, qt, cowBuf.getText().toString());

                } else {
                    Toast.makeText(CollectionActivity.this, "Please choose values first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fat.addTextChangedListener(new TextWatcher() {

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
                float fa = 0, de = 0, qt = 0;
                if (!cowBuf.getText().toString().equals("")) {
                    if (swapBoth.getVisibility() == View.VISIBLE) {
                        if (cowBuff.equals("Cow"))
                            cowBuf.setText("Cow");
                        if (cowBuff.equals("Buffalo"))
                            cowBuf.setText("Buffalo");
                    }
                    if (!s.toString().equals(""))
                        fa = Float.parseFloat(s.toString());
                    if (!degree.getText().toString().equals(""))
                        de = Float.parseFloat(degree.getText().toString());
                    if (!quantity.getText().toString().equals(""))
                        qt = Float.parseFloat(quantity.getText().toString());
                    if (settingsPrefs.equals("true")) {
                        calculateSNF(de, fa);
                    }
                    getRateAmt(de, fa, qt, cowBuf.getText().toString());
                } else {
                    Toast.makeText(CollectionActivity.this, "Please choose values first!", Toast.LENGTH_SHORT).show();
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
                float fa = 0, de = 0, qt = 0;
                if (!cowBuf.getText().toString().equals("")) {
                    if (swapBoth.getVisibility() == View.VISIBLE) {
                        if (cowBuff.equals("Cow"))
                            cowBuf.setText("Cow");
                        if (cowBuff.equals("Buffalo"))
                            cowBuf.setText("Buffalo");
                    }
                    if (!fat.getText().toString().equals(""))
                        fa = Float.parseFloat(fat.getText().toString());
                    if (!degree.getText().toString().equals(""))
                        de = Float.parseFloat(degree.getText().toString());
                    if (!s.toString().equals(""))
                        qt = Float.parseFloat(s.toString());
                    if (settingsPrefs.equals("true")) {
                        calculateSNF(de, fa);
                    }
                    getRateAmt(de, fa, qt, cowBuf.getText().toString());
                } else {
                    Toast.makeText(CollectionActivity.this, "Please choose values first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        radioGroupMorEve = findViewById(R.id.morEve);
        radioGroupMorEve.clearCheck();

        radioGroupMorEve.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (null != rb) {
                    //Toast.makeText(SaleActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                    mornEve = rb.getText().toString();
                }

            }
        });

        radioGroup = findViewById(R.id.cowBuff);
        radioGroup.clearCheck();
        swapBoth = findViewById(R.id.swapBoth);
        swapCB = findViewById(R.id.swapCB);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (null != rb) {
                    //Toast.makeText(SaleActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                    cowBuff = rb.getText().toString();
                }

            }
        });
        initNames();

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtName.setText("");
                membType.setText("");
                cowBuf.setText("");
                txtCode.setText("");
                rate.setText("");
                amt.setText("");
                degree.setText("");
                fat.setText("");
                quantity.setText("");
                date.setText("");
                radioGroup.clearCheck();
                radioGroupMorEve.clearCheck();
            }
        });

        final DatePickerDialog datePickerDialog;
        Calendar mcurrentDate = Calendar.getInstance();
        final int mYear = mcurrentDate.get(Calendar.YEAR);
        final int mMonth = mcurrentDate.get(Calendar.MONTH);
        final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(CollectionActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                if (date.getText().toString().equals("") || edtName.getText().toString().equals("") || membType.getText().toString().equals("") || txtCode.getText().toString().equals("") || degree.getText().toString().equals("") || fat.getText().toString().equals("") || quantity.getText().toString().equals("") || radioGroupMorEve.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CollectionActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
                } else {
                    int memCode = Integer.parseInt(txtCode.getText().toString());
                    float deg = Float.parseFloat(degree.getText().toString());
                    float lit = Float.parseFloat(quantity.getText().toString());
                    float f = Float.parseFloat(fat.getText().toString());
                    float q = Float.parseFloat(quantity.getText().toString());

                    if (swapBoth.getVisibility() == View.VISIBLE) {
                        if (cowBuff.equals("Cow"))
                            cowBuf.setText("Cow");
                        if (cowBuff.equals("Buffalo"))
                            cowBuf.setText("Buffalo");
                    }
                    //getRateAmt(deg, f, q, cowBuf.getText().toString());

                    if (!rate.getText().toString().equals("") || !amt.getText().toString().equals("")) {
                        float r = Float.parseFloat(rate.getText().toString());
                        float a = Float.parseFloat(amt.getText().toString());
                        if (!cowBuf.getText().toString().equals("")) {
                            dbClass.addColl(date.getText().toString(), memCode, edtName.getText().toString(), cowBuf.getText().toString(), mornEve, deg, lit, f, r, a);
                            Toast.makeText(CollectionActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                            edtName.setText("");
                            membType.setText("");
                            cowBuf.setText("");
                            txtCode.setText("");
                            degree.setText("");
                            fat.setText("");
                            quantity.setText("");
                            date.setText("");
                            swapBoth.setVisibility(View.GONE);
                            swapCB.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(CollectionActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CollectionActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
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
                int type = Integer.parseInt(member.getMembType());
                type = type -1;
                Log.e("11111111111", String.valueOf(type));
                int cb = Integer.parseInt(member.getCowbfType());
                rateGroupNo = Integer.parseInt(member.getRateGrpNo());
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
                membType.setText(memb_type[type]);
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

    public void getRateAmt(float deg, float fat, float qty, String cobf) {
        Cursor c;
        if (degree.getHint().toString().equals("SNF")) {
            c = dbQuery.getRateFromSNF(deg, fat, cobf, rateGroupNo);
        } else {
            if (settingsPrefs.equals("true")) {
                float s = 0;
                if (!snf.getText().toString().equals(""))
                    s = Float.parseFloat(snf.getText().toString());
                c = dbQuery.getRateFromSNF(s, fat, cobf, rateGroupNo);
            } else {
                c = dbQuery.getRate(deg, fat, cobf, rateGroupNo);
            }
        }
        float val;
        c.moveToFirst();
        if (c.getCount() > 0 && c != null) {
            val = c.getFloat(c.getColumnIndex("rate"));
            //Toast.makeText(this, String.valueOf(val), Toast.LENGTH_SHORT).show();
            rate.setText(String.valueOf(val));
            //q = Float.parseFloat(quantity.getText().toString());
            a = qty * val;
            amt.setText(String.valueOf(a));
            //float m = dbQuery.getMilkCount();
            //Toast.makeText(this, String.valueOf(m), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Value not found!", Toast.LENGTH_SHORT).show();
        }
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
            int type = Integer.parseInt(member.getMembType());
            int cb = Integer.parseInt(member.getCowbfType());
            rateGroupNo = Integer.parseInt(member.getRateGrpNo());
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
            membType.setText(memb_type[type]);
        } else {
            Toast.makeText(this, "Member not found!", Toast.LENGTH_SHORT).show();
        }
    }

    public void calculateSNF(float deg, float fat) {
        /** This function calculates SNF and sets it to the added SNF **/
        double res = 0;
        res = (deg / 4) + (fat * 0.21) + 0.36;
        snf.setText(String.valueOf(res));
    }
}
