package com.company.propertyprice.dao;

import java.util.ArrayList;
import java.util.List;

import com.company.propertyprice.model.PropertySale;
import com.company.utils.DateUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PropertySaleDAO extends SQLiteOpenHelper {

    private static PropertySaleDAO instance = null;
    private static SQLiteDatabase dbConnection = null;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "irish_property_v1.db";
    public static final String DATABASE_PATH = "/data/data/com.example.mapdemo/databases/";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS propertysale";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS propertysale (id INTEGER PRIMARY KEY,"
	    + "propertyid INTEGER UNIQUE,"
	    + "addressid INTEGER,"
	    + "gridid INTEGER,"
	    + "price REAL,"
	    + "dateofsale TEXT,"
	    + "fullmarketprice TEXT,"
	    + "vatexclusive TEXT,"
	    + "propertysize TEXT,"
	    + "description TEXT,"
	    + "pprurl TEXT,"
	    + "datecreated TEXT," + "dateupdated TEXT)";

    // " FOREIGN KEY(addressid) REFERENCES address(id))";

    public PropertySaleDAO(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	dbConnection = getWritableDatabase();

	if (dbConnection != null) {
	    Log.e("PropertySaleDAO : ", "dbConnection" + "not null");
	}

	// dbConnection.execSQL(SQL_DELETE_ENTRIES);
	// onUpgrade(dbConnection, 1, 2);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	onUpgrade(db, oldVersion, newVersion);
    }

    // instance method
    public static PropertySaleDAO getInstance(Context context) {

	if (instance == null) {
	    instance = new PropertySaleDAO(context);
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
     * Delete propertysales inside a grid
     * 
     * @param gridId
     *            The gridId to delete
     * 
     */
    public int onDeletePropertySaleItem(int gridId) {

	Log.e("PropertySaleDAO : ", "onDeletePropertySale" + gridId);

	String[] selectionArgs = { String.valueOf(gridId) };

	return dbConnection.delete("propertysale", "gridid = ?", selectionArgs);

    }

    /**
     * Add a new propertysale to the table
     * 
     * @param item
     *            The new propertysale to add
     * 
     */
    public long onAddPropertySale(PropertySale sale, int gridId) {

	onCreate(dbConnection);

	ContentValues values = new ContentValues();
	values.put("propertyid", sale.getId());
	values.put("addressid", sale.getAddress().getId());
	values.put("gridid", gridId);
	values.put("price", sale.getPrice());
	values.put("dateofsale", DateUtils.getString(sale.getDateOfSale()));
	values.put("fullmarketprice", sale.isFullMarketPrice() ? "T" : "F");
	values.put("vatexclusive", sale.isVatExclusive() ? "T" : "F");
	values.put("propertysize", sale.getPropertySize());
	values.put("description", sale.getDescription());
	values.put("pprurl", sale.getPprUrl());
	values.put("datecreated", DateUtils.getString(sale.getDateCreated()));
	values.put("dateupdated", DateUtils.getString(sale.getDateUpdated()));

	long id = dbConnection.insert("propertysale", null, values);

	return id;
    }

    /**
     * Get the PropertySales
     * 
     */
    public List<PropertySale> onGetPropertySales(int gridId) {

	List<PropertySale> items = new ArrayList<PropertySale>();
	String[] columns = { "id", "propertyid", "price", "dateofsale",
		"fullmarketprice", "vatexclusive", "propertysize",
		"description", "pprurl", "datecreated", "dateupdated" };
	String[] selectionArgs = { String.valueOf(gridId) };

	Cursor cursor = dbConnection.query("propertysale", columns,
		"gridid = ?", selectionArgs, null, null, "id");

	if (cursor.moveToFirst()) {
	    do {
		Log.e("PropertySaleDAO : ", "onGetPropertySales");
		PropertySale item = new PropertySale();
		item.setId(cursor.getInt(cursor.getColumnIndex("propertyid")));
		item.setPrice(cursor.getFloat(cursor.getColumnIndex("price")));
		item.setDateOfSale(DateUtils.getDate(cursor.getString(cursor
			.getColumnIndex("dateofsale"))));
		item.setFullMarketPrice(cursor.getString(
			cursor.getColumnIndex("fullmarketprice")).equals("T") ? true
			: false);
		item.setVatExclusive(cursor.getString(
			cursor.getColumnIndex("vatexclusive")).equals("T") ? true
			: false);
		item.setPropertySize(cursor.getString(cursor
			.getColumnIndex("propertysize")));
		item.setDescription(cursor.getString(cursor
			.getColumnIndex("description")));
		item.setPprUrl(cursor.getString(cursor.getColumnIndex("pprurl")));
		item.setDateCreated(DateUtils.getDate(cursor.getString(cursor
			.getColumnIndex("datecreated"))));
		item.setDateUpdated(DateUtils.getDate(cursor.getString(cursor
			.getColumnIndex("dateupdated"))));

		items.add(item);

	    } while (cursor.moveToNext());
	}

	if (cursor != null && !cursor.isClosed())
	    cursor.close();

	return items;

    }

    public void onCreate(SQLiteDatabase db) {
	Log.e("PropertySaleDAO : ", "onCreate");
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
