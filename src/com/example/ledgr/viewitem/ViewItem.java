package com.example.ledgr.viewitem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ledgr.ActivityLauncher;
import com.example.ledgr.LedgrApplication;
import com.example.ledgr.R;
import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemsData;
import com.example.ledgr.rental.CreateRental;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TitlePageIndicator;

public class ViewItem extends FragmentActivity {
	ScreenSlidePagerAdapter mPagerAdapter;
	String item_id;
	Item item;
	Activity activity;
	Fragment[] fragments = new Fragment[2];
	ViewPager pager;

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {
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
		activity = this;
		setContentView(R.layout.activity_view_item);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		item_id = getIntent().getStringExtra("ITEMID");
		item = ItemsData.retrieveItemById(item_id, this);
		getActionBar().setTitle("");

		
		
			Bundle bundle = new Bundle();
			bundle.putString("ITEMID", item_id);
			fragments[0] = new ViewItemFragment();
			fragments[0].setArguments(bundle);
			fragments[1] = new ViewItemPictureFragment();
			fragments[1].setArguments(bundle);
			
			
			mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
			pager = (ViewPager)super.findViewById(R.id.viewpager);
	        pager.setAdapter(this.mPagerAdapter);
	        TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.titles);
	        titleIndicator.setViewPager(pager);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();

			return true;
		case R.id.action_rent_item:
			rentItem();
			return true;
		case R.id.action_delete:
			new AlertDialog.Builder(this)
					.setTitle("Delete item")
					.setMessage("Are you sure you want to delete this item?")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with delete
									deleteEntry();
								}
							})
					.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).show();

			return true;
		case R.id.action_edit:
			ActivityLauncher.CreateItem(getApplicationContext(), activity, true, item_id);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void deleteEntry() {
		ItemsData.deleteItemById(item_id, this);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("HERE RANDY");
		ViewItemPictureFragment pictureFragment = (ViewItemPictureFragment) fragments[1];
		pictureFragment.reloadPictures();
		
		ViewItemFragment viewFragment = (ViewItemFragment)fragments[0];
		viewFragment.reloadInfo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.view_item, menu);
		return true;
	}

	private void rentItem() {
		Intent intent = new Intent(this, CreateRental.class);
		intent.putExtra("ITEMID", item.getItem_id());
		startActivityForResult(intent, 888);
		overridePendingTransition(R.animator.slide_in_left, R.animator.fade_out);
		finish();
	}
}
