package com.kshitijharsh.dairymanagement.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.kshitijharsh.dairymanagement.R;

public class ExportDialog extends Dialog implements View.OnClickListener {

    private Button export, cancel;

    public ExportDialog(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.export_dialog);
        export = findViewById(R.id.btn_export);
        cancel = findViewById(R.id.btn_cancel);
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
