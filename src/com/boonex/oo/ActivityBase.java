package com.boonex.oo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.boonex.oo.home.HomeActivity;

abstract public class ActivityBase extends Activity {
	
	protected RelativeLayout m_layoutToolbarContainer;
	protected LinearLayout m_layoutToolbar;
	protected ImageButton m_btnReload;
	protected ImageButton m_btnHome;
	protected ImageButton m_btnAction;

	protected View m_viewMain;
	
	protected Boolean m_isToolbarEnabled;
	protected Boolean m_isReloadEnabled;
	protected ActivityBase m_actThis;
	protected Bundle m_savedInstanceState;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	this.onCreate (savedInstanceState, true, true);    	
    }
    
    public void onCreate(Bundle savedInstanceState, boolean isToolbarEnabled) {
    	this.onCreate (savedInstanceState, isToolbarEnabled, true);
    }
    
    public void onCreate(Bundle savedInstanceState, boolean isToolbarEnabled, boolean isReloadEnabled) {
    	this.onCreate (savedInstanceState, isToolbarEnabled, isReloadEnabled, true);
    }
    
    public void onCreate(Bundle savedInstanceState, boolean isToolbarEnabled, boolean isReloadEnabled, boolean isTryToRestoreConnector) {
        super.onCreate(savedInstanceState);
        m_actThis = this;
        m_isToolbarEnabled = isToolbarEnabled;
        m_isReloadEnabled = isReloadEnabled;
        m_savedInstanceState = savedInstanceState;
        
        if (isTryToRestoreConnector) {
        	Connector o = Main.getConnector();
			if (null == o) {
				o = Connector.restoreConnector(this);
				Main.setConnector(o);
			}        
        }
    }
    
    
    @Override
    public void setContentView (int iLayoutResID) {
    	m_viewMain = getLayoutInflater().inflate(iLayoutResID, null);    	
    	
    	super.setContentView(m_viewMain);
    
    	if (m_isToolbarEnabled) {
    		m_layoutToolbar = (LinearLayout)getLayoutInflater().inflate(R.layout.toolbar, null);    	
    		LinearLayout.LayoutParams paramsToolbar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    		this.addContentView(m_layoutToolbar, paramsToolbar);
    		    		
    		m_layoutToolbarContainer = (RelativeLayout) findViewById(R.id.toolbar_container);    		
    		final float scale = getResources().getDisplayMetrics().density;
    		m_viewMain.setPadding(0, 0, 0, (int)(50 * scale + 0.5f));    		
    		    		
    		m_btnReload = (ImageButton) findViewById(R.id.toolbar_btn_reload);
    		m_btnHome = (ImageButton) findViewById(R.id.toolbar_btn_home);
    		m_btnAction = (ImageButton) findViewById(R.id.toolbar_btn_action);
    
    		m_btnReload.setVisibility(m_isReloadEnabled ? View.VISIBLE : View.INVISIBLE);
    		
    		m_btnHome.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {                    
                	m_actThis.gotoHome();
                }
            });

    		m_btnReload.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {                	                	
                	m_actThis.reloadRemoteData();
                }
            });

    		m_btnAction.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {                	                	
                	m_actThis.customAction();
                }
            });
    		
    	}
    }
    
    public void setTitleCaption (String s) {
    	setTitle(s);
    }
    
    protected void setTitleCaption (int iStringId) {
    	setTitle(getString(iStringId));
    }
    
    protected void reloadRemoteData () {
    	
    }

    protected void customAction () {
    	
    }
   
    protected void gotoHome () {
        if (!(m_actThis instanceof HomeActivity)) {

        	Intent intentHome = new Intent(m_actThis, HomeActivity.class);
        	intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        	Connector o = Main.getConnector();
        	intentHome.putExtra("site", o.getSiteUrl());
        	intentHome.putExtra("username", o.getUsername());
        	intentHome.putExtra("password", o.getPasswordClear());
        	intentHome.putExtra("protocol", o.getProtocolVer());

        	m_actThis.startActivity(intentHome);
        }    	
    }    
}
