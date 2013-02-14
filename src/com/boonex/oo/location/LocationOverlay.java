package com.boonex.oo.location;

import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;

import com.boonex.oo.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class LocationOverlay  extends Overlay {

	//  Store these as global instances so we don't keep reloading every time
    private Bitmap bubbleIcon, shadowIcon;
    
    private LocationActivity locationActivity;
    
	private Paint	innerPaint, borderPaint, textPaint;
    
    //  The currently selected Map Location...if any is selected.  This tracks whether an information  
    //  window should be displayed & where...i.e. whether a user 'clicked' on a known map location
    private GeoPoint selectedMapLocation;  
    
	public LocationOverlay(LocationActivity locationActivity) {
		
		this.locationActivity = locationActivity;
		
		bubbleIcon = BitmapFactory.decodeResource(locationActivity.getResources(),R.drawable.bubble);
		shadowIcon = BitmapFactory.decodeResource(locationActivity.getResources(),R.drawable.shadow);
	}
	
	@Override
	public boolean onTap(GeoPoint p, MapView	mapView)  {
		
		//  Store whether prior popup was displayed so we can call invalidate() & remove it if necessary.
		boolean isRemovePriorPopup = selectedMapLocation != null;  

		//  Next test whether a new popup should be displayed
		selectedMapLocation = getHitMapLocation(mapView,p);
		if ( isRemovePriorPopup || selectedMapLocation != null) {
			mapView.invalidate();
		}		
		
		//  Lastly return true if we handled this onTap()
		return selectedMapLocation != null;
	}
	
    @Override
	public void draw(Canvas canvas, MapView	mapView, boolean shadow) {
    	
   		drawMapLocations(canvas, mapView, shadow);
   		
    }

    /**
     * Test whether an information balloon should be displayed or a prior balloon hidden.
     */
    private GeoPoint getHitMapLocation(MapView	mapView, GeoPoint	tapPoint) {
    	
    	//  Track which GeoPoint was hit...if any
    	GeoPoint hitMapLocation = null;
		
    	RectF hitTestRecr = new RectF();
		Point screenCoords = new Point();
    	Iterator<GeoPoint> iterator = locationActivity.getMapLocations().iterator();
    	while(iterator.hasNext()) {
    		GeoPoint testLocation = iterator.next();
    		
    		//  Translate the MapLocation's lat/long coordinates to screen coordinates
    		mapView.getProjection().toPixels(testLocation, screenCoords);

	    	// Create a 'hit' testing Rectangle w/size and coordinates of our icon
	    	// Set the 'hit' testing Rectangle with the size and coordinates of our on screen icon
    		hitTestRecr.set(-bubbleIcon.getWidth()/2,-bubbleIcon.getHeight(),bubbleIcon.getWidth()/2,0);
    		hitTestRecr.offset(screenCoords.x,screenCoords.y);

	    	//  Finally test for a match between our 'hit' Rectangle and the location clicked by the user
    		mapView.getProjection().toPixels(tapPoint, screenCoords);
    		if (hitTestRecr.contains(screenCoords.x,screenCoords.y)) {
    			hitMapLocation = testLocation;
    			break;
    		}
    	}
    	
    	//  Lastly clear the newMouseSelection as it has now been processed
    	tapPoint = null;
    	
    	return hitMapLocation; 
    }
    
    private void drawMapLocations(Canvas canvas, MapView mapView, boolean shadow) {    	
    	List<GeoPoint> lLocations = locationActivity.getMapLocations();
    	if (null == lLocations)
    		return;
    	
		Iterator<GeoPoint> iterator = lLocations.iterator();
		Point screenCoords = new Point();
    	while(iterator.hasNext()) {	   
    		GeoPoint location = iterator.next();
    		mapView.getProjection().toPixels(location, screenCoords);
			
	    	if (shadow) {
	    		//  Only offset the shadow in the y-axis as the shadow is angled so the base is at x=0; 
	    		canvas.drawBitmap(shadowIcon, screenCoords.x, screenCoords.y - shadowIcon.getHeight(),null);
	    	} else {
    			canvas.drawBitmap(bubbleIcon, screenCoords.x - bubbleIcon.getWidth()/2, screenCoords.y - bubbleIcon.getHeight(),null);
	    	}
    	}
    }

	public Paint getInnerPaint() {
		if ( innerPaint == null) {
			innerPaint = new Paint();
			innerPaint.setARGB(225, 75, 75, 75); //gray
			innerPaint.setAntiAlias(true);
		}
		return innerPaint;
	}

	public Paint getBorderPaint() {
		if ( borderPaint == null) {
			borderPaint = new Paint();
			borderPaint.setARGB(255, 255, 255, 255);
			borderPaint.setAntiAlias(true);
			borderPaint.setStyle(Style.STROKE);
			borderPaint.setStrokeWidth(2);
		}
		return borderPaint;
	}

	public Paint getTextPaint() {
		if ( textPaint == null) {
			textPaint = new Paint();
			textPaint.setARGB(255, 255, 255, 255);
			textPaint.setAntiAlias(true);
		}
		return textPaint;
	}
}
