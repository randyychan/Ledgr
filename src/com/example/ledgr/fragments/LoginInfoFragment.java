package com.example.ledgr.fragments;

import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ledgr.R;
import com.example.ledgr.dataobjects.User;
import com.facebook.widget.ProfilePictureView;

public class LoginInfoFragment extends Fragment {

	ProfilePictureView profileImage;
	TextView profileName;
	EditText email;
	EditText email_confirm;
	EditText phone;
	String facebookid;
	String username;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.login_info_fragment, container, false);
		profileImage = (ProfilePictureView)view.findViewById(R.id.profile_pic);
		profileName = (TextView)view.findViewById(R.id.profile_name);
		email = (EditText) view.findViewById(R.id.email);
		email_confirm = (EditText) view.findViewById(R.id.email_confirm);
		phone = (EditText) view.findViewById(R.id.phone);
		return view;
	}
	
	public void setFacebookPrfile(String userid, String username) {
		facebookid = userid;
		this.username = username;
		profileImage.setProfileId(userid);
		profileName.setText(username);
	}
	
	public User createUser() {
		String uuid = UUID.randomUUID().toString();
		String pwdhash = "";
		String gcmId = "";
		String firstName = username;
		String facebookId = facebookid;
		String lastName = "";
		String emailAddress = email.getText().toString();
		String phoneNumber = phone.getText().toString();
		String city = "";
		double longitude = 0;
		double latitude = 0;
		
		User user = new User(uuid, pwdhash, facebookId, gcmId, firstName, lastName, emailAddress, phoneNumber, city, longitude, latitude);

		return user;
	}

}
