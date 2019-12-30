package com.kshitijharsh.dairymanagement.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.kshitijharsh.dairymanagement.model.Customer;
import com.kshitijharsh.dairymanagement.model.Member;
import com.kshitijharsh.dairymanagement.utils.RoundUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class SaleActivity extends AppCompatActivity {

    DBQuery dbQuery;
    AutoCompleteTextView edtName;
    ArrayList<String> names;
    TextView amt, date;
    EditText txtCode;
    HashMap<String, Member> members;
    EditText fat, quantity, rate;
    Button save, clear;
    DBHelper dbHelper;
    DatabaseClass dbClass;
    RadioGroup radioGroup, radioGroupCB;
    LinearLayout swapBoth, saleDetails;
    String cowBuff;
    String mornEve = "";
    TextView todayDate, totLit, totAmt;
    String id;
    RadioGroup cashCredit;
    LinearLayout memDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        dbQuery = new DBQuery(this);
        dbQuery.open();
        dbHelper = new DBHelper(this);
        dbClass = new DatabaseClass(this);

        Customer c = dbQuery.getCustomerDetails();
        final String zoonCode = c.getBranchCode();

        Objects.requireNonNull(getSupportActionBar()).setTitle("Milk Sale");

        edtName = findViewById(R.id.edt_memb_name);
        txtCode = findViewById(R.id.edt_memb_id);
        rate = findViewById(R.id.rate);
        amt = findViewById(R.id.amt);
        fat = findViewById(R.id.fat);
        quantity = findViewById(R.id.qty);
        date = findViewById(R.id.date);
        save = findViewById(R.id.save);
        clear = findViewById(R.id.clear);
        radioGroup = findViewById(R.id.morEve);
        initNames(zoonCode);
        radioGroup.clearCheck();

        radioGroupCB = findViewById(R.id.cowBuff);
        radioGroupCB.clearCheck();

        cashCredit = findViewById(R.id.cash_credit);
        memDetails = findViewById(R.id.member_details_layout);

        swapBoth = findViewById(R.id.swapBoth);

        saleDetails = findViewById(R.id.sale_details);
        todayDate = findViewById(R.id.today_date);
        totAmt = findViewById(R.id.tot_amt);
        totLit = findViewById(R.id.tot_lit);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            edtName.setText(bundle.getString("name"));
            txtCode.setText(bundle.getString("memId"));
            rate.setText(bundle.getString("rate"));
            amt.setText(bundle.getString("amt"));
            fat.setText(bundle.getString("fat"));
            quantity.setText(bundle.getString("qty"));
            date.setText(bundle.getString("date"));
            switch (bundle.getString("morEve")) {
                case "Morning":
                    ((RadioButton) radioGroup.findViewById(R.id.radioButtonMor)).setChecked(true);
                    break;
                case "Evening":
                    ((RadioButton) radioGroup.findViewById(R.id.radioButtonEve)).setChecked(true);
                    break;
            }
            switch (bundle.getString("milkType")) {
                case "Cow":
                    ((RadioButton) radioGroupCB.findViewById(R.id.radioButtonCow)).setChecked(true);
                    break;
                case "Buffalo":
                    ((RadioButton) radioGroupCB.findViewById(R.id.radioButtonBuff)).setChecked(true);
                    break;
                default:
                    break;
            }

            RadioButton button;
            if (bundle.getString("memId").equals("0")) {
                button = findViewById(R.id.radioButtonCash);
                button.setChecked(true);
            } else {
                button = findViewById(R.id.radioButtonCredit);
                button.setChecked(true);
                memDetails.setVisibility(View.VISIBLE);
            }

            todayDate.setText(date.getText().toString());
            totAmt.setText(String.valueOf(dbClass.getAmtFromDate(date.getText().toString())));
            totLit.setText(String.valueOf(dbClass.getMilkFromDate(date.getText().toString())));
            saleDetails.setVisibility(View.VISIBLE);

        }

        rate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                float ra = 0, qt = 0;
                if (radioGroupCB.getCheckedRadioButtonId() != -1) {
                    if(!s.toString().equals(""))
                        ra = Float.parseFloat(s.toString());
                    if(!quantity.getText().toString().equals(""))
                        qt = Float.parseFloat(quantity.getText().toString());
                    float a = qt * ra;
                    amt.setText(String.valueOf(a));
                }
//                else {
//                    Toast.makeText(SaleActivity.this, "Please choose values first!", Toast.LENGTH_SHORT).show();
//                }
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

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                float ra = 0, qt = 0;
                if (radioGroupCB.getCheckedRadioButtonId() != -1) {
                    if(!s.toString().equals(""))
                        qt = Float.parseFloat(s.toString());
                    if(!rate.getText().toString().equals(""))
                        ra = Float.parseFloat(rate.getText().toString());
                    float a = qt * ra;
                    amt.setText(String.valueOf(a));
                }
