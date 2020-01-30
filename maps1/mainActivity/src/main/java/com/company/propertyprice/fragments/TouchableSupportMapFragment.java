package com.company.propertyprice.fragments;

import com.company.propertyprice.TouchableWrapper;
import com.google.android.gms.maps.SupportMapFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TouchableSupportMapFragment extends SupportMapFragment {
	public View mOriginalContentView;
	public TouchableWrapper mTouchView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);
		mTouchView = new TouchableWrapper(getActivity());
		mTouchView.addView(mOriginalContentView);
		return mTouchView;
	}

	@Override
	public View getView() {
		return mOriginalContentView;
	}
	
	public void setTouchableSupportCallback(MapFragment callback) {
		mTouchView.setUpdateMapAfterUserInterection(callback);
	}
}
