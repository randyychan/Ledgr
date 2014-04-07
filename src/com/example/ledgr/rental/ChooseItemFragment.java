package com.example.ledgr.rental;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ledgr.R;
import com.example.ledgr.cards.CreateNewItem;
import com.example.ledgr.cards.EmptyItemListCard;
import com.example.ledgr.contentprovider.ItemContentProvider;
import com.example.ledgr.dataobjects.Item;
import com.example.ledgr.dataobjects.ItemsData;
import com.fima.cardsui.views.CardUI;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ChooseItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	CardUI noItem;
	Cursor items;
	ListView list;
	View.OnClickListener onClickListener;
	onItemSelectedListener itemSelectedListener;
	ItemAdapter adapter;
	public interface onItemSelectedListener {
		public void selectedItem(Item item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			itemSelectedListener = (onItemSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement onItemSelectedListener");
		}
	}

	public ChooseItemFragment() {
		// Empty constructor required for fragment subclasses
		onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView text = (TextView) v.findViewById(R.id.card_id);
				String item_id = (String) text.getText();
				Item item = ItemsData.retrieveItemById(item_id, getActivity());
				itemSelectedListener.selectedItem(item);
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getLoaderManager().initLoader(-1, null, this);
		View view = inflater.inflate(R.layout.listview_rental_activity, container, false);



		CardUI newItem = (CardUI) view.findViewById(R.id.cardUI);
		noItem = new CardUI(getActivity());

		newItem.setPadding(0, 20, 0, 0);
		newItem.addCard(new CreateNewItem(getActivity().getApplicationContext(), getActivity()));

		noItem.addCard(new EmptyItemListCard(getActivity()));
		noItem.setPadding(0, 20, 0, 0);

		noItem.refresh();


		list = (ListView) view.findViewById(R.id.cardListView);
		list.setOnItemClickListener(new OnItemClickListener() {
			 
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				TextView item_id = (TextView) view.findViewById(R.id.card_id);
				itemSelectedListener.selectedItem(ItemsData.retrieveItemById(item_id.getText().toString(), getActivity()));
			}});
		
		adapter = new ItemAdapter(getActivity(), items);
		list.setAdapter(adapter);

	//	mainLayout.addView(newItem);
		newItem.refresh();
//
//		if (items.size() > 0) {
//			mainLayout.addView(list);
//		} else {
//			mainLayout.addView(noItem);
//		}
		//mainLayout.setBackgroundColor(Color.parseColor(Utils.BACKGROUND_COLOR));

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// progress.show();

	}



	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}


	private class ItemsAdapter extends ArrayAdapter<Item> {
		Activity activity;
		ArrayList<Item> items;
		private LayoutInflater inflater = null;
		DisplayImageOptions options;
		ImageLoader imageLoader;
		public ItemsAdapter(Activity activity, ArrayList<Item> items) {
			super(activity, 0, items);
			this.activity = activity;
			this.items = items;
			this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = ImageLoader.getInstance();
			this.options = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Item getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View vi = convertView;
			if (convertView == null) {
				vi = inflater.inflate(R.layout.list_item, null);
			}
			
			((TextView) vi.findViewById(R.id.title)).setText(items.get(position).getTitle());
			((TextView) vi.findViewById(R.id.time)).setText(items.get(position).getPriceString());
			((TextView) vi.findViewById(R.id.description))
					.setText(items.get(position).getDescription());
			((TextView) vi.findViewById(R.id.card_id)).setText(items.get(position).getItem_id());
			ImageView image = ((ImageView) vi.findViewById(R.id.image));
			
			
			File[] files = activity.getExternalFilesDir(items.get(position).getItem_id()).listFiles();
			File imageFile;
			if (files.length > 0) {
				imageFile = files[0];
			
				imageLoader.displayImage("file://"+imageFile.getAbsolutePath(), image);
	
			}
					
					return vi;
		}
		
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
		return new CursorLoader(this.getActivity(), ItemContentProvider.CONTENT_URI, null, ItemContentProvider.C_RENTED + "= ?", new String[] {"0"}, null);
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
