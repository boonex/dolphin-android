package com.boonex.oo;

import java.net.URI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.boonex.oo.Connector;


public class SiteAddActivity extends ActivityBase {
	private static final String TAG = "OO SiteAddActivity";
	
	protected Button m_buttonSubmit;	
	protected EditText m_editSite;
	
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, true, false, false);
        
        setContentView(R.layout.site_add);
        setTitleCaption (R.string.title_login);
               
        m_editSite = (EditText) findViewById(R.id.site);
        m_buttonSubmit = (Button) findViewById(R.id.submit);        
        
        	
        View.OnClickListener listener = new View.OnClickListener() {        	
            public void onClick(View view) {
            	if (0 == m_editSite.getText().toString().length())
            		alertError(R.string.msg_url_incorrect);
            	else
            		actionAddSite();
            }          
        };                
        m_buttonSubmit.setOnClickListener(listener);


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
	
}
