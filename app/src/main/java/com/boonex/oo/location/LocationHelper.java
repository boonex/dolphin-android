package com.boonex.oo.location;

import java.util.Timer;
import java.util.TimerTask;

import com.boonex.oo.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

public class LocationHelper {
	private static final int TIMEOUIT = 20000;
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    Activity m_context;
    boolean gps_enabled=false;
    boolean network_enabled=false;

    public boolean getLocation(Activity context, LocationResult result)
    {
    	m_context = context;
    	
        //I use LocationResult callback class to pass location value from LocationHelper to user code.
        locationResult=result;
        if(lm==null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try{gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}

        //don't start listeners if no provider is enabled
        if(!gps_enabled && !network_enabled)
            return false;

        if(gps_enabled)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        if(network_enabled)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        timer1=new Timer();
        timer1.schedule(new GetLastLocation(), TIMEOUIT);
        return true;
    }

    public void openLocationEnableDialog() {
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(m_context);
        
        // Setting Dialog Title
        alertDialog.setTitle(R.string.location_settings_title);

        // Setting Dialog Message
        alertDialog.setMessage(R.string.location_settings_msg);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.button_settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                m_context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();    	
    }
    
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(m_context, location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(m_context, location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGps);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
             lm.removeUpdates(locationListenerGps);
             lm.removeUpdates(locationListenerNetwork);

             Location net_loc=null, gps_loc=null;
             if(gps_enabled)
                 gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
             if(network_enabled)
                 net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

             //if there are both values use the latest one
             if(gps_loc!=null && net_loc!=null){
                 if(gps_loc.getTime()>net_loc.getTime())
                     locationResult.gotLocation(m_context, gps_loc);
                 else
                     locationResult.gotLocation(m_context, net_loc);
                 return;
             }

             if(gps_loc!=null){
                 locationResult.gotLocation(m_context, gps_loc);
                 return;
             }
             if(net_loc!=null){
                 locationResult.gotLocation(m_context, net_loc);
                 return;
             }
             locationResult.gotLocation(m_context, null);
        }
    }

    public static abstract class LocationResult{
    	protected void gotLocation (Activity context, final Location location) {
    		context.runOnUiThread(new Runnable() {
    			   public void run() {
    			      gotLocation(location);
    			   }
    			});    		
    	}
        public abstract void gotLocation(Location location);
    }
}