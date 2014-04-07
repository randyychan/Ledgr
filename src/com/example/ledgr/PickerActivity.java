package com.example.ledgr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;

import com.facebook.FacebookException;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;

public class PickerActivity extends FragmentActivity {

	private FriendPickerFragment friendPickerFragment;
	private static final String TAG = "PickerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picker);
		getActionBar().hide();
		Bundle args = getIntent().getExtras();
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragmentToShow = null;

		if (savedInstanceState == null) {
			friendPickerFragment = new FriendPickerFragment(args);
		} else {
			friendPickerFragment = (FriendPickerFragment) manager
					.findFragmentById(R.id.picker_fragment);
		}
		friendPickerFragment.setMultiSelect(false);
		// Set the listener to handle errors
		friendPickerFragment
				.setOnErrorListener(new PickerFragment.OnErrorListener() {
					@Override
					public void onError(PickerFragment<?> fragment,
							FacebookException error) {
						PickerActivity.this.onError(error);
					}
				});
		// Set the listener to handle button clicks
		friendPickerFragment
				.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
					@Override
					public void onDoneButtonClicked(PickerFragment<?> fragment) {

						finishActivity();
					}
				});
		fragmentToShow = friendPickerFragment;
		manager.beginTransaction()
				.replace(R.id.picker_fragment, fragmentToShow).commit();
	}

	private void onError(Exception error) {
		onError(error.getLocalizedMessage(), false);
	}

	private void onError(String error, final boolean finishActivity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("ERROR")
				.setMessage(error)
				.setPositiveButton("NEXT",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {
								if (finishActivity) {
									finishActivity();
								}
							}
						});
		builder.show();
	}

	private void finishActivity() {
		Log.d(TAG, "finishActivity");
		((LedgrApplication) this.getApplication())
				.setSelectedUsers(friendPickerFragment.getSelection());
		setResult(RESULT_OK, null);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.picker, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		try {
			friendPickerFragment.loadData(false);
		} catch (Exception ex) {
			onError(ex);
		}
	}

}
