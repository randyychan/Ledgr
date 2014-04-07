package com.example.ledgr.dataobjects;

import java.util.ArrayList;
import java.util.UUID;

import junit.framework.Assert;

import org.joda.time.LocalDate;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.example.ledgr.MainActivity;
import com.example.ledgr.Utils;
import com.example.ledgr.alarmandnotification.Alarm;
import com.example.ledgr.contentprovider.ItemContentProvider;
import com.example.ledgr.contentprovider.RentalContentProvider;
import com.example.ledgr.temboo.TembooFacebook;

public class ItemsData {
	
	public static final int SYNCED = 0;
	public static final int INSERTED = 1;
	public static final int UPDATED = 2;
	public static final int DELETED = 3;
	public static final int GET = 4;
	
	public static final int RENTED_OUT = 0;
	public static final int CURRENTLY_RENTING = 1;
	
	public static final int PENDING = 0;
	public static final int COMPLETE = 1;
	
	protected int alarm_id = 0;

	public static Cursor getItems_data(Activity activity) {
		Cursor cursor = activity.getContentResolver().
			query(ItemContentProvider.CONTENT_URI, null, null, null, null);
		return cursor;
	}

	public static void addItem(Item item, Activity activity) {
		ContentValues values = ItemContentProvider.itemToValues(item);	
		values.put(ItemContentProvider.C_DIRTY, INSERTED);
		
		activity.getContentResolver().
			insert(ItemContentProvider.CONTENT_URI, values);
		
		activity.getContentResolver().notifyChange(ItemContentProvider.CONTENT_URI, null, true);
		
		//temboo
		TembooFacebook tfb = new TembooFacebook();
		tfb.postItem(item);
	}

	public static void syncedItem(String item_id, Context context) {
		ContentValues values = new ContentValues();
		values.put(ItemContentProvider.C_DIRTY, SYNCED);
		context.getContentResolver().update(ItemContentProvider.CONTENT_URI,
					values, ItemContentProvider.C_ID + " = ?", new String[] {item_id});
	}
	
	public static void syncedDeleteItem(String item_id, Context context) {
		context.getContentResolver().delete(ItemContentProvider.CONTENT_URI, 
				ItemContentProvider.C_ID + " = ?", new String[] {item_id});
	}
	
	public static Cursor itemsToSync(Context context) {
		Cursor cursor = context.getContentResolver()
				.query(ItemContentProvider.CONTENT_URI,
						null,
						ItemContentProvider.C_DIRTY + " != ?",
						new String[] {Integer.toString(SYNCED)},
						null);
		
		return cursor;
	}
	
	
	public static Cursor rentalsToSync(Context context) {
		Cursor cursor = context.getContentResolver()
				.query(RentalContentProvider.CONTENT_URI,
						null,
						RentalContentProvider.C_DIRTY + " != ?",
						new String[] {Integer.toString(SYNCED)},
						null);
		
		return cursor;
	}
	
	
	public static ArrayList<Item> retrieveItemsRentedByUserId(String userId, Activity activity) {
		Cursor cursor = activity.getContentResolver()
				.query(RentalContentProvider.CONTENT_URI, null, 
						RentalContentProvider.C_RENTER_ID + " = ? AND " + RentalContentProvider.C_DIRTY + " != ? AND " + RentalContentProvider.C_DIRTY + " != ?",
						new String[] {userId, Integer.toString(DELETED), Integer.toString(GET)}, null);
		cursor.moveToFirst();
		
		ArrayList<Item> items = new ArrayList<Item>();

		while(!cursor.isAfterLast()) {			
			Item item = RentalContentProvider.createItemFromCursor(cursor);
			items.add(item);
			cursor.moveToNext();
		}
		
		return items;
	}
	
	public static ArrayList<Rental> retrieveItemsLentByUserId(String userId, Activity activity) {
		Cursor cursor = activity.getContentResolver()
				.query(RentalContentProvider.CONTENT_URI, null, 
						RentalContentProvider.C_OWNER_ID + " = ? AND " + RentalContentProvider.C_DIRTY + " != ? AND " + RentalContentProvider.C_DIRTY + " != ?",
						new String[] {userId, Integer.toString(DELETED), Integer.toString(GET)}, null);
		cursor.moveToFirst();
		
		ArrayList<Rental> rentals = new ArrayList<Rental>();

		while(!cursor.isAfterLast()) {			
			Rental rental = RentalContentProvider.createRentalFromCursor(cursor);
			rentals.add(rental);
			cursor.moveToNext();
		}
		
		return rentals;
	}
	

