package com.example.ledgr.create;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ledgr.R;
import com.example.ledgr.Utils;
import com.example.ledgr.cards.ItemInfoCard;
import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemPriceUnit;
import com.example.ledgr.dataobjects.ItemState;
import com.example.ledgr.dataobjects.ItemsData;
import com.fima.cardsui.views.CardUI;

public class CreateItemFragment extends Fragment {

	ScrollView scroller;
	LinearLayout layout;
	CardUI infoUI;
	ItemInfoCard createItemCard;
	public String item_id, title, price, description;
	private ItemState state;
	private Item item;
	private ItemPriceUnit unit;
	protected int rating;
	TextView ratingText;
	RatingBar ratingBar;
	String[] ratingStrings = new String[] {"Broken! Completely unusable!","Almost broken... Kinda usable.",
			"Still working, but its seen better days.",
			"Looks like its been used, still working good!",
			"Looks good, works good!",
			"Almost perfect condition!"};
	
	
	public CreateItemFragment() {
		// Empty constructor required for fragment subclasses
	}
	
	public void setRatingText(int value) {
		ratingText = (TextView)layout.findViewById(R.id.ratingstring);
		ratingText.setText(ratingStrings[value]);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Item saveItem = saveItemFromValues();
		outState.putString("ITEMID", saveItem.getItem_id());
		outState.putString("TITLE", saveItem.getTitle());
		outState.putString("PRICE", String.valueOf(saveItem.getPrice()));
		outState.putString("DESCRIPTION", saveItem.getDescription());
		outState.putSerializable("ITEMSTATE", saveItem.getItem_state());
		outState.putSerializable("UNIT", saveItem.getItem_price_unit());
		outState.putSerializable("RATING", saveItem.getQuality_rating());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Bundle bundle = getArguments();
		title = null;
		description = null;
		price = null;
		state = ItemState.FOR_RENT;
		unit = ItemPriceUnit.DAY;


		boolean update = bundle.getBoolean("UPDATE");
		if (savedInstanceState != null) {
			item_id = savedInstanceState.getString("ITEMID");
			title = savedInstanceState.getString("TITLE");
			description = savedInstanceState.getString("DESCRIPTION");
			price = savedInstanceState.getString("PRICE");
			state = (ItemState) savedInstanceState.getSerializable("ITEMSTATE");
			unit = (ItemPriceUnit) savedInstanceState.getSerializable("UNIT");
			rating = savedInstanceState.getInt("RATING");
		} else if (update) {
			item_id = bundle.getString("ITEMID");
			item = ItemsData.retrieveItemById(item_id, getActivity());
			title = item.getTitle();
			description = item.getDescription();
			price = String.valueOf(item.getPrice());
			state = item.getItem_state();
			unit = item.getItem_price_unit();
			rating = item.getQuality_rating();
		} else {
			item_id = bundle.getString("ITEMID");
			item = null;
		}

		View rootView = inflater.inflate(R.layout.fragment_create_item_base,
				container, false);

		scroller = (ScrollView) rootView.findViewById(R.id.scroll);

		layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);


		createItemCard = new ItemInfoCard(this, title, description,
				price, state, unit, rating);
		infoUI = new CardUI(getActivity());
		infoUI.addCard(createItemCard);

		infoUI.refresh();
		infoUI.setPadding(0, 0, 0, 20);
		View view = new View(getActivity());
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 20));
		layout.addView(view);
		layout.addView(infoUI);
 		scroller.addView(layout);
		scroller.setBackgroundColor(Color.parseColor(Utils.BACKGROUND_COLOR));

		return scroller;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}


	public void setInfoValues(String title, String description, String price) {
		EditText editTitle = (EditText) layout.findViewById(R.id.title);
		EditText editDescription = (EditText) layout
				.findViewById(R.id.description);
		EditText editPrice = (EditText) layout.findViewById(R.id.price);

		editTitle.setText(title);
		editDescription.setText(description);
		editPrice.setText(description);

	}

	public Item createItemFromValues() {
		EditText editTitle = (EditText) layout.findViewById(R.id.title);
		EditText editDescription = (EditText) layout
				.findViewById(R.id.description);
		EditText editPrice = (EditText) layout.findViewById(R.id.price);

		if (editTitle.getText().length() == 0
				|| editPrice.getText().length() == 0
				|| editPrice.getText().toString().equals(".")) {
			return null;
		}

		RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.rent);
		Spinner price_unit = (Spinner) layout.findViewById(R.id.price_unit);
		ItemPriceUnit unit;

		if (price_unit.getSelectedItemPosition() == 0) {
			unit = ItemPriceUnit.DAY;
		} else if (price_unit.getSelectedItemPosition() == 1) {
			unit = ItemPriceUnit.WEEK;
		} else {
			unit = ItemPriceUnit.MONTH;
		}

		Item item = new Item(editTitle.getText().toString().trim(),
				editDescription.getText().toString().trim());
		item.setItem_id(item_id);

		if (editPrice.getText().length() > 0) {
			item.setPrice(Double.valueOf(editPrice.getText().toString()));
		} else {
			item.setPrice(0);
		}

		item.setPrice(Double.valueOf(editPrice.getText().toString()));
		item.setItem_price_unit(unit);
		int selected = radioGroup.getCheckedRadioButtonId();

		if (selected == R.id.forrent) {
			item.setItem_state(ItemState.FOR_RENT);
		} else if (selected == R.id.notforrent) {
			item.setItem_state(ItemState.NOT_FOR_RENT);
		}
		
		//Rating
		RatingBar rating = (RatingBar)layout.findViewById(R.id.rating);
		item.setQuality_rating(Math.round(rating.getRating()));

		return item;
	}

	public Item saveItemFromValues() {
		EditText editTitle = (EditText) layout.findViewById(R.id.title);
		EditText editDescription = (EditText) layout
				.findViewById(R.id.description);
		EditText editPrice = (EditText) layout.findViewById(R.id.price);

		RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.rent);
		Spinner price_unit = (Spinner) layout.findViewById(R.id.price_unit);
		ItemPriceUnit unit;
	
		if (price_unit.getSelectedItemPosition() == 0) {
			unit = ItemPriceUnit.DAY;
		} else if (price_unit.getSelectedItemPosition() == 1) {
			unit = ItemPriceUnit.WEEK;
		} else {
			unit = ItemPriceUnit.MONTH;
		}
		ContentValues values;
		Item item = new Item(editTitle.getText().toString().trim(),
				editDescription.getText().toString().trim());
		item.setItem_id(item_id);
		if (editPrice.getText().toString().length() > 0) {
			item.setPrice(Double.valueOf(editPrice.getText().toString().trim()));
		} else {
			item.setPrice(0);
		}

		item.setItem_price_unit(unit);

		int selected = radioGroup.getCheckedRadioButtonId();

		if (selected == R.id.forrent) {
			item.setItem_state(ItemState.FOR_RENT);
		} else if (selected == R.id.notforrent) {
			item.setItem_state(ItemState.NOT_FOR_RENT);
		}
		
		
		
		//Rating
		RatingBar rating = (RatingBar)layout.findViewById(R.id.rating);
		item.setQuality_rating(Math.round(rating.getRating()));

		return item;
	}


}
