package com.boonex.oo.search;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.boonex.oo.R;

public class SearchKeywordActivity extends SearchBaseActivity {
		
	protected EditText m_editKeyword;
	protected CheckBox m_cbOnlineOnly;
	protected CheckBox m_cbWithPhotosOnly;
    
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b, true, false);
        
        setContentView(R.layout.search_keyword);
        setTitleCaption (R.string.title_search_keyword);
        
        m_editKeyword = (EditText) findViewById(R.id.keyword);        
        m_cbOnlineOnly = (CheckBox) findViewById(R.id.online_only);
        m_cbWithPhotosOnly = (CheckBox) findViewById(R.id.with_photos_only);
        
        checkSearchWithPhotos(m_cbWithPhotosOnly, findViewById(R.id.with_photos_only_title));
    }
    
    @Override
    protected void actionSearchSubmit() {
		Intent i = new Intent(m_actThis, SearchResultsKeywordActivity.class);    			                         	
		i.putExtra("keyword", m_editKeyword.getText().toString());
		i.putExtra("online_only", m_cbOnlineOnly.isChecked());
		i.putExtra("with_photos_only", m_cbWithPhotosOnly.isChecked());
		i.putExtra("start", 0);
		startActivityForResult(i, ACTIVITY_SEARCH_RESULTS);
    }

}
