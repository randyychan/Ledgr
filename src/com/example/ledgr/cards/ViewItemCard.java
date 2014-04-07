package com.example.ledgr.cards;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ledgr.R;
import com.example.ledgr.dataobjects.ItemPriceUnit;
import com.example.ledgr.dataobjects.ItemState;
import com.fima.cardsui.objects.RecyclableCard;

public class ViewItemCard extends RecyclableCard {

	TextView textDescription, textPrice, textTitle;
	RadioGroup radioGroup;
	Activity activity;
	ItemState state;
	ItemPriceUnit unit;
	String title, description, price;
	Spinner unit_spinner;
	RatingBar ratingbar;
	int rating;
	
	String[] ratingStrings = new String[] {"Broken! Completely unusable!","Almost broken... Kinda usable.",
			"Still working, but its seen better days.",
			"Looks like its been used, still working good!",
			"Looks good, works good!",
			"Almost perfect condition!"};
	
	public ViewItemCard(Activity activity, String description, String price,
			String title, ItemState state, ItemPriceUnit unit, int rating) {
		this.activity = activity;
		this.description = description;
		this.price = price;
		this.title = title;
		this.state = state;
		this.unit = unit;
		this.rating = rating;
	}
	
	

	@Override
	protected int getCardLayoutId() {
		return R.layout.fragment_view_item;
	}

	@Override
	protected void applyTo(View convertView) {
		textDescription = (TextView) convertView.findViewById(R.id.description);
		textDescription.setText(description);

		textPrice = (TextView) convertView.findViewById(R.id.price);
		textPrice.setText(price);

		textTitle = (TextView) convertView.findViewById(R.id.title);
		textTitle.setText(title);

		radioGroup = (RadioGroup) convertView.findViewById(R.id.rent);
		unit_spinner = (Spinner) convertView.findViewById(R.id.price_unit);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				activity, R.array.item_unit_strings,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		unit_spinner.setAdapter(adapter);

		if (state == ItemState.FOR_RENT) {
			radioGroup.check(R.id.forrent);
		} else {
			radioGroup.check(R.id.notforrent);
		}
		radioGroup.setFocusable(false);
		radioGroup.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		radioGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		if (unit == ItemPriceUnit.DAY) {
			unit_spinner.setSelection(0);
		} else if (unit == ItemPriceUnit.WEEK) {
			unit_spinner.setSelection(1);
		} else {
			unit_spinner.setSelection(2);
		}
		unit_spinner.setFocusable(false);
		unit_spinner.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		ratingbar = (RatingBar) convertView.findViewById(R.id.rating);
		ratingbar.setRating(rating);
		
		ratingbar.setOnTouchListener(new OnTouchListener() {
		        public boolean onTouch(View v, MotionEvent event) {
		            return true;
		        }
		    });
		ratingbar.setFocusable(false);

		TextView ratingText = (TextView) convertView.findViewById(R.id.ratingstring);
		ratingText.setText(ratingStrings[rating]);
	}

	@Override
	public boolean convert(View convertCardView) {
		// TODO Auto-generated method stub

		return super.convert(convertCardView);

	}

	@Override
	public OnClickListener getClickListener() {
		// TODO Auto-generated method stub
		return super.getClickListener();
	}

	@Override
	protected int getCardLayout() {
		// TODO Auto-generated method stub
		return super.getCardLayout();
	}

}
