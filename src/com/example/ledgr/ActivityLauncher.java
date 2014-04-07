package com.example.ledgr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.ledgr.create.CreateItem;

public class ActivityLauncher {

	public static void CreateItem(Context context, Activity activity, boolean update, String item_id) {
		Intent intent = new Intent(
				context,
				CreateItem.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean("UPDATE", update);
		if (update) {
			bundle.putString("ITEMID", item_id);
		}
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, 999);
		activity.overridePendingTransition(
				R.animator.slide_in_left,
				R.animator.fade_out);
	}
}
