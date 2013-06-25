package com.boonex.oo.friends;

import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boonex.oo.R;

public class ThumbViewActionApproveReject extends ThumbViewBase {

	protected Button m_btnAccept = null;
	protected Button m_btnReject = null;	
	
	public ThumbViewActionApproveReject(Context context, Map<String, Object> map, String username) {
		super(context, map, username);		
	}
	
    @Override
	protected void addControls() {
    	super.addControls();
    	
		LinearLayout l = new LinearLayout(m_context);		
		l.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
		l.setOrientation(HORIZONTAL);
		
    	l.addView(getButtonAccept());
    	l.addView(getButtonReject());
    	    	
    	m_viewInfoWrapper.addView(l);
    }
    
	protected Button getButtonAccept() {
		if (null == m_btnAccept) {
			
			CustomOnClickListener listener = new CustomOnClickListener(this.getContext(), this.m_sUsername) {
				@Override
				public void onClick(View arg0) {
	        		FriendRequestsActivity activity = (FriendRequestsActivity)m_context;
	        		activity.onAcceptFriend(m_sData);
				}
	        };

	        m_btnAccept = (Button)LayoutInflater.from(this.getContext()).inflate(R.layout.view_action_button, null, false);	        
	        m_btnAccept.setOnClickListener(listener);
	        m_btnAccept.setText(getContext().getString(R.string.friends_approve));
	        m_btnAccept.setFocusable(false);
	        m_btnAccept.setFocusableInTouchMode(false);
		}
		return m_btnAccept;
	}
	
	protected Button getButtonReject() {
		if (null == m_btnReject) {
			
			ConfirmationOnClickListener listener = new ConfirmationOnClickListener(this.getContext(), this.m_sUsername) {
				public void onConfirm() {
	        		FriendRequestsActivity activity = (FriendRequestsActivity)m_context;
	        		activity.onRejectFriend(m_sData);
				}
	        };

	        m_btnReject = (Button)LayoutInflater.from(this.getContext()).inflate(R.layout.view_action_button, null, false);
	        m_btnReject.setOnClickListener(listener);
	        m_btnReject.setText(getContext().getString(R.string.friends_reject));
	        m_btnReject.setFocusable(false);
	        m_btnReject.setFocusableInTouchMode(false);
	        
		}
		return m_btnReject;
	}
		
}
