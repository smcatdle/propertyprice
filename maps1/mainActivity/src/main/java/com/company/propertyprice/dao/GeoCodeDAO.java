package com.company.propertyprice.dao;

import java.util.ArrayList;
import java.util.List;

import com.company.propertyprice.model.GeoCode;
import com.company.utils.DateUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GeoCodeDAO extends SQLiteOpenHelper {

    private static GeoCodeDAO instance = null;
    private static SQLiteDatabase dbConnection = null;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "irish_property_v1.db";
    public static final String DATABASE_PATH = "/data/data/com.example.mapdemo/databases/";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS geocode";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS geocode (id INTEGER PRIMARY KEY,"
	    + "geocodeid INTEGER UNIQUE,"
	    + "gridid INTEGER,"
	    + "latitude REAL,"
	    + "longitude REAL,"
	    + "formataddrr TEXT,"
	    + "status TEXT,"
	    + "type TEXT,"
	    + "locationtype TEXT,"
	    + "partialmatch TEXT,"
	    + "results INTEGER,"
	    + "datecreated TEXT,"
	    + "dateupdated TEXT)";

    // " FOREIGN KEY(addressid) REFERENCES address(id))";

    public GeoCodeDAO(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	dbConnection = getWritableDatabase();

	if (dbConnection != null) {
	    Log.e("GeoCodeDAO : ", "dbConnection" + "not null");
	}
	// dbConnection.execSQL(SQL_DELETE_ENTRIES);
	// onUpgrade(dbConnection, 1, 2);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	onUpgrade(db, oldVersion, newVersion);
    }

    // instance method
    public static GeoCodeDAO getInstance(Context context) {

	if (instance == null) {
	    instance = new GeoCodeDAO(context);
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
     * Delete geocode inside a grid
     * 
     * @param gridId
     *            The gridId to delete
     * 
     */
    public int onDeleteGeoCodeItem(int gridId) {

	Log.e("GeoCodeDAO : ", "onDeleteGeoCode" + gridId);

	String[] selectionArgs = { String.valueOf(gridId) };

	return dbConnection.delete("geocode", "gridid = ?", selectionArgs);

    }

    /**
     * Add a new geocode to the table
     * 
     * @param item
     *            The new geocode to add
     * 
     */
    public long onAddGeoCode(GeoCode geocode, int gridId) {

	onCreate(dbConnection);

	ContentValues values = new ContentValues();
	values.put("geocodeid", geocode.getId());
	values.put("gridid", gridId);
	values.put("latitude", geocode.getLatitude());
	values.put("longitude", geocode.getLongitude());
	values.put("formataddrr", geocode.getFormattedAddress());
	values.put("status", geocode.getStatus());
	values.put("type", geocode.getType());
	values.put("locationtype", geocode.getLocationType());
	values.put("partialmatch", geocode.isPartialMatch() ? "T" : "F");
	values.put("results", geocode.getResults());
	values.put("datecreated", DateUtils.getString(geocode.getDateCreated()));
	values.put("dateupdated", DateUtils.getString(geocode.getDateUpdated()));

	long id = dbConnection.insert("geocode", null, values);

	return id;
    }

    /**
     * Get the GeoCodes
     * 
     */
    public List<GeoCode> onGetGeoCodes(int gridId) {

	List<GeoCode> items = new ArrayList<GeoCode>();
	String[] columns = { "id", "geocodeid", "gridid", "latitude",
		"longitude", "formataddrr", "status", "type", "locationtype",
		"partialmatch", "results", "datecreated", "dateupdated" };
	String[] selectionArgs = { String.valueOf(gridId) };

	Cursor cursor = dbConnection.query("geocode", columns, "gridid = ?",
		selectionArgs, null, null, "id");

	if (cursor.moveToFirst()) {
	    do {
		Log.e("GeoCodeDAO : ", "onGetGeoCodes");
		GeoCode item = new GeoCode();
		item.setId(cursor.getInt(cursor.getColumnIndex("geocodeid")));
		item.setLatitude(cursor.getFloat(cursor
			.getColumnIndex("latitude")));
		item.setLongitude(cursor.getFloat(cursor
			.getColumnIndex("longitude")));
		item.setFormattedAddress(cursor.getString(cursor
			.getColumnIndex("formataddrr")));
		item.setStatus(cursor.getString(cursor.getColumnIndex("status")));
		item.setType(cursor.getString(cursor.getColumnIndex("type")));
		item.setLocationType(cursor.getString(cursor
			.getColumnIndex("locationtype")));
		item.setPartialMatch(cursor.getString(
			cursor.getColumnIndex("partialmatch")).equals("T") ? true
			: false);
		item.setResults(cursor.getInt(cursor.getColumnIndex("results")));

		int dateCreated = cursor.getColumnIndex("datecreated");
		if (dateCreated != 0) {
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
	Log.e("GeoCodeDAO : ", "onCreate");
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
