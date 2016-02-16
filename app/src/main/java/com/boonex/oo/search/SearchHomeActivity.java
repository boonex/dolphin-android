package com.boonex.oo.search;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.boonex.oo.Connector;
import com.boonex.oo.ListActivityBase;
import com.boonex.oo.Main;
import com.boonex.oo.R;
import com.boonex.oo.home.WebPageActivity;

public class SearchHomeActivity extends ListActivityBase {

	private static final String TAG = "SearchHomeActivity";
	
	private static final int ACTIVITY_SEARCH_KEYWORD=0;
	private static final int ACTIVITY_SEARCH_LOCATION=1;
	private static final int ACTIVITY_SEARCH_NEAR_ME=2;	
	private static final int ACTIVITY_WEB_PAGE=7;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.list);
        setTitleCaption (R.string.title_search_home);
        
		reloadRemoteData ();
    }
    
    protected void initMenuPredefined () {
		Map<String, String> mapKeyword = new HashMap<String, String>();
		mapKeyword.put("title", getString(R.string.search_keyword));
		mapKeyword.put("action", "30");
		mapKeyword.put("bubble", "");
	    	
		Map<String, String> mapLocation = new HashMap<String, String>();
		mapLocation.put("title", getString(R.string.search_location));
		mapLocation.put("action", "31");
		mapLocation.put("bubble", "");

		Map<String, String> mapNearMe = new HashMap<String, String>();
		mapNearMe.put("title", getString(R.string.search_near_me));
		mapNearMe.put("action", "32");
		mapNearMe.put("bubble", "");
	
		Object[] aMenuDefault = {mapKeyword, mapLocation, mapNearMe};
        SearchHomeAdapter adapter = new SearchHomeAdapter(this, aMenuDefault);         
		setListAdapter(adapter);    	
    }

    protected void initMenu (Object[] aMenu) {    	
        SearchHomeAdapter adapter = new SearchHomeAdapter(this, aMenu);         
		setListAdapter(adapter);    	
    }

    
    protected void reloadRemoteData () {
    	
		if (Main.getConnector().getProtocolVer() < 3) {
			initMenuPredefined();
			return;
		}
		
    	Connector o = Main.getConnector();
        Object[] aParams = {
        		o.getUsername(), 
        		o.getPassword(),
        		Main.getLang()
        };                    

        o.execAsyncMethod("dolphin.getSeachHomeMenu3", aParams, new Connector.Callback() {
			@SuppressWarnings("unchecked")
			public void callFinished(Object result) {				 
				Log.d(TAG, "dolphin.getSeachHomeMenu3 result: " + result.toString());		
				Map<String, Object> map = (Map<String, Object>)result;
				Log.d(TAG, "dolphin.getSeachHomeMenu3 menu: " + map.get("menu"));
				initMenu ((Object[])map.get("menu"));
			}
        }, this);    	
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {    	
        super.onListItemClick(l, v, position, id);
        
        Object[] aMenu = ((SearchHomeAdapter)getListAdapter()).getMenu();        
        if (position < 0 || position > aMenu.length)
        	return;
        
        @SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>)aMenu[position];         
        String sAction = map.get("action");
        int iAction = Integer.parseInt(sAction);
        
        switch (iAction) {
        case 30:
    		{
    			Intent i = new Intent(this, SearchKeywordActivity.class);
    			startActivityForResult(i, ACTIVITY_SEARCH_KEYWORD);
    		}
        	break;        	
        case 31:
        	{
    			Intent i = new Intent(this, SearchLocationActivity.class);
    			startActivityForResult(i, ACTIVITY_SEARCH_LOCATION);
        	}
        	break;
		case 32:
        	{
    			Intent i = new Intent(this, SearchNearMeActivity.class);
    			startActivityForResult(i, ACTIVITY_SEARCH_NEAR_ME);
        	}
        	break;
        case 100:
			{
				String sUrl = map.get("action_data");
				String sTitle = map.get("title");
				Intent i = new Intent(this, WebPageActivity.class);
				i.putExtra("title", sTitle);
				i.putExtra("url", sUrl);
				startActivityForResult(i, ACTIVITY_WEB_PAGE);
			}		
			break;
        case 101:
			{
				String sUrl = map.get("action_data");
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sUrl));
				startActivity(browserIntent);
			}		
			break;			
        }
        
    }
    
}
