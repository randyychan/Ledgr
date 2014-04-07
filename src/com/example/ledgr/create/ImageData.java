package com.example.ledgr.create;

import android.graphics.Bitmap;

public class ImageData {
	protected Bitmap bitmap;
	protected String imagePath;

	public ImageData(Bitmap bitmap, String imagePath) {
		this.bitmap = bitmap;
		this.imagePath = imagePath;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
