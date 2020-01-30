package com.company.propertyprice.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.company.propertyprice.model.android.Grid;
import com.company.utils.DateUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GridDAO extends SQLiteOpenHelper {

	private static GridDAO instance = null;
	private static SQLiteDatabase dbConnection = null;

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "irish_property_v1.db";
	public static final String DATABASE_PATH = "/data/data/com.example.mapdemo/databases/";
	public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS grid";

	public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS grid (id INTEGER PRIMARY KEY,"
			+ "gridid INTEGER UNIQUE,"
			+ "json TEXT,"
			+ "update INTEGER,"
			+ "datelastused TEXT,"
			+ "dateupdated TEXT)";

	public GridDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		dbConnection = getWritableDatabase();

		if (dbConnection != null) {
			Log.e("GridDAO : ", "dbConnection" + "not null");
		}

	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	// instance method
	public static GridDAO getInstance(Context context) {

		if (instance == null) {
			instance = new GridDAO(context);
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
	 * Delete grid
	 * 
	 * @param gridId
	 *            The gridId to delete
	 * 
	 */
	public int onDeleteGridItem(int gridId) {

		Log.e("GridDAO : ", "onDeleteGrid" + gridId);

		String[] selectionArgs = { String.valueOf(gridId) };

		return dbConnection.delete("grid", "gridid = ?", selectionArgs);

	}

	/**
	 * Add a new grid to the table
	 * 
	 * @param item
	 *            The new grid to add
	 * 
	 */
	public long onAddGrid(Grid grid) {

		onCreate(dbConnection);

		ContentValues values = new ContentValues();
		values.put("gridid", grid.getId());
		values.put("json", grid.getJson());
		values.put("update", 0);
		values.put("datelastused", DateUtils.getString(grid.getDateLastUsed()));
		values.put("dateupdated", DateUtils.getString(grid.getDateUpdated()));

		long id = dbConnection.insert("grid", null, values);

		return id;
	}

	/**
	 * Get the Grid
	 * 
	 */
	public List<Grid> onGetGrids(int gridId) {

		List<Grid> items = new ArrayList<Grid>();
		String[] columns = { "gridid", "json", "update" };
		String[] selectionArgs = { String.valueOf(gridId) };
		Grid grid = null;

		Cursor cursor = dbConnection.query("grid", columns, "gridid = ?",
				selectionArgs, null, null, "id");

		if (cursor.moveToFirst()) {
			do {
				grid = new Grid();
				Log.e("GridDAO : ", "onGetGrid");
				grid.setJson(cursor.getString(cursor.getColumnIndex("json")));
				/*grid.setUpdate(cursor.getInt(cursor.getColumnIndex("update")) == 0 ? false
						: true);*/

				items.add(grid);

			} while (cursor.moveToNext());
		}

		if (cursor != null && !cursor.isClosed())
			cursor.close();

		items = null;
		columns = null;
		selectionArgs = null;

		return items;

	}

	public void onCreate(SQLiteDatabase db) {
		Log.e("GridDAO : ", "onCreate");
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
