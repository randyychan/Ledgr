package com.example.ledgr;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ledgr.dataobjects.User;
import com.example.ledgr.fragments.LoginInfoFragment;
import com.example.ledgr.fragments.SplashFragment;
import com.example.ledgr.server.ServerComm;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;



public class LoginActivity extends FragmentActivity {

	String SENDER_ID = "763728620863";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    String regid;
    
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
	String TAG = "LoginActivity";
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	private UiLifecycleHelper uiHelper;
	Fragment[] fragments = new Fragment[2];
	ViewPager pager;
	ScreenSlidePagerAdapter mPagerAdapter;
	
	
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter  {
    	Fragment[] fragments;

    	    	
        public ScreenSlidePagerAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
        	return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
    

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		case R.id.action_register:
			register();
			finish();
			return true;
		}
		return false;
		//return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    context = getApplicationContext();

		setContentView(R.layout.activity_splash);
		
		getActionBar().hide();
		
		fragments[0] = new SplashFragment();
		fragments[1] = new LoginInfoFragment();
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
		pager = (ViewPager)super.findViewById(R.id.viewpager);
        pager.setAdapter(this.mPagerAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
			// check for the OPENED state instead of session.isOpened() since
			// for the
			// OPENED_TOKEN_UPDATED state, the selection fragment should already
			// be showing.
			if (state.equals(SessionState.OPENED)) {
				setResult(RESULT_OK);
				makeMeRequest(session);
				pager.setCurrentItem(1);
				getActionBar().show();
				//finish();
				// Get the user's data.
			} else if (state.isClosed()) {
				//finish();

			}
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResult(RESULT_CANCELED);
		finish();
		super.onBackPressed();
	}
	
	private void makeMeRequest(final Session session) {
		// Make an API call to get user data and define a
		// new callback to handle the response.
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// If the response is successful
						if (session == Session.getActiveSession()) {
							if (user != null) {
								// Set the id for the ProfilePictureView
								// view that in turn displays the profile
								// picture.
								LoginInfoFragment frag = (LoginInfoFragment) fragments[1];
								frag.setFacebookPrfile(user.getId(), user.getName());
							}
						}
						if (response.getError() != null) {
							// Handle errors, will do so later.
						}
					}
				});

			request.executeAsync();
	
	}
	
	private void registerUser() {
		 if (checkPlayServices()) {
	            gcm = GoogleCloudMessaging.getInstance(this);
	            regid = getRegistrationId(context);

	            if (regid.isEmpty()) {

	                registerInBackground();
	            }
	            

	     } else {
	        Log.i(TAG, "No valid Google Play Services APK found.");
	     }
	}
	
	
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            finish();
	        }
	        return false;
	    }
	    return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
	    return getSharedPreferences(MainActivity.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	}
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}	
	
	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.i(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	
	private void registerInBackground() {
		new registerInBackgroundTask().execute(new Void[] {});
	}
	
	private class registerInBackgroundTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... voids) {
			 String msg = "";
	            try {

	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }

	                regid = gcm.register(SENDER_ID);
	                msg = "Device registered, registration ID=" + regid;

	                // You should send the registration ID to your server over HTTP,
	                // so it can use GCM/HTTP or CCS to send messages to your app.
	                // The request to your server should be authenticated if your app
	                // is using accounts.
	                
	                sendRegistrationIdToBackend();

	                // For this demo: we don't need to send it because the device
	                // will send upstream messages to a server that echo back the
	                // message using the 'from' address in the message.

	                // Persist the regID - no need to register again.
	                storeRegistrationId(context, regid);
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	                // If there is an error, don't just keep trying to register.
	                // Require the user to click a button again, or perform
	                // exponential back-off.

	            }
			return null;
		}

		protected void onProgressUpdate(Void... progress) {
		}

		protected void onPostExecute(Void result) {

		}
	}
	
	private void sendRegistrationIdToBackend() {
	    // Your implementation here.

		LoginInfoFragment fragment = (LoginInfoFragment) fragments[1];
		User user = fragment.createUser();
		user.setGcmId(regid);
		try {

			String uuid = ServerComm.createUser(user);
			saveUserToSharedPref(uuid, user);
			//here i save to sharedpreferences.
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}		
	}
	
	private void saveUserToSharedPref(String uuid, User user) {
		SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(MainActivity.MAIN_UUID, uuid);
	    editor.putString(MainActivity.MAIN_USERID, user.getFacebookId());
	    editor.putString(MainActivity.MAIN_USERNAME, user.getFirstName());
	    editor.commit();
	}
	
	
	public void register() {
		 if (checkPlayServices()) {
			 gcm = GoogleCloudMessaging.getInstance(this);
			 regid = getRegistrationId(context);

			 if (regid.isEmpty()) {
				 registerInBackground();
			 }
        } else {
			 Log.i(TAG, "No valid Google Play Services APK found.");
		}
	}
	
}
