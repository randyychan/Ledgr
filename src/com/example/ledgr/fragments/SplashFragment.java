package com.example.ledgr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ledgr.R;

public class SplashFragment extends Fragment {

	private Button skipLoginButton;
	private SkipLoginCallback skipLoginCallback;

	public interface SkipLoginCallback {
		void onSkipLoginPressed();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.splash, container, false);


		return view;
	}

	public void setSkipLoginCallback(SkipLoginCallback callback) {
		skipLoginCallback = callback;
	}
}
