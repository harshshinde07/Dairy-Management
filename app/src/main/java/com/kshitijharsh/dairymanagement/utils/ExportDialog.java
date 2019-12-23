package com.kshitijharsh.dairymanagement.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.kshitijharsh.dairymanagement.R;

import java.util.Calendar;

public class ExportDialog extends Dialog implements View.OnClickListener {

    private Button export, cancel;
    private TextView dateFrom, dateTo;
    private Context ctx;

    public ExportDialog(Context context) {
        super(context);
        ctx = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.export_dialog);
        export = findViewById(R.id.btn_export);
        cancel = findViewById(R.id.btn_cancel);

        dateFrom = findViewById(R.id.export_from);
        dateTo = findViewById(R.id.export_to);

        // dateFrom DatePickerDialog
        final DatePickerDialog datePickerDialogFrom;
        Calendar dateStart = Calendar.getInstance();
        final int startYear = dateStart.get(Calendar.YEAR);
        final int startMonth = dateStart.get(Calendar.MONTH);
        final int startDay = dateStart.get(Calendar.DAY_OF_MONTH);
        datePickerDialogFrom = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int m = month + 1;
                String tmp = dayOfMonth + "-" + m + "-" + year;
                dateFrom.setText(tmp);
            }
        }, startYear, startMonth, startDay);

        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialogFrom.show();
            }
        });

        // dateTo DatePickerDialog
        final DatePickerDialog datePickerDialogTo;
        Calendar dateEnd = Calendar.getInstance();
        final int endYear = dateEnd.get(Calendar.YEAR);
        final int endMonth = dateEnd.get(Calendar.MONTH);
        final int endDay = dateEnd.get(Calendar.DAY_OF_MONTH);
        datePickerDialogTo = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int m = month + 1;
                String tmp = dayOfMonth + "-" + m + "-" + year;
                dateTo.setText(tmp);
            }
        }, endYear, endMonth, endDay);

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialogTo.show();
            }
        });


        export.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_export:
                // TODO initiate export with from and to dates
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();

    }
}

//HOW to USE?
//ExportDialog cdd=new ExportDialog(this);
//cdd.show();
