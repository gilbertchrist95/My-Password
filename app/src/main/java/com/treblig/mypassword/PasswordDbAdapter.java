

package com.treblig.mypassword;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class PasswordDbAdapter {
	public static final String KEY_TITLE="title";
	public static final String KEY_EMAIL="email";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_WEBSITE = "website";
	public static final String KEY_OTHERS = "others";
	public static final String KEY_ROWID = "_id";
	
	private static final String TAG = "PasswordDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	private static final String DATABASE_CREATE = 
			"create table password (_id integer primary key autoincrement, "
			+ "title text not null, email text not null, username text not null, password text not null," +
			"website text not null, others text not null);";
	
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "password";
	private static final int DATABASE_VERSION = 2;
	
	private final Context mCtx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper{

		 DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " 
					+ newVersion + "which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS password");
			onCreate(db);
		}
	}
	
	/**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
	public PasswordDbAdapter (Context ctx)
	{
		this.mCtx=ctx;
	}
	
	/**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
	
	public PasswordDbAdapter open() throws SQLException{
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		mDbHelper.close();
	}

	public void onDelete()
	{
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		mDb.delete(DATABASE_TABLE, null, null);
	}


	/**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * * @param title the title of the note
     * @return rowId or -1 if failed
     */
	
	public long createPassword(String title, String email, String username, String password, String website, String others)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_EMAIL, email);
		initialValues.put(KEY_USERNAME, username);
		initialValues.put(KEY_PASSWORD, password);
		initialValues.put(KEY_WEBSITE, website);
		initialValues.put(KEY_OTHERS, others);
		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
		
	}
	
	/**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
	
	public boolean deletePassword(long rowId)
	{
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	/**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
	
	public Cursor fetchAllPassword(){
		return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_EMAIL, KEY_USERNAME,
				KEY_PASSWORD, KEY_WEBSITE, KEY_OTHERS}, null, null, null, null, null);
		
	}
	
	/**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
	
	public Cursor fetchPassword (long rowId) throws SQLException{
		
		Cursor mCursor = 
				mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_TITLE,KEY_EMAIL,KEY_USERNAME,KEY_PASSWORD,KEY_WEBSITE,KEY_OTHERS}, KEY_ROWID + "=" + rowId ,null, null, null, null, null);
		
		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
		
	}
	
	/**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
	
	public boolean updatePassword(long rowId, String title, String email, String username, String password, String website, String others){
		ContentValues args = new ContentValues();
		
		args.put(KEY_TITLE, title);
		args.put(KEY_EMAIL, email);
		args.put(KEY_USERNAME, username);
		args.put(KEY_PASSWORD, password);
		args.put(KEY_WEBSITE, website);
		args.put(KEY_OTHERS, others);
		
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null)>0;
	}
	
}
