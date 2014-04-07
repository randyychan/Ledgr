package com.example.ledgr.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemPriceUnit;
import com.example.ledgr.dataobjects.ItemState;

public class ItemContentProvider extends ContentProvider {

	public static final String AUTHORITY = "content://com.example.ledgr.provider.Item";
	public static final Uri CONTENT_URI = Uri.parse(AUTHORITY);
	
	public static final String DB_NAME = "items.db";
	public static final int DB_VERSION = 6;
	public static final String TABLE = "items";
	
	public static final String C_ID = BaseColumns._ID;
	public static final String C_TITLE = "title";
	public static final String C_DESCRIPTION = "description";
	public static final String C_PRICE = "price";
	public static final String C_QUALITY = "quality";
	public static final String C_RENTED = "rented";
	public static final String C_STATE = "state";
	public static final String C_PRICE_UNIT = "price_unit";
	public static final String C_OWNER_ID = "owner_id";
	public static final String C_DIRTY = "dirty";
	
	DbHelper dbHelper;
	SQLiteDatabase db;
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbHelper = new DbHelper(getContext());
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		db = dbHelper.getWritableDatabase();
		long id = db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		if (id != -1) {
			uri = Uri.withAppendedPath(uri, Long.toString(id));
		}
	
		return uri;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		// TODO Auto-generated method stub
		db = dbHelper.getWritableDatabase();
		int num = db.delete(TABLE, selection, selectionArgs);
		return num;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE, projection, selection, selectionArgs, null, null, sortOrder);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		db = dbHelper.getWritableDatabase();
		int num = db.update(TABLE, values, selection, selectionArgs);
		return num;
	}
	
	class DbHelper extends SQLiteOpenHelper {
		static final String TAG = "DbHelper";
		
		public DbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = String.format("create table %s " +//TABLE
								"(%s text primary key, " +//C_ID
								"%s text," +//TITLE
								"%s text," +//DESCRIPTION
								"%s real," +//PRICE
								"%s integer," +//RENTED
								"%s integer," +//QUALITY
								"%s integer," +//STATE
								"%s integer," +//PRICE_UNIT
								"%s text," + //OWNER_ID
								"%s integer)", //DIRTY
								TABLE, C_ID, C_TITLE, C_DESCRIPTION, C_PRICE, C_RENTED, C_QUALITY, C_STATE, C_PRICE_UNIT, C_OWNER_ID, C_DIRTY);
					
			db.execSQL(sql);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "onUpgrade from " + oldVersion + " to " + newVersion);
			db.execSQL("drop table if exists " + TABLE);
			onCreate(db);
		}
	}
	
	public static ContentValues itemToValues(Item item) {
		ContentValues values = new ContentValues();
		values.put(C_ID, item.getItem_id());
		values.put(C_TITLE, item.getTitle());
		values.put(C_DESCRIPTION, item.getDescription());
		values.put(C_PRICE, item.getPrice());
		values.put(C_RENTED, item.isRented());
		values.put(C_QUALITY, item.getQuality_rating());
		
		int state = 0;
		if (item.getItem_state() == ItemState.NOT_FOR_RENT) {
			state = 0;
		} else {
			state = 1;
		}
		values.put(C_STATE, state);
		
		int price_unit = 0;
		if (item.getItem_price_unit() == ItemPriceUnit.DAY) {
			price_unit = 0;
		} else if (item.getItem_price_unit() == ItemPriceUnit.WEEK) {
			price_unit = 1;
		} else {
			price_unit = 2;
		}
		
		values.put(C_PRICE_UNIT, price_unit);
		return values;
	}

	public static Item createItemFromCursor(Cursor cursor) {
		String title = cursor.getString(cursor.getColumnIndexOrThrow(ItemContentProvider.C_TITLE));
		String description = cursor.getString(cursor.getColumnIndexOrThrow(ItemContentProvider.C_DESCRIPTION));
		String item_id = cursor.getString(cursor.getColumnIndexOrThrow(ItemContentProvider.C_ID));
		double price = cursor.getDouble(cursor.getColumnIndexOrThrow(ItemContentProvider.C_PRICE));
		int rented = cursor.getInt(cursor.getColumnIndexOrThrow(ItemContentProvider.C_RENTED));
		int quality = cursor.getInt(cursor.getColumnIndexOrThrow(ItemContentProvider.C_QUALITY));
		int state = cursor.getInt(cursor.getColumnIndexOrThrow(ItemContentProvider.C_STATE));
		
		Item item = new Item(title, description);
		item.setItem_id(item_id);
		item.setPrice(price);
		item.setRented(rented == 1);
		item.setQuality_rating(quality);
		item.setItem_state(state == 0 ? ItemState.NOT_FOR_RENT : ItemState.FOR_RENT);
		return item;
	}
	

	
	
}
