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
import com.example.ledgr.cards.ItemCardOnline;
import com.example.ledgr.dataobjects.ItemsData;
import com.example.ledgr.dataobjects.Rental;
import com.example.ledgr.dataobjects.User;
import com.example.ledgr.venmo.VenmoLibrary;
import com.example.ledgr.venmo.VenmoLibrary.VenmoResponse;
import com.example.ledgr.venmo.VenmoWebViewActivity;
import com.example.ledgr.viewrental.ViewRentalOnline;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CurrentlyRenting extends Fragment {

	View.OnClickListener onClickListener;
	ArrayList<CardUI> cardViews;
	LinearLayout layout;
	ScrollView scroller;
	LayoutInflater myInflater;
	ArrayList<View> views;
    ArrayList<MLRoundedImageView> profilePictures;
    ArrayList<User> users;
    ArrayList<Bitmap> bitmaps;
    String venmo_item_id = null;
    ReloadFragments task;
	public CurrentlyRenting() {
		onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView text = (TextView) v.findViewById(R.id.card_id);
				String item_id = (String) text.getText();
				venmo_item_id = item_id;
				Rental rental = ItemsData.retrieveRentalById(item_id, getActivity());
				boolean pending = rental.isPending();
				
				if (pending) {
					double price = rental.getPrice();
					String title = rental.getTitle();
					User user = rental.getOwner();
					
					
					try { 
						Intent venmoIntent = VenmoLibrary.openVenmoPayment(VenmoLibrary.APP_ID,
																		   VenmoLibrary.NAME,
																		   "ksutardji@gmail.com",
																		   String.valueOf(price), 
																		   "Payment for: " + title,
																	   	   "pay");
						startActivityForResult(venmoIntent, 1); 
				
					} catch (android.content.ActivityNotFoundException e) { 
						Intent venmoIntent = new Intent(getActivity(), VenmoWebViewActivity.class);
						String venmo_uri = VenmoLibrary.openVenmoPaymentInWebView(VenmoLibrary.APP_ID,
																				  VenmoLibrary.NAME,
																				  "ksutardji@gmail.com",
																				  String.valueOf(price), 
																				  "Payment for: " + title,
							   												  	  "pay");
						venmoIntent.putExtra("url", venmo_uri); startActivityForResult(venmoIntent, 1); 
					}
				} else {
					
					Intent intent = new Intent(getActivity(), ViewRentalOnline.class);
					Bundle bundle = new Bundle();
					bundle.putString("ITEMID",item_id);
					intent.putExtras(bundle);
					getActivity().startActivityForResult(intent, 999);
				}
				
			}
		};
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("VENMO DONE!");
	    switch(requestCode) {
        case 1: { //1 is the requestCode we picked for Venmo earlier when we called startActivityForResult
            if(resultCode == getActivity().RESULT_OK) {
                String signedrequest = data.getStringExtra("signedrequest");
                if(signedrequest != null) {
                    VenmoResponse response = (new VenmoLibrary()).validateVenmoPaymentResponse(signedrequest, VenmoLibrary.SECRET);
                    if(response.getSuccess().equals("1")) {
                        //Payment successful.  Use data from response object to display a success message
                        String note = response.getNote();
                        String amount = response.getAmount();
                        
                        //need to notify the server that the payment is successful.
                        
                        //change the rentals database from pending to complete.
                        ItemsData.venmoChargeComplete(venmo_item_id, getActivity());
                        // refreshViews();
                    }
                }
                else {
                    String error_message = data.getStringExtra("error_message");
                    //An error ocurred.  Make sure to display the error_message to the user
                }                               
            }
            else if(resultCode == getActivity().RESULT_CANCELED) {
                //The user cancelled the payment
            }
        break;
        }           
    }
		
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
		
		users = ItemsData.retrieveUserIdsCurrentlyRenting(getActivity());
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
				views.add(createUserRentals(ItemsData.retrieveItemsLentByUserId(u.getFacebookId(), getActivity())));
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
		
		return scroller;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//refreshViews();
	}

	public void refreshViews() {
		layout.removeAllViews();
		views = new ArrayList<View>();
		cardViews = new ArrayList<CardUI>();
		profilePictures = new ArrayList<MLRoundedImageView>();
		
		users = ItemsData.retrieveUserIdsCurrentlyRenting(getActivity());
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
				views.add(createUserRentals(ItemsData.retrieveItemsLentByUserId(u.getFacebookId(), getActivity())));
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

			users = ItemsData.retrieveUserIdsCurrentlyRenting(getActivity());
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
					views.add(createUserRentals(ItemsData.retrieveItemsLentByUserId(u.getFacebookId(), getActivity())));
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
			
			new FacebookPictures().execute(new Void[] {});
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
		
		String url = "http://graph.facebook.com/" + user.getFacebookId() + "/picture?type=small";
		ImageLoader.getInstance().displayImage(url, roundedImage);
		//roundedImage.setImageBitmap(getFacebookProfilePicture(user.getFacebookId()));
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
	
	public View createUserRentals(ArrayList<Rental> rentals) {

		CardUI card_view = new CardUI(getActivity().getApplicationContext());
		cardViews.add(card_view);
		card_view.setSwipeable(false);
		CardStack card_stack = new CardStack();
		card_view.addStack(card_stack);
		ItemCardOnline myCard;

		for (Rental i : rentals) {
			myCard = Utils.createCardViewOnline(i, getActivity());
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
