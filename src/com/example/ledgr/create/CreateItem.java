package com.example.ledgr.create;

import java.io.File;
import java.util.UUID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ledgr.R;
import com.example.ledgr.Utils;
import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemsData;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TitlePageIndicator;

public class CreateItem extends FragmentActivity {

	ScreenSlidePagerAdapter mPagerAdapter;
	String item_id;
	Fragment[] fragments = new Fragment[2];
	ViewPager pager;
	boolean update;
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    	Fragment[] fragments;

    	
        protected final String[] CONTENT = new String[] { "Info","Pictures" };

    	protected final int[] ICONS = new int[] {
    			R.drawable.icon,
    			R.drawable.perm_group_camera
    };
    	
        public ScreenSlidePagerAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
        	return fragments[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
          return CONTENT[position % CONTENT.length];
        }
        
		@Override
		public int getIconResId(int index) {
			// TODO Auto-generated method stub
			return ICONS[index];
		}
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle intentBundle = getIntent().getExtras();
		update = intentBundle.getBoolean("UPDATE");
		
		if (!update) {
			item_id = UUID.randomUUID().toString();			
		} else {
			item_id = intentBundle.getString("ITEMID");
		}
		

			fragments[0] = new CreateItemFragment();
			Bundle bundle = new Bundle();
			bundle.putBoolean("UPDATE", update);
			bundle.putString("ITEMID", item_id);
			fragments[0].setArguments(bundle);
			fragments[1] = new CreateItemPictureFragment();
			fragments[1].setArguments(bundle);
	
		
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
		pager = (ViewPager)super.findViewById(R.id.viewpager);
        pager.setAdapter(this.mPagerAdapter);
        TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.titles);
        titleIndicator.setViewPager(pager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (update) {
			getMenuInflater().inflate(R.menu.view_update_item, menu);

		} else {
			getMenuInflater().inflate(R.menu.create, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			File f = getExternalFilesDir(item_id);
			delete(f);
			setResult(RESULT_CANCELED);
			finish();

			return true;
		case R.id.action_update_item:
		case R.id.action_create_item:
			createItem();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static boolean delete(File file) {

		File[] flist = null;

		if (file == null) {
			return false;
		}

		if (file.isFile()) {
			return file.delete();
		}

		if (!file.isDirectory()) {
			return false;
		}

		flist = file.listFiles();
		if (flist != null && flist.length > 0) {
			for (File f : flist) {
				if (!delete(f)) {
					return false;
				}
			}
		}

		return file.delete();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//File f = getExternalFilesDir(item_id);
		//delete(f);
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

	}

	private void createItem() {
		Item item = ((CreateItemFragment)fragments[0]).createItemFromValues();

		if (item == null) {
			Toast.makeText(this, "Fill in Title and Price fields",
					Toast.LENGTH_SHORT).show();
			pager.setCurrentItem(0, true);
			return;
		}

		Bitmap bitmap = Utils.getSinglePictureForId(item.getItem_id(), this);
		if (bitmap == null) {
			Toast.makeText(this, "Take at least one picture",
					Toast.LENGTH_SHORT).show();
			pager.setCurrentItem(1, true);

			return;
		}

		if (!update) {
			ItemsData.addItem(item, this);
		} else {
			ItemsData.updateItem(item, this);
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable("ITEM", item);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();

	}

}
