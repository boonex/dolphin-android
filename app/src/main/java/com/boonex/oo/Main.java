package com.boonex.oo;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.boonex.oo.about.AboutActivity;
import com.boonex.oo.home.HomeActivity;


public class Main extends ListActivityBase {
	private static final String TAG = "OO Main";
	
	public static final String LOCK_TO_SITE=null; // specify site you want to lock to, for example: "mysite.com/dolphin"
	
    private static final int ACTIVITY_LOGIN=0;
    private static final int ACTIVITY_HOME=1;
    private static final int ACTIVITY_ABOUT=2;
    private static final int ACTIVITY_SITE_ADD=3;
    
    public static Main MainActivity = null;
    
    protected static Connector m_oConnector = null;    
	protected SiteAdapter adapter;
	protected FrameLayout m_viewBtnAddSite; 
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, true, false, false);
        
        MainActivity = this;
        
        setContentView(R.layout.main);        
        setTitleCaption (R.string.title);
        
        m_viewBtnAddSite = (FrameLayout)findViewById(R.id.btn_add_site);
        m_viewBtnAddSite.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {              	
            	actionAddSite();
            }
        });  
        
        adapter = new SiteAdapter (this, null);
        setListAdapter(adapter);       
    }
    
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id); 
        
        Intent i = new Intent(this, LoginActivity.class);
        Site oSite = (Site)adapter.getItem(position);
        i.putExtra("index", position);
        i.putExtra("site", oSite.getUrl());
        i.putExtra("username", oSite.getUsername());
        i.putExtra("password", oSite.getPwd());
        startActivityForResult(i, ACTIVITY_LOGIN);
    }    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        if (null == i) 
        	return;
        Bundle b = i.getExtras();
        
        switch(requestCode) {

        case ACTIVITY_HOME:
        	switch (resultCode) {
        		case HomeActivity.RESULT_LOGOUT:
        		{
        			Main.setConnector(null);

        			int index = b.getInt("index");
        			adapter.updatePassword(index, "");
        			adapter.writeToFile(this);
        			setListAdapter(adapter);

        			Log.d(TAG, "Logged out:" + index);
        		}
        	}
        break;

        case ACTIVITY_SITE_ADD:
        {
			String site = b.getString("site");
			if (site.length() > 0)
				adapter.add(site, "", "");
			adapter.writeToFile(this);
			setListAdapter(adapter);
        }
        break;
        	
        case ACTIVITY_LOGIN:
        	switch (resultCode) {
        		case LoginActivity.RESULT_LOGIN:
        		{
        			int index = b.getInt("index");
        			String site = b.getString("site");
        			int iMemberId = b.getInt("member_id");
        			String username = b.getString("username");
        			String password = b.getString("password");
        			int iProtocolVer = b.getInt("protocol");
        			Boolean isRememberPassword = b.getBoolean("remember_password");

        			adapter.update(index, site, username, isRememberPassword ? password : "");
        			adapter.writeToFile(this);
        			setListAdapter(adapter);

        			Intent intentHome = new Intent(this, HomeActivity.class);
        			intentHome.putExtra("site", site);
        			intentHome.putExtra("member_id", iMemberId);
        			intentHome.putExtra("username", username);
        			intentHome.putExtra("password", password);
        			intentHome.putExtra("protocol", iProtocolVer);
        			intentHome.putExtra("index", index);
        		    
        			startActivityForResult(intentHome, ACTIVITY_HOME);
    	       		        
        		}
        		break;        		
        		case LoginActivity.RESULT_DELETE:
        		{
        			int index = b.getInt("index");
        			adapter.delete(index);
        			adapter.writeToFile(this);
        			setListAdapter(adapter);
        		}
        		break;
        	}
        	break;        	
        }
        
    }            
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main, menu);
    	if (null != Main.LOCK_TO_SITE)
    		menu.removeItem(R.id.main_add_site);
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.main_add_site:        	
        	actionAddSite();
            break;
        case R.id.main_about:
        	Intent i = new Intent(this, AboutActivity.class);        
            startActivityForResult(i, ACTIVITY_ABOUT);            
            break;            
        }
        return super.onOptionsItemSelected(item);
    }
    
    public static Connector getConnector() {
    	return m_oConnector;    
    }

    public static void setConnector(Connector connector) {    	
    	m_oConnector = connector;    	
    }    
    
    public static String getLang () {
    	String s = Locale.getDefault().getLanguage();
    	Log.i(TAG, "Lang: " + s);
    	return s;
    }
    
    public static String formatUserInfo (Map<String, Object> map, Context act) {
    	if (getConnector().getProtocolVer() > 2) {
    		return (String)map.get("user_info");
    	} else {
	    	String sAge = null != map.get("Age") ? (String)map.get("Age") : (String)map.get("age");
	    	String sSex = null != map.get("Sex") ? (String)map.get("Sex") : (String)map.get("sex");
	    	String sInfo = "";
	    	
	    	if (sAge != null && sAge.length() > 0 && !sAge.equalsIgnoreCase("0") && sSex != null && sSex.length() > 0)
	    		sInfo = String.format(act.getString(R.string.x_yo), sAge) + " " + sSex;
	    	else if (sAge != null && sAge.length() > 0 && !sAge.equalsIgnoreCase("0"))
	    		sInfo = String.format(act.getString(R.string.x_yo), sAge);
	    	else if (sSex != null && sSex.length() > 0)
	    		sInfo = sSex;

	    	return sInfo;
    	}    	
    }
    
    protected void actionAddSite () {
    	Intent i = new Intent(this, SiteAddActivity.class);        
    	startActivityForResult(i, ACTIVITY_SITE_ADD);
    }    
    
    public static String getCookieForLoggedInUser () {
    	Connector oConnector = Main.getConnector();
    	if (Main.getConnector() != null && Main.getConnector().getProtocolVer() >= 4)
    		return "memberID=" + oConnector.getMemberId() + "; memberPassword=" + oConnector.getPassword() + "; lang=" + Main.getLang();
    	else
    		return null;
    }
    public static Map<String, String> getHeadersForLoggedInUser () {
    	Map<String, String> mapHeaders = new HashMap<String, String>();
    	if (Main.getConnector() != null && Main.getConnector().getProtocolVer() >= 4)
    		mapHeaders.put("Cookie", Main.getCookieForLoggedInUser ());    	
    	return mapHeaders;
    }
}