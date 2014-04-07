package com.example.ledgr;

import java.io.File;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ledgr.contentprovider.ItemContentProvider;
import com.example.ledgr.dataobjects.ItemsData;
import com.example.ledgr.fragments.CurrentlyRenting;
import com.example.ledgr.fragments.FindItemsForRent;
import com.example.ledgr.fragments.ItemsForRent;
import com.example.ledgr.fragments.ItemsNotForRent;
import com.example.ledgr.fragments.RentedOut;
import com.example.ledgr.fragments.SettingsFragment;
import com.example.ledgr.rental.CreateRental;
import com.example.ledgr.search.SearchItems;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
	
    // Constants
    // The authority for the sync adapter's content provider
	
	public static final String MAIN_USERID = "facebook_id";
	public static final String MAIN_UUID = "uuid";
	public static final String MAIN_USERNAME = "facebook_username";
	
    public static final String AUTHORITY = ItemContentProvider.AUTHORITY;
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";

	private static final String TAG = "MainActivity";

	// Declarations for left drawer fragments
	public static final int FRAGMENT_ITEMS_RENTED_OUT = 0;
	public static final int FRAGMENT_ITEMS_FOR_RENT = 1;
	public static final int FRAGMENT_ITEMS_NOT_FOR_RENT = 2;
	public static final int FRAGMENT_FIND_ITEMS_FOR_RENT = 3;
	private static final int FRAGMENT_SETTINGS = 4;
	public static final int FRAGMENT_ITEMS_CURRENTLY_RENTING = 5;
	private static final int FRAGMENT_COUNT = FRAGMENT_ITEMS_CURRENTLY_RENTING + 1;

	private static final String KEY_FRAGMENT_COUNT = "KEY_FRAGMENT_COUNT";

	private String[] actionBarTitles = new String[FRAGMENT_COUNT];
	public static Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	private ProfilePictureView profilePictureView;
	private TextView userNameView;
    Account mAccount;
    ContentResolver mResolver;
	SlidingMenu sliding_menu;
	ListView left_list;
	private boolean isResumed = false;
	private UiLifecycleHelper uiHelper;
	private int fragment_count = 0;
	Activity activity;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
        mAccount = CreateSyncAccount(this);
        mResolver = getContentResolver();
       
        
        TableObserver observer = new TableObserver(new Handler());
        observer.observe();
        
		activity = this;
		Log.i(TAG, "onCreate called");
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_drawer);
		

		if (savedInstanceState == null) {
			fragment_count = 0;
		} else {
			fragment_count = savedInstanceState.getInt(KEY_FRAGMENT_COUNT);
		}

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		setUpFacebookProfile();
		setUpFragments(savedInstanceState);
		setUpSlidingMenu();
	}

	private void setUpFacebookProfile() {
		Log.i(TAG, "setUpFacebookProfile");

		profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
		profilePictureView.setPresetSize(ProfilePictureView.SMALL);
		profilePictureView.setCropped(true);
		// Find the user's name view
		userNameView = (TextView) findViewById(R.id.selection_user_name);
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
								profilePictureView.setProfileId(user.getId());
								// Set the Textview's text to the user's name.
								userNameView.setText(user.getName());
							}
						}
						if (response.getError() != null) {
							// Handle errors, will do so later.
						}
					}
				});
		request.executeAsync();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");
		// TODO Auto-generated method stub
		super.onResume();

		// reloadFragments();

		uiHelper.onResume();
		isResumed = true;
		hideFragments();

		Session session = Session.getActiveSession();
		
		if (session != null && session.isOpened() ) {
			// if the session is already open, try to show the selection
			// fragment
			showFragment(fragment_count, false, false);
			makeMeRequest(session);
		} else {
//			// otherwise present the splash screen and ask the user to login,
//			// unless the user explicitly skipped.
			Intent intent = new Intent(this,LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, 9999);
		}
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause");

		// TODO Auto-generated method stub
		super.onPause();
//		if (((LedgrApplication) this.getApplication()).data.isNeedToSave()) {
//			((LedgrApplication) this.getApplication()).save();
//		}
		fragment_count = 0;
		for (int i = 0; i < FRAGMENT_COUNT; i++) {
			if (fragments[i].isVisible()) {
				fragment_count = i;
				break;
			}
		}
		Log.i(TAG, "onPause.fragment_count = " + fragment_count);

		uiHelper.onPause();
		isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "onActivityResult called");
		Log.i(TAG, "RequestCode: " + requestCode);
		Log.i(TAG, "ResultCode: " + resultCode);
		Log.i(TAG, "Data: " + data);

		switch (requestCode) {
		case 9999:
			if (resultCode == RESULT_CANCELED) {
				
				finish();
			}
			return;
		case 999:
		case 888:				
			//((LedgrApplication) this.getApplication()).save();
			reloadFragments();

			break;
		default:
			break;

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (!fragments[FRAGMENT_SETTINGS].isVisible()) {
				sliding_menu.toggle();

			}
			return true;
		case R.id.action_create:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			AlertDialog dialog = builder.setItems(
					new String[] { "New rental", "New item" },
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							if (which == 0) {
								Intent intent = new Intent(
										getApplicationContext(),
										CreateRental.class);
								intent.putExtra("ITEMID", (String) null);
								startActivityForResult(intent, 888);
								overridePendingTransition(
										R.animator.slide_in_left,
										R.animator.fade_out);
							} else if (which == 1) {
							     Bundle settingsBundle = new Bundle();
							        settingsBundle.putBoolean(
							                ContentResolver.SYNC_EXTRAS_MANUAL, true);
							        settingsBundle.putBoolean(
							                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
								ContentResolver.requestSync(mAccount, "com.example.ledgr.provider.Item", settingsBundle);
								ActivityLauncher.CreateItem(getApplicationContext()	, activity, false, null);
							}
						}
					}).create();

			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			WindowManager.LayoutParams wmlp = dialog.getWindow()
					.getAttributes();

			wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
			wmlp.x = 50; // x position
			wmlp.y = 50; // y position
			dialog.show();
			dialog.getWindow().setLayout(500, 400);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setUpSlidingMenu() {
		sliding_menu = getSlidingMenu();
		sliding_menu.setMode(SlidingMenu.LEFT);
		sliding_menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sliding_menu.setShadowWidthRes(R.dimen.shadow_width);
		sliding_menu.setShadowDrawable(R.drawable.shadow);
		sliding_menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sliding_menu.setFadeDegree(0.35f);

		getActionBar().setHomeButtonEnabled(true);

		// Sets up left draw list view
		String[] left_list_items = getResources().getStringArray(
				R.array.left_drawer_strings);
		left_list = (ListView) sliding_menu.findViewById(R.id.left_drawer);
		left_list.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, left_list_items));

		// On click listener for left list
		// Changes the fragment to the correct view
		left_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (position) {
				case 0:
					showFragment(FRAGMENT_ITEMS_RENTED_OUT, false, false);
					break;
				case 1:
					showFragment(FRAGMENT_ITEMS_CURRENTLY_RENTING, false, false);
					break;
				case 2:
					showFragment(FRAGMENT_ITEMS_FOR_RENT, false,false);
					break;
				case 3:
					showFragment(FRAGMENT_ITEMS_NOT_FOR_RENT, false,false);
					break;
				case 4:
					//showFragment(FRAGMENT_FIND_ITEMS_FOR_RENT, false, false);
					startSearch();
					break;
				default:
					break;
				}
				sliding_menu.toggle();
			}
		});
	}
	private void startSearch() {
		Intent intent = new Intent(this, SearchItems.class);
		startActivity(intent);
	}
	
	private void setUpFragments(Bundle savedInstanceState) {
		Log.i(TAG, "setUpFragments");

		actionBarTitles[FRAGMENT_ITEMS_RENTED_OUT] = "Rented Out";
		actionBarTitles[FRAGMENT_ITEMS_FOR_RENT] = "For Rent";
		actionBarTitles[FRAGMENT_FIND_ITEMS_FOR_RENT] = "Search For Rentals";
		actionBarTitles[FRAGMENT_SETTINGS] = "Settings";
		actionBarTitles
		[FRAGMENT_ITEMS_NOT_FOR_RENT] = "Not For Rent";
		actionBarTitles[FRAGMENT_ITEMS_CURRENTLY_RENTING] = "Currently Renting";
		if (savedInstanceState == null) {
			fragments[FRAGMENT_ITEMS_RENTED_OUT] = new RentedOut();
			fragments[FRAGMENT_ITEMS_FOR_RENT] = new ItemsForRent();
			fragments[FRAGMENT_ITEMS_NOT_FOR_RENT] = new ItemsNotForRent();
			fragments[FRAGMENT_FIND_ITEMS_FOR_RENT] = new FindItemsForRent();
			fragments[FRAGMENT_SETTINGS] = new SettingsFragment();
			fragments[FRAGMENT_ITEMS_CURRENTLY_RENTING] = new CurrentlyRenting();
			getSupportFragmentManager().addOnBackStackChangedListener(
					new OnBackStackChangedListener() {

						@Override
						public void onBackStackChanged() {
							// TODO Auto-generated method stub
							if (fragments[FRAGMENT_SETTINGS].isVisible()) {
								sliding_menu
										.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
								;

							} else {
								sliding_menu
										.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

							}
						}
					});
			addFragments();

		} else {
			for (int i = 0; i < FRAGMENT_COUNT; i++) {
				fragments[i] = getSupportFragmentManager().findFragmentByTag(
						String.valueOf(i));
			}
			showFragment(fragment_count, false, false);
		}
	}

	private void hideFragments() {
		Log.i(TAG, "hideFragments");

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		for (int i = 0; i < FRAGMENT_COUNT; i++) {
			transaction.hide(fragments[i]);
		}

		transaction.commit();
	}

	private void addFragments() {
		Log.i(TAG, "addFragments");

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		for (int i = 0; i < FRAGMENT_COUNT; i++) {
			Log.i(TAG, "addFragments:" + i);

			transaction
					.add(R.id.content_frame, fragments[i], String.valueOf(i));
		}

		transaction.commit();
	}

	private void showFragment(int fragmentIndex, boolean addToBackStack,
			boolean animate) {
		Log.i(TAG, "showFragment = " + fragmentIndex);
		hideFragments();

		if (fragmentIndex == FRAGMENT_ITEMS_CURRENTLY_RENTING) {
			CurrentlyRenting curRent = (CurrentlyRenting)fragments[FRAGMENT_ITEMS_CURRENTLY_RENTING];
			curRent.refreshViews();
		}
		
		// update the main content by replacing fragments
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < FRAGMENT_COUNT; i++) {
			if (i == fragmentIndex) {
				transaction.show(fragments[i]);
			} else if (fragments[i].isVisible()) {
				transaction.hide(fragments[i]);
			}
		}
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();

		getActionBar().setTitle(actionBarTitles[fragmentIndex]);

	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		Log.i(TAG, "onSessionStateChange");

		if (isResumed) {
			FragmentManager manager = getSupportFragmentManager();
			int backStackSize = manager.getBackStackEntryCount();
			for (int i = 0; i < backStackSize; i++) {
				manager.popBackStack();
			}
			// check for the OPENED state instead of session.isOpened() since
			// for the
			// OPENED_TOKEN_UPDATED state, the selection fragment should already
			// be showing.
			if (state.equals(SessionState.OPENED)) {
				showFragment(FRAGMENT_ITEMS_RENTED_OUT, false, false);
				// Get the user's data.
				makeMeRequest(session);
			} else if (state.isClosed()) {
				Intent intent = new Intent(this, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent,9999);
			}
		}
	}

	public void reloadFragments() {
			((ItemsNotForRent) fragments[FRAGMENT_ITEMS_NOT_FOR_RENT])
					.refreshViews();
			((ItemsForRent) fragments[FRAGMENT_ITEMS_FOR_RENT]).refreshViews();
			((RentedOut) fragments[FRAGMENT_ITEMS_RENTED_OUT]).refreshViews();
			showFragment(fragment_count, false, false);
		
	}

	private void removeExtraImages() {
		Log.i(TAG, "removeExtraImages");

		boolean exists = false;
		File dir = getExternalFilesDir(null);
		File[] files = dir.listFiles();
		for (File f : files) {
			String file_name = f.getName();
			Cursor cursor =  ItemsData.getItems_data(this);
			cursor.moveToFirst();
			
			while (!cursor.isAfterLast()) {
				int idx = cursor.getColumnIndex(ItemContentProvider.C_ID);
				String item_id = cursor.getString(idx);
				if (item_id.equals(file_name)) {
					exists = true;
				}
			}

			if (!exists) {
				Utils.delete(f);
			}
			exists = false;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroy");
		removeExtraImages();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_FRAGMENT_COUNT, fragment_count);
	}

	public void deleteItemPictures(String item_id) {
		Log.i(TAG, "deleteItemPictures");

		File dir = getExternalFilesDir(item_id);
		Utils.delete(dir);
	}
	
    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }
	
    
    public class TableObserver extends ContentObserver {
        public TableObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		/*
         * Define a method that's called when data in the
         * observed content provider changes.
         * This method signature is provided for compatibility with
         * older platforms.
         */
        @Override
        public void onChange(boolean selfChange) {
            /*
             * Invoke the method signature available as of
             * Android platform version 4.1, with a null URI.
             */
            onChange(selfChange, null);

        }
        
        void observe() {
            mResolver.registerContentObserver(ItemContentProvider.CONTENT_URI, true, this);
        }
        

        /*
         * Define a method that's called when data in the
         * observed content provider changes.
         */
        @Override
        public void onChange(boolean selfChange, Uri changeUri) {
        	 Bundle settingsBundle = new Bundle();
		        settingsBundle.putBoolean(
		                ContentResolver.SYNC_EXTRAS_MANUAL, true);
		        settingsBundle.putBoolean(
		                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            getContentResolver().requestSync(mAccount, "com.example.ledgr.provider.Item", settingsBundle);
        }
    }
}
