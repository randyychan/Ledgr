package com.example.ledgr.cards;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.example.ledgr.R;
import com.example.ledgr.rental.CreateRental;
import com.fima.cardsui.objects.RecyclableCard;

public class CalendarCard extends RecyclableCard {

	CreateRental activity;
	LocalDate date;

	public CalendarCard(CreateRental activity, LocalDate date) {
		this.activity = activity;
		this.date = date;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.fragment_calendar_card;
	}

	@Override
	protected void applyTo(View convertView) {

		if (date != null) {
			TextView text = (TextView) convertView.findViewById(R.id.date);
			text.setText((date.getMonthOfYear() + 1) + "/"
					+ date.getDayOfMonth() + "/" + date.getYear());
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DateTime now = DateTime.now();
				CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
						.newInstance(activity, now.getYear(),
								now.getMonthOfYear() - 1, now.getDayOfMonth());
				calendarDatePickerDialog.show(
						activity.getSupportFragmentManager(),
						"fragment_date_picker_name");
			}
		});
	}

}
