package com.example.ledgr.alarmandnotification;

import java.util.Calendar;

import org.joda.time.LocalDate;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

import com.example.ledgr.MainActivity;
import com.example.ledgr.R;
import com.example.ledgr.Utils;
import com.example.ledgr.viewrental.ViewRental;

public class Alarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String title, friend, item_id;
		title = intent.getStringExtra("TITLE");
		friend = intent.getStringExtra("FRIEND");
		item_id = intent.getStringExtra("ITEMID");
		int id = intent.getIntExtra("ID", 0);
		Bitmap bitmap = Utils.getSinglePictureForId(item_id, context);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.icon)
				.setLargeIcon(bitmap)
				.setContentTitle(title + " due today")
				.setContentText("Borrowed by: " + friend)
				.setTicker(
						friend + " needs to return your " + title + " today!")
				.setAutoCancel(true);

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, ViewRental.class);
		resultIntent.putExtra("ITEMID", item_id);

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
		mNotificationManager.notify(id, notif);

	}

	public void SetAlarm(Context context, int id, String title, String friend,
			String item_id, LocalDate dueDate) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, Alarm.class);
		i.putExtra("TITLE", title);
		i.putExtra("FRIEND", friend);
		i.putExtra("ITEMID", item_id);
		i.putExtra("ID", id);
		PendingIntent pi = PendingIntent.getBroadcast(context, id, i, 0);

		Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(System.currentTimeMillis());
		cal.clear();
		cal.set(dueDate.getYear(), dueDate.getMonthOfYear() - 1,
				dueDate.getDayOfMonth());
		am.set(AlarmManager.RTC, cal.getTimeInMillis(), pi);
	}

	public void CancelAlarm(Context context, int id) {
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, id, intent,
				0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}