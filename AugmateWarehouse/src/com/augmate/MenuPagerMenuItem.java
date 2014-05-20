package com.augmate;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuPagerMenuItem extends Fragment
{
	public interface MenuPagerMenuItemListener
	{
		public void menuPagerMenuItemSelected(MenuPagerMenuItem item);
	}

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final MenuPagerMenuItem newInstance(String itemName, int itemIconId, int itemId)
    {
    	MenuPagerMenuItem menuItem = new MenuPagerMenuItem();
        Bundle bundle = new Bundle(3);
        bundle.putString("itemName", itemName);
        bundle.putInt("itemId", itemId);
        bundle.putInt("itemIconId", itemIconId);
        menuItem.setArguments(bundle);
        return menuItem;
    }

    private CharSequence mItemName;
    private int mItemId;
    private MenuPagerMenuItemListener mMenuItemListener;
    private int mIconId;
    
    public int getItemId()
    {
    	return mItemId;
    }
    
    public void setMenuItemListener(MenuPagerMenuItemListener listener)
    {
    	mMenuItemListener = listener;
    }
    
    /**
     * During creation, if arguments have been supplied to the fragment
     * then parse those out.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) 
        {
        	mItemName = args.getCharSequence("itemName", mItemName);
        	mItemId = args.getInt("itemId");
        	mIconId = args.getInt("itemIconId");
        }
        else
        {
        	mItemName = "";
        	mIconId = R.drawable.alert;
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         final View view = inflater.inflate(R.layout.menu_item_layout, container, false);
         
         TextView textView = (TextView) view.findViewById(R.id.menu_item_name);
         textView.setText(mItemName);
         
         ImageView iconView = (ImageView) view.findViewById(R.id.menu_item_imageview);
         iconView.setImageResource(mIconId);
         
         final MenuPagerMenuItem me = this;
         view.setOnKeyListener(new View.OnKeyListener() 
	         {			
	             public boolean onKey( View v, int keyCode, KeyEvent event )
	             {
             		if (mMenuItemListener != null)
            		{
            			if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER)
            					&& Build.MANUFACTURER.equals("Vuzix")            					
            			) 
            			{
            				me.mMenuItemListener.menuPagerMenuItemSelected(me);
            			    return true;
            			}
            		}
             		return false;
	             }
	         }
         );
        return view;
    }
	
	public boolean onTouchEvent(MotionEvent event) 
	{
 		if (mMenuItemListener != null)
		{
			mMenuItemListener.menuPagerMenuItemSelected(this);
		    return true;
		}
	    return false;
	}
	
	public boolean onClick(int keyCode, KeyEvent event) 
	{
 		if (mMenuItemListener != null)
		{
			if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER)
					&& Build.MANUFACTURER.equals("Vuzix")            					
 					&& event.getRepeatCount() == 0
 					) 
 			{
 				mMenuItemListener.menuPagerMenuItemSelected(this);
 			    return true;
 		    }
		}
		
		return false;
	}
	
}