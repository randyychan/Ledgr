package com.example.ledgr.cards;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.ledgr.R;
import com.facebook.widget.ProfilePictureView;
import com.fima.cardsui.objects.RecyclableCard;

public class FacebookProfileCard extends RecyclableCard {
	String username, userid;

	Activity activity;

	public FacebookProfileCard(String username, String userid, Activity activity) {
		this.activity = activity;
		this.username = username;
		this.userid = userid;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.profile_picture;
	}

	@Override
	protected void applyTo(View convertView) {
		ProfilePictureView profilePictureView = (ProfilePictureView) convertView
				.findViewById(R.id.profile_pic);
		// profilePictureView.setPresetSize(ProfilePictureView.SMALL);
		// profilePictureView.setCropped(true);
		profilePictureView.setProfileId(userid);
		// Find the user's name view
		TextView userNameView = (TextView) convertView
				.findViewById(R.id.profile_name);
		userNameView.setText(username);
	}

}
