/*
 * Copyright 2013 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.company.propertyprice.fragments;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.company.propertyprice.activities.MainActivity;
import com.company.propertyprice.dao.PropertyListItemDAO;
import com.company.propertyprice.model.android.PropertyListItem;
import com.company.propertyprice.model.mdo.PropertyMDO;
import com.company.propertypricetest.R;
import com.company.utils.DateUtils;
import com.company.utils.PriceUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class MyListFragment extends Fragment {

	protected ListView mListView;
	protected MainActivity mcontext = null;


	public ListView getListView() {
		return mListView;
	}

	protected com.haarman.listviewanimations.ArrayAdapter<PropertyListItem> createListAdapter() {
		return new MyListAdapter(mcontext, getItems());
	}

	public ArrayList<PropertyListItem> getItems() {
		ArrayList<PropertyListItem> items = new ArrayList<PropertyListItem>();

		PropertyListItemDAO dao = PropertyListItemDAO.getInstance(mcontext);
		return (ArrayList<PropertyListItem>) dao.onGetPropertyListItems();

	}
    
	private static class MyListAdapter extends com.haarman.listviewanimations.ArrayAdapter<PropertyListItem> {

		private Context mContext;

		public MyListAdapter(Context context, ArrayList<PropertyListItem> items) {
			super(items);
			mContext = context;
		}

		@Override
		public long getItemId(int position) {
			return getItem(position).hashCode();
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout l = (LinearLayout) convertView;
			if (l == null) {
				l = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.property_item, parent,
						false);
			}

			PropertyListItem item =  (PropertyListItem)getItem(position);
			String jsonString = item.getJson();
			
			Type collectionType = new TypeToken<PropertyMDO>() {
			}.getType();
			final PropertyMDO propertyMDO = new GsonBuilder()
					.setDateFormat("MMM dd, yyyy").create()
					.fromJson((String) jsonString, collectionType);
			
			TextView addressText = (TextView) l
					.findViewById(R.id.address_text);
			TextView priceText = (TextView) l
					.findViewById(R.id.price_text);
			TextView dateText = (TextView) l
					.findViewById(R.id.date_text);
			
			ImageView markerWindowViewImage = (ImageView) l
					.findViewById(R.id.marker_window_image);
			Button streetViewButton = (Button) l
				    .findViewById(R.id.streetmap_property_button);
			Button mapViewButton = (Button) l
				    .findViewById(R.id.map_property_button);
			Button saveButton = (Button) l
				    .findViewById(R.id.save_property_button);
			
			Picasso.with(mContext).load("http://maps.googleapis.com/maps/api/streetview?size="
				    + URLEncoder.encode("160x160") + "&location="
				    + propertyMDO.getL() + "," + propertyMDO.getO() + "&pitch=10").into(markerWindowViewImage);;
            
			addressText.setText(propertyMDO.getAddress().formatAddress());
			priceText.setText(PriceUtils.formatPrice(propertyMDO
					.getP()));
			dateText.setText(DateUtils.getString(propertyMDO
					.getD()));
			
			
			// Show the 'Street' button
			streetViewButton.setVisibility(android.view.View.VISIBLE);
		    streetViewButton.setText("Street");   
		    streetViewButton.setOnClickListener(new View.OnClickListener() {
			    	
					public void onClick(View v) {
			
						((MainActivity)mContext).addViewTabIfRequired();
						((MainActivity)mContext).setSelectedTab(1);
					}
					
			    });
			
		    
			// Show the 'Map' button
			mapViewButton.setVisibility(android.view.View.VISIBLE);
			mapViewButton.setText("Map");   
			mapViewButton.setOnClickListener(new View.OnClickListener() {
			    	
					public void onClick(View v) {
						((MainActivity)mContext).setSelectedProperty(propertyMDO);
						((MainActivity)mContext).viewProperty(propertyMDO);;
					}
					
			    });
		    
			// Hide the 'Save' button
			saveButton.setVisibility(android.view.View.INVISIBLE);
		    
		    
			// Set the item layout height
			//l.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 350));
			
			return l;
		}
	}

    protected void selectItem(int position) {
        // update the main content by replacing fragments
        /*Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);*/
    }

    /*@Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }*/

}
