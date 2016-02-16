package com.boonex.oo.friends;

import java.util.Map;

import android.content.Context;
import android.widget.Button;

import com.boonex.oo.ThumbView;


public class ThumbViewBase extends ThumbView {
		
	protected Button m_btnViewProfile = null;
	
	public ThumbViewBase(Context context, Map<String, Object> map, String username) {
		super(context, map, username);
	}
	
    @Override
	protected void addControls() {
    	
    }
            
}
