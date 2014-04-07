package com.example.ledgr.cards;

import android.app.Activity;
import android.view.View;

import com.example.ledgr.R;
import com.fima.cardsui.objects.RecyclableCard;

public class RentalOptionsCard extends RecyclableCard {

	Activity activity;

	public RentalOptionsCard(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.fragment_rental_options_card;
	}

	@Override
	protected void applyTo(View convertView) {

	}

}
