package com.example.ledgr;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.Days;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;

import com.example.ledgr.cards.ItemCard;
import com.example.ledgr.cards.ItemCardOnline;
import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.Rental;

public class Utils {

	public static final String BACKGROUND_COLOR = "#E5E5E5";
	
	public static int daysBetweenDates(DateTime time1, DateTime time2) {
		return Days.daysBetween(time1, time2).getDays();
	}

	public static ItemCard createCardView(Item item, Activity activity) {
		if (item == null) {
			return null;
		}

		Bitmap bitmap = getSinglePictureForId(item.getItem_id(), activity);

		return new ItemCard(item.getTitle(), item.getPriceString(),
				item.getDescription(), item.getItem_id(), bitmap);
	}
	
	public static ItemCard createCardView(Rental rental, Activity activity) {
		if (rental == null) {
			return null;
		}

		Bitmap bitmap = getSinglePictureForId(rental.getItem_id(), activity);

		String pendingString = rental.isPending()? "Pending" : "";
		
		return new ItemCard(rental.getTitle(), pendingString,
				rental.getDescription(), rental.getItem_id(), bitmap);
	}
	
	public static ItemCardOnline createCardViewOnline(Rental rental, Activity activity) {
		if (rental == null) {
			return null;
		}

		//Bitmap bitmap = getSinglePictureForId(rental.getItem_id(), activity);

		String pendingString = rental.isPending()? "Pending" : "";
		
		return new ItemCardOnline(rental.getTitle(), pendingString,
				rental.getDescription(), rental.getItem_id());
	}

	public static Bitmap getSinglePictureForId(String item_id, Context context) {
		File dir = context.getExternalFilesDir(item_id);
		if (dir == null) {
			return null;
		}
		File imageFile = null;
		Bitmap bitmap = null;
		if (dir.exists()) {
			File[] files = dir.listFiles();
			if (files.length > 0) {
				imageFile = files[0];
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;

				bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory
						.decodeFile(imageFile.getAbsolutePath(), options), 300,
						300);
			}
		}
		return bitmap;
	}

	public static ArrayList<Bitmap> getPicturesForId(String item_id,
			Context context) {
		File dir = context.getExternalFilesDir(item_id);
		if (dir == null) {
			return null;
		}
		ArrayList<Bitmap> list = new ArrayList<Bitmap>();
		File imageFile = null;
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				imageFile = files[i];
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;

				list.add(ThumbnailUtils.extractThumbnail(BitmapFactory
						.decodeFile(imageFile.getAbsolutePath(), options), 300,
						300));
			}
		}
		return list;
	}
	
	public static ArrayList<File> getImageFilesForId(String item_id,
			Context context) {
		File dir = context.getExternalFilesDir(item_id);
		if (dir == null) {
			return null;
		}
		ArrayList<File> list = new ArrayList<File>();
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				list.add(files[i]);
			}
		}
		return list;
	}
	
	public static ArrayList<String> getImageStringsForId(String item_id,
			Context context) {
		File dir = context.getExternalFilesDir(item_id);
		if (dir == null) {
			return null;
		}
		ArrayList<String> list = new ArrayList<String>();
		File imageFile;
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				imageFile = files[i];
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap img = ThumbnailUtils.extractThumbnail(BitmapFactory
						.decodeFile(imageFile.getAbsolutePath(), options), 300,
						300);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

		        img.compress(Bitmap.CompressFormat.JPEG,40,baos); 
		        byte[] byteArray = baos.toByteArray();
		        
				list.add(Base64.encodeToString(byteArray,Base64.DEFAULT));
			}
		}
		return list;
	}

	public static boolean delete(File file) {

		File[] flist = null;

		if (file == null) {
			return false;
		}

		if (file.isFile()) {
			return file.delete();
		}

		if (!file.isDirectory()) {
			return false;
		}

		flist = file.listFiles();
		if (flist != null && flist.length > 0) {
			for (File f : flist) {
				if (!delete(f)) {
					return false;
				}
			}
		}

		return file.delete();
	}
	
	public static void notifyRental(Context context, Rental rental) {
		String title, friend, item_id;
		title = rental.getTitle();
		friend = rental.getOwner().getFirstName();
		//item_id = intent.getStringExtra("ITEMID");
		//int id = intent.getIntExtra("ID", 0);
		//Bitmap bitmap = Utils.getSinglePictureForId(item_id, context);
		System.out.println("TEST NOTIFY RENTAL!");
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.icon)
				//.setLargeIcon(bitmap)
				.setContentTitle("Borrowing " + title)
				.setContentText("from " + friend)
//				.setTicker(
//						friend + " needs to return your " + title + " today!")
				.setAutoCancel(true);

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, MainActivity.class);
		//resultIntent.putExtra("ITEMID", item_id);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		Notification notif = mBuilder.build();
		notif.defaults |= Notification.DEFAULT_VIBRATE;
		notif.defaults |= Notification.DEFAULT_SOUND;
		mNotificationManager.notify(100, notif);
	}
}
