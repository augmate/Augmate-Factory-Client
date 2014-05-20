package com.augmate;

import java.util.ArrayList;
import java.util.List;

import com.augmate.MenuPagerMenuItem.MenuPagerMenuItemListener;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;

public class MenuPagerDialogFragment extends DialogFragment implements MenuPagerMenuItemListener
{
	protected static final String DEBUG_TAG = "MenuPagerDialogFragment";
	private List<MenuPagerMenuItem> mMenuItems;
	private MenuPagerMenuItemListener mMenuItemListener;
	private ViewPager mMenuOptions;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_layout, container);

        mMenuOptions = (ViewPager) view.findViewById(R.id.menu_view_pager);
        mMenuOptions.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// Re-map the volume keys to DPAD right and left 
				if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)
				{
					mMenuOptions.executeKeyEvent(new KeyEvent(event.getDownTime(), event.getEventTime(), event.getAction(), KeyEvent.KEYCODE_DPAD_RIGHT, 0));
					return true;
				}
				else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
				{
					mMenuOptions.executeKeyEvent(new KeyEvent(event.getDownTime(), event.getEventTime(), event.getAction(), KeyEvent.KEYCODE_DPAD_LEFT, 0));
					return true;
				}
				return false;
			}
		});
        MenuPagerAdapter menuPagerAdapter = new MenuPagerAdapter(getChildFragmentManager(), mMenuItems);
        mMenuOptions.setAdapter(menuPagerAdapter);

        UnderlinePageIndicator indicator = (UnderlinePageIndicator)view.findViewById(R.id.pageIndicator);
        indicator.setViewPager(mMenuOptions);
        indicator.setFades(false);
        
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	    super.onCreate(savedInstanceState);
	    setStyle(DialogFragment.STYLE_NO_FRAME, R.style.dialogMenuStyle);
    }
    
    @Override
    public void onDismiss(DialogInterface dialog)
    {
    	if (mMenuOptions != null)
    	{
	    	MenuPagerMenuItem selectedItem = mMenuItems.get(mMenuOptions.getCurrentItem());
	    	this.menuPagerMenuItemSelected(selectedItem);
    	}
    	super.onDismiss(dialog);
    }
    
    public void setMenuItemListener(MenuPagerMenuItemListener listener)
    {
    	mMenuItemListener = listener;
    }

    public void addMenuItem(String itemName, int itemIconId, int itemId)
    {
    	MenuPagerMenuItem menuItem = MenuPagerMenuItem.newInstance(itemName, itemIconId, itemId);
    	menuItem.setMenuItemListener(this);
    	this.addMenuItem(menuItem);
    }

    public void addMenuItem(MenuPagerMenuItem item)
    {
    	if (mMenuItems == null)
    	{
    	    mMenuItems = new ArrayList<MenuPagerMenuItem>();
    	}
    	mMenuItems.add(item);
    }

	public void menuPagerMenuItemSelected(MenuPagerMenuItem item)
	{
		if (this.mMenuItemListener != null)
		{
			mMenuItemListener.menuPagerMenuItemSelected(item);
		}
	}    
}
