package com.boonex.oo.mail;

import java.util.Map;

import android.content.Context;

import com.boonex.oo.Main;
import com.boonex.oo.R;
import com.boonex.oo.ThumbView;

public class MailMessageView extends ThumbView {		
	
	protected Boolean m_isInbox;
	
    public MailMessageView(Context context, Map<String, Object> map, Boolean isInbox) {
    	super(context, map, "");    	
    	m_isInbox = isInbox;
    	setControlsData2();
    }
        
    protected void setControlsData() {
    
    }
    
    protected void setControlsData2() {
    	super.setControlsData();
    }
    
    protected String getTextTitle() {
    	return (String)m_map.get("Subject");
    }

    protected String getText1() {
    	return String.format(m_context.getString(m_isInbox ? R.string.mail_from_x : R.string.mail_to_x), getInterlocutorTitle ());    	
    }
    
    protected String getText2() {
    	return (String)m_map.get("Date") + (0 == ((String)m_map.get("New")).compareTo("1") ? "   " + m_context.getString(R.string.mail_new) : "");
    }
    
	protected String getThumbFieldName () {
		return "Thumb";
	}
	
	protected String getInterlocutorTitle () {
		if (Main.getConnector().getProtocolVer() > 2)
			return (String)m_map.get("UserTitleInterlocutor");
		else
			return (String)m_map.get("Nick");
	}
}
