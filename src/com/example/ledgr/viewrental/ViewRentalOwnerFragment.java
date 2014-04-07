package com.example.ledgr.viewrental;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ledgr.Utils;
import com.example.ledgr.cards.DueDateCard;
import com.example.ledgr.cards.FriendCardView;
import com.example.ledgr.create.ImageData;
import com.example.ledgr.dataobjects.ItemsData;
import com.example.ledgr.dataobjects.Rental;
import com.fima.cardsui.views.CardUI;

public class ViewRentalOwnerFragment extends Fragment {

	String item_id;
	CardUI friendCardUI, dueDateCardUI;
	Rental rental;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	public ViewRentalOwnerFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		item_id = bundle.getString("ITEMID");
		rental = ItemsData.retrieveRentalById(item_id, getActivity());
		
		friendCardUI = new CardUI(getActivity());
		dueDateCardUI = new CardUI(getActivity());
		
		LinearLayout mainLayout = new LinearLayout(getActivity()
				.getApplicationContext());		
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackgroundColor(Color.parseColor(Utils.BACKGROUND_COLOR));		
		
		friendCardUI.setPadding(0, 20, 0, 0);
		friendCardUI.addCard(new FriendCardView(rental.getOwner()));
		friendCardUI.refresh();

		dueDateCardUI.setPadding(0, 20, 0, 20);
		dueDateCardUI.addCard(new DueDateCard(getActivity(), rental.getDueDate()));
		dueDateCardUI.refresh();
		
		mainLayout.addView(friendCardUI);
		mainLayout.addView(dueDateCardUI);

		return mainLayout;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	
	private void deleteImage(ImageData imageData) {
		File file = new File(imageData.getImagePath());
		Utils.delete(file);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
