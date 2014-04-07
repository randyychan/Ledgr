package com.example.ledgr.create;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ledgr.R;
import com.example.ledgr.Utils;
import com.example.ledgr.cards.CameraCard;
import com.example.ledgr.cards.EmptyItemListCard;
import com.example.ledgr.cards.EmptyPictureCreateCard;
import com.example.ledgr.cards.EmptyPictureViewCard;
import com.example.ledgr.cards.PictureCard;
import com.fima.cardsui.objects.Card;
import com.fima.cardsui.objects.Card.OnCardSwiped;
import com.fima.cardsui.views.CardUI;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class CreateItemPictureFragment extends Fragment {
	ArrayList<ImageData> pictureArray;
	CardUI noItem;
	ArrayList<CardUI> cardUIS;
	CardUI picturesUI;
	String item_id;
	DisplayImageOptions options;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	public CreateItemPictureFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		Bundle bundle = getArguments();
		item_id = bundle.getString("ITEMID");
		pictureArray = new ArrayList<ImageData>();
		
		cardUIS = new ArrayList<CardUI>();
		LinearLayout mainLayout = new LinearLayout(getActivity()
				.getApplicationContext());
		mainLayout.setOrientation(LinearLayout.VERTICAL);

		CardUI newItem = new CardUI(getActivity());
		noItem = new CardUI(getActivity());
		newItem.setPadding(0, 20, 0, 0);
		newItem.addCard(new CameraCard(getActivity(), this, item_id));

		noItem.addCard(new EmptyItemListCard(getActivity()));
		noItem.setPadding(0, 20, 0, 0);

		noItem.refresh();

		picturesUI = new CardUI(getActivity());
		picturesUI.setSwipeable(true);
		picturesUI.setPadding(0, 20, 0, 0);

		mainLayout.addView(newItem);
		newItem.refresh();

		//if (pictureArray.size() > 0) {
			mainLayout.addView(picturesUI);
		//} else {
			//mainLayout.addView(noItem);
		//}
		mainLayout.setBackgroundColor(Color.parseColor(Utils.BACKGROUND_COLOR));

		return mainLayout;
	}

	public void addPicture(ImageData imageData) {
		pictureArray.add(imageData);
	}

	public void replacePictureList(ArrayList<ImageData> list) {
		pictureArray = list;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		retrievePictures(item_id);
		refreshPictureUI();
	}
	
	private void retrievePictures(String item_id) {
		pictureArray.clear();
		File dir = getActivity().getExternalFilesDir(item_id);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (File f : files) {
				if (f.length() == 0) {
					f.delete();
					continue;
				}
				pictureArray.add(new ImageData(ThumbnailUtils.extractThumbnail(
						BitmapFactory.decodeFile(f.getAbsolutePath(), options),
						300, 300), f.getAbsolutePath()));
			}
		}
	}

	private class GetImagesTask extends AsyncTask<String, Void, Void> {
		protected Void doInBackground(String... strings) {
			retrievePictures(strings[0]);
			return null;
		}

		protected void onProgressUpdate(Void... progress) {
		}

		protected void onPostExecute(Void result) {
			refreshPictureUI();
		}
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
	
	public void refreshPictureUI() {
		picturesUI.clearCards();

		if (pictureArray.size() == 0) {
			picturesUI.addCard(new EmptyPictureCreateCard(getActivity(), this, item_id));
			return;
		}
		
		for (ImageData b : pictureArray) {
			PictureCard card = new PictureCard(getActivity(), b.getImagePath(), options);
			card.setOnCardSwipedListener(new OnCardSwiped() {

				@Override
				public void onCardSwiped(Card card, View layout) {
					// TODO Auto-generated method stub
					deleteImage((ImageData) card.getData());
				}
			});
			
			card.setData(b);
			picturesUI.addCard(card);
		}
		picturesUI.refresh();
		picturesUI.setSwipeable(true);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 666 && resultCode == Activity.RESULT_OK) {

			File dir = getActivity().getExternalFilesDir(item_id);
			File[] fileList = dir.listFiles();

			ArrayList<ImageData> bitmapList = new ArrayList<ImageData>();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			for (File f : fileList) {
				if (f.length() == 0) {
					f.delete();
					continue;
				}
				Bitmap bitmap = ThumbnailUtils.extractThumbnail(
						BitmapFactory.decodeFile(f.getAbsolutePath(), options),
						300, 300);
				bitmapList.add(new ImageData(bitmap, f.getAbsolutePath()));
			}
			replacePictureList(bitmapList);

			refreshPictureUI();
		}
	}
	
}
