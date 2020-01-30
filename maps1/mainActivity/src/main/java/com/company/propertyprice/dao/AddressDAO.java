package com.company.propertyprice.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.company.propertyprice.model.Address;
import com.company.utils.DateUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AddressDAO extends SQLiteOpenHelper {

    private static AddressDAO instance = null;
    private static SQLiteDatabase dbConnection = null;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "irish_property_v1.db";
    public static final String DATABASE_PATH = "/data/data/com.example.mapdemo/databases/";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS address";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS address (id INTEGER PRIMARY KEY,"
	    + "addressid INTEGER UNIQUE,"
	    + "geocodeid INTEGER,"
	    + "gridid INTEGER,"
	    + "addressline1 TEXT,"
	    + "addressline2 TEXT,"
	    + "addressline3 TEXT,"
	    + "addressline4 TEXT,"
	    + "addressline5 TEXT," + "datecreated TEXT," + "dateupdated TEXT)";

    // " FOREIGN KEY(addressid) REFERENCES address(id))";

    public AddressDAO(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	dbConnection = getWritableDatabase();

	if (dbConnection != null) {
	    Log.e("AddressDAO : ", "dbConnection" + "not null");
	}
	// dbConnection.execSQL(SQL_DELETE_ENTRIES);

	//onUpgrade(dbConnection, 1, 2);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	onUpgrade(db, oldVersion, newVersion);
    }

    // instance method
    public static AddressDAO getInstance(Context context) {

	if (instance == null) {
	    instance = new AddressDAO(context);
	}

	return instance;
    }

    public void checkDatabaseOpen() {

	// check the database is open
	if (!dbConnection.isOpen()) {
	    dbConnection = getWritableDatabase();
	}

    }

    /**
     * Delete address inside a grid
     * 
     * @param gridId
     *            The gridId to delete
     * 
     */
    public int onDeleteAddressItem(int gridId) {

	Log.e("AddressDAO : ", "onDeleteAddress" + gridId);

	String[] selectionArgs = { String.valueOf(gridId) };

	return dbConnection.delete("address", "gridid = ?", selectionArgs);

    }

    /**
     * Add a new address to the table
     * 
     * @param item
     *            The new address to add
     * 
     */
    public long onAddAddress(Address address, int gridId) {

	Log.e("AddressDAO : ", "onAddAddress" + address.getAddressLine1());

	onCreate(dbConnection);

	ContentValues values = new ContentValues();
	values.put("addressid", address.getId());
	values.put("geocodeid", address.getGeocode().getId());
	values.put("gridid", gridId);
	values.put("addressline1", address.getAddressLine1());
	values.put("addressline2", address.getAddressLine2());
	values.put("addressline3", address.getAddressLine3());
	values.put("addressline4", address.getAddressLine4());
	values.put("addressline5", address.getAddressLine5());

	Date dateCreated = address.getDateCreated();

	if (dateCreated != null) {
	    values.put("datecreated",
		    DateUtils.getString(address.getDateCreated()));
	    values.put("dateupdated",
		    DateUtils.getString(address.getDateUpdated()));
	}

	long id = dbConnection.insert("address", null, values);

	return id;
    }

    /**
     * Get the Addresss
     * 
     */
    public List<Address> onGetAddresss(int gridId) {

	List<Address> items = new ArrayList<Address>();
	String[] columns = { "id", "addressid", "geocodeid", "addressline1",
		"addressline2", "addressline3", "addressline4", "addressline5",
		"datecreated", "dateupdated" };
	String[] selectionArgs = { String.valueOf(gridId) };

	Cursor cursor = dbConnection.query("address", columns, "gridid = ?",
		selectionArgs, null, null, "id");

	if (cursor.moveToFirst()) {
	    do {
		Log.e("AddressDAO : ", "onGetAddresss");
		Address item = new Address();
		item.setId(cursor.getInt(cursor.getColumnIndex("addressid")));
		// address?
		item.setAddressLine1(cursor.getString(cursor
			.getColumnIndex("addressline1")));
		item.setAddressLine2(cursor.getString(cursor
			.getColumnIndex("addressline2")));
		item.setAddressLine3(cursor.getString(cursor
			.getColumnIndex("addressline3")));
		item.setAddressLine4(cursor.getString(cursor
			.getColumnIndex("addressline4")));
		item.setAddressLine5(cursor.getString(cursor
			.getColumnIndex("addressline5")));
		String date = cursor.getString(cursor
			.getColumnIndex("datecreated"));

		if (date != null) {
		    item.setDateCreated(DateUtils.getDate(cursor
			    .getString(cursor.getColumnIndex("datecreated"))));
		    item.setDateUpdated(DateUtils.getDate(cursor
			    .getString(cursor.getColumnIndex("dateupdated"))));
		}

		items.add(item);

	    } while (cursor.moveToNext());
	}

	if (cursor != null && !cursor.isClosed())
	    cursor.close();

	return items;

    }

    public void onCreate(SQLiteDatabase db) {
	Log.e("AddressDAO : ", "onCreate");
	db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	db.execSQL(SQL_DELETE_ENTRIES);
	onCreate(db);

    }

    public void initialize() {
	onCreate(dbConnection);
    }
}
