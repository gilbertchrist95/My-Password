package com.treblig.mypassword;



import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordEditActivity extends Activity {

	private EditText mtitle,memail,musername,mpassword,mwebsite,mothers;
	private Long mRowId;
	private Cursor Password;
	private PasswordDbAdapter mDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_edit);
		
		mtitle = (EditText) findViewById(R.id.title_editText);
		memail = (EditText) findViewById(R.id.email_editText);
		musername = (EditText) findViewById(R.id.username_editText);
		mpassword = (EditText) findViewById(R.id.password_editText);
		mwebsite = (EditText) findViewById(R.id.web_editText);
		mothers = (EditText) findViewById(R.id.notes_editText);
		
		mDbHelper =  new PasswordDbAdapter(this);
		mDbHelper.open();
		
		mRowId = (savedInstanceState == null) ? null:
		(Long) savedInstanceState.getSerializable(PasswordDbAdapter.KEY_ROWID);
		
		if (mRowId == null) {
       	 Bundle extras = getIntent().getExtras();
           mRowId = extras != null ? extras.getLong(PasswordDbAdapter.KEY_ROWID)
           		: null ;

       }
		
		populateFields();
	}

	
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(PasswordDbAdapter.KEY_ROWID, mRowId);
	}




	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		saveState();
	}




	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		populateFields();
	}




	private void populateFields(){
		if (mRowId != null)
		{
			Cursor Password = mDbHelper.fetchPassword(mRowId);
			startManagingCursor(Password);
			
			mtitle.setText(Password.getString(Password.getColumnIndexOrThrow(PasswordDbAdapter.KEY_TITLE)));
			memail.setText(Password.getString(Password.getColumnIndexOrThrow(PasswordDbAdapter.KEY_EMAIL)));
			musername.setText(Password.getString(Password.getColumnIndexOrThrow(PasswordDbAdapter.KEY_USERNAME)));
			mpassword.setText(Password.getString(Password.getColumnIndexOrThrow(PasswordDbAdapter.KEY_PASSWORD)));
			mwebsite.setText(Password.getString(Password.getColumnIndexOrThrow(PasswordDbAdapter.KEY_WEBSITE)));
			mothers.setText(Password.getString(Password.getColumnIndexOrThrow(PasswordDbAdapter.KEY_OTHERS)));
		}
	}
	
	private void saveState(){
		String title = mtitle.getText().toString();
		String email = memail.getText().toString();
		String username = musername.getText().toString();
		String password = mpassword.getText().toString();
		String website = mwebsite.getText().toString();
		String others =  mothers.getText().toString();
		
		if (title != null)
		{
			if (mRowId == null)
			{
				long id = mDbHelper.createPassword(title, email, username, password, website, others);
				if (id>0)
				{
					mRowId = id;
				}
			}
			else
			{
				mDbHelper.updatePassword(mRowId, title, email, username, password, website, others);
				
			}
		}
		else
		{
			Toast.makeText(getBaseContext(), "Title is null", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.password_edit, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
				saveState();
				finish();
			return true;
		
		case R.id.cancel:
				//Password.close();
//			Intent i = new Intent (this, PasswordEditActivity.class);
//			startActivityForResult(i, ACTIVITY_CREATE);
			if (getTitle().equals(""))
			{AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			mDbHelper.deletePassword(info.id);}
			Password.close();
			Intent kembali = new Intent(this, PasswordListActivity.class);
			startActivity(kembali);
//				finish();
			return true;
		default:
	    	return super.onOptionsItemSelected(item);
		
		
		
		}
	}

}
