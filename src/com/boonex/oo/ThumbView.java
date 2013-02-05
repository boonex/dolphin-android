package com.boonex.oo;

import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ThumbView extends LinearLayout {
	protected Context m_context;
	protected Map<String, Object> m_map;
	protected TextView m_viewTitle;
	protected TextView m_viewText1;
	protected TextView m_viewText2;
	protected String m_sUsername;
	protected LoaderImageView m_viewImageThumb;
	protected LinearLayout m_viewInfoContainer;
	protected FrameLayout m_viewInfoWrapper;
	
    public ThumbView(Context context, Map<String, Object> map, String username) {
    	super(context);    	
    	
    	LayoutInflater.from(context).inflate(R.layout.view_thumb, this, true); 
        
        m_context = context;
        m_sUsername = username;
        m_map = map;
        
        m_viewImageThumb = (LoaderImageView) findViewById(R.id.thumb_img);
        m_viewTitle = (TextView) findViewById(R.id.thumb_title);
        m_viewText1 = (TextView) findViewById(R.id.thumb_text1);
        m_viewText2 = (TextView) findViewById(R.id.thumb_text2);
        m_viewInfoContainer = (LinearLayout) findViewById(R.id.thumb_info_cont);
        m_viewInfoWrapper = (FrameLayout) findViewById(R.id.thumb_info_wrap);
        
        addControls();
        setControlsData();
    }
    
    protected void addControls() {
    	    	        	
    }
    
    protected void setControlsData() {
    	m_viewImageThumb.setImageDrawable(getImageUrl());
    	m_viewTitle.setText(getTextTitle());
    	m_viewText1.setText(getText1());
    	m_viewText2.setText(getText2());
    }    
    
    protected String getTextTitle() {
    	return getUserTitle();
    }
    
    protected String getText1() {    	
    	if (Main.getConnector().getProtocolVer() > 2) {
    		return null != m_map.get("user_info") ? (String)m_map.get("user_info") : ""; 
    	} else {
    		return Main.formatUserInfo(m_map, m_context);
    	}
    }

    protected String getText2() {
    	return "";
    }
    
    public String getUsername() {
    	return m_sUsername;	
    }

    public String getUserTitle() {
    	if (Main.getConnector().getProtocolVer() > 2) {
    		return null != m_map.get("user_title") ? (String)m_map.get("user_title") : ""; 
    	} else {
    		return getUsername();
    	}
    }

    protected String getImageUrl() {    	
    	return (String)m_map.get(getThumbFieldName ());    	
    }
    
	protected String getThumbFieldName () {
		return "Thumb";
	}	
}
