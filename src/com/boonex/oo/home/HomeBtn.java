package com.boonex.oo.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.boonex.oo.R;

public class HomeBtn extends FrameLayout {
	
	protected TextView m_viewTitle;
	protected TextView m_viewBubble;
	protected ImageView m_viewIcon;
	protected FrameLayout m_viewBtnFrame;

	
	public HomeBtn(Context context, String sTitle, String sBubble, int iIconResource) {
		this(context, R.layout.view_home_btn, sTitle, sBubble);
		
		m_viewIcon = (ImageView) findViewById(R.id.home_btn_icon);
    	if (iIconResource > 0)
    		m_viewIcon.setImageResource(iIconResource);
    	else
    		m_viewIcon.setImageResource(R.drawable.ic_site_view);
    	
    	// TODO: resize buttons depending on - getResources().getDisplayMetrics().density
	}
	
    public HomeBtn(Context context, int iLayout, String sTitle, String sBubble) {
        super(context);
                        
        LayoutInflater.from(context).inflate(iLayout, this, true);
                
        m_viewTitle = (TextView) findViewById(R.id.home_btn_title);
        m_viewBubble = (TextView) findViewById(R.id.home_btn_bubble);        
        m_viewBtnFrame = (FrameLayout) findViewById(R.id.home_btn);
        
        m_viewTitle.setText(sTitle);
        
        if (sBubble.length() > 0 && !sBubble.equals("0"))
        	m_viewBubble.setText(sBubble);
        else
        	m_viewBubble.setVisibility(INVISIBLE);        	       
    }

	public HomeBtn(Context context, String sTitle) {
		this(context, sTitle, "", 0);
	}
	
	public FrameLayout getBtn() {
		return m_viewBtnFrame;
	}
	
	public void getTitleText(String s) {
		m_viewTitle.setText(s);
	}
	
	public void getBubbleText(String s) {
		m_viewBubble.setText(s);
        m_viewBubble.setVisibility(s.length() > 0 && !s.equals("0") ? VISIBLE : INVISIBLE);
	}
}
