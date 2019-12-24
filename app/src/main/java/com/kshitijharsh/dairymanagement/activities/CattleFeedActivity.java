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
import android.widget.Spinner;
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
import java.util.List;
import java.util.Objects;

public class CattleFeedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText qty, rate, particulars;
    AutoCompleteTextView edtName;
    TextView amt, date;
    EditText txtCode;
    Spinner item;
    Button clear, save;
    DatabaseClass dbClass;
    DBHelper dbHelper;
    DBQuery dbQuery;
    String label;
    ArrayList<String> names;
    HashMap<String, Member> members;
    TextView todayDate, totAmt;
    LinearLayout cattleDetails;
    RadioGroup cashCredit;
    LinearLayout memDetails;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle_feed);

        dbQuery = new DBQuery(this);
        dbQuery.open();
        dbHelper = new DBHelper(this);

        Customer c = dbQuery.getCustomerDetails();
        final String zoonCode = c.getBranchCode();

        Objects.requireNonNull(getSupportActionBar()).setTitle("CattleFeed");

        dbClass = new DatabaseClass(this);
        date = findViewById(R.id.date);
        edtName = findViewById(R.id.edt_memb_name);
        txtCode = findViewById(R.id.edt_memb_id);
        qty = findViewById(R.id.qty);
        rate = findViewById(R.id.rate);
        particulars = findViewById(R.id.particulars);
        amt = findViewById(R.id.amt);
        item = findViewById(R.id.spinnerItem);
        initNames(zoonCode);
        clear = findViewById(R.id.clear);
        save = findViewById(R.id.save);

        cashCredit = findViewById(R.id.cash_credit);
        memDetails = findViewById(R.id.member_details_layout);

        item.setOnItemSelectedListener(CattleFeedActivity.this);
        loadItemData();

        cattleDetails = findViewById(R.id.cattle_details);
        todayDate = findViewById(R.id.today_date);
        totAmt = findViewById(R.id.tot_amt);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            date.setText(bundle.getString("date"));
            edtName.setText(bundle.getString("name"));
            txtCode.setText(bundle.getString("memId"));
            qty.setText(bundle.getString("rate"));
            rate.setText(bundle.getString("qty"));
            particulars.setText(bundle.getString("part"));
            amt.setText(bundle.getString("amt"));
            item.setSelection(((ArrayAdapter) item.getAdapter()).getPosition(bundle.getString("itemName")));

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
            totAmt.setText(String.valueOf(dbClass.getCattleAmtFromDate(date.getText().toString())));
            cattleDetails.setVisibility(View.VISIBLE);
        }

        qty.addTextChangedListener(new TextWatcher() {

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
                    if(!s.toString().equals(""))
                        qt = Float.parseFloat(s.toString());
                    if(!rate.getText().toString().equals(""))
                        ra = Float.parseFloat(rate.getText().toString());
                    float a = qt * ra;
                    amt.setText(String.valueOf(a));
            }
        });

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
                if(!s.toString().equals(""))
                    ra = Float.parseFloat(s.toString());
                if(!qty.getText().toString().equals(""))
                    qt = Float.parseFloat(qty.getText().toString());
                float a = qt * ra;
                amt.setText(String.valueOf(a));
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date.setText(R.string.select_date);
                txtCode.setText("");
                edtName.setText("");
//                rate.setText("");
                amt.setText("");
                qty.setText("");
                item.setSelected(false);
                particulars.setText("");
                cattleDetails.setVisibility(View.GONE);

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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p, cashCr;
                if (date.getText().toString().equals("") || rate.getText().toString().equals("") || amt.getText().toString().equals("") || qty.getText().toString().equals("") || item.getSelectedItem().equals("Select Item") || cashCredit.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(CattleFeedActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
                } else {
                    int memId = 0;
                    String memName = "Not available";
                    int cashCrId = cashCredit.getCheckedRadioButtonId();
                    RadioButton crDr = findViewById(cashCrId);
                    if (crDr.getText().toString().equals("Cash"))
                        cashCr = "1";
                    else
                        cashCr = "2";

                    if (cashCrId == R.id.radioButtonCredit) {
                        if (!txtCode.getText().toString().equals(""))
                            memId = Integer.parseInt(txtCode.getText().toString());
                        if (!edtName.getText().toString().equals(""))
                            memName = edtName.getText().toString();
                    }
                    float quantity = Float.parseFloat(qty.getText().toString());
                    float r = Float.parseFloat(rate.getText().toString());
                    float a = quantity * r;
                    amt.setText(String.valueOf(a));
                    if (particulars.getText().toString().equals(""))
                        p = "None";
                    else
                        p = particulars.getText().toString();

                    a = RoundUtil.roundTwoDecimals(a);

                    if (bundle != null) {
                        dbClass.editCattle(id, date.getText().toString(), memId, memName, label, quantity, r, a, p, cashCr);
                    } else {
                        dbClass.addCattle(date.getText().toString(), memId, memName, label, quantity, r, a, p, cashCr);
                        Toast.makeText(CattleFeedActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                    }
//                    date.setText(R.string.select_date);
                    txtCode.setText("");
                    edtName.setText("");
                    rate.setText("");
                    qty.setText("");
                    item.setSelected(false);
                    particulars.setText("");
                    //Update day wise details
                    todayDate.setText(date.getText().toString());
                    totAmt.setText(String.valueOf(dbClass.getCattleAmtFromDate(date.getText().toString())));

                    txtCode.requestFocus();

                    cashCredit.clearCheck();
                    memDetails.setVisibility(View.GONE);
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

        final DatePickerDialog datePickerDialog;
        Calendar mcurrentDate = Calendar.getInstance();
        final int mYear = mcurrentDate.get(Calendar.YEAR);
        final int mMonth = mcurrentDate.get(Calendar.MONTH);
        final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(CattleFeedActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int m = month + 1;
                String tmp = dayOfMonth + "-" + m + "-" + year;
                date.setText(tmp);
                todayDate.setText(tmp);
                totAmt.setText(String.valueOf(dbClass.getCattleAmtFromDate(tmp)));
                cattleDetails.setVisibility(View.VISIBLE);

            }
        }, mYear, mMonth, mDay);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
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

    private void loadItemData() {
        // database handler
        DBQuery db = new DBQuery(getApplicationContext());

        // Spinner Drop down elements
        List<String> labels = db.getAllItems();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        item.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        label = adapterView.getItemAtPosition(i).toString();
        getSelectedItemRate(label);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getSelectedItemRate(String item) {
        Cursor c = dbQuery.getItemRate(item);
        float val;
        c.moveToFirst();
        if (c.getCount() > 0) {
            val = c.getFloat(c.getColumnIndex("rate"));
            rate.setText(String.valueOf(val));
        } else {
            Toast.makeText(this, "Value not found!", Toast.LENGTH_SHORT).show();
        }
        c.close();
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
            startActivity(new Intent(this, CattleDetailActivity.class));
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
