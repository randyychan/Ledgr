package com.example.ledgr.cards;

import android.view.View;
import android.widget.TextView;

import com.example.ledgr.R;
import com.example.ledgr.rental.CreateRental;
import com.fima.cardsui.objects.RecyclableCard;

public class DateCard extends RecyclableCard {
	int day, month, year;

	public DateCard(int month, int day, int year) {
		this.month = month;
		this.day = day;
		this.year = year;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.fragment_calendar_card;
	}

	@Override
	protected void applyTo(View convertView) {
		TextView text = (TextView) convertView.findViewById(R.id.date);
		text.setText((month + 1) + "/" + day + "/" + year);

	}

}
