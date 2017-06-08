package com.treblig.mypassword;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class PasswordListActivity extends ListActivity{
	
	
	
	
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private PasswordDbAdapter mDbHelper;
    private Cursor mPasswordCursor;
	
 
    
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_list);
		 
		mDbHelper = new PasswordDbAdapter(this);
		mDbHelper.open();
		fillData();
		registerForContextMenu(getListView());
	}
	
	
	
	private void fillData()
	{
		mPasswordCursor =  mDbHelper.fetchAllPassword();
		startManagingCursor(mPasswordCursor);
		
		// Create an array to specify the fields we want to display in the list (only TITLE)
				String[] from = new String[]{PasswordDbAdapter.KEY_TITLE};
					
				// and an array of the fields we want to bind those fields to (in this case just text1)
				int[] to = new int[]{R.id.Password_row};
				
				// Now create a simple cursor adapter and set it to display
				SimpleCursorAdapter Password = 
						new SimpleCursorAdapter(this, R.layout.activity_password_row, mPasswordCursor, from, to);
				setListAdapter(Password);
	}
	
	private void createPassword()
	{
		Intent i = new Intent (this, PasswordEditActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	        super.onActivityResult(requestCode, resultCode, intent);
	        fillData();
	         
	    }
	 
	 @Override
	    protected void onListItemClick(ListView l, View v, int position, long id) {
	        super.onListItemClick(l, v, position, id);
	        Intent i = new Intent(this, PasswordEditActivity.class);
	        i.putExtra(PasswordDbAdapter.KEY_ROWID, id);
	        startActivityForResult(i, ACTIVITY_EDIT);
	    }


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}



	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			mDbHelper.deletePassword(info.id);
			fillData();
			return true;
			
		}
		return super.onContextItemSelected(item);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.password_list, menu);
		return true;
	}

	private void setSetting()
	{
		Intent setting = new Intent (this, SettingActivity.class);
		startActivityForResult(setting, ACTIVITY_CREATE);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_new:
			createPassword();
		break;
		case R.id.action_settings:
			setSetting();
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
}