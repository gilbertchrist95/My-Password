package com.treblig.mypassword;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class SettingActivity extends Activity{
    DataHandler handler;
    PasswordDbAdapter passwordDbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        String setListItemSetting[] ={"Ubah Password","Hapus Akun"};
        final ListView listItemSetting;
        listItemSetting = (ListView)findViewById(R.id.listView_setting);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, setListItemSetting);
        listItemSetting.setAdapter(adapter);
        listItemSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String)listItemSetting.getItemAtPosition(position);

                // Show Alert
//                Toast.makeText(getApplicationContext(),
//                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
//                        .show();

                if (itemPosition==0)
                {
                    Intent changePassword = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                    startActivity(changePassword);
                }
                else if (itemPosition==1)
                {


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilder.setMessage("Are you sure want to delete this account?");

                    AlertDialog.Builder yes = alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String email = "";

                            handler = new DataHandler(getBaseContext());
                            handler.open();
                            Cursor cursor = handler.returnData();
                            if (cursor.moveToFirst()) {
                                do {
                                    email = cursor.getString(1);
                                }
                                while (cursor.moveToNext());
                            }
                            handler.deleteDataPassword(email);
                            handler.close();

                            passwordDbAdapter = new PasswordDbAdapter(getBaseContext());
                            passwordDbAdapter.open();
                            passwordDbAdapter.onDelete();
                            passwordDbAdapter.close();


                            Toast.makeText(SettingActivity.this, "Account has been deleted !", Toast.LENGTH_SHORT).show();
                            Intent Awal = new Intent(SettingActivity.this, FirstActivity.class);
                            startActivity(Awal);

                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    alertDialogBuilder.create();
                    alertDialogBuilder.show();
                }


            }

        });
    }



}
