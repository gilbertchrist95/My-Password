package com.treblig.mypassword;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends Activity {

    TextView textViewNotification;
    EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    Button buttonCreateAccount;
    DataHandler handler;

    private static final  String TAG = "RegistrationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textViewNotification = (TextView)findViewById(R.id.text_view_notif);
        editTextName = (EditText)findViewById(R.id.edit_text_name);
        editTextEmail = (EditText) findViewById(R.id.edit_text_email);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);
        editTextConfirmPassword = (EditText)findViewById(R.id.edit_text_confirm_password);
        buttonCreateAccount = (Button)findViewById(R.id.button_create_account);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int success=0; //if success = 4, then save data
                String report = ""; // if terjadi kesalahan, maka  not success
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password  = editTextPassword.getText().toString();
                String confirmPassword = editTextPassword.getText().toString();

                //cek nama if not empty maka, plus 1
                if (!isEmpty(name))
                {
                    success+=1;
                }
                else
                {
                    Log.w(TAG, "Nama kosong");
                    report = report.concat("Nama belum diisi");
                }

                //cek email jika not empty dan valid, maka plus 1
                if (!isEmpty(email))
                {
                    if (isEmailValid(email))
                    {
                        success+=1;
                    }
                    else
                    {
                        Log.w(TAG,"Email tidak kosong namun masih tidak valid");
                        report = report.concat(" Email tidak valid");
                    }
                }
                else
                {
                    Log.w(TAG,"Email kosong");
                    report = report.concat("Email belum terisi belum diisi");
                }

                if (!isEmpty(password) && !isEmpty(confirmPassword))
                {
                    if (isPasswordMatching(password,confirmPassword))
                    {
                        Log.w(TAG,"Password match dengan confirm password");
                        success+=1;
                    }
                    else if (!isPasswordMatching(password, confirmPassword))
                    {
                        Log.w(TAG,"Password tidak sama dengan confirm Password");
                        report = report.concat(" Password tidak sama dengan confirm Password");
                    }
                }
                else
                {
                    Log.w(TAG,"Password kosong");
                    report = report.concat(" Password belum terisi");
                }

                if (success ==3)
                {
                    handler = new DataHandler(getBaseContext());
                    handler.open();
                    long id = handler.insertData(name,email,password);
                    Toast.makeText(getBaseContext(), "data Inserted", Toast.LENGTH_SHORT).show();
                    handler.close();
                    Intent dataList = new Intent(RegistrationActivity.this, PasswordListActivity.class);
                    startActivity(dataList);

                }
                else if (success <=3)
                {
                    textViewNotification.setText(report);
                }
            }
        });
    }

    private boolean isEmpty(String text) {
        if (text.trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    public boolean isPasswordMatching(String password, String confirmPassword) {
        Pattern pattern = Pattern.compile(password, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(confirmPassword);

        if (!matcher.matches()) {
            // do your Toast("passwords are not matching");

            return false;
        }

        return true;
    }
}


