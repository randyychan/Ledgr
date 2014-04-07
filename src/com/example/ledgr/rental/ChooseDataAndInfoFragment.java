package com.example.ledgr.rental;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ledgr.R;
import com.example.ledgr.Utils;
import com.example.ledgr.cards.CalendarCard;
import com.example.ledgr.cards.FriendCardPicker;
import com.example.ledgr.cards.RentalOptionsCard;
import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemsData;
import com.example.ledgr.dataobjects.User;
import com.facebook.widget.ProfilePictureView;
import com.fima.cardsui.views.CardUI;

public class ChooseDataAndInfoFragment extends Fragment {

	EditText name, facebook;
	Button button;
	LinearLayout layout;
	CardUI calendarCard, optionsInfoCard, friendCard, itemCard;
	LocalDate selectedDate = null;
	User selectedUser;
	ProfilePictureView profilePicture;
	TextView profileName;
	String item_id;
	Item item;

	public ChooseDataAndInfoFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putSerializable("SELECTEDUSER", selectedUser);
		outState.putSerializable("SELECTEDDATE", selectedDate);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			selectedDate = (LocalDate) savedInstanceState
					.getSerializable("SELECTEDDATE");
			selectedUser = (User) savedInstanceState
					.getSerializable("SELECTEDUSER");
		}

		Bundle bundle = getArguments();
		item_id = bundle.getString("ITEMID");
		item = ItemsData.retrieveItemById(item_id, getActivity());

		layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);

		itemCard = new CardUI(getActivity());
		itemCard.setPadding(0, 20, 0, 0);
		itemCard.addCard(Utils.createCardView(item, getActivity()));
		itemCard.refresh();

		friendCard = new CardUI(getActivity());
		friendCard.setPadding(0, 20, 0, 0);
		friendCard.addCard(new FriendCardPicker((CreateRental) getActivity(),
				selectedUser));
		friendCard.refresh();

		calendarCard = new CardUI(getActivity());
		calendarCard.setPadding(0, 20, 0, 0);
		calendarCard.addCard(new CalendarCard((CreateRental) getActivity(),
				selectedDate));
		calendarCard.refresh();

		optionsInfoCard = new CardUI(getActivity());
		optionsInfoCard.setPadding(0, 20, 0, 0);
		optionsInfoCard.addCard(new RentalOptionsCard(getActivity()));
		optionsInfoCard.refresh();

		layout.addView(itemCard);
		layout.addView(friendCard);
		layout.addView(calendarCard);
		layout.addView(optionsInfoCard);
		layout.setBackgroundColor(Color.parseColor(Utils.BACKGROUND_COLOR));

		return layout;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	public void setSelectedUser(User user) {
		selectedUser = user;
		profilePicture = (ProfilePictureView) layout
				.findViewById(R.id.profile_pic);
		profileName = (TextView) layout.findViewById(R.id.profile_name);
		profileName.setText(selectedUser.getFirstName());
		profilePicture.setProfileId(selectedUser.getFacebookId());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}

	public void hello() {
		calendarCard.setVisibility(LinearLayout.VISIBLE);
		Animation animation = AnimationUtils.loadAnimation(getActivity(),
				R.animator.slide_in_right);
		animation.setDuration(500);
		calendarCard.setAnimation(animation);
		calendarCard.animate();
		// animation.start();
	}

	public void setDate(LocalDate dateTime) {
		selectedDate = dateTime;
		TextView date = (TextView) calendarCard.findViewById(R.id.date);
		TextView dateWeek = (TextView) calendarCard.findViewById(R.id.dateweek);
		TextView dateNum = (TextView) calendarCard.findViewById(R.id.datenum);
		TextView dateWeekSmall = (TextView) calendarCard.findViewById(R.id.dateoftheweek);
		date.setText((dateTime.getMonthOfYear()) + "/"
				+ dateTime.getDayOfMonth() + "/" + dateTime.getYear());
		dateNum.setText("" + dateTime.getDayOfMonth());
		
		switch (dateTime.getDayOfWeek()) {
		case 1:
			dateWeekSmall.setText("Mon");
			break;
		case 2:
			dateWeekSmall.setText("Tues");
			break;
		case 3:
			dateWeekSmall.setText("Wed");
			break;
		case 4:
			dateWeekSmall.setText("Thurs");
			break;
		case 5:
			dateWeekSmall.setText("Fri");
			break;
		case 6:
			dateWeekSmall.setText("Sat");
			break;
		case 7:
			dateWeekSmall.setText("Sun");
			break;
		}
		
		
		LocalDate nowDate = LocalDate.now();
		String dueString = null;
		int daysbetween = Days.daysBetween(nowDate, dateTime).getDays();

		if (daysbetween > 28) {
			int months = (daysbetween+15) / 30;
			dueString = "Due in about " + months + (months>1? " months" : " month");
		} else if (daysbetween > 7) {
			int weeks = (daysbetween+3) / 7;
			dueString = "Due in about " + weeks + (weeks>1? " weeks" : " week");
		} else if (daysbetween > 0){
			dueString = "Due in about " + daysbetween + (daysbetween>1? " days" : " day");
		} else {
			dueString = "Due now!";
		}
		dateWeek.setText(dueString);
	}
}
