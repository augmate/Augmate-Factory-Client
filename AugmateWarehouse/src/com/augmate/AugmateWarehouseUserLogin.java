package com.augmate;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.PopupMenu;

import com.augmate.MenuPagerMenuItem.MenuPagerMenuItemListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AugmateWarehouseUserLogin extends FragmentActivity implements
		WebServiceCallbackable, PopupMenu.OnMenuItemClickListener, MenuPagerMenuItemListener  {

	private static final String DEBUG_TAG = "AugmateWarehouseUserLogin";
	
	final int START_ORDER = IntentIntegrator.REQUEST_CODE + 1;
	final int TUTORIAL_VIDEO = START_ORDER + 1;
	private boolean receivedBackKeyDown = false; 
	
	User user = new User();
	
	TextView QRTextView;
	MediaPlayer mp;
	
	static int loginRequestCode = 0;
	static int userTrainedRequestCode = loginRequestCode + 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		QRTextView = (TextView) findViewById(R.id.QRTextView);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER)
				&& Build.MANUFACTURER.equals("Vuzix")) 
		{
			finish();
			return true;
		}
		return super.onKeyLongPress(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
//		Log.v(DEBUG_TAG, "onKeyUp: Manufacturer, Brand, Model: " + Build.MANUFACTURER + ", " + Build.BRAND + ", " + Build.MODEL);
//		Log.v(DEBUG_TAG, "onKeyUp: keyCode: '" + keyCode + "': event: " + event.toString());
		if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER)
				&& Build.MANUFACTURER.equals("Vuzix")) 
		{
			if (!((event.getFlags() & KeyEvent.FLAG_CANCELED_LONG_PRESS) == KeyEvent.FLAG_CANCELED_LONG_PRESS)
				&& !((event.getFlags() & KeyEvent.FLAG_LONG_PRESS) == KeyEvent.FLAG_LONG_PRESS))
			{
				// Put in this safeguard because we were getting additional key up events from dismissal of the Zxing app
				if (this.receivedBackKeyDown)
				{
				    showMenuPagerDialogFragment();
				}
			}
////		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP
////				|| keyCode == KeyEvent.KEYCODE_DPAD_CENTER // Google Glass tap
////				) {
////			IntentIntegrator zxingIntegrator = new IntentIntegrator(this);
//////			zxingIntegrator.setMessage("Will we want a message here?");
////			zxingIntegrator.initiateScan();
////		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
////				|| keyCode == KeyEvent.KEYCODE_TAB && event.isShiftPressed()) // Google Glass swipe backward
////		{
////            initiateSkype((Context)this);
////            /*
////            QRTextView.setText("Logging In");
////			String token = "pete:ca72431bf6bc7729c82dbb19ed7a9f2c";
////			WebServiceCallHelper webServiceCallHelper = new WebServiceCallHelper(this, loginRequestCode, "login/0", token);
////			webServiceCallHelper.execute();
////			*/s
////		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
////			finish();
		}
		this.receivedBackKeyDown = false;
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.v(DEBUG_TAG, "onKeyDown: keyCode: '" + keyCode + "': event: " + event.toString());
		if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER)
				&& Build.MANUFACTURER.equals("Vuzix")
				&& event.getRepeatCount() == 0) 
		{
			// Start tracking for long press of back button, which exits the app from this screen
			event.startTracking();
			this.receivedBackKeyDown = true;
			return true;
		}
		if (
			keyCode == KeyEvent.KEYCODE_DPAD_CENTER // Google Glass tap
				) {
			openOptionsMenu();
		} 
		else if (keyCode == KeyEvent.KEYCODE_BACK // Google Glass swipe down
				&& !Build.MANUFACTURER.equals("Vuzix")) {
			finish();
		}
		return true;
	}
	
	public boolean onTouchEvent(MotionEvent event) 
	{
	    Log.v(DEBUG_TAG,"OnTouchEvent !!!");
	    // HO-R For now faking a logging by clicking on view when in emulator
//		QRTextView.setText("Logging In");
//		String token = "pete:ca72431bf6bc7729c82dbb19ed7a9f2c";
//		WebServiceCallHelper webServiceCallHelper = new WebServiceCallHelper(this, loginRequestCode, "login/0", token);
//		webServiceCallHelper.execute();
		openOptionsMenu();
	    return true;
	}

	private void showMenuPagerDialogFragment() {
		MenuPagerDialogFragment dialogMenu = new MenuPagerDialogFragment();
	    dialogMenu.setMenuItemListener(this);
	    dialogMenu.addMenuItem(getString(R.string.action_signin), R.drawable.scan, R.id.action_signin);
//	    dialogMenu.addMenuItem(getString(R.string.action_play_tutorial), R.drawable.play, R.id.action_play_tutorial);
	    dialogMenu.show(getSupportFragmentManager(), "");
	}
	
	@Override
    public void onBackPressed() {
     this.finish();
    }
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		if (requestCode == START_ORDER) {
			
			Intent newintent = new Intent(this, AugmateWarehouseOrder.class);
			newintent.putExtra("User", user);
			QRTextView.setText("");
			startActivity(newintent);

		} else if (requestCode == IntentIntegrator.REQUEST_CODE) {

			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, intent);
			if (scanResult.getContents() != null) {
				String result = scanResult.getContents();
				// QRTextView.setText(result);
				QRTextView.setText("Looking up animal...");
				String token = result;
				WebServiceCallHelper webServiceCallHelper = new WebServiceCallHelper(
						this, loginRequestCode, "login/1", token);
				webServiceCallHelper.execute();

			}
		}
	}

	public void webServiceCallback(int requestCode, int status, JSONObject jObj) {
		
		if (requestCode == loginRequestCode) {
			
			if(status == 200){
				Log.d("AugmateWarehouseActivity.webServiceCallback:", jObj.toString());
				mp = MediaPlayer.create(AugmateWarehouseUserLogin.this, R.raw.ding);  
				mp.start();

				user.parseJson(jObj);		

				Intent newintent = new Intent(this, AugmateWarehouseOrder.class);
				newintent.putExtra("User", user);
				QRTextView.setText("");
				startActivity(newintent);

//				Intent intent = new Intent(this, glassvideo.class);
//				startActivityForResult(intent, START_ORDER);
			}
			else if(status == 401){
				QRTextView.setText("User/Password is incorrect");
				mp = MediaPlayer.create(AugmateWarehouseUserLogin.this, R.raw.buzzer);  
				mp.start();
			}
			else if(status == 501){ // HO-R for now
				QRTextView.setText("Could not reach server");
			}
			else {
				QRTextView.setText(String.format(
						"Status code not handeled: %d", status));
			}
		}
	}
					
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.augmate_warehouse_home_menu, menu);
	        for (int i = 0; i < menu.size(); i++)
	        {
	        	MenuItem item = menu.getItem(i);
	        	SpannableString whiteText = new SpannableString(item.getTitle());
	        	whiteText.setSpan(new ForegroundColorSpan(Color.LTGRAY), 0, item.getTitle().length(), 0);
	        	item.setTitle(whiteText);
	        }
	        return true;
	    }

//	    @Override
//	    public boolean onPrepareOptionsMenu(Menu menu) {
//	        // Implement if needed
//	    }

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
	    
	    public boolean onMenuItemClick(MenuItem item) 
	    {
	    	return this.handleMenuItemClick(item.getItemId());
	    }
	    	    
	    private boolean handleMenuItemClick(int itemId)
	    {
	        switch (itemId) {
//            case R.id.action_play_tutorial:
//            	// TODO: Play the tutorial
//				Intent intent = new Intent(this, glassvideo.class);
//				startActivityForResult(intent, TUTORIAL_VIDEO);
//				Log.v(DEBUG_TAG, "Initiating tutorial video...");
//                return true;
            case R.id.action_signin:
    			IntentIntegrator zxingIntegrator = new IntentIntegrator(this);
    			zxingIntegrator.setMessage("Will we want a message here?");
    			zxingIntegrator.initiateScan();
                return true;
            default:
                return false;
	        }
	    }

		public void menuPagerMenuItemSelected(MenuPagerMenuItem item) {
	    	this.handleMenuItemClick(item.getItemId());
		}
}
