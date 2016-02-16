package com.boonex.oo.mail;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.boonex.oo.Connector;
import com.boonex.oo.Main;
import com.boonex.oo.R;
import com.boonex.oo.ViewText;

public class MailHomeAdapter extends BaseAdapter {

	private Context m_context;
	
	public MailHomeAdapter(Context context) {
		this.m_context = context;
	}
	
	public int getCount() {
		return 3;
	}

	public Object getItem(int arg0) {
		// not implemented
		return null; 
	}

	public long getItemId(int arg0) {		
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
			
		String s = "";		
		Connector o = Main.getConnector();
		
		switch (arg0) {		
		case 0:
			int iNum  = o.getUnreadLettersNum();
			if (iNum > 0)
				s = String.format(m_context.getString(R.string.mail_menu_inbox_num), iNum);
			else
				s = m_context.getString(R.string.mail_menu_inbox);
			break;
		case 1:			
			s = m_context.getString(R.string.mail_menu_sent);
			break;
		case 2:			
			s = m_context.getString(R.string.mail_menu_compose);
			break;
		}
			
		return new ViewText (m_context, s);
	}

}
