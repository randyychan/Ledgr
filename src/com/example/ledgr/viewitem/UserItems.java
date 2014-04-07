package com.example.ledgr.viewitem;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ledgr.R;
import com.example.ledgr.Utils;
import com.example.ledgr.cards.ItemCard;
import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemsData;
import com.facebook.widget.ProfilePictureView;
import com.fima.cardsui.views.CardUI;

public class UserItems extends Activity {

	ActionBar action_bar;
	String userId, username;
	LinearLayout layout;
	View.OnClickListener onClickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_items);
		userId = getIntent().getStringExtra("USERID");
		username = getIntent().getStringExtra("USERNAME");
		action_bar = getActionBar();
		action_bar.setTitle("Items rented by");
		action_bar.setDisplayHomeAsUpEnabled(true);
		onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView text = (TextView) v.findViewById(R.id.card_id);
				String item_id = (String) text.getText();

				viewItemById(item_id);
				// MyCard card = (MyCard) v.findViewById(R.id.cardContent);
			}
		};
		layout = (LinearLayout) findViewById(R.id.user_layout);
		createProfile(userId, username);
		addItems(userId);

	}

	private void viewItemById(String item_id) {
		Intent intent = new Intent(this, ViewItem.class);
		intent.putExtra("ITEMID", item_id);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_items, menu);
		return true;
	}

	public void createProfile(String userId, String username) {
		View rootView = getLayoutInflater().inflate(R.layout.profile_picture,
				null);

		ProfilePictureView profile_pic = (ProfilePictureView) rootView
				.findViewById(R.id.profile_pic);
		TextView profile_name = (TextView) rootView
				.findViewById(R.id.profile_name);
		profile_pic.setProfileId(userId);
		profile_name.setText(username);

		layout.addView(rootView);
	}

	public void addItems(String userId) {
		ArrayList<Item> items = ItemsData.retrieveItemsRentedByUserId(userId, this);
		CardUI card_view = new CardUI(getApplicationContext());
		card_view.setSwipeable(false);
		ItemCard myCard;

		for (Item i : items) {

			myCard = Utils.createCardView(i, this);
			myCard.setOnClickListener(onClickListener);

			card_view.addCard(myCard);
		}
		card_view.refresh();
		layout.addView(card_view);
	}

}
