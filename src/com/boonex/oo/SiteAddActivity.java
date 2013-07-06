package com.boonex.oo;

import java.net.URI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.boonex.oo.Connector;


public class SiteAddActivity extends ActivityBase {
	private static final String TAG = "OO SiteAddActivity";
		
	protected EditText m_editSite;
	
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, true, false, false);
        
        setContentView(R.layout.site_add);
        setTitleCaption (R.string.title_login);
               
        m_editSite = (EditText) findViewById(R.id.site);
    }
	
    protected void actionAddSite() {            			
    	String sMethod = "dolphin.concat";    	
    	String sCorrectUrl = correctSiteUrl(m_editSite.getText().toString());
    	
    	try {
    		URI.create(sCorrectUrl);
    	} catch (IllegalArgumentException e) {    		
    		alertError(R.string.msg_url_incorrect);    		
	        return;
    	}
    	
        Connector o = new Connector (sCorrectUrl, "", "", 0);
        
        Object[] aParams = {"O", "K"};
        
        Connector.Callback oCallback = new Connector.Callback() {    	
    		public void callFinished(Object result) {
    			Log.d(TAG, "dolphin.login result: " + result + " / class: " + result.getClass() + " - " + (result instanceof String) + " & " + (result == "OK"));
    			if (result instanceof String && ((String)result).equals("OK")) {
    				
        			Bundle b = new Bundle();
        			b.putString("site", correctSiteUrl(m_editSite.getText().toString()));
        			Intent i = new Intent();
        			i.putExtras(b);        			        			
        			setResult(0, i);
        			finish();
        			
    			} else {
    				
    				alertError(R.string.msg_url_incorrect);
    				
    			}
    		}
        };
        
        o.execAsyncMethod(sMethod, aParams, oCallback, m_actThis);        
    }    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.site_add, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.site_add:
        	if (0 == m_editSite.getText().toString().length())
        		alertError(R.string.msg_url_incorrect);
        	else
        		actionAddSite();
            break;
        }
        return super.onOptionsItemSelected(item);
    }    
}
