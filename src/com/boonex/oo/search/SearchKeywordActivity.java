package com.boonex.oo.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.boonex.oo.ActivityBase;
import com.boonex.oo.R;

public class SearchKeywordActivity extends ActivityBase {
	
	private static final int ACTIVITY_SEARCH_RESULTS=0;
	
	protected Button m_buttonSubmit;	
	protected EditText m_editKeyword;
	protected CheckBox m_cbOnlineOnly;
	protected CheckBox m_cbWithPhotosOnly;

    
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, false);
        
        setContentView(R.layout.search_keyword);
        setTitleCaption (R.string.title_search_keyword);
        
        m_editKeyword = (EditText) findViewById(R.id.keyword);        
        m_cbOnlineOnly = (CheckBox) findViewById(R.id.online_only);
        m_cbWithPhotosOnly = (CheckBox) findViewById(R.id.with_photos_only);
        m_buttonSubmit = (Button) findViewById(R.id.submit);
                
        MySetContextInterface listener = new MySetContextInterface() {
        	public SearchKeywordActivity context;
            public void onClick(View view) {            	
    			    			
    			Intent i = new Intent(context, SearchResultsKeywordActivity.class);    			                         	
    			i.putExtra("keyword", m_editKeyword.getText().toString());
    			i.putExtra("online_only", m_cbOnlineOnly.isChecked());
    			i.putExtra("with_photos_only", m_cbWithPhotosOnly.isChecked());
    			i.putExtra("start", 0);
    			startActivityForResult(i, ACTIVITY_SEARCH_RESULTS);
    			                
            }          
            public void setContext(SearchKeywordActivity context) {
            	this.context = context;
            }
        };        
        listener.setContext(this);
        m_buttonSubmit.setOnClickListener(listener);
        
    }
    
    interface MySetContextInterface extends View.OnClickListener {
    	public void setContext(SearchKeywordActivity context);    
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
	    
    
}
