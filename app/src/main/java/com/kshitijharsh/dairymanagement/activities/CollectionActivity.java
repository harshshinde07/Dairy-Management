package com.kshitijharsh.dairymanagement.activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class CollectionActivity extends AppCompatActivity {
    DBQuery dbQuery;
    AutoCompleteTextView edtName;
    TextView membType, cowBuf;
    ArrayList<String> names;
    TextView txtCode, rate, amt;
    HashMap<String, Member> members;
    EditText degree, fat, quantity, date;
    Button save, clear;
    float f, q, d, a;
    DBHelper dbHelper;
    DatabaseClass dbClass;
    RadioGroup radioGroup;
    LinearLayout swapCB, swapBoth;
    String cowBuff;
    String[] memb_type = {"Member", "Contractor", "Labour Contractor"};

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
                if (date.getText().toString().equals("") || edtName.getText().toString().equals("") || membType.getText().toString().equals("") || txtCode.getText().toString().equals("") || degree.getText().toString().equals("") || fat.getText().toString().equals("") || quantity.getText().toString().equals("")) {
                    Toast.makeText(CollectionActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
                } else {
                    int memCode = Integer.parseInt(txtCode.getText().toString());
                    float deg = Float.parseFloat(degree.getText().toString());
                    float lit = Float.parseFloat(quantity.getText().toString());
                    float f = Float.parseFloat(fat.getText().toString());

                    if (swapBoth.getVisibility() == View.VISIBLE) {
                        if (cowBuff.equals("Cow"))
                            cowBuf.setText("Cow");
                        if (cowBuff.equals("Buffalo"))
                            cowBuf.setText("Buffalo");
                    }
                    getRateAmt(deg, f, cowBuf.getText().toString());

                    if (!rate.getText().toString().equals("") || !amt.getText().toString().equals("")) {
                        float r = Float.parseFloat(rate.getText().toString());
                        float a = Float.parseFloat(amt.getText().toString());
                        if (!cowBuf.getText().toString().equals("")) {
                            dbClass.addColl(date.getText().toString(), memCode, cowBuf.getText().toString(), deg, lit, f, r, a);
                            Toast.makeText(CollectionActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
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

        edtName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                TextView txtName = (TextView) view;
                Member member = members.get(txtName.getText().toString());
                int type = Integer.parseInt(member.getMembType());
                Log.e("11111111111", String.valueOf(type));
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

                //TODO - A BUG
                if (type == 3)
                    type = 2;
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
                    cursor.getString(3));
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

    public void getRateAmt(float degree, float fat, String cobf) {
        Cursor c = dbQuery.getRate(degree, fat, cobf);
        float val;
        c.moveToFirst();
        if (c.getCount() > 0 && c != null) {
            val = c.getFloat(c.getColumnIndex("rate"));
            //Toast.makeText(this, String.valueOf(val), Toast.LENGTH_SHORT).show();
            rate.setText(String.valueOf(val));
            q = Float.parseFloat(quantity.getText().toString());
            a = q * val;
            amt.setText(String.valueOf(a));
            //float m = dbQuery.getMilkCount();
            //Toast.makeText(this, String.valueOf(m), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Value not found!", Toast.LENGTH_SHORT).show();
        }
    }
}
