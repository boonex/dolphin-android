package com.boonex.oo.profile;

import java.util.Map;

import android.content.Context;

import com.boonex.oo.ThumbView;

public class ProfileView extends ThumbView {
	
    public ProfileView(Context context, Map<String, Object> map, String username) {
    	super(context, map, username);
    }
    
    @Override
    protected String getText2() {    	
    	return "";
    }
    
	public void updateTextWitNewStatusMessage (String sStatusMesage) {
		if (0 != sStatusMesage.compareTo((String)m_map.get("status")))
			m_map.put("status", sStatusMesage);
		m_viewText2.setText(getText2());	
	}

	protected String getThumbFieldName () {
		return "thumb";
	}
}
