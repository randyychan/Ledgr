package com.example.ledgr.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
    	String regId = intent.getExtras().getString("registration_id");
    	   if(regId != null && !regId.equals("")) {
    	      /* Do what ever you want with the regId eg. send it to your server */
    		   System.out.println("RANDY GCM ONRECEIVE ID =" + regId);
    		   
    	   }
    	
    	
    	
    	
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}