package com.boonex.oo.media;

import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boonex.oo.R;

public class ThumbViewMediaEdit extends ThumbViewMedia {

	protected Button m_btnDelete;
	protected Button m_btnView;
	
	public ThumbViewMediaEdit(Context context, Map<String, Object> map, String username) {
		super(context, map, username);				
	}
	
	protected void addControls() {
		super.addControls();
		
		LinearLayout l = new LinearLayout(m_context);
		l.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
		l.setOrientation(HORIZONTAL);		
		    	    	
    	l.addView(getButtonDeleteControl());
    	
    	m_viewInfoWrapper.addView(l);
	}
	protected Button getButtonDeleteControl() {
	    if (null == m_btnDelete) {
	    	
			MyOnClickListener listener = new MyOnClickListener() {
				protected String m_sFileId;
				protected Context m_context;
		    	public void setContext(Context context) {
		    		m_context = context;
		    	}
		    	public void setFileId(String s) {
		    		m_sFileId = s;
		    	}
	        	public void onClick(View view) {
	        		MediaFilesActivity activity = (MediaFilesActivity)m_context;
	        		activity.onRemoveFile(m_sFileId);
	        	}
	        };
	        
	        listener.setFileId((String)m_map.get("id"));
	        listener.setContext(this.getContext());
	             					
	        m_btnDelete = (Button)LayoutInflater.from(this.getContext()).inflate(R.layout.view_action_button, null, false);
	        m_btnDelete.setOnClickListener(listener);
	        m_btnDelete.setText(getContext().getString(R.string.media_files_delete));
	        m_btnDelete.setFocusable(false);
	        m_btnDelete.setFocusableInTouchMode(false);	        
	    }
		return m_btnDelete;
	}
		
    interface MyOnClickListener extends View.OnClickListener {
    	public void setContext(Context context);
    	public void setFileId(String s);
    }	
}
