package com.example.ledgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.example.ledgr.dataobjects.ItemsData;
import com.facebook.model.GraphUser;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class LedgrApplication extends Application {
	private List<GraphUser> selectedUsers;

	private static final String TAG = "LedgrApplication";

	public List<GraphUser> getSelectedUsers() {
		Log.d(TAG, "getSelectedUsers");
		return selectedUsers;
	}

	public void setSelectedUsers(List<GraphUser> users) {
		Log.d(TAG, "setSelectedUsers");

		selectedUsers = users;
	}
	
	
	@Override
	public void onCreate() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
//			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}

		super.onCreate();

		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	
//
//	public void save() {
//		Log.i(TAG, "save");
//		new SaveAsync().execute((new Void[] {}));
//
//	}
//
//	private class SaveAsync extends AsyncTask<Void, Void, Void> {
//		protected Void doInBackground(Void... voids) {
//
//			try {
//				File dir = getExternalFilesDir(null);
//				File f = new File(dir.getAbsolutePath() + "_data");
//				FileOutputStream fileOut = new FileOutputStream(f);
//				ObjectOutputStream out = new ObjectOutputStream(fileOut);
//				out.writeObject(data);
//				out.close();
//				fileOut.close();
//				Log.i(TAG, "SaveAsync success");
//				data.setNeedToSave(false);
//			} catch (IOException i) {
//				Log.i(TAG, "SaveAsync fail");
//
//				i.printStackTrace();
//			}
//			return null;
//		}
//
//		protected void onProgressUpdate(Void... progress) {
//		}
//
//		protected void onPostExecute(Void result) {
//
//		}
//	}
//	
//	
//
//	public void restore() {
//		Log.i(TAG, "restore");
//
//		try {
//			new RestoreAsync().execute((new Void[] {})).get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	private class RestoreAsync extends AsyncTask<Void, Void, Void> {
//		protected Void doInBackground(Void... voids) {
//
//			try {
//				File dir = getExternalFilesDir(null);
//
//				FileInputStream fileIn = new FileInputStream(
//						dir.getAbsolutePath() + "_data");
//				ObjectInputStream in = new ObjectInputStream(fileIn);
//				data = (ItemsData) in.readObject();
//				in.close();
//				fileIn.close();
//		
//
//			} catch (IOException i) {
//				Log.i(TAG, "RestoreAsync fail");
//
//				i.printStackTrace();
//				return null;
//			} catch (ClassNotFoundException c) {
//				Log.i(TAG, "RestoreAsync fail");
//
//				c.printStackTrace();
//				return null;
//			}
//			return null;
//		}
//
//		protected void onProgressUpdate(Void... progress) {
//		}
//
//		protected void onPostExecute(Void result) {
//			//removeExtraImages();
//
//		}
//	}
//	

}
