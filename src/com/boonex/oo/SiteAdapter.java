package com.boonex.oo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SiteAdapter extends BaseAdapter {

	private static final String TAG="OO SiteAdapter";
	private static final String FILENAME = "sites.txt";
    protected Context m_context;
    protected List<Site> m_listSites;
    protected List<View> m_listViews;
    
    public SiteAdapter (Context context, ArrayList<Site> listSites) {
    	
    	m_context = context;
    	
    	m_listSites = listSites;
    	
    	if (null == listSites) {
    		this.readFromFile (context);
    		Log.d(TAG, "creating from file");
    	}
    	
    	if (null == m_listSites) {
    		m_listSites = new ArrayList<Site>();
    		if (Main.LOCK_TO_SITE != null)
    			this.add(Main.LOCK_TO_SITE, "", "");
    		Log.d(TAG, "creating from empty array");
    	}
    	
    	initViews();
    }
    
	protected void initViews() {
		m_listViews = new ArrayList<View>();
		for (int i=0 ; i < m_listSites.size() ; ++i)			
			m_listViews.add(i, getView(i, null, null));		
	}
	
	public int getCount() {		
		return m_listSites.size();
	}

	public Object getItem(int position) {
		return m_listSites.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (position >= 0 && position < m_listViews.size() && null != m_listViews.get(position))
			return m_listViews.get(position);
		
		Log.d(TAG, "creating new view for position: " + position);
		
		Site oSite = m_listSites.get(position);
		
		Main actMain = (Main)m_context;
		LinearLayout viewSite = (LinearLayout)actMain.getLayoutInflater().inflate(R.layout.view_site, null);    	
		
		TextView viewTextUsername = (TextView)viewSite.findViewById(R.id.site_username);
		if (oSite.getUsername().length() > 0)
			viewTextUsername.setText(oSite.getUsername());
		else
			viewTextUsername.setText(m_context.getString(R.string.title_login));
		
		TextView viewTextUrl = (TextView)viewSite.findViewById(R.id.site_url);
		String sSiteUrl = oSite.getUrl().replace("xmlrpc/", "");		
		viewTextUrl.setText(sSiteUrl);
		
		LoaderImageView viewImageLoader = (LoaderImageView)viewSite.findViewById(R.id.site_icon);
		viewImageLoader.setNoImageResource(R.drawable.ic_site_view);
		viewImageLoader.setImageDrawable(sSiteUrl + "media/images/mobile_logo.png");
		
		return viewSite;
	}

	public void add (Site site) {
		m_listSites.add(site);
		initViews();
	}
	
	public void add (String url, String username, String password) {
		m_listSites.add(new Site(url, username, password));
	}
	
	public void update (int index, String url, String username, String password) {
		Site site = m_listSites.get(index);
		site.setUrl(url);
		site.setUsername(username);
		site.setPwd(password);
		initViews();
	}
	
	public void delete (int index) {
		m_listSites.remove(index);
		initViews();
	}
	
	@SuppressWarnings("unchecked")
	public void readFromFile (Context context) {
	      try  {	         
	         FileInputStream istream = context.openFileInput(FILENAME);
	         ObjectInputStream p = new ObjectInputStream(istream);

	         m_listSites = (List<Site>) p.readObject();

	         istream.close();
	         
	      } catch (Exception e) {
	    	  Log.e(TAG, "Error during reading from file: " + e.getMessage());  
	      }
	}
	
	public void writeToFile (Context context) {
		
	      try 
	      {	    	      	  
	         FileOutputStream ostream = context.openFileOutput(FILENAME, 0);
	         ObjectOutputStream p = new ObjectOutputStream(ostream);
	         
	         Log.d(TAG, "writing....");
	         p.writeObject(m_listSites); 

	         ostream.flush();
	         ostream.close();	         

	      } catch (Exception e) {
	    	 Log.e(TAG, "Error during writing to file: " + e.getMessage());	         
	      }	
	}
}
