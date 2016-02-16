package com.boonex.oo;

import com.boonex.oo.home.HomeActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Build;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

public class ActivityBaseHelper {

    protected Activity m_oActivity;

    protected ActivityBaseHelper(Activity activity, boolean isTryToRestoreConnector, boolean isToolbarEnabled) {
    	String sClass = activity.getClass().getSimpleName();    	    	
    	m_oActivity = activity;
    	        
        if (sClass.equals("ImagesGallery")) {
        	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        		activity.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);        		
        		ActionBar actionBar = activity.getActionBar();
        		if (actionBar != null)
        			actionBar.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.actionbar_overlay_bg));
        	} else {
        		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        	}
        } else {
            if (!isToolbarEnabled)
            	activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            	activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        }
        
        if (isTryToRestoreConnector) {
        	Connector o = Main.getConnector();
			if (null == o) {
				o = Connector.restoreConnector(m_oActivity);
				Main.setConnector(o);
			}
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        	ActionBar actionBar = activity.getActionBar();
        	if (!sClass.equals("HomeActivity") && !sClass.equals("Main") && null != actionBar) {
        		actionBar.setHomeButtonEnabled(true); 
        		actionBar.setDisplayHomeAsUpEnabled(true);
        	}
        }                
    }
        
    public void gotoHome () {
    	if (null == Main.MainActivity) {
        	Intent intentHome = new Intent(m_oActivity, Main.class);
        	intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	m_oActivity.startActivity(intentHome);
        	return;
    	}
    	
    	Connector o = Main.getConnector();
    	String sClass = m_oActivity.getClass().getSimpleName();
    	
    	if (sClass.equals("AboutActivity") || sClass.equals("LoginActivity") || sClass.equals("SiteAddActivity")) {
    		
    		m_oActivity.finish();
    		
    	} else if (o != null && !sClass.equals("HomeActivity")) {

        	Intent intentHome = new Intent(m_oActivity, HomeActivity.class);
        	intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	
        	intentHome.putExtra("site", o.getSiteUrl());
        	intentHome.putExtra("member_id", o.getMemberId());
        	intentHome.putExtra("username", o.getUsername());
        	intentHome.putExtra("password", o.getPasswordClear());
        	intentHome.putExtra("protocol", o.getProtocolVer());
        	intentHome.putExtra("index", o.getSiteIndex());

        	m_oActivity.startActivity(intentHome);
        }    	
    } 
        
    public boolean onCreateOptionsMenu(Menu menu, boolean isReloadEnabled) {
    	if (isReloadEnabled) {
    		// by default only one refresh button in common.xml menu
    		MenuInflater inflater = m_oActivity.getMenuInflater();
    		inflater.inflate(R.menu.common, menu);
    	}
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	gotoHome();
            return true;        
        }
        return false;
    }
    
    public void alertError (String s) {
        Builder builder = new AlertDialog.Builder(m_oActivity);
        builder.setTitle(R.string.error);				          
        builder.setMessage(s);				        
        builder.setNegativeButton(R.string.close, null); 
        builder.show();	
    }
    
    public String correctSiteUrl (String sUrl) {    	
    	if (!sUrl.startsWith("http://") && !sUrl.startsWith("https://"))
    		sUrl = "http://" + sUrl;
    	if (!sUrl.endsWith("/"))
    		sUrl += "/";
    	if (!sUrl.endsWith("xmlrpc/"))
    		sUrl += "xmlrpc/";    	
    	return sUrl;
    }    
    
}
