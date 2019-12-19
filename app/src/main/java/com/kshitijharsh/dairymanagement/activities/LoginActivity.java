package com.kshitijharsh.dairymanagement.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.R;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login;

    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAILURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.user_id);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(password.getText())) {
                    Toast.makeText(LoginActivity.this, "Please enter required values", Toast.LENGTH_SHORT).show();
                } else {
//                    int res = checkLogin(username.getText().toString(), password.getText().toString());
//                    if(res == LOGIN_SUCCESS) {
//                        // TODO navigate to mainActivity
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });
    }

//    private int checkLogin(String id, String password) {
//        int res;
//        // TODO database operations
//        // if success, res = LOGIN_SUCCESS
//        // else res = LOGIN_FAILURE
////        return res;
//    }

}
