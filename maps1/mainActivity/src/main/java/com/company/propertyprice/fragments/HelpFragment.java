package com.company.propertyprice.fragments;



import com.company.propertyprice.activities.MainActivity;
import com.company.propertyprice.views.MiniGraphView;
import com.company.propertypricetest.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;

public class HelpFragment extends Fragment {


    private View fragView = null;
    private MainActivity mcontext = null;

    @Override
    public void onAttach(Activity activity) {
		mcontext = (MainActivity) activity;

		super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	    
		if (fragView == null) {
			
		    fragView = inflater.inflate(R.layout.help_tab,
			    container, false);
		    
		    
		}
		
	    /*ViewGroup layout = null;
	    TableLayout tableLayout = null;
	    layout = (ViewGroup) fragView.findViewById(R.id.minigraph);
	    
	    MiniGraphView graph = new MiniGraphView(this.mcontext);
	    graph.setLayoutParams(new LayoutParams(200, 200));
	    
	    layout.addView(graph); */
	    
		return fragView;
    }

    
    
    /*
     * (non-Javadoc)
     * 
     * @see android.app.Fragment#onViewCreated(android.view.View,
     * android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
	

    }


    

    
    
}
