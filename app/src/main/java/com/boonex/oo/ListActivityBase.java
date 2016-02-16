package com.boonex.oo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.boonex.oo.actionbar.ActionBarListActivity;

abstract public class ListActivityBase extends ActionBarListActivity {
	
	protected ActivityBaseHelper m_oActivityHelper;

	protected View m_viewMain;
		
	protected Boolean m_isToolbarEnabled;
	protected Boolean m_isReloadEnabled;
	protected ListActivityBase m_actThis;
	protected Bundle m_savedInstanceState;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {		
		this.onCreate (savedInstanceState, true, true);			
    }
    
	protected void onCreate(Bundle savedInstanceState, boolean isToolbarEnabled) {
    	this.onCreate (savedInstanceState, isToolbarEnabled, true);
    }
    
	protected void onCreate(Bundle savedInstanceState, boolean isToolbarEnabled, boolean isReloadEnabled) {
    	this.onCreate (savedInstanceState, isToolbarEnabled, isReloadEnabled, true);
    }
    
	protected void onCreate(Bundle savedInstanceState, boolean isToolbarEnabled, boolean isReloadEnabled, boolean isTryToRestoreConnector) {
        super.onCreate(savedInstanceState);
        m_actThis = this;
        m_isToolbarEnabled = isToolbarEnabled;
        m_isReloadEnabled = isReloadEnabled;
        m_savedInstanceState = savedInstanceState;
        m_oActivityHelper = new ActivityBaseHelper(this, isTryToRestoreConnector, isToolbarEnabled);
    }

    
    @Override
    public void setContentView (int iLayoutResID) {
    	m_viewMain = getLayoutInflater().inflate(iLayoutResID, null);
    	super.setContentView(m_viewMain);
    }
    
    protected void setTitleCaption (String s) {
    	setTitle(s);
    }
    
    protected void setTitleCaption (int iStringId) {
    	setTitle(getString(iStringId));
    }
    
    protected void reloadRemoteData () {
    	
    }

    protected void customAction () {
    	
    }
    
    protected void alertError (Integer iLangString) {
    	m_oActivityHelper.alertError(getString(iLangString));
    }
        
    public String correctSiteUrl (String sUrl) {    	
    	return m_oActivityHelper.correctSiteUrl(sUrl);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	return m_oActivityHelper.onCreateOptionsMenu(menu, m_isReloadEnabled);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    	
    	if (m_oActivityHelper.onOptionsItemSelected(item))
    		return true;
    	
        switch (item.getItemId()) {
        case R.id.menu_refresh:
        	reloadRemoteData();
            return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
}