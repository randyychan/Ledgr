package com.example.ledgr.cards;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.ledgr.ActivityLauncher;
import com.example.ledgr.R;
import com.example.ledgr.create.CreateItem;
import com.fima.cardsui.objects.RecyclableCard;

public class EmptyPictureViewCard extends RecyclableCard {

	Activity activity;

	public EmptyPictureViewCard(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.fragment_empty_picture_card;
	}

	@Override
	protected void applyTo(View convertView) {

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
	}
}
