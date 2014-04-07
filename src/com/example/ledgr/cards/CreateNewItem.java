package com.example.ledgr.cards;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.ledgr.ActivityLauncher;
import com.example.ledgr.R;
import com.fima.cardsui.objects.RecyclableCard;

public class CreateNewItem extends RecyclableCard {

	Activity activity;
	Context context;
	public CreateNewItem(Context context, Activity activity) {
		this.activity = activity;
		this.context = context;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.fragment_create_new_item;
	}

	@Override
	protected void applyTo(View convertView) {

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityLauncher.CreateItem(context , activity, false, null);


			}
		});

	}
}
