package com.augmate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class AugmateZoetisIllnessLibrary extends FragmentActivity implements PopupMenu.OnMenuItemClickListener
{
	private GestureDetector gestureDetector;
	Context context;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    context=(Context)this;
	    
	    // TODO Auto-generated method stub
	    setContentView(R.layout.activity_zoetis_illness_library);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		if (Build.MANUFACTURER.equals("Google"))
		{
			this.createGestureDetector(context);
		}
	}

	private void createGestureDetector(Context context)
	{
		this.gestureDetector = new GestureDetector(context);
		this.gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			
			public boolean onGesture(Gesture gesture) {
				if (gesture == Gesture.SWIPE_RIGHT)
				{
					goToNextIllness();
					return true;
				}
				else if (gesture == Gesture.SWIPE_LEFT)
				{
					goToPreviousIllness();
					return true;
				}
				return false;
			}
		});
	}

	protected void goToNextIllness() {
		TextView illnessName = (TextView) findViewById(R.id.illness_name);
		ImageView illnessImage = (ImageView) findViewById(R.id.illness_image); 
		if (illnessName.getText().equals("BRD"))
		{
			illnessName.setText("Foot Rot");
			illnessImage.setImageResource(R.drawable.foot_rot);
		}
	}

	protected void goToPreviousIllness() {
		TextView illnessName = (TextView) findViewById(R.id.illness_name);
		ImageView illnessImage = (ImageView) findViewById(R.id.illness_image); 
		if (illnessName.getText().equals("Foot Rot"))
		{
			illnessName.setText("BRD");
			illnessImage.setImageResource(R.drawable.brd);
		}
	}
	
    /*
     * Send generic motion events to the gesture detector
     */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (this.gestureDetector != null) {
            return this.gestureDetector.onMotionEvent(event);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
       	inflater.inflate(R.menu.augmate_zoetis_cattle_reported_menu, menu);
        for (int i = 0; i < menu.size(); i++)
        {
        	MenuItem item = menu.getItem(i);
        	SpannableString whiteText = new SpannableString(item.getTitle());
        	whiteText.setSpan(new ForegroundColorSpan(Color.LTGRAY), 0, item.getTitle().length(), 0);
        	item.setTitle(whiteText);
        }
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
    	return this.onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (this.handleMenuItemClick(item.getItemId()))
    	{
    		return true;
        }
    	return super.onOptionsItemSelected(item);
    }

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) // Google Glass tap
		{
			invalidateOptionsMenu();
			openOptionsMenu();
		} 
		return true;
 	}
	
	public boolean onTouchEvent(MotionEvent event) 
	{
		openOptionsMenu();
	    return true;
	}
	
    public boolean onMenuItemClick(MenuItem item) 
    {
    	return this.handleMenuItemClick(item.getItemId());
    }
    	    
    private boolean handleMenuItemClick(int itemId)
    {
        switch (itemId) {
        	case R.id.action_cattle_reported:
        		Intent intent = new Intent(this, AugmateWarehouseUserLogin.class);
        		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(intent);        		
        		return true;        	
        default:
            return false;
        }
    }
    
	@Override
    public void onBackPressed() {
		this.finish();
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK // Google Glass swipe down
				&& !Build.MANUFACTURER.equals("Vuzix")) {
			finish();
		}
		return true;
	}
}
