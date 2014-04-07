package com.example.ledgr.cards;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ledgr.R;
import com.fima.cardsui.objects.RecyclableCard;

public class ItemCard extends RecyclableCard {

	String time;
	String description;
	String card_id;
	Bitmap bitmap;

	public ItemCard(String title, String time, String description,
			String card_id, Bitmap bitmap) {
		super(title);
		this.time = time;
		this.description = description;
		this.card_id = card_id;
		this.bitmap = bitmap;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_ex;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView) convertView.findViewById(R.id.title)).setText(title);
		((TextView) convertView.findViewById(R.id.time)).setText(time);
		((TextView) convertView.findViewById(R.id.description))
				.setText(description);
		((TextView) convertView.findViewById(R.id.card_id)).setText(card_id);
		((ImageView) convertView.findViewById(R.id.image))
		.setImageBitmap(bitmap);
	}
}
