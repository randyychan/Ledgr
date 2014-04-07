package com.example.ledgr.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.ledgr.R;
import com.example.ledgr.fragments.SearchDemo;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class SearchItems extends FragmentActivity {

	SearchDemo searchFragment;
	SlidingUpPanelLayout panelLayout;
	Button search;
	EditText searchField;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_items);
		connectUI();
		initUI();
		
		getActionBar().setTitle("Search for Items");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		panelLayout.expandPane();
	}



	private void connectUI() {
		panelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		search = (Button) findViewById(R.id.button);
		searchField = (EditText) findViewById(R.id.searchField);
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				panelLayout.collapsePane();
				panelLayout.setSlidingEnabled(true);
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
					
					searchFragment = new SearchDemo();
					FragmentManager fm = getSupportFragmentManager();
					FragmentTransaction transaction = fm.beginTransaction();
					transaction.add(R.id.frame, searchFragment);
					transaction.commit();
			}
		});
	}
	
	private void initUI() {
		panelLayout.setPanelHeight(150);
		panelLayout.expandPane();
		
		SlidingUpPanelLayout.PanelSlideListener slideListener = new SlidingUpPanelLayout.PanelSlideListener() {
			
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPanelExpanded(View panel) {
				// TODO Auto-generated method stub
				panelLayout.setSlidingEnabled(false);
			}
			
			@Override
			public void onPanelCollapsed(View panel) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPanelAnchored(View panel) {
				// TODO Auto-generated method stub
				
			}
		};
		
		panelLayout.setPanelSlideListener(slideListener);
		
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_items, menu);
		return true;
	}

}
