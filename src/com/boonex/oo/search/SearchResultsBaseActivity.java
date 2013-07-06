package com.boonex.oo.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.boonex.oo.ListActivityBase;
import com.boonex.oo.R;
import com.boonex.oo.profile.ProfileActivity;

public class SearchResultsBaseActivity extends ListActivityBase {

	Menu m_oMenu;
	Integer m_iPerPage = 10;	
	SearchResultsAdapter adapterSearchResults;
	Object m_aProfiles[];	

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.list);
        setTitleCaption (R.string.title_search_results);
    }
        
	public void onNext () {
		// override
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
                       
        String sUsername = adapterSearchResults.getUsername(position);
        if (null == sUsername)
        	return;
        
    	Intent i = new Intent(this, ProfileActivity.class);    			                         	
    	i.putExtra("username", sUsername);
    	startActivityForResult(i, 0);
    }	
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	m_oMenu = menu;
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.search_results, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.search_next:
        	onNext();
            break;
        }
        return super.onOptionsItemSelected(item);
    }    
    
    protected void checkNextButton (boolean bEnable) {
    	if (bEnable || null == m_oMenu)
    		return;
    	MenuItem item = m_oMenu.getItem(0);
    	item.setVisible(bEnable);
    }
}
