package com.boonex.oo.friends;

import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boonex.oo.R;

public class ThumbViewActionDelete extends ThumbViewBase {

	public Button m_btnDelete = null;
	
	public ThumbViewActionDelete(Context context, Map<String, Object> map, String username) {
		super(context, map, username);				
	}
	
    @Override
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
				protected String m_sNick;
				protected Context m_context;
		    	public void setContext(Context context) {
		    		m_context = context;
		    	}
		    	public void setNick(String s) {
		    		m_sNick = s;
		    	}
	        	public void onClick(View view) {
	        		FriendsActivity activity = (FriendsActivity)m_context;
	        		activity.onRemoveFriend(m_sNick);
	        	}
	        };
	        
	        listener.setNick(this.m_sUsername);
	        listener.setContext(this.getContext());
	             	        
	        m_btnDelete = (Button)LayoutInflater.from(this.getContext()).inflate(R.layout.view_action_button, null, false);	        
	        m_btnDelete.setOnClickListener(listener);
	        m_btnDelete.setText(getContext().getString(R.string.friends_delete));
	        m_btnDelete.setFocusable(false);
	        m_btnDelete.setFocusableInTouchMode(false);	        
	    }
		return m_btnDelete;
	}

}
