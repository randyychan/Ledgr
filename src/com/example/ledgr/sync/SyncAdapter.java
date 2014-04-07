package com.example.ledgr.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.ledgr.MainActivity;
import com.example.ledgr.contentprovider.ItemContentProvider;
import com.example.ledgr.contentprovider.RentalContentProvider;
import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemsData;
import com.example.ledgr.dataobjects.Rental;
import com.example.ledgr.dataobjects.User;
import com.example.ledgr.server.ServerComm;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }
    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		// TODO Auto-generated method stub
		syncItems();
		syncRentals();
		
	}
	
	private void syncItems() {
		Cursor cursor = ItemsData.itemsToSync(getContext());
		cursor.moveToFirst();
		SharedPreferences prefs = getContext().getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String uuid = prefs.getString(MainActivity.MAIN_UUID, "");

		while (!cursor.isAfterLast()) {
			
			int sync_type = cursor.getInt(cursor.getColumnIndexOrThrow(ItemContentProvider.C_DIRTY));
			
			if (sync_type == ItemsData.INSERTED) {
				Item item = ItemContentProvider.createItemFromCursor(cursor);
				try {
					ServerComm.createItem(item, uuid, getContext());
					ItemsData.syncedItem(item.getItem_id(), getContext());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (sync_type == ItemsData.UPDATED) {
				Item item = ItemContentProvider.createItemFromCursor(cursor);
				try {
					ServerComm.updateItem(item, uuid);
					ItemsData.syncedItem(item.getItem_id(), getContext());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (sync_type == ItemsData.DELETED) {
				Item item = ItemContentProvider.createItemFromCursor(cursor);
				try {
					ServerComm.deleteItem(item,uuid);
					ItemsData.syncedDeleteItem(item.getItem_id(), getContext());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			cursor.moveToNext();
		}
	}
	
	private void syncRentals() {
		Cursor cursor = ItemsData.rentalsToSync(getContext());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			
			int sync_type = cursor.getInt(cursor.getColumnIndexOrThrow(RentalContentProvider.C_DIRTY));
			Rental rental = RentalContentProvider.cursorToRental(cursor);
			SharedPreferences prefs = getContext().getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
			String ownerid = prefs.getString(MainActivity.MAIN_USERID, "");
			String ownername = prefs.getString(MainActivity.MAIN_USERNAME, "");
			String uuid = prefs.getString(MainActivity.MAIN_UUID, "DOESNOTEXIST");
			rental.setOwner(new User(ownerid, ownername));
			
			if (sync_type == ItemsData.INSERTED) {
				try {
					ServerComm.createRental(rental, uuid);
					ItemsData.syncedRental(rental.getRental_id(), getContext());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (sync_type == ItemsData.UPDATED) {
				try {
					ServerComm.updateRental(rental, uuid);
					ItemsData.syncedRental(rental.getRental_id(), getContext());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (sync_type == ItemsData.DELETED) {
				try {
					ServerComm.deleteRental(rental, uuid);
					ItemsData.syncedDeleteRental(rental.getRental_id(), getContext());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (sync_type == ItemsData.GET) {
				try {
					Rental fetchedRental = ServerComm.getRental(rental, uuid);
					System.out.println("pcount sync " + fetchedRental.getPicture_count());
					Log.i("SYNC", "Finished Rental REST GET");
					Log.i("SYNC", fetchedRental.toString());
					fetchedRental.setRental_id(rental.getRental_id());
					ItemsData.syncedGetRental(fetchedRental, getContext());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			cursor.moveToNext();
		}
	}
    
}