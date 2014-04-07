package com.example.ledgr.viewrental;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ledgr.R;
import com.example.ledgr.dataobjects.ItemsData;
import com.example.ledgr.viewitem.ViewItemFragment;
import com.example.ledgr.viewitem.ViewItemPictureOnlineFragment;
import com.viewpagerindicator.TitlePageIndicator;

public class ViewRentalOnline extends FragmentActivity {
	ScreenSlidePagerAdapter mPagerAdapter;
	String item_id;
	ViewItemFragment fragment_view_item;
	Activity activity;
	Fragment[] fragments = new Fragment[3];
	ViewPager pager;

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter  {
    	Fragment[] fragments;

    	
        protected final String[] CONTENT = new String[] { "Owner Info", "Item Info", "Pictures" };
    	
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
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
          return CONTENT[position % CONTENT.length];
        }
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.activity_view_item);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		item_id = getIntent().getStringExtra("ITEMID");
		getActionBar().setTitle("");

		
		
			Bundle bundle = new Bundle();
			bundle.putString("ITEMID", item_id);
			
			
			fragments[0] = new ViewRentalOwnerFragment();
			fragments[0].setArguments(bundle);
			fragments[1] = new ViewItemFragment();
			fragments[1].setArguments(bundle);
			fragments[2] = new ViewItemPictureOnlineFragment();
			fragments[2].setArguments(bundle);
			
			
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
		case R.id.action_cancel_rental:
			new AlertDialog.Builder(this)
			.setTitle("Cancel rental?")
			.setMessage("Are you sure you want to cancel this rental?")
			.setPositiveButton(android.R.string.yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							// continue with delete
							ItemsData.cancelRentalById(item_id, activity);
							finish();
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_rental, menu);
		return true;
	}


}
