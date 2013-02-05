package com.boonex.oo;

import java.io.Serializable;

public class Site extends Object implements Serializable {

	private static final long serialVersionUID = 1L;
	protected String m_sUrl;
	protected String m_sUsername;
	protected String m_sPwd;
	
	public Site (String sUrl, String sUsername, String sPwd) {
		super();
		m_sUrl = sUrl;
		m_sUsername = sUsername;
		m_sPwd = sPwd;					
	}	
	
	public String getUrl () {
		return m_sUrl;
	}

	public void setUrl(String url) {
		m_sUrl = url;
	}
	
	public String getUsername () {
		return m_sUsername;
	}
	
	public void setUsername(String username) {
		m_sUsername = username;
	}
	
	public String getPwd () {
		return m_sPwd;
	}	
	
	public void setPwd(String pwd) {
		m_sPwd = pwd;
	}	

}
