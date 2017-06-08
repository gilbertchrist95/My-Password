package com.treblig.mypassword;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

    TextView textViewNotifPassword;
    EditText editTextPassword;
    Button buttonLogin;
    String passwordEditText,passwordDataBase;
    DataHandler handler;
    private static final  String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // /inisialisasi
        textViewNotifPassword = (TextView) findViewById(R.id.text_view_notif_password);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);
        buttonLogin = (Button)findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //ambil data dari layout
                passwordEditText = editTextPassword.getText().toString();
                String report = "";
                //ambild data dari database DataHandler

                handler = new DataHandler(getBaseContext());
                handler.open();
                Cursor cursor = handler.returnData();
                if (cursor.moveToFirst()) {
                    do {
                        //getName[0] = c.getString(0);
                        passwordDataBase = cursor.getString(2);
                    }
                    while (cursor.moveToNext());
                }

                Log.w(TAG, "password yang diinsert: " + passwordEditText);
                Log.w(TAG, "password yang didatabase: " + passwordDataBase);
                if (passwordEditText.equals(passwordDataBase)) {

                    Intent dataList = new Intent(LoginActivity.this, PasswordListActivity.class);
                    startActivity(dataList);
                } else {

                    report = report.concat(" Password is invalid");
                    textViewNotifPassword.setText("Password invalid");
                }

            }
        });

    }

}
