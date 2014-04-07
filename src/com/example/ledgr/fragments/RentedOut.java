package com.example.ledgr.fragments;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ledgr.MLRoundedImageView;
import com.example.ledgr.R;
import com.example.ledgr.Utils;
import com.example.ledgr.cards.EmptyRentalListCard;
import com.example.ledgr.cards.ItemCard;
import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemsData;
import com.example.ledgr.dataobjects.User;
import com.example.ledgr.viewrental.ViewRental;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RentedOut extends Fragment {

	View.OnClickListener onClickListener;
	ArrayList<CardUI> cardViews;
	LinearLayout layout;
	ScrollView scroller;
	LayoutInflater myInflater;
	ArrayList<View> views;
    ArrayList<MLRoundedImageView> profilePictures;
    ArrayList<User> users;
    ArrayList<Bitmap> bitmaps;
    
	public RentedOut() {
		onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
//				// TODO Auto-generated method stub
				TextView text = (TextView) v.findViewById(R.id.card_id);
				String item_id = (String) text.getText();
//				MainActivity.data.cancelRentalById(item_id);
//				refreshViews();
				Intent intent = new Intent(getActivity(), ViewRental.class);
				Bundle bundle = new Bundle();
				bundle.putString("ITEMID",item_id);
				intent.putExtras(bundle);
				getActivity().startActivityForResult(intent, 999);
				
			}
		};
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		profilePictures = new ArrayList<MLRoundedImageView>();
		views = new ArrayList<View>();
		cardViews = new ArrayList<CardUI>();
		myInflater = inflater;
		scroller = new ScrollView(getActivity().getApplicationContext());
		layout = new LinearLayout(getActivity().getApplicationContext());
		layout.setPadding(0, 5, 0, 5);
		layout.setOrientation(LinearLayout.VERTICAL);

		scroller.addView(layout);
		scroller.setBackgroundColor(Color.parseColor(Utils.BACKGROUND_COLOR));
		

		return scroller;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		refreshViews();
	}

	public void refreshViews() {
		layout.removeAllViews();
		views = new ArrayList<View>();
		cardViews = new ArrayList<CardUI>();
		profilePictures = new ArrayList<MLRoundedImageView>();
		users = ItemsData.retrieveUserIdsRenting(getActivity());
		bitmaps = new ArrayList<Bitmap>();
		if (users.size() == 0) {
			CardUI cards = new CardUI(getActivity());
			cards.setPadding(0, 20, 0, 20);
			cards.addCard(new EmptyRentalListCard(getActivity()));
			cards.refresh();
			views.add(cards);
		} else {
			for (User u : users) {
				views.add(createProfile(u));
				views.add(createUserRentals(ItemsData.retrieveItemsRentedByUserId(u.getFacebookId(), getActivity())));
			}
		}
		for (int i = 0; i < views.size(); i++) {
			View v = views.get(i);
			v.setVisibility(LinearLayout.VISIBLE);
			Animation animation = AnimationUtils.loadAnimation(
					getActivity(), R.animator.fade_in);
			animation.setDuration(300);
			animation.setStartOffset(i * 100);
			v.setAnimation(animation);
			layout.addView(v);
		}
	}

	private class ReloadFragments extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... voids) {

			users = ItemsData.retrieveUserIdsRenting(getActivity());
			bitmaps = new ArrayList<Bitmap>();
			if (users.size() == 0) {
				CardUI cards = new CardUI(getActivity());
				cards.setPadding(0, 20, 0, 20);
				cards.addCard(new EmptyRentalListCard(getActivity()));
				cards.refresh();
				views.add(cards);
			} else {
				for (User u : users) {
					views.add(createProfile(u));
					views.add(createUserRentals(ItemsData.retrieveItemsRentedByUserId(u.getFacebookId(), getActivity())));
				}
			}

			return null;
		}

		protected void onProgressUpdate(Void... progress) {
		}

		protected void onPostExecute(Void result) {
			for (int i = 0; i < views.size(); i++) {
				View v = views.get(i);
				v.setVisibility(LinearLayout.VISIBLE);
				Animation animation = AnimationUtils.loadAnimation(
						getActivity(), R.animator.fade_in);
				animation.setDuration(300);
				animation.setStartOffset(i * 100);
				v.setAnimation(animation);
				layout.addView(v);
			}
			
			//new FacebookPictures().execute(new Void[] {});
		}
	}

	public View createProfile(User user) {
		View rootView = myInflater.inflate(R.layout.profile_picture, null,
				false);
		rootView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				User user = ((User) v.getTag());
//
//				Intent intent = new Intent(getActivity(), UserItems.class);
//				intent.putExtra("USERID", user.getUserId());
//				intent.putExtra("USERNAME", user.getName());
//				startActivity(intent);
			}
		});
		rootView.setTag(user);
		
		MLRoundedImageView roundedImage = (MLRoundedImageView) rootView.findViewById(R.id.round_image);
		//roundedImage.setImageBitmap(getFacebookProfilePicture(user.getFacebookId()));
		
		String url = "http://graph.facebook.com/" + user.getFacebookId() + "/picture?type=small";
		ImageLoader.getInstance().displayImage(url, roundedImage);
		profilePictures.add(roundedImage);
		TextView profile_name = (TextView) rootView
				.findViewById(R.id.profile_name);
		profile_name.setText(user.getFirstName());
		return rootView;
	}
	
	
	private class FacebookPictures extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... voids) {
			for (User user : users) {
				bitmaps.add(getFacebookProfilePicture(user.getFacebookId()));
			}

			return null;
		}

		protected void onProgressUpdate(Void... progress) {
		}

		protected void onPostExecute(Void result) {
			for (int i = 0; i < bitmaps.size(); i++) {
				Bitmap bitmap = bitmaps.get(i);
				profilePictures.get(i).setImageBitmap(bitmap);
			}
		}
	}

	public static Bitmap getFacebookProfilePicture(String userID){
	    URL imageURL = null;
		try {
			imageURL = new URL("http://graph.facebook.com/" + userID + "/picture?type=small");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Bitmap bitmap = null;
		try {
			//THIS ISNT WORKING!!
			bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return bitmap;
	} 
	
	public View createUserRentals(ArrayList<Item> items) {

		CardUI card_view = new CardUI(getActivity().getApplicationContext());
		cardViews.add(card_view);
		card_view.setSwipeable(false);
		CardStack card_stack = new CardStack();
		card_view.addStack(card_stack);
		ItemCard myCard;

		for (Item i : items) {
			myCard = Utils.createCardView(i, getActivity());
			myCard.setOnClickListener(onClickListener);
			card_view.addCardToLastStack(myCard);
		}
		card_view.setPadding(0, 0, 0, 20);
		card_view.refresh();
		return card_view;
	}

	public void animateCards() {
		for (int i = 0; i < views.size(); i++) {
			View v = views.get(i);
			v.setVisibility(LinearLayout.VISIBLE);
			Animation animation = AnimationUtils.loadAnimation(getActivity(),
					R.animator.fade_in);
			animation.setDuration(200);
			animation.setStartOffset((i + 1) * 75);
			v.setAnimation(animation);
		}
	}
}
