package com.example.ledgr.viewitem;

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
import android.widget.ScrollView;

import com.example.ledgr.R;
import com.example.ledgr.Utils;
import com.example.ledgr.cards.ViewItemCard;
import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemPriceUnit;
import com.example.ledgr.dataobjects.ItemState;
import com.example.ledgr.dataobjects.ItemsData;
import com.example.ledgr.dataobjects.Rental;
import com.fima.cardsui.views.CardUI;

public class ViewItemFragment extends Fragment {

	Rental rental;
	ScrollView scroller;
	LinearLayout layout;
	CardUI infoUI;
	ViewItemCard viewItemCard;
	protected String item_id, title, description, price;
	int rating;
	ItemState itemState;
	ItemPriceUnit unit;
	public ViewItemFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		item_id = getArguments().getString("ITEMID");
		rental = ItemsData.retrieveRentalById(item_id, getActivity());
		title = rental.getTitle();
		description = rental.getDescription();
		price = "";//
		itemState = ItemState.FOR_RENT;
		unit = rental.getItem_price_unit();
		rating = rental.getQuality();
		
		View rootView = inflater.inflate(R.layout.fragment_create_item_base,
				container, false);

		scroller = (ScrollView) rootView.findViewById(R.id.scroll);

		layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);

		viewItemCard = new ViewItemCard(getActivity(), description, price,
				title, itemState, unit, rating);
		infoUI = new CardUI(getActivity());
		infoUI.addCard(viewItemCard);

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

	public void reloadInfo() {
		scroller.removeAllViews();
		
		rental = ItemsData.retrieveRentalById(item_id, getActivity());
		title = rental.getTitle();
		description = rental.getDescription();
		price = "";//rental.getPriceString();
		itemState = ItemState.FOR_RENT;//rental.getItem_state();
		unit = rental.getItem_price_unit();
		rating = rental.getQuality();
		
		layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);

		viewItemCard = new ViewItemCard(getActivity(), description, price,
				title, itemState, unit, rating);
		infoUI = new CardUI(getActivity());
		infoUI.addCard(viewItemCard);

		infoUI.refresh();
		infoUI.setPadding(0, 0, 0, 20);
		View view = new View(getActivity());
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 20));
		layout.addView(view);

		layout.addView(infoUI);
		scroller.addView(layout);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}


	public Item createItemFromValues() {
		EditText editTitle = (EditText) layout.findViewById(R.id.title);
		EditText editDescription = (EditText) layout
				.findViewById(R.id.description);
		EditText editPrice = (EditText) layout.findViewById(R.id.price);
		RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.rent);

		Item item = new Item(editTitle.getText().toString(), editDescription
				.getText().toString());
		item.setItem_id(item_id);
		item.setPrice(Double.valueOf(editPrice.getText().toString()));
		int selected = radioGroup.getCheckedRadioButtonId();

		if (selected == R.id.forrent) {
			item.setItem_state(ItemState.FOR_RENT);
		} else if (selected == R.id.notforrent) {
			item.setItem_state(ItemState.NOT_FOR_RENT);
		}

		return item;
	}


}
