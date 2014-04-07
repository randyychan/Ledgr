package com.example.ledgr.rental;

import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.example.ledgr.LedgrApplication;
import com.example.ledgr.R;
import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemsData;
import com.example.ledgr.dataobjects.User;
import com.example.ledgr.rental.ChooseFriendFragment.onFriendSelectedListener;
import com.example.ledgr.rental.ChooseItemFragment.onItemSelectedListener;
import com.facebook.model.GraphUser;

public class CreateRental extends FragmentActivity implements
		onItemSelectedListener, onFriendSelectedListener,
		CalendarDatePickerDialog.OnDateSetListener {

	private static final int CHOOSE_ITEM = 0;
	private static final int CHOOSE_DATE = 1;
	private static final int REVIEW = 2;
	private static final int FRAGMENT_COUNT = REVIEW + 1;
	Activity activity;
	FragmentManager fragmentManager;
	int position = 0;
	Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	String[] fragmentTitles = new String[] { "Pick an item", "",
			"Review rental" };
	private Item selectedItem;
	private User selectedUser;
	private LocalDate selectedDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_rental);
		activity = this;
		String item_id = getIntent().getStringExtra("ITEMID");
		getActionBar().setTitle("Choose an Item");

		fragmentManager = getSupportFragmentManager();
		fragmentManager
				.addOnBackStackChangedListener(new OnBackStackChangedListener() {

					@Override
					public void onBackStackChanged() {
						// TODO Auto-generated method stub
						invalidateOptionsMenu();
						getActionBar().setTitle(fragmentTitles[position]);

						if (position == 0) {
							selectedItem = null;
							selectedDate = null;
							selectedUser = null;
						}
					}
				});
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState != null) {
			selectedDate = (LocalDate) savedInstanceState
					.getSerializable("SELECTEDDATE");
			selectedUser = (User) savedInstanceState
					.getSerializable("SELECTEDUSER");
			selectedItem = (Item) savedInstanceState
					.getSerializable("SELECTEDITEM");

		}

		setUpFragments(savedInstanceState);

		if (item_id != null) {
			selectedItem = ItemsData.retrieveItemById(item_id, this);
			selectedItem(selectedItem);
		}
	}

	private void setUpFragments(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			fragments[CHOOSE_ITEM] = new ChooseItemFragment();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.add(R.id.content_frame, fragments[CHOOSE_ITEM],
					"CHOOSE_ITEM");
			transaction.commit();
		} else {
			position = savedInstanceState.getInt("POSITION");
			fragments[CHOOSE_ITEM] = (ChooseItemFragment) getSupportFragmentManager()
					.findFragmentByTag("CHOOSE_ITEM");

			if (position == 1) {
				fragments[CHOOSE_DATE] = (ChooseDataAndInfoFragment) getSupportFragmentManager()
						.findFragmentByTag("CHOOSE_DATE");
			}
			invalidateOptionsMenu();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("POSITION", position);
		outState.putSerializable("SELECTEDDATE", selectedDate);
		outState.putSerializable("SELECTEDITEM", selectedItem);
		outState.putSerializable("SELECTEDUSER", selectedUser);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 999:
			if (resultCode == RESULT_OK) {
				Item item = (Item) data.getExtras().getSerializable("ITEM");
				selectedItem(item);
			}
			return;
		case 555:
			if (resultCode == RESULT_OK) {
				List<GraphUser> users = ((LedgrApplication) this
						.getApplication()).getSelectedUsers();
				if (users.size() == 0) {
					return;
				}
				String name, userid;
				name = users.get(0).getName();
				userid = users.get(0).getId();
				selectedUser = new User(userid, name);
				selectedUser(selectedUser);
			}
			return;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			new AlertDialog.Builder(this)
					.setMessage("Cancel rental?")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							})
					.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			return true;
		case R.id.action_review:

			if (selectedDate == null) {
				Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT)
						.show();
			} else if (selectedUser == null) {
				Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT)
						.show();
			} else {
				new AlertDialog.Builder(this)
						.setMessage(
								"Let " + selectedUser.getFirstName() + " rent "
										+ selectedItem.getTitle() + " until "
										+ (selectedDate.getMonthOfYear()) + "/"
										+ selectedDate.getDayOfMonth() + "/"
										+ selectedDate.getYear())
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										Toast.makeText(activity,
												"Creating Rental",
												Toast.LENGTH_SHORT).show();
										ItemsData.rentItemById(
												selectedItem.getItem_id(),
												selectedUser, selectedDate, activity);
										finish();
									}
								})
						.setNegativeButton(android.R.string.no,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();

			}
			return true;
		case R.id.action_confirm:

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		switch (position) {
		case CHOOSE_DATE:
			getMenuInflater().inflate(R.menu.create_rental_review, menu);
			break;
		case REVIEW:
			getMenuInflater().inflate(R.menu.create_rental_confirm, menu);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void selectedItem(Item item) {
		position=1;
		selectedItem = item;
		fragments[CHOOSE_DATE] = new ChooseDataAndInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putString("ITEMID", selectedItem.getItem_id());
		fragments[CHOOSE_DATE].setArguments(bundle);
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.content_frame, fragments[CHOOSE_DATE],
				"CHOOSE_DATE");
		transaction.addToBackStack("DATE");
		transaction.commit();

	}

	@Override
	public void selectedUser(User user) {
		ChooseDataAndInfoFragment fragment = (ChooseDataAndInfoFragment) fragments[CHOOSE_DATE];
		fragment.setSelectedUser(user);
	}

	@Override
	public void onDateSet(CalendarDatePickerDialog dialog, int year,
			int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub
		LocalDate now = LocalDate.now();
		selectedDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);

		Calendar myCal = Calendar.getInstance();
		myCal.setTimeInMillis(System.currentTimeMillis());
		myCal.clear();
		myCal.set(selectedDate.getYear(), selectedDate.getMonthOfYear(),
				selectedDate.getDayOfMonth());

		Calendar nowCal = Calendar.getInstance();
		nowCal.setTimeInMillis(System.currentTimeMillis());
		nowCal.clear();
		nowCal.set(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth());

		if (nowCal.getTimeInMillis() > myCal.getTimeInMillis()) {
			Toast.makeText(this, "Select a date in the future",
					Toast.LENGTH_SHORT).show();
			selectedDate = null;
			DateTime dtNow = DateTime.now();
			CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
					.newInstance(this, dtNow.getYear(),
							dtNow.getMonthOfYear() - 1, dtNow.getDayOfMonth());
			calendarDatePickerDialog.show(getSupportFragmentManager(),
					"fragment_date_picker_name");
			return;
		}

		ChooseDataAndInfoFragment f = (ChooseDataAndInfoFragment) fragments[CHOOSE_DATE];
		f.setDate(selectedDate);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (position == 0) {
			new AlertDialog.Builder(this)
					.setMessage("Cancel rental?")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							})
					.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
		} else {
			position--;
			fragmentManager.popBackStack();
		}
	}

}
