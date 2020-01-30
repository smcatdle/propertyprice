/**
 * @author smcardle
 *
 */

package com.company.propertyprice.activities;




import com.comany.propertyprice.tasks.SearchResultsActivityTask;
import com.company.propertyprice.fragments.MapFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

public class SearchResultsActivity extends Activity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
	
	private SearchView searchView;
	private TextView txtQuery;
    private MapFragment callback;
	
    public SearchResultsActivity(SearchView searchView, MapFragment callback) {
    	this.searchView = searchView;
    	this.callback = callback;
    	
    	searchView.setIconifiedByDefault(true);
    }
    
    public void setCallback(MapFragment callback) {
    	this.callback = callback;
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_search_results);
	        
		// Enabling Back navigation on Action Bar icon
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		//txtQuery = (TextView) findViewById(R.id.txtQuery);
		
	}
	

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
	}

    public boolean onQueryTextChange(String newText) {
  
        return false;
    }
 
    public boolean onQueryTextSubmit(String query) {
    	searchView.setIconified(true);
        searchView.setQuery(query, false);
        searchView.clearFocus();
        
    	SearchResultsActivityTask task = new SearchResultsActivityTask(callback);
    	task.execute(query);
	
    	searchView.setIconified(true);
        return false;
    }
 
    public boolean onClose() {
    	/*searchView.setIconified(true);
        searchView.setQuery("", false);
        searchView.clearFocus();
        searchView.setFocusable(false);*/

        return false;
    }

}
