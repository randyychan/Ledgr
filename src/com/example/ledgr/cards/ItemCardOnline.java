package com.example.ledgr.cards;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ledgr.R;
import com.fima.cardsui.objects.RecyclableCard;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ItemCardOnline extends RecyclableCard {

	String time;
	String description;
	String card_id;
	String rental_id;
	ImageLoader imageLoader;
	public ItemCardOnline(String title, String time, String description,
			String card_id) {
		super(title);
		this.time = time;
		this.description = description;
		this.card_id = card_id;
		imageLoader = ImageLoader.getInstance();
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
		ImageView image = (ImageView) convertView.findViewById(R.id.image);
		imageLoader.displayImage("http://kevinsutardji.com/ledger/media/images/" + card_id + "/" + card_id + "_0.jpg", image);
	}
}
