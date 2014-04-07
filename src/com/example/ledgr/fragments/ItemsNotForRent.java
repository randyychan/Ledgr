package com.example.ledgr.fragments;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ledgr.R;
import com.example.ledgr.contentprovider.ItemContentProvider;
import com.example.ledgr.dataobjects.ItemsData;
import com.example.ledgr.viewitem.ViewItem;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ItemsNotForRent extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
	ListView list;
	Cursor items;
	ItemAdapter adapter;
	public ItemsNotForRent() {

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		items = ((LedgrApplication) getActivity().getApplication()).data
//				.retrieveAllItemsNotUpForRent(getActivity());
		getLoaderManager().initLoader(-1, null, this);
		View view = inflater.inflate(R.layout.listview_activity, container, false);
		list = (ListView) view.findViewById(R.id.cardListView);
		adapter = new ItemsNotForRent.ItemAdapter(getActivity(), items);
		list.setAdapter(adapter);
		 setUpList();
		return view;
	}
	
	private void setUpList() {
		list.setOnItemClickListener(new OnItemClickListener() {
			 
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				TextView item_id = (TextView) view.findViewById(R.id.card_id);
				Intent intent = new Intent(getActivity(), ViewItem.class);
				intent.putExtra("ITEMID",item_id.getText().toString());
				getActivity().startActivityForResult(intent, 888);
				getActivity().overridePendingTransition(
						R.animator.slide_in_left, R.animator.fade_out);
			}});
	}
	
	
	public void refreshViews() {
		getLoaderManager().restartLoader(-1, null, this);
	}
	
	public class ItemAdapter extends CursorAdapter {
	    private LayoutInflater mLayoutInflater;
	    private Context mContext;
	    private ImageLoader imageLoader;
	    
	    @SuppressWarnings("deprecation")
		public ItemAdapter(Context context, Cursor c) {
	        super(context, c);
	        mContext = context;
			imageLoader = ImageLoader.getInstance();

	        mLayoutInflater = LayoutInflater.from(context); 
	    }

	    @Override
	    public View newView(Context context, Cursor cursor, ViewGroup parent) {
	        View v = mLayoutInflater.inflate(R.layout.list_item, parent, false);
	        return v;
	    }

	    @Override
	    public void bindView(View vi, Context context, Cursor c) {
	    	
			((TextView) vi.findViewById(R.id.title)).setText(c.getString(c.getColumnIndexOrThrow(ItemContentProvider.C_TITLE)));
			((TextView) vi.findViewById(R.id.time)).setText(c.getString(c.getColumnIndexOrThrow(ItemContentProvider.C_PRICE)));
			((TextView) vi.findViewById(R.id.description))
					.setText(c.getString(c.getColumnIndexOrThrow(ItemContentProvider.C_DESCRIPTION)));
			((TextView) vi.findViewById(R.id.card_id)).setText(c.getString(c.getColumnIndexOrThrow(ItemContentProvider.C_ID)));
			ImageView image = ((ImageView) vi.findViewById(R.id.image));
			
			File[] files = mContext.getExternalFilesDir(c.getString(c.getColumnIndexOrThrow(ItemContentProvider.C_ID))).listFiles();
			File imageFile;
			if (files.length > 0) {
				imageFile = files[0];
				imageLoader.displayImage("file://"+imageFile.getAbsolutePath(), image);
			}
	    }
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new CursorLoader(this.getActivity(), 
				ItemContentProvider.CONTENT_URI,
				null, ItemContentProvider.C_RENTED + "= ? AND " + ItemContentProvider.C_STATE + " = ? AND " + ItemContentProvider.C_DIRTY + " != ?",
				new String[] {"0","0", Integer.toString(ItemsData.DELETED)}, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		// TODO Auto-generated method stub
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		adapter.swapCursor(items);

	}
	

}