	public static ArrayList<User> retrieveUserIdsCurrentlyRenting(Activity activity) {
		Cursor cursor = activity.getContentResolver().query(RentalContentProvider.CONTENT_URI, 
				new String[] {"DISTINCT " + RentalContentProvider.C_OWNER_ID, RentalContentProvider.C_OWNER_NAME},
				RentalContentProvider.C_DIRTY + " != ? AND " + RentalContentProvider.C_DIRTY + " != ? AND " + RentalContentProvider.C_RENTED_OUT_OR_RENTING + " = ?",
				new String[] {Integer.toString(DELETED), Integer.toString(GET), Integer.toString(CURRENTLY_RENTING)}, null);
		
		ArrayList<User> users = new ArrayList<User>();
		
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			String userId = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_OWNER_ID));
			String name = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_OWNER_NAME));
			System.out.println("Current Users Renting = " + name + " " + userId);
			User user = new User(userId, name);
			users.add(user);
			cursor.moveToNext();
		}
		return users;
	}
	
	public static ArrayList<User> retrieveUserIdsRenting(Activity activity) {
		Cursor cursor = activity.getContentResolver().query(RentalContentProvider.CONTENT_URI, 
				new String[] {"DISTINCT " + RentalContentProvider.C_RENTER_ID, RentalContentProvider.C_RENTER_NAME},
					RentalContentProvider.C_DIRTY + " != ? AND " + RentalContentProvider.C_DIRTY + " != ? AND " + RentalContentProvider.C_RENTED_OUT_OR_RENTING + " = ?",
					new String[] {Integer.toString(DELETED), Integer.toString(GET), Integer.toString(RENTED_OUT)}, null);
		
		ArrayList<User> users = new ArrayList<User>();
		
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			String userId = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_RENTER_ID));
			String name = cursor.getString(cursor.getColumnIndexOrThrow(RentalContentProvider.C_RENTER_NAME));
			User user = new User(userId, name);
			users.add(user);
			cursor.moveToNext();
		}
		return users;
	}

	public static Item retrieveItemById(String item_id, Activity activity) {
		Cursor cursor = activity.getContentResolver().
				query(ItemContentProvider.CONTENT_URI,
					  null,
					  ItemContentProvider.C_ID + " = ? and " + ItemContentProvider.C_DIRTY + " != ?",
					  new String[] {item_id, Integer.toString(DELETED)},
					  null);
		cursor.moveToFirst();
		Item item = ItemContentProvider.createItemFromCursor(cursor);
		
		return item;
	}

	public static boolean deleteItemById(String item_id, Activity activity) {
		ContentValues values = new ContentValues();
		values.put(ItemContentProvider.C_DIRTY, DELETED);
		activity.getContentResolver().update(ItemContentProvider.CONTENT_URI,
				values, ItemContentProvider.C_ID + " = ?", new String[] {item_id});
		activity.getContentResolver().notifyChange(ItemContentProvider.CONTENT_URI, null, true);

		return true;
	}

	public static void updateItem(Item item, Activity activity) {
		ContentValues values = ItemContentProvider.itemToValues(item);
		values.put(ItemContentProvider.C_DIRTY, UPDATED);
		
		activity.getContentResolver().update(ItemContentProvider.CONTENT_URI,
				values, ItemContentProvider.C_ID + " = ?", new String[] {item.getItem_id()});
		activity.getContentResolver().notifyChange(ItemContentProvider.CONTENT_URI, null, true);
	}

	public static boolean rentItemById(String item_id, User renter, LocalDate dueDate, Activity activity) {
		ContentValues values = new ContentValues();
		values.put(ItemContentProvider.C_RENTED, 1);
		values.put(ItemContentProvider.C_DIRTY, UPDATED);
		activity.getContentResolver().update(ItemContentProvider.CONTENT_URI, values,
				ItemContentProvider.C_ID + " = ?", new String[] {item_id});

		Cursor cursor = activity.getContentResolver().query(ItemContentProvider.CONTENT_URI, null, ItemContentProvider.C_ID + " = ?", new String[] {item_id}, null);
		cursor.moveToFirst();
		
		Item item = ItemContentProvider.createItemFromCursor(cursor);
		
		SharedPreferences prefs = activity.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String userid = prefs.getString(MainActivity.MAIN_USERID, "");
		String username = prefs.getString(MainActivity.MAIN_USERNAME, "");
		//TODO need to send rental
		Rental rental = new Rental(item, dueDate, renter,
		new User(userid, username));
		Alarm alarm = new Alarm();
		alarm.SetAlarm(activity.getApplicationContext(), 0, item.getTitle(), renter.getFirstName(), item_id, dueDate);
		rental.setRental_alarm_id(0);
		
		ContentValues contentValues = RentalContentProvider.rentalToValues(rental);
		contentValues.put(RentalContentProvider.C_ID, UUID.randomUUID().toString());
		contentValues.put(RentalContentProvider.C_DIRTY, INSERTED);
		contentValues.put(RentalContentProvider.C_RENTED_OUT_OR_RENTING, RENTED_OUT);

		activity.getContentResolver().insert(RentalContentProvider.CONTENT_URI, contentValues);
		
		activity.getContentResolver().notifyChange(ItemContentProvider.CONTENT_URI, null, true);
		return false;
	}

	public static boolean cancelRentalById(String item_id, Activity activity) {
		ContentValues values = new ContentValues();
		values.put(ItemContentProvider.C_RENTED, 0);
		values.put(ItemContentProvider.C_DIRTY, UPDATED);
		activity.getContentResolver().update(ItemContentProvider.CONTENT_URI, values, ItemContentProvider.C_ID + " = ?", new String[] {item_id});
		
		ContentValues rentalValues = new ContentValues();
		rentalValues.put(RentalContentProvider.C_DIRTY, DELETED);
		activity.getContentResolver()
			.update(RentalContentProvider.CONTENT_URI, rentalValues, 
					RentalContentProvider.C_ITEMID + " = ?", new String[] {item_id});
		activity.getContentResolver().notifyChange(ItemContentProvider.CONTENT_URI, null, true);

		//activity.getContentResolver().delete(RentalContentProvider.CONTENT_URI, RentalContentProvider.C_ITEMID + " = ?", new String[] {item_id});
		return false;
	}
	
	public static void syncedDeleteRental(String rental_id, Context context) {
		context.getContentResolver().delete(RentalContentProvider.CONTENT_URI, 
				RentalContentProvider.C_ID + " = ?", new String[] {rental_id});
	}
	
	public static void syncedRental(String rental_id, Context context) {
		ContentValues values = new ContentValues();
		values.put(ItemContentProvider.C_DIRTY, SYNCED);
		context.getContentResolver().update(RentalContentProvider.CONTENT_URI,
					values, RentalContentProvider.C_ID + " = ?", new String[] {rental_id});
	}
	
	public static void syncedGetRental(Rental rental, Context context) {
		ContentValues values = RentalContentProvider.rentalToValues(rental);
		System.out.println("pcount values" + values.getAsInteger(RentalContentProvider.C_PICTURE_COUNT));
		values.put(RentalContentProvider.C_DIRTY, SYNCED);
		values.put(RentalContentProvider.C_RENTED_OUT_OR_RENTING, CURRENTLY_RENTING);
		values.put(RentalContentProvider.C_PENDING_RENTAL, PENDING);
		context.getContentResolver().update(RentalContentProvider.CONTENT_URI,
				values, RentalContentProvider.C_ID + " = ?", new String[] {rental.getRental_id()});
		
		Cursor cursor = context.getContentResolver().query(RentalContentProvider.CONTENT_URI,
				null, RentalContentProvider.C_DIRTY + " = ?", new String[] {rental.getRental_id()}, null);
		
		//TODO: perform notification to user about new rental here!
		// also refresh the rental screen
		Utils.notifyRental(context, rental);
	}
	
	public static void fetchRental(String rental_id, Context context) {
		ContentValues values = new ContentValues();
		//put fake mock data
		values.put(RentalContentProvider.C_DUEDAY, 15);
		values.put(RentalContentProvider.C_DUEMONTH, 6);
		values.put(RentalContentProvider.C_DUEYEAR, 2000);
		
		values.put(RentalContentProvider.C_ID, rental_id);
		values.put(RentalContentProvider.C_DIRTY, GET);
		values.put(RentalContentProvider.C_RENTED_OUT_OR_RENTING, CURRENTLY_RENTING);
		values.put(RentalContentProvider.C_PENDING_RENTAL, PENDING);
		context.getContentResolver().insert(RentalContentProvider.CONTENT_URI, values);
		context.getContentResolver().notifyChange(ItemContentProvider.CONTENT_URI, null, true);
	}
	
	public static void deleteRental(String rental_id, Context context) {
		context.getContentResolver().delete(RentalContentProvider.CONTENT_URI, RentalContentProvider.C_ID + " = ?", new String[] {rental_id});
	}
	
	public static Rental retrieveRentalById(String item_id, Activity activity) {
		Cursor cursor = activity.getContentResolver().query(RentalContentProvider.CONTENT_URI, null,
				RentalContentProvider.C_ITEMID + " = ? AND " + RentalContentProvider.C_DIRTY + " != ? AND " + RentalContentProvider.C_DIRTY + " != ?", 
				new String[] {item_id, Integer.toString(DELETED), Integer.toString(GET)}, null);
		cursor.moveToFirst();
		
		return RentalContentProvider.cursorToRental(cursor);
	}
	
	public static void venmoChargeComplete(String item_id, Context context) {
		ContentValues values = new ContentValues();
		values.put(RentalContentProvider.C_PENDING_RENTAL, COMPLETE);
		int count = context.getContentResolver().update(RentalContentProvider.CONTENT_URI,
					values, RentalContentProvider.C_ITEMID + " = ?", new String[] {item_id});
		Assert.assertEquals(1, count);
	}
}
