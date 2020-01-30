package com.company.propertyprice.fragments;


import java.lang.reflect.Type;
import java.net.URLEncoder;

import com.company.propertyprice.MarkerStorage;
import com.company.propertyprice.activities.MainActivity;
import com.company.propertyprice.dao.PropertyListItemDAO;
import com.company.propertyprice.model.GridWrapper;
import com.company.propertyprice.model.android.PropertyListItem;
import com.company.propertyprice.model.mdo.PropertyMDO;
import com.company.propertypricetest.R;
import com.company.utils.DateUtils;
import com.company.utils.PriceUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.haarman.listviewanimations.ArrayAdapter;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.haarman.listviewanimations.itemmanipulation.contextualundo.ContextualUndoAdapter;
import com.haarman.listviewanimations.itemmanipulation.contextualundo.ContextualUndoAdapter.CountDownFormatter;
import com.haarman.listviewanimations.itemmanipulation.contextualundo.ContextualUndoAdapter.DeleteItemCallback;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SavedPropertyFragment extends MyListFragment implements OnDismissCallback, DeleteItemCallback {

	private View fragView = null;
	private ArrayAdapter mAdapter;
	
	@Override
	public void onAttach(Activity activity) {
		mcontext =  (MainActivity) activity;
		mcontext.setSavedFragmentCallback(this);
	    super.onAttach(activity);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	if (fragView == null) {
	    	fragView = inflater.inflate(R.layout.activity_mylist, container, false);
	    	
			mListView = (ListView) fragView.findViewById(R.id.activity_mylist_listview);
			mListView.setDivider(null);

	        if (savedInstanceState == null) {
	            selectItem(0);
	        }
    	}
        
		mAdapter = createListAdapter();
		setAdapter();
		setContextualUndoWithTimedDeleteAndCountDownAdapter();
		
    	return fragView;
    }
    
	private void setAdapter() {
		SwipeDismissAdapter adapter = new SwipeDismissAdapter(mAdapter, this);
		adapter.setAbsListView(getListView());
		getListView().setAdapter(adapter);
	}
	
	@Override
	public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {
		/*String productName = "";
		SuperShopApplication mApplication = (SuperShopApplication)getApplicationContext();
		BasketManager basketManager = mApplication.getBasketManager();
		
		for (int position : reverseSortedPositions) {
			basketManager.deleteBasketItem((BasketItem)mAdapter.getItem(position), getBaseContext());
			productName = ((BasketItem)mAdapter.getItem(position)).getDescription();
			mAdapter.remove(position);
			Log.e("onDismiss", "position : " + position);
		}
		Toast.makeText(this, "Removed product " + productName, Toast.LENGTH_SHORT).show();*/
	}
	
	private void setContextualUndoAdapter() {
		ContextualUndoAdapter adapter = new ContextualUndoAdapter(mAdapter, R.layout.undo_row, R.id.undo_row_undobutton);
		adapter.setAbsListView(getListView());
		getListView().setAdapter(adapter);
		adapter.setDeleteItemCallback(this);
	}

	@Override
	public void deleteItem(int position) {
		
PropertyListItem item =  (PropertyListItem)mAdapter.getItem(position);
		
		PropertyListItemDAO dao = PropertyListItemDAO.getInstance(mcontext);
		dao.onDeletePropertyListItem(item);
		
		mAdapter.remove(position);
		mAdapter.notifyDataSetChanged();		
	}

	private void setContextualUndoWithTimedDeleteAdapter() {
		ContextualUndoAdapter adapter = new ContextualUndoAdapter(mAdapter, R.layout.undo_row, R.id.undo_row_undobutton, 3000);
		adapter.setAbsListView(getListView());
		getListView().setAdapter(adapter);
		adapter.setDeleteItemCallback(this);
	}

	private void setContextualUndoWithTimedDeleteAndCountDownAdapter() {
		ContextualUndoAdapter adapter = new ContextualUndoAdapter(mAdapter, R.layout.undo_row, R.id.undo_row_undobutton, 3000, R.id.undo_row_texttv, new MyFormatCountDownCallback());
		adapter.setAbsListView(getListView());
		getListView().setAdapter(adapter);
		adapter.setDeleteItemCallback(this);
	}

	private class MyFormatCountDownCallback implements CountDownFormatter {

		@Override
		public String getCountDownString(long millisUntilFinished) {
			int seconds = (int) Math.ceil((millisUntilFinished / 1000.0));

			if (seconds > 0) {
				return getResources().getQuantityString(R.plurals.countdown_seconds, seconds, seconds);
			}
			return getString(R.string.countdown_dismissing);
		}
	}
	
	private class AnimSelectionAdapter extends ArrayAdapter<String> {

		public AnimSelectionAdapter() {
			addAll("Swipe-To-Dismiss", "Contextual Undo", "CU - Timed Delete", "CU - Count Down");
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout l = (LinearLayout) convertView;
			if (l == null) {
				l = (LinearLayout) LayoutInflater.from(mcontext).inflate(android.R.layout.simple_list_item_1, parent, false);
			}

			TextView addressText = (TextView) fragView
					.findViewById(R.id.address_text);
			TextView priceText = (TextView) fragView
					.findViewById(R.id.price_text);
			TextView dateText = (TextView) fragView
					.findViewById(R.id.date_text);
			
			ImageView markerWindowViewImage = (ImageView) fragView
					.findViewById(R.id.marker_window_image);

			
			PropertyListItem item =  (PropertyListItem)mAdapter.getItem(position);
			String jsonString = item.getJson();
			
			Type collectionType = new TypeToken<PropertyMDO>() {
			}.getType();
			PropertyMDO propertyMDO = new GsonBuilder()
					.setDateFormat("MMM dd, yyyy").create()
					.fromJson((String) jsonString, collectionType);

			Picasso.with(mcontext).load("http://maps.googleapis.com/maps/api/streetview?size="
				    + URLEncoder.encode("160x160") + "&location="
				    + propertyMDO.getL() + "," + propertyMDO.getO() + "&pitch=10").into(markerWindowViewImage);;
            
			addressText.setText(propertyMDO.getAddress().formatAddress());
			priceText.setText(PriceUtils.formatPrice(propertyMDO
					.getP()));
			dateText.setText(DateUtils.getString(propertyMDO
					.getD()));
			
			
			return l;
		}
	}
}
