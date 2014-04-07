package com.example.ledgr.cards;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.example.ledgr.R;
import com.fima.cardsui.objects.RecyclableCard;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PictureCard extends RecyclableCard {

	Activity activity;
	String filePath;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	
	public PictureCard(Activity activity, String filePath, DisplayImageOptions options) {
		this.activity = activity;
		this.filePath = filePath;
		this.options = options;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.fragment_picture_card;
	}

	@Override
	protected void applyTo(View convertView) {
		ImageView image = (ImageView) convertView.findViewById(R.id.picture);
		image.setScaleType(ScaleType.CENTER_CROP);
		imageLoader.displayImage("file://"+filePath, image, options);
		image.setTag(filePath);
	}

}
