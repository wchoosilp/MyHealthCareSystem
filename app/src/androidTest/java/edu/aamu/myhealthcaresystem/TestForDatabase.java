package edu.aamu.myhealthcaresystem;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by WChoosilp-Asus on 8/21/2016.
 */
public class TestForDatabase extends AndroidTestCase {

    private static String mUsername;
    private static String mPassword;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testDropDb() {
        assert(mContext.deleteDatabase(LoginDataBaseAdapter.DATABASE_NAME));
        System.out.println("testDropDb passed!");
        Log.d("TestForDatabase", "testDropDb passed!");
    }

    public void testCreateDb() {
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(mContext , LoginDataBaseAdapter.DATABASE_NAME, null, LoginDataBaseAdapter.DATABASE_VERSION);
        SQLiteDatabase db = dataBaseAdapter.getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
        System.out.println("testCreateDb passed!");
        Log.d("TestForDatabase", "testCreateDb passed!");
    }

    public void testInsertData() {
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(mContext , LoginDataBaseAdapter.DATABASE_NAME, null, LoginDataBaseAdapter.DATABASE_VERSION);
        SQLiteDatabase db = dataBaseAdapter.getWritableDatabase();

        mUsername = "wchoosilp";
        mPassword = "123";

        ContentValues contentValues = new ContentValues();

        contentValues.put("USERNAME", mUsername);
        contentValues.put("PASSWORD",mPassword);

        long mUserDBAssignId = db.insert("LOGIN", null, contentValues);
        assertTrue(mUserDBAssignId != -1);
        System.out.println("testInsertData passed!");
        Log.d("TestForDatabase", "testInsertData passed!");
    }

    public void testIsDataCorrectInDb() {
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(mContext , LoginDataBaseAdapter.DATABASE_NAME, null, LoginDataBaseAdapter.DATABASE_VERSION);
        SQLiteDatabase db = dataBaseAdapter.getWritableDatabase();
        Cursor cursor = db.query("LOGIN", null, null, null, null, null, null);
        assertTrue(cursor.moveToFirst());

        int iUsername = cursor.getColumnIndex("USERNAME");
        int iPassword = cursor.getColumnIndex("PASSWORD");
        String sUsername = cursor.getString(iUsername);
        String sPassword = cursor.getString(iPassword);

        assertEquals(mUsername, sUsername);
        assertEquals(mPassword, sPassword);

        System.out.println("testIsDataCorrectInDB passed!");
        Log.d("TestForDatabase", "testIsDataCorrectInDB passed!");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
