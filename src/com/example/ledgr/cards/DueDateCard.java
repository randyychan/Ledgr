package com.example.ledgr.cards;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.ledgr.R;
import com.fima.cardsui.objects.RecyclableCard;

public class DueDateCard extends RecyclableCard {

	Activity activity;
	LocalDate dateTime;

	public DueDateCard(Activity activity, LocalDate dateTime) {
		this.activity = activity;
		this.dateTime = dateTime;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.fragment_calendar_card;
	}

	@Override
	protected void applyTo(View convertView) {

		if (dateTime != null) {
			TextView date = (TextView) convertView.findViewById(R.id.date);
			TextView dateWeek = (TextView) convertView.findViewById(R.id.dateweek);
			TextView dateNum = (TextView) convertView.findViewById(R.id.datenum);
			TextView dateWeekSmall = (TextView) convertView.findViewById(R.id.dateoftheweek);
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

}
