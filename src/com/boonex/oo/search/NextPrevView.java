package com.boonex.oo.search;


import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boonex.oo.R;

public class NextPrevView extends LinearLayout {
		
	protected Button m_btnNext;
	
    public NextPrevView(Context context) {
    	super(context);    	
    	
        this.setOrientation(HORIZONTAL);
                
        addControls();        
    }
    
    protected void addControls() {
    	
    	LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	paramsText.setMargins(5, 5, 5, 5);
    	addView(getButtonNextControl(), paramsText);
        	
    }

	protected Button getButtonNextControl() {
	    if (null == m_btnNext) {
	    	
			MyOnClickListener listener = new MyOnClickListener() {
				protected Context m_context;
		    	public void setContext(Context context) {
		    		m_context = context;
		    	}
	        	public void onClick(View view) {
	        		SearchResultsBaseActivity activity = (SearchResultsBaseActivity)m_context;
	        		activity.onNext();
	        	}
	        };
	        
	        listener.setContext(this.getContext());
	             					
	        m_btnNext = new Button(this.getContext());
	        m_btnNext.setOnClickListener(listener);
	        m_btnNext.setText(getContext().getString(R.string.search_next));
	    }
		return m_btnNext;
	}
	
    interface MyOnClickListener extends View.OnClickListener {
    	public void setContext(Context context);
    }	
}
