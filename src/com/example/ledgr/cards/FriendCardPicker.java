package com.example.ledgr.cards;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.ledgr.PickerActivity;
import com.example.ledgr.R;
import com.example.ledgr.dataobjects.User;
import com.example.ledgr.rental.CreateRental;
import com.facebook.widget.ProfilePictureView;
import com.fima.cardsui.objects.RecyclableCard;

public class FriendCardPicker extends RecyclableCard {

	CreateRental activity;
	User user;

	public FriendCardPicker(CreateRental activity, User user) {
		this.activity = activity;
		this.user = user;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.friend_card;
	}

	@Override
	protected void applyTo(View convertView) {

		if (user != null) {
			ProfilePictureView profilePicture = (ProfilePictureView) convertView
					.findViewById(R.id.profile_pic);
			TextView profileName = (TextView) convertView
					.findViewById(R.id.profile_name);
			System.out.println("RANDY USER ID " + user.getFacebookId());
			profileName.setText(user.getFirstName());
			profilePicture.setProfileId(user.getFacebookId());

		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startPickerActivity(null, 555);
			}
		});
	}

	private void startPickerActivity(Uri data, int requestCode) {
		Intent intent = new Intent();
		intent.setData(data);
		intent.setClass(activity, PickerActivity.class);
		activity.startActivityForResult(intent, requestCode);
	}

}
