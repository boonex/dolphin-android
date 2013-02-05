package com.boonex.oo;




import java.net.URI;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends ActivityBase {
	private static final String TAG = "OO LoginActivity";
	
	protected Button m_buttonSubmit;	
	protected EditText m_editSite;
	protected EditText m_editUsername;
	protected EditText m_editPassword;
	protected CheckBox m_cbRemebmer;
	protected int m_iIndex;

    // result codes
    public static final int RESULT_DELETE = RESULT_FIRST_USER + 1;
    public static final int RESULT_LOGIN = RESULT_FIRST_USER + 2;
    public static final int RESULT_ADD = RESULT_FIRST_USER + 3;
    
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, false, false, false);
        
        setContentView(R.layout.login);
        setTitleCaption (R.string.title_login);
               
        m_editSite = (EditText) findViewById(R.id.site);
        m_editUsername = (EditText) findViewById(R.id.username);
        m_editPassword = (EditText) findViewById(R.id.password);
        m_cbRemebmer = (CheckBox) findViewById(R.id.remember_password);
        m_buttonSubmit = (Button) findViewById(R.id.submit);        
        
        Intent i = getIntent();
        
        if (null == i.getStringExtra("site")) {
        	m_iIndex = -1;        	
        } else {
        	m_iIndex = i.getIntExtra("index", 0);
        	m_editSite.setText(i.getStringExtra("site"));
        	m_editUsername.setText(i.getStringExtra("username"));
        	m_editPassword.setText(i.getStringExtra("password"));        	
        	m_buttonSubmit.setText(R.string.title_login);
        	if (i.getStringExtra("password").length() > 0)
        		m_cbRemebmer.setChecked(true);
        	
        	m_editSite.setEnabled(false);        	        	
        	m_editSite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        	    public void onFocusChange(View v, boolean hasFocus) {
        	        if (hasFocus) {
        	            hideKeyboard();
        	        }
        	    }
        	});
        }        	
        	
        Button submitButton = (Button) findViewById(R.id.submit);
                
        MySetContextInterface listener = new MySetContextInterface() {
        	public LoginActivity context;
            public void onClick(View view) {            	
    			
            	String sCorrectUrl = correctSiteUrl(m_editSite.getText().toString());
            	
            	try {
            		URI.create(sCorrectUrl);
            	} catch (IllegalArgumentException e) {
            		
			        Builder builder = new AlertDialog.Builder(context);
			        builder.setTitle(R.string.error);				          
			        builder.setMessage(R.string.msg_url_incorrect);				        
			        builder.setNegativeButton(R.string.close, null); 
			        builder.show();
			        return;
            	}
            	
                Connector o = new Connector (sCorrectUrl, m_editUsername.getText().toString(), m_editPassword.getText().toString());
                
                Object[] aParams = {
                		m_editUsername.getText().toString(), 
                		o.md5(m_editPassword.getText().toString())		
                };                    
                
                LoginActionCallback oCallback = new LoginActionCallback() {
                	public boolean callFailed(Exception e) {
                		if (e.getMessage().endsWith("[code 1]") && "dolphin.login2" == sMethod) { // method dolphin.login2 not found
                			Log.d(TAG, "new protocol dolphin.login2 function not found, using old protocol dolphin.login function");
                			setMethod("dolphin.login");  
                			oConnector.execAsyncMethod("dolphin.login", aParams, this, context);
                			return false;
                		} else {
                			return true;
                		}
                	}
        			public void callFinished(Object result) {
        				Log.d(TAG, "dolphin.login result: " + result.toString() + " / class: " + result.getClass());
        				
        				int iIdProfile;
        				int iProtocolVer;
        				
        				if ("dolphin.login2" == sMethod) {
        					@SuppressWarnings("unchecked")
							Map<String, Integer> map = (Map<String, Integer>)result;        					
        					iIdProfile = map.get("member_id");        					
        					iProtocolVer = map.get("protocol_ver");
        				} else {
        					iIdProfile = Integer.valueOf(result.toString());
        					iProtocolVer = 1;
        				}
        				
        				Log.d(TAG, "MEMBER ID: " + iIdProfile);
        				Log.d(TAG, "PROTOCOL VERSION: " + iProtocolVer);
        				
        				if (iIdProfile > 0) {
        					
        	    			Bundle b = new Bundle();
        	    			b.putInt("index", m_iIndex);
        	    			b.putString("site", correctSiteUrl(m_editSite.getText().toString()));
        	    			b.putString("username", m_editUsername.getText().toString());
        	    			b.putString("password", m_editPassword.getText().toString());            	           
        	    			b.putBoolean("remember_password", m_cbRemebmer.isChecked());
        	    			b.putInt("protocol", iProtocolVer);
        	    			Intent i = new Intent();
        	    			i.putExtras(b);
        	    			
        	    			if (-1 == m_iIndex)
        	    				setResult(RESULT_ADD, i);
        	    			else
        	    				setResult(RESULT_LOGIN, i);
        	        	
        	    			finish();
        	    			
        				} else {
        					
    				        Builder builder = new AlertDialog.Builder(context);
    				        builder.setTitle(R.string.error);				          
    				        builder.setMessage(R.string.msg_login_incorrect);				        
    				        builder.setNegativeButton(R.string.close, null); 
    				        builder.show();    				                				
        				}
        			}
                };
                oCallback.setMethod("dolphin.login2");
                oCallback.setParams(aParams);
                oCallback.setConnector(o);
                o.execAsyncMethod("dolphin.login2", aParams, oCallback, this.context);
                
            }          
            public void setContext(LoginActivity context) {
            	this.context = context;            
            }
        };        
        listener.setContext(this);
        submitButton.setOnClickListener(listener);
        
    }
    
    interface MySetContextInterface extends View.OnClickListener {
    	public void setContext(LoginActivity context);    
    }
 
    protected void hideKeyboard() {
    	this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    
    @Override
	protected void onPause() {		
		super.onPause();
	}

	@Override
	protected void onResume() {		
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);		  
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	if (m_iIndex >= 0 && Main.LOCK_TO_SITE == null) {
    		MenuInflater inflater = getMenuInflater();
    		inflater.inflate(R.menu.login, menu);
    		return true;
    	}
    	return false;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.login_delete_site:
        	Bundle b = new Bundle();
        	b.putInt("index", m_iIndex);            	           
        	Intent i = new Intent();
        	i.putExtras(b);        	
            setResult(RESULT_DELETE, i);
            finish();            
            break;
        }
        return super.onOptionsItemSelected(item);
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
    
    class LoginActionCallback extends Connector.Callback {
    	protected String sMethod;
    	protected Object[] aParams;
    	protected Connector oConnector;
    	public void setMethod(String s) {
    		sMethod = s;
    	}
    	public void setParams(Object[] a) {
    		aParams = a;
    	}
    	public void setConnector(Connector o) {
    		oConnector = o;
    	}    	
    }
}

