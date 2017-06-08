package com.treblig.mypassword;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class FirstActivity extends Activity {

	int data_block = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);

		final DataHandler handler;
		handler = new DataHandler (getBaseContext());
		handler.open();
		final String[] getName = new String[1];

		Thread timeSplash = new Thread() {
			public void run() {
				try {
					sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {

					Cursor c = handler.returnData();
					if (c.moveToFirst())
					{
						do {
							getName[0] = c.getString(0);
						}
						while(c.moveToNext());
					}

					if (getName[0]!=null)
					{
						handler.close();
						Intent login = new Intent(FirstActivity.this, LoginActivity.class);
						startActivity(login);
					}
					else
					{
						handler.close();
						Intent main = new Intent(FirstActivity.this, RegistrationActivity.class);
						startActivity(main);
					}
				}
			}
		};
		timeSplash.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

}




