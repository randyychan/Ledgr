package com.example.ledgr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ledgr.R;

public class FindItemsForRent extends Fragment {
	public static final String ARG_PLANET_NUMBER = "planet_number";

	public FindItemsForRent() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_find_items_for_rent,
				container, false);

		return rootView;
	}
}
