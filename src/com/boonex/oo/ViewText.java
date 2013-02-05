package com.boonex.oo;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewText extends LinearLayout {
	
	protected TextView m_viewText;
	protected LinearLayout m_layout;
	
    public ViewText(Context context, String sText) {
        super(context);
        
        LayoutInflater.from(context).inflate(R.layout.view_text, this, true);
               
        m_layout = (LinearLayout) findViewById(R.id.layout);
        m_viewText = (TextView) findViewById(R.id.text);
        
        m_viewText.setText(sText);
                             
    }

    
}
