package com.example.ledgr.contentprovider;

import org.joda.time.LocalDate;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemPriceUnit;
import com.example.ledgr.dataobjects.ItemState;
import com.example.ledgr.dataobjects.ItemsData;
import com.example.ledgr.dataobjects.Rental;
import com.example.ledgr.dataobjects.User;

public class RentalContentProvider extends ContentProvider {

	public static final String AUTHORITY = "content://com.example.ledgr.provider.Rental";
	public static final Uri CONTENT_URI = Uri.parse(AUTHORITY);
	
	public static final String DB_NAME = "rental.db";
	public static final int DB_VERSION = 5;
	
	public static final String TABLE = "rentals";
	public static final String C_ID = BaseColumns._ID;
	public static final String C_ITEMID = "item_id";
	public static final String C_NAME = "name";
	public static final String C_DESCRIPTION = "description";
	public static final String C_PRICE = "price";
	public static final String C_QUALITY = "quality";
	public static final String C_PRICE_UNIT = "price_unit";
	public static final String C_RENTED_OUT_OR_RENTING = "rented_renting";
	public static final String C_ALARMID = "alarm_id";
	public static final String C_RENTER_ID = "renter_id";
	public static final String C_RENTER_NAME = "renter_name";
	public static final String C_OWNER_ID = "owner_id";
	public static final String C_OWNER_NAME = "owner_name";
	public static final String C_DUEDAY = "due_date_day";
	public static final String C_DUEMONTH = "due_date_month";
	public static final String C_DUEYEAR = "due_date_year";
	public static final String C_PENDING_RENTAL = "pending_rental";
	public static final String C_PICTURE_COUNT = "picture_count";
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
								"%s text," +//C_ITEM_ID
								"%s text," +//TITLE
								"%s text," +//DESCRIPTION
								"%s real," +//PRICE
								"%s integer," +//QUALITY
								"%s integer, " +//PRICE_UNIT
								"%s integer," +//ALARM_ID
								"%s text," +//USERID
								"%s text," +//USERNAME
								"%s text," +//OWNERID
								"%s text," +//ownername
								"%s integer," +//DUEDAY
								"%s integer," +//DUEMONTH
								"%s integer,"+//DUEYEAR
								"%s integer,"+//DIRTY
								"%s integer,"+//PENDING_RENTAL
								"%s integer,"+
								"%s integer)",//rented/rentedout
								TABLE, C_ID, C_ITEMID, C_NAME, C_DESCRIPTION, C_PRICE, C_QUALITY, 
								C_PRICE_UNIT, C_ALARMID, C_RENTER_ID, C_RENTER_NAME, C_OWNER_ID,
								C_OWNER_NAME, C_DUEDAY, C_DUEMONTH, C_DUEYEAR, C_DIRTY, C_PENDING_RENTAL, C_RENTED_OUT_OR_RENTING, C_PICTURE_COUNT);			
			db.execSQL(sql);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "onUpgrade from " + oldVersion + " to " + newVersion);
			db.execSQL("drop table if exists " + TABLE);
			onCreate(db);
		}
	}
	
	public static ContentValues rentalToValues(Rental rental) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(C_NAME, rental.getTitle());
		contentValues.put(C_ITEMID, rental.getItem_id());
		contentValues.put(C_DESCRIPTION, rental.getDescription());
		contentValues.put(C_PRICE, rental.getPrice());
		contentValues.put(C_QUALITY, rental.getQuality());

		int price_unit = 0;
		if (rental.getItem_price_unit() == ItemPriceUnit.DAY) {
			price_unit = 0;
		} else if (rental.getItem_price_unit() == ItemPriceUnit.WEEK) {
			price_unit = 1;
		} else {
			price_unit = 2;
		}
		
		contentValues.put(C_PRICE_UNIT, price_unit);
		
		contentValues.put(C_ALARMID, rental.getRental_alarm_id());
		contentValues.put(C_RENTER_ID, rental.getRenter().getFacebookId());
		contentValues.put(C_RENTER_NAME, rental.getRenter().getFirstName());
		contentValues.put(C_OWNER_ID, rental.getOwner().getFacebookId());
		contentValues.put(C_OWNER_NAME, rental.getOwner().getFirstName());
		contentValues.put(C_DUEDAY, rental.getDueDate().getDayOfMonth());
		contentValues.put(C_DUEMONTH, rental.getDueDate().getMonthOfYear());
		contentValues.put(C_DUEYEAR, rental.getDueDate().getYear());
		contentValues.put(C_PICTURE_COUNT, rental.getPicture_count());
		
		return contentValues;
		
	}
	
	public static Rental cursorToRental(Cursor cursor) {
		String item_title = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_NAME));
		String item_description = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_DESCRIPTION));
		double item_price = cursor.getDouble(cursor.getColumnIndexOrThrow(RentalContentProvider.C_PRICE));
		int item_quality = cursor.getInt(cursor.getColumnIndexOrThrow(RentalContentProvider.C_QUALITY));
		String item_id = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_ITEMID));
		String rental_id = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_ID));
		int price_unit = cursor.getInt(cursor.getColumnIndexOrThrow(RentalContentProvider.C_PRICE_UNIT));
		int picture_count = cursor.getInt(cursor.getColumnIndexOrThrow(RentalContentProvider.C_PICTURE_COUNT));
		Item item = new Item(item_title, item_description);
		item.setPrice(item_price);
		item.setQuality_rating(item_quality);
		item.setItem_id(item_id);
		
		ItemPriceUnit unit;
		if (price_unit == 0) {
			unit = ItemPriceUnit.DAY;
		} else if (price_unit == 1) {
			unit = ItemPriceUnit.WEEK;
		} else {
			unit = ItemPriceUnit.MONTH;
		}
		
		item.setItem_price_unit(unit);

		
		int day = cursor.getInt(cursor.getColumnIndexOrThrow(RentalContentProvider.C_DUEDAY));
		int month = cursor.getInt(cursor.getColumnIndexOrThrow(RentalContentProvider.C_DUEMONTH));
		int year = cursor.getInt(cursor.getColumnIndexOrThrow(RentalContentProvider.C_DUEYEAR));
		LocalDate dueDate = new LocalDate(year, month, day);
		
		String renterid = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_RENTER_ID));
		String rentername = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_RENTER_NAME));
		User renter = new User(renterid, rentername);
		
		String ownerid = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_OWNER_ID));
		String ownername = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_OWNER_NAME));
		boolean pending = cursor.getInt(cursor.getColumnIndexOrThrow(RentalContentProvider.C_PENDING_RENTAL)) == ItemsData.PENDING? true : false;
		User owner = new User(ownerid,ownername);
		
		Rental rental = new Rental(item, dueDate, renter, owner);
		rental.setRental_id(rental_id);
		rental.setPending(pending);
		rental.setPicture_count(picture_count);
		return rental;
	}
	
	public static Item createItemFromCursor(Cursor cursor) {
		String title = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_NAME));
		String description = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_DESCRIPTION));
		String item_id = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_ITEMID));
		double price = cursor.getDouble(cursor.getColumnIndexOrThrow(RentalContentProvider.C_PRICE));
		int quality = cursor.getInt(cursor.getColumnIndexOrThrow(RentalContentProvider.C_QUALITY));
		
		Item item = new Item(title, description);
		item.setItem_id(item_id);
		item.setPrice(price);
		item.setQuality_rating(quality);
		return item;
	}
	
	public static Rental createRentalFromCursor(Cursor cursor) {
		String title = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_NAME));
		String description = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_DESCRIPTION));
		String item_id = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_ITEMID));
		String id = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_ID));
		double price = cursor.getDouble(cursor.getColumnIndexOrThrow(RentalContentProvider.C_PRICE));
		int quality = cursor.getInt(cursor.getColumnIndexOrThrow(RentalContentProvider.C_QUALITY));
		boolean pending = cursor.getInt(cursor.getColumnIndexOrThrow(RentalContentProvider.C_PENDING_RENTAL)) == ItemsData.PENDING? true : false;
		
		Item item = new Item(title, description);
		item.setItem_id(item_id);
		item.setPrice(price);
		item.setQuality_rating(quality);
		
		Rental rental = new Rental(item, null, null, null);
		rental.setRental_id(id);
		rental.setPending(pending);
		return rental;
	}

}
