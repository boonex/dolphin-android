package org.xmlrpc.android;

public class XMLRPCRedirectException extends Exception {
	private static final long serialVersionUID = 1L;
	protected String m_sRedirectUrl;
	
	public XMLRPCRedirectException (String sUrl) {
		super("HTTP Redirect");
		m_sRedirectUrl = sUrl;
	}
	
	public String getRedirectUrl() {
		return m_sRedirectUrl;
	}
}
