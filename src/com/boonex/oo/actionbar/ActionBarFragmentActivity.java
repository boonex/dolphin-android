package com.boonex.oo.actionbar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;

public abstract class ActionBarFragmentActivity extends FragmentActivity {
    final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);

    /**
     * Returns the {@link ActionBarHelper} for this activity.
     */
    public ActionBarHelper getActionBarHelper() {
        return mActionBarHelper;
    }

    /**{@inheritDoc}*/
    @Override
    public MenuInflater getMenuInflater() {
        return mActionBarHelper.getMenuInflater(super.getMenuInflater());
    }

    /**{@inheritDoc}*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBarHelper.onCreate(savedInstanceState);
    }

    /**{@inheritDoc}*/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarHelper.onPostCreate(savedInstanceState);
    }

    @Override
	protected void onDestroy() {
    	mActionBarHelper.onDestroy();
		super.onDestroy();
	}
    
    /**
     * Base action bar-aware implementation for
     * {@link Activity#onCreateOptionsMenu(android.view.Menu)}.
     *
     * Note: marking menu items as invisible/visible is not currently supported.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retValue = false;
        retValue |= mActionBarHelper.onCreateOptionsMenu(menu);
        retValue |= super.onCreateOptionsMenu(menu);
        return retValue;
    }

    /**{@inheritDoc}*/
    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        mActionBarHelper.onTitleChanged(title, color);
        super.onTitleChanged(title, color);
    }
}
