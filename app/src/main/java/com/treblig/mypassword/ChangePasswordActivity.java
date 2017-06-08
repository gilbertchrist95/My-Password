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
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends Activity {

    EditText oldPassword,newPassword,confNewPassword;
    Button savePassword;
    TextView notificationError;
    DataHandler handler;
    String passwordDataBase,oldPasswordEditText,newPasswordEditText,confNewPasswordEditText;
    String notifError ="";
    String TAG ="ChangePassworActivity";
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldPassword = (EditText)findViewById(R.id.edit_text_old_password);
        newPassword = (EditText)findViewById(R.id.edit_text_new_password);
        confNewPassword = (EditText)findViewById(R.id.edit_text_conf_new_password);
        savePassword = (Button)findViewById(R.id.button_save_password);
        notificationError = (TextView)findViewById(R.id.text_view_notif_password_error);

        Log.w(TAG, passwordDataBase + " " + oldPasswordEditText);
        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oldPasswordEditText = oldPassword.getText().toString();
                newPasswordEditText = newPassword.getText().toString();
                confNewPasswordEditText = confNewPassword.getText().toString();

                handler = new DataHandler(getBaseContext());
                handler.open();
                Cursor cursor = handler.returnData();
                if (cursor.moveToFirst()) {
                    do {
                        //getName[0] = c.getString(0);
                        passwordDataBase = cursor.getString(2);
                        email = cursor.getString(1);
                    }
                    while (cursor.moveToNext());
                }


                Log.w(TAG, "Password di DB: " + passwordDataBase);
                Log.w(TAG, "Password di input: " + oldPasswordEditText);

                if (isPasswordMatching(passwordDataBase, oldPasswordEditText))
                {
                    if (isPasswordMatching(newPasswordEditText,confNewPasswordEditText))
                    {
                        handler.updateDataPassword(newPasswordEditText,email);
                        Toast.makeText(ChangePasswordActivity.this, "New password are saved!", Toast.LENGTH_SHORT).show();
                        Intent dataList = new Intent(ChangePasswordActivity.this, PasswordListActivity.class);
                        startActivity(dataList);
                    }
                    else
                    {
                        //Toast.makeText(ChangePasswordActivity.this, "New Password and Confirm Password is not the same", Toast.LENGTH_SHORT).show();
                        notifError = notifError.concat("New Password and Confirm Password is not the same ");
                    }
                }
                else {
                    //Toast.makeText(ChangePasswordActivity.this, "Old Password is wrong", Toast.LENGTH_SHORT).show();
                    notifError = notifError.concat("Old Password is wrong ");
                }
                notificationError.setText(notifError);
                handler.close();
            }
        });
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
