package com.example.ledgr.cards;

import android.view.View;
import android.widget.TextView;

import com.example.ledgr.R;
import com.example.ledgr.dataobjects.User;
import com.facebook.widget.ProfilePictureView;
import com.fima.cardsui.objects.RecyclableCard;

public class FriendCardView extends RecyclableCard {

	User user;

	public FriendCardView(User user) {
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

	}



}
