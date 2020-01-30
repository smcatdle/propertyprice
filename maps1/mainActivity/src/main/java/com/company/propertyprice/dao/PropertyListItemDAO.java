

package com.company.propertyprice.dao;

import java.util.ArrayList;
import java.util.List;

import com.company.propertyprice.model.android.PropertyListItem;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class PropertyListItemDAO extends SQLiteOpenHelper {

	private static PropertyListItemDAO instance = null;
	private static SQLiteDatabase dbConnection = null;
	
    public static final int DATABASE_VERSION = 1;
    //public static final String DATABASE_NAME = Environment.getExternalStorageDirectory().getPath() + "/downloads/scanshop/irish_property_v1.db";
    public static final String DATABASE_NAME = "irish_property_v1.db";
    public static final String DATABASE_PATH = "/data/data/com.example.mapdemo/databases/";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS propertylistitem";
    
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS propertylistitem (id INTEGER PRIMARY KEY," +
    																			" propertysaleid INTEGER UNIQUE," +
    																			" json TEXT)";
    																			//" FOREIGN KEY(propertysaleid) REFERENCES propertysale(id))";

    public PropertyListItemDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbConnection = getWritableDatabase();
        
        if (dbConnection != null) {
        	Log.e("PropertyListItemDAO : ", "dbConnection" + "not null");
        }
        
        //onUpgrade(dbConnection, 1, 2);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

	
	// instance method
	public static PropertyListItemDAO getInstance(Context context) {
		
        if (instance == null) {
        	instance = new PropertyListItemDAO(context);
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
     * Update a basketItem
     * 
     * @param basketItem  The basket item to update
     * 
     */
    /*public int onUpdateBasketItem(BasketItem basketItem) {
    	
    	Log.e("BasketItemDAO : ", "onUpdateBaskeItem" + basketItem.getDescription());
    	
    	String[] selectionArgs = {String.valueOf(basketItem.getId()),String.valueOf(basketItem.getBasketId())};
    	ContentValues values = new ContentValues();
    	values.put("description", basketItem.getDescription());
    	values.put("price", basketItem.getPrice());
    	values.put("amount", basketItem.getAmount());
    	values.put("type", basketItem.getType());
    	
    	return dbConnection.update("basketitem", values, "id = ?, basketid=?", selectionArgs);
    	
    }*/
    
    /**
     * Delete a propertyListItem
     * 
     * @param propertyListItem  The Property List item to delete
     * 
     */
    public int onDeletePropertyListItem(PropertyListItem item) {
    	
    	Log.e("PropertyListItemDAO : ", "onDeletePropertyListItem" + item.getJson());
    	
    	String[] selectionArgs = {String.valueOf(item.getId())};
    	
    	return dbConnection.delete("propertyListItem",  "id = ?", selectionArgs);
    	
    }
    
    /**
     * Add a new propertyListItem to the table
     * 
     * @param item  The new propertyListItem to add
     * 
     */
    public long onAddPropertyListItem(PropertyListItem item) {
    	
    	Log.e("PropertyListItemDAO : ", "onAddPropertyListItem" + item.getJson());
    	
    	onCreate(dbConnection);
    	
    	ContentValues values = new ContentValues();
    	values.put("propertysaleid", item.getPropertysaleid());
    	values.put("json", item.getJson());

    	long id = dbConnection.insert("propertyListItem", null, values);
    	
    	item.setId(id);
    	
    	return id;
    }
    

    /**
     * Get the PropertyListItems
     * 
     */
    public List<PropertyListItem> onGetPropertyListItems() {
    	
    	List<PropertyListItem> items = new ArrayList<PropertyListItem>();
    	String[] columns = {"id", "propertysaleid", "json"};
    	String[] selectionArgs = {};
    	
    	Cursor cursor = dbConnection.query("propertylistitem", columns, null , selectionArgs, null, null, "id");
    		
		if  (cursor.moveToFirst()) {
		        do {
		        	Log.e("PropertyListItemDAO : ", "onGetPropertyListItems");
		        	PropertyListItem item = new PropertyListItem();
		        	item.setId(cursor.getInt(cursor.getColumnIndex("id")));
		        	item.setPropertysaleid(cursor.getInt(cursor.getColumnIndex("propertysaleid")));
		        	item.setJson(cursor.getString(cursor.getColumnIndex("json")));
		        	
		        	items.add(item);
		        	
		    } while (cursor.moveToNext());
		}
		
		if(cursor!= null && !cursor.isClosed()) cursor.close();
		 
		return items;
    	
    }


    
    public void onCreate(SQLiteDatabase db) {
    	Log.e("PropertyListItemDAO : ", "onCreate");
        db.execSQL(SQL_CREATE_ENTRIES);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
        onCreate(db);

    }

    public void initialize() {
	onCreate(dbConnection);
    }
    
}
