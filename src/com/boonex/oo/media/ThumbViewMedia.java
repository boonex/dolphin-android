package com.boonex.oo.media;

import java.util.Map;

import android.content.Context;

import com.boonex.oo.ThumbView;

public class ThumbViewMedia extends ThumbView {

	
	public ThumbViewMedia(Context context, Map<String, Object> map, String username) {
		super(context, map, username);				
	}
	
    protected String getTextTitle() {
    	return (String)m_map.get("title");
    }
    
    protected String getText1() {
    	return (String)m_map.get("desc");
    }
    
	protected String getThumbFieldName () {
		return "thumb";
	}    
}