//                else {
//                    Toast.makeText(SaleActivity.this, "Please choose values first!", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (null != rb) {
                    mornEve = rb.getText().toString();
                }

            }
        });



        radioGroupCB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (null != rb) {
                    cowBuff = rb.getText().toString();
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtName.setText("");
                txtCode.setText("");
                rate.setText("");
                amt.setText("");
                fat.setText("");
                quantity.setText("");
                date.setText(R.string.select_date);
                radioGroup.clearCheck();
                radioGroupCB.clearCheck();
                saleDetails.setVisibility(View.GONE);

                cashCredit.clearCheck();
                memDetails.setVisibility(View.GONE);
            }
        });

        cashCredit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonCash:
                        memDetails.setVisibility(View.GONE);
                        break;
                    case R.id.radioButtonCredit:
                        memDetails.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        final DatePickerDialog datePickerDialog;
        Calendar mcurrentDate = Calendar.getInstance();
        final int mYear = mcurrentDate.get(Calendar.YEAR);
        final int mMonth = mcurrentDate.get(Calendar.MONTH);
        final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(SaleActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int m = month + 1;
                String tmp = dayOfMonth + "-" + m + "-" + year;
                date.setText(tmp);
                todayDate.setText(tmp);
                totAmt.setText(String.valueOf(dbClass.getAmtFromDate(tmp)));
                totLit.setText(String.valueOf(dbClass.getMilkFromDate(tmp)));
                saleDetails.setVisibility(View.VISIBLE);

            }
        }, mYear, mMonth, mDay);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String cashCr;
                if (date.getText().toString().equals("Select Date") || quantity.getText().toString().equals("") || radioGroup.getCheckedRadioButtonId() == -1 || cashCredit.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SaleActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
                } else {

                    int memCode = 0;
                    String memName = "Not available";
                    int cashCrId = cashCredit.getCheckedRadioButtonId();
                    RadioButton crDr = findViewById(cashCrId);
                    if (crDr.getText().toString().equals("Cash"))
                        cashCr = "1";
                    else
                        cashCr = "2";

                    if (cashCrId == R.id.radioButtonCredit) {
                        if (!txtCode.getText().toString().equals(""))
                            memCode = Integer.parseInt(txtCode.getText().toString());
                        if (!edtName.getText().toString().equals(""))
                            memName = edtName.getText().toString();
                    }

                    float lit = Float.parseFloat(quantity.getText().toString());
                    float f = 0;
                    if (!fat.getText().toString().equals(""))
                        f = Float.parseFloat(fat.getText().toString());
                    float r = Float.parseFloat(rate.getText().toString());

                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton mE = findViewById(selectedId);

                    if (radioGroupCB.getCheckedRadioButtonId() != -1) {

                        int selected = radioGroupCB.getCheckedRadioButtonId();
                        RadioButton tempCB = findViewById(selected);

                        String cb, me, m = "";
                        if (tempCB.getText().toString().equals("Cow"))
                            cb = "C";
                        else
                            cb = "B";
                        if (mE.getText().toString().equals("Morning"))
                            me = "1";
                        else
                            me = "2";

                        if (!mornEve.equals("")) {
                            if (mornEve.equals("Morning"))
                                m = "1";
                            else
                                m = "2";
                        }

                        float a = RoundUtil.roundTwoDecimals(Float.parseFloat(amt.getText().toString()));

                        int zone = dbQuery.getZoneCode(memCode);

                        if (bundle != null)
                            dbClass.editSale(id, date.getText().toString(), memCode, memName, me, cb, lit, f, r, a, cashCr, zone);
                        else
                            dbClass.addSale(date.getText().toString(), memCode, memName, m, cb, lit, f, r, a, cashCr, zone);
                        Toast.makeText(SaleActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                        edtName.setText("");
                        txtCode.setText("");
                        rate.setText("");
                        fat.setText("");
                        amt.setText("");
                        quantity.setText("");
                        radioGroupCB.clearCheck();
                        //Update day wise details
                        todayDate.setText(date.getText().toString());
                        totAmt.setText(String.valueOf(dbClass.getAmtFromDate(date.getText().toString())));
                        totLit.setText(String.valueOf(dbClass.getMilkFromDate(date.getText().toString())));

                        txtCode.requestFocus();

                        cashCredit.clearCheck();
                        memDetails.setVisibility(View.GONE);
                    }
//                    else {
//                        Toast.makeText(SaleActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
//                    }
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
                    getMemNameFromID(val, zoonCode);
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
                txtCode.setText(member.getCode());

            }
        });
    }

    private void initNames(String zoonCode) {
        Cursor cursor = dbQuery.getAllMembers(zoonCode);
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
        cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_name_list,
                names);

        edtName.setAdapter(adapter);
        edtName.setThreshold(1);
    }

    public void getMemNameFromID(int id, String zoonCode) {
        Cursor c = dbQuery.getMemName(id, zoonCode);
        String name;
        c.moveToFirst();
        edtName.setText("");
        if (c.getCount() > 0) {
            name = c.getString(c.getColumnIndex("memb_name"));
            edtName.setText(name);
        } else {
            Toast.makeText(this, "Member not found!", Toast.LENGTH_SHORT).show();
        }
        c.close();
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
            startActivity(new Intent(this, SaleDetailActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }
}
