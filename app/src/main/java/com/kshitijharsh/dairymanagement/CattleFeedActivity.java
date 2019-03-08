package com.kshitijharsh.dairymanagement;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.database.DBHelper;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
import com.kshitijharsh.dairymanagement.model.Member;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CattleFeedActivity extends AppCompatActivity {
    EditText date, tran, bal, qty, rate, particulars;
    AutoCompleteTextView edtName;
    TextView amt, txtCode;
    Spinner item;
    Button clear, save;
    DatabaseClass dbClass;
    DBHelper dbHelper;
    DBQuery dbQuery;

    ArrayList<String> names;
    HashMap<String, Member> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle_feed);

        dbQuery = new DBQuery(this);
        dbQuery.open();
        dbHelper = new DBHelper(this);

        dbClass = new DatabaseClass(this);
        date = findViewById(R.id.date);
        edtName = findViewById(R.id.edt_memb_name);
        txtCode = findViewById(R.id.edt_memb_id);
        tran = findViewById(R.id.tran);
        qty = findViewById(R.id.qty);
        rate = findViewById(R.id.rate);
        particulars = findViewById(R.id.particulars);
        amt = findViewById(R.id.amt);
        item = findViewById(R.id.spinnerItem);
        initNames();
        clear = findViewById(R.id.clear);
        save = findViewById(R.id.save);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date.setText("");
                txtCode.setText("");
                edtName.setText("");
                tran.setText("");
                rate.setText("");
                amt.setText("");
                qty.setText("");
                item.setSelected(false);
                particulars.setText("");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (date.getText().toString().equals("") || txtCode.getText().toString().equals("") || tran.getText().toString().equals("") ||  rate.getText().toString().equals("") || amt.getText().toString().equals("") || qty.getText().toString().equals("") || particulars.getText().toString().equals("") || item.getSelectedItem().equals("Select Item")) {
                    Toast.makeText(CattleFeedActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
                } else {
                    int tranNo = Integer.parseInt(tran.getText().toString());
                    int memId = Integer.parseInt(txtCode.getText().toString());
                    float quantity = Float.parseFloat(qty.getText().toString());
                    float r = Float.parseFloat(rate.getText().toString());
                    float a = quantity * r;
                    amt.setText(String.valueOf(a));
                    dbClass.addCattle(date.getText().toString(), tranNo, memId, item.getSelectedItem().toString(), quantity, r, a, particulars.getText().toString());
                    Toast.makeText(CattleFeedActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    date.setText("");
                    txtCode.setText("");
                    edtName.setText("");
                    tran.setText("");
                    rate.setText("");
                    qty.setText("");
                    item.setSelected(false);
                    particulars.setText("");
                }

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
}
