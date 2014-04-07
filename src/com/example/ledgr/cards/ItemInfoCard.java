package com.example.ledgr.cards;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ledgr.R;
import com.example.ledgr.create.CreateItemFragment;
import com.example.ledgr.dataobjects.ItemPriceUnit;
import com.example.ledgr.dataobjects.ItemState;
import com.fima.cardsui.objects.RecyclableCard;



public class ItemInfoCard extends RecyclableCard {

	String[] ratingStrings = new String[] {"Broken! Completely unusable!","Almost broken... Kinda usable.",
			"Still working, but its seen better days.",
			"Looks like its been used, still working good!",
			"Looks good, works good!",
			"Almost perfect condition!"};
	
	
	EditText editTitle, editDescription, editPrice;
	RadioGroup radioGroup;
	CreateItemFragment fragment;
	String title, description, price;
	ItemState state;
	Spinner unit_spinner;
	ItemPriceUnit unit;
	RatingBar ratingBar;
	int rating;
	public ItemInfoCard(CreateItemFragment fragment, String title, String description,
			String price, ItemState state, ItemPriceUnit unit, int rating) {
		this.fragment = fragment;
		this.title = title;
		this.description = description;
		this.price = price;
		this.state = state;
		this.unit = unit;
		this.rating = rating;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.fragment_create_item;
	}

	@Override
	protected void applyTo(View convertView) {
		ratingBar = (RatingBar) convertView.findViewById(R.id.rating);
		ratingBar.setRating((float)rating);
		TextView ratingtext = (TextView) convertView.findViewById(R.id.ratingstring);
		ratingtext.setText(ratingStrings[rating]);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				fragment.setRatingText(Math.round(rating));
			}
		});
		
		editTitle = (EditText) convertView.findViewById(R.id.title);
		editDescription = (EditText) convertView.findViewById(R.id.description);
		editPrice = (EditText) convertView.findViewById(R.id.price);
		radioGroup = (RadioGroup) convertView.findViewById(R.id.rent);
		unit_spinner = (Spinner) convertView.findViewById(R.id.price_unit);

		editPrice.addTextChangedListener(new CurrencyFormat());

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				fragment.getActivity(), R.array.item_unit_strings,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		unit_spinner.setAdapter(adapter);

		if (unit == ItemPriceUnit.DAY) {
			unit_spinner.setSelection(0);
		} else if (unit == ItemPriceUnit.WEEK) {
			unit_spinner.setSelection(1);
		} else {
			unit_spinner.setSelection(2);
		}

		editTitle.setText(title);
		editDescription.setText(description);
		editPrice.setText(price);

		if (state == ItemState.FOR_RENT) {
			radioGroup.check(R.id.forrent);
		} else {
			radioGroup.check(R.id.notforrent);
		}
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

	public static class CurrencyFormat implements TextWatcher {

		public void onTextChanged(CharSequence arg0, int start, int arg2,
				int arg3) {
		}

		public void beforeTextChanged(CharSequence arg0, int start, int arg2,
				int arg3) {
		}

		public void afterTextChanged(Editable arg0) {
			int length = arg0.length();
			if (length > 0) {
				if (nrOfDecimal(arg0.toString()) > 2)
					arg0.delete(length - 1, length);
			}

		}

		private int nrOfDecimal(String nr) {
			int len = nr.length();
			int pos = len;
			for (int i = 0; i < len; i++) {
				if (nr.charAt(i) == '.') {
					pos = i + 1;
					break;
				}
			}
			return len - pos;
		}
	}

}
