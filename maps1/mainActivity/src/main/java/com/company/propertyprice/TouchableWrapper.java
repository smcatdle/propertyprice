package com.company.propertyprice;

import com.company.propertyprice.fragments.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchableWrapper extends FrameLayout {

	public TouchableWrapper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TouchableWrapper(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TouchableWrapper(Context context) {
		super(context);
	}

	private CameraPosition lastCameraPosition = null;

	private MapFragment callback;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			if (callback != null) {
				lastCameraPosition = callback.getCameraPosition();
				
				callback.hideMarkerWindow();
			}
			break;
		case MotionEvent.ACTION_UP:
			// checking map position change rather than time?
			// final long now = SystemClock.uptimeMillis();
			// if ((now - lastTouched) > SCROLL_TIME) {
			// Update the map
			if (callback != null && lastCameraPosition != null) {
				CameraPosition cameraPosition = callback.getCameraPosition();

				// Has the user panned enough to justify an update to the map?
				if (Math.abs(cameraPosition.target.latitude
						- lastCameraPosition.target.latitude) > 0.0001
						|| Math.abs(cameraPosition.target.longitude
								- lastCameraPosition.target.longitude) > 0.0001) {
					
					// Has the map stopped panning yet (e.g. fast panning from swiping across)?
					CameraPosition newestCameraPosition = callback.getCameraPosition();
					if (cameraPosition.target.latitude
						== newestCameraPosition.target.latitude
						&& cameraPosition.target.longitude
								== newestCameraPosition.target.longitude) {
						
						callback.onUpdateMapAfterUserInterection();
					}

					// Reset the last position
					lastCameraPosition = null;
				}
			}
			// }
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	// Map Activity must implement this interface
	public interface UpdateMapAfterUserInterection {
		public void onUpdateMapAfterUserInterection();
	}

	public void setUpdateMapAfterUserInterection(MapFragment callback) {
		this.callback = callback;
	}
}