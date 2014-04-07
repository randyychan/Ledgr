package com.example.ledgr.rental;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ledgr.PickerActivity;
import com.example.ledgr.R;
import com.example.ledgr.Utils;
import com.example.ledgr.dataobjects.User;

public class ChooseFriendFragment extends Fragment {

	public interface onFriendSelectedListener {
		public void selectedUser(User user);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			friendSelectedListener = (onFriendSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement onFriendSelectedListener");
		}
	}

	onFriendSelectedListener friendSelectedListener;

	EditText name, facebook;
	Button button;

	public ChooseFriendFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_pick_friend,
				container, false);
		name = (EditText) rootView.findViewById(R.id.name);
		facebook = (EditText) rootView.findViewById(R.id.facebook);
		button = (Button) rootView.findViewById(R.id.select);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				User user = new User(facebook.getText().toString(), name
						.getText().toString());
				friendSelectedListener.selectedUser(user);

			}
		});
		startPickerActivity(null, 555);
		rootView.setBackgroundColor(Color.parseColor(Utils.BACKGROUND_COLOR));

		return rootView;
	}

	private void startPickerActivity(Uri data, int requestCode) {
		Intent intent = new Intent();
		intent.setData(data);
		intent.setClass(getActivity(), PickerActivity.class);
		startActivityForResult(intent, requestCode);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

}
