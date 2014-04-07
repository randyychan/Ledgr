package com.example.ledgr.cards;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.ledgr.R;
import com.fima.cardsui.objects.RecyclableCard;

public class CameraCard extends RecyclableCard {

	Activity activity;
	String item_id;
	Fragment fragment;

	public CameraCard(Activity activity, Fragment fragment, String item_id) {
		this.activity = activity;
		this.item_id = item_id;
		this.fragment = fragment;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.fragment_camera_card;
	}

	@Override
	protected void applyTo(View convertView) {

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dispatchTakePictureIntent();
			}
		});
	}

	private void dispatchTakePictureIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (intent.resolveActivity(activity.getPackageManager()) != null) {
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// TODO Error occurred while creating the File
			}

			System.out.println("RANDY PATH " + photoFile.getAbsolutePath());
			if (photoFile != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				fragment.startActivityForResult(intent, 666);
			}
		}

	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = activity.getExternalFilesDir(item_id);
		storageDir.mkdir();

		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);
		// Save a file: path for use with ACTION_VIEW intents
		// mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}
}
