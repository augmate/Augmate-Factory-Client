package com.augmate;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.augmate.DownloadImageTask.DownloadImageTaskConfirmationDelegate;
import com.augmate.MenuPagerMenuItem.MenuPagerMenuItemListener;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AugmateWarehouseOrder extends FragmentActivity implements WebServiceCallbackable, DownloadImageTaskConfirmationDelegate
													, PopupMenu.OnMenuItemClickListener, MenuPagerMenuItemListener
{
	Order order;
	User user;
	int currentProductIndex = 0;
	List<String> codeTypes;
	MediaPlayer mp;
	
	static int orderRequestCode = 0;
	static int updateQtyPickedRequestCode = orderRequestCode + 1;
	static int completingOrderRequestCode = updateQtyPickedRequestCode + 1;
	static int confirmDeliveryRequestCode = completingOrderRequestCode + 1;

	// User the zxing request code + 1, so it will be different
	static int CONFIRM_PICK_REQUEST_CODE = IntentIntegrator.REQUEST_CODE + 1;
	static int CONFIRM_DELIVERY_REQUEST_CODE = CONFIRM_PICK_REQUEST_CODE + 1;

	private boolean receivedBackKeyDown = false; 
	private GestureDetector gestureDetector;
	private boolean receivedSwipeDown = false;
	private boolean selectedReportCattle = false;
	private boolean showConfirmation = false;
	
	Context context;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    context=(Context)this;
	    // TODO Auto-generated method stub
	    setContentView(R.layout.current_product_to_pick);
		TextView skuText = (TextView) findViewById(R.id.NameTextView);
		skuText.setText("Loading...");

		TextView latestDiagnosisLabel = (TextView) findViewById(R.id.latestDiagnosis);
		latestDiagnosisLabel.setVisibility(View.INVISIBLE);
		
	    user= (User)getIntent().getSerializableExtra("User");
	    
	    Toast.makeText(this, "Looking up your animal now. Please wait", Toast.LENGTH_SHORT).show();
	    
	    //Product Scan codes
	    this.codeTypes= new ArrayList<String>();
	    this.codeTypes.add("UPC_A");
	    this.codeTypes.add("UPC_E");	
		
	    String token = user.username + ":" + user.password;
		WebServiceCallHelper webServiceCallHelper = new WebServiceCallHelper(this, orderRequestCode, "nextOrder/0", token);
		webServiceCallHelper.execute();

		LinearLayout ll = (LinearLayout) findViewById(R.id.productLayout);
		ll.setVisibility(View.INVISIBLE);
		LinearLayout llProductRight = (LinearLayout) findViewById(R.id.productRightLayout);
		llProductRight.setVisibility(View.INVISIBLE);
		
		// Commenting this out to not allow swiping between products
		if (Build.MANUFACTURER.equals("Google"))
		{
			this.createGestureDetector(context);
		}
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	private void createGestureDetector(Context context)
	{
		this.gestureDetector = new GestureDetector(context);
		this.gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			
			public boolean onGesture(Gesture gesture) {
				if (gesture == Gesture.SWIPE_RIGHT)
				{
					goToNextProduct();
					return true;
				}
				else if (gesture == Gesture.SWIPE_LEFT)
				{
					goToPreviousProduct();
					return true;
				}
				return false;
			}
		});
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
        if (receivedSwipeDown)
        {
        	this.receivedSwipeDown = false;
        	inflater.inflate(R.menu.augmate_warehouse_confirm_logout, menu);
        }
        else if (selectedReportCattle)
        {
        	this.selectedReportCattle = false;
        	inflater.inflate(R.menu.augmate_zoetis_cattle_report_menu, menu);
        }
        else if (showConfirmation)
        {
        	this.showConfirmation = false;
        	inflater.inflate(R.menu.augmate_zoetis_cattle_reported_menu, menu);
        }
        else
        {
	        inflater.inflate(R.menu.augmate_warehouse_order_item_menu, menu);	        
        }
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
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER)
				&& Build.MANUFACTURER.equals("Vuzix")) 
		{
			finish();
			return true;
		}
		return super.onKeyLongPress(keyCode, event);
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
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
		}
		else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)		
		{
			// Commenting this out to not allow swiping between products
//			goToNextProduct();
		} 
		else if  (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
		{
			// Commenting this out to not allow swiping between products
//			goToPreviousProduct();
		}
		else if (keyCode == KeyEvent.KEYCODE_BACK // Google Glass swipe down
				&& !Build.MANUFACTURER.equals("Vuzix")) {
			receivedSwipeDown = true;
			invalidateOptionsMenu();
			openOptionsMenu();
//			finish();
		}
		else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) // Google Glass tap
		{
			invalidateOptionsMenu();
			openOptionsMenu();
		} 
		this.receivedBackKeyDown = false;
		return true;
 	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER)
				&& Build.MANUFACTURER.equals("Vuzix")
				&& event.getRepeatCount() == 0) 
		{
			// Start tracking for long press of back button, which exits the app from this screen
			event.startTracking();
			this.receivedBackKeyDown = true;
			return true;
		}
		return true;
	}

	public boolean onTouchEvent(MotionEvent event) 
	{
		openOptionsMenu();
	    return true;
	}
	
	private void showMenuPagerDialogFragment() {
		MenuPagerDialogFragment dialogMenu = new MenuPagerDialogFragment();
	    dialogMenu.setMenuItemListener(this);
	    dialogMenu.addMenuItem(getString(R.string.report_cattle), R.drawable.scan, R.id.action_report_cattle);
//	    dialogMenu.addMenuItem(getString(R.string.action_report_item), R.drawable.alert, R.id.action_report_item);
//	    dialogMenu.addMenuItem(getString(R.string.action_call_manager), R.drawable.alert, R.id.action_call_manager);
	    dialogMenu.show(getSupportFragmentManager(), "");
	}

	private void goToNextProduct() {
		ArrayList<Product> products = order.products;
		if (products.size() > 0) {
			if (currentProductIndex < products.size() - 1) {
				currentProductIndex++;
			} else {
				currentProductIndex = 0;
			}
			populate();
		}
	}
	
	private void goToPreviousProduct() {
		ArrayList<Product> products = order.products;
		if (products.size() > 0) {
			if (currentProductIndex > 0) {
				currentProductIndex--;
			} else {
				currentProductIndex = products.size() - 1;
			}
			populate();
		}
	}

	private boolean mScanResultWasSuccessful = true;
	private boolean mReturningFromScanResult = false;
	
	@Override
	protected void onPostResume()
	{
		super.onPostResume();
		// Had to put this code here to avoid the IllegalArgumentException that gets thrown if attempting to show the alert dialog
		// during onActivityResult
		if (this.mReturningFromScanResult)
		{
			if (mScanResultWasSuccessful)
			{
	    		final AlertMessageDialogFragment alert = AlertMessageDialogFragment.newInstance("ITEM MATCH", R.drawable.complete, Color.GREEN);
	    		alert.show(getSupportFragmentManager(), "");
	    		final AugmateWarehouseOrder me = this;
	    		Timer timer = new Timer();
	    		timer.schedule(new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							
							public void run() {
								alert.dismiss();
					    		// TODO: This should probably happen after the web service has successfully recorded the pick...
								if (orderComplete()) 
								{
								    String token = user.username + ":" + user.password;
								    String command = String.format("completingOrder/%d", me.order.idOrder);
								    WebServiceCallHelper completeOrderHelper = new WebServiceCallHelper(me, completingOrderRequestCode, command, token);
								    completeOrderHelper.execute();
								} 
								else 
								{
									goToNextProduct();
								}
							}
						});
					}
				}, 2000);
			}
			else
			{
				this.mScanResultWasSuccessful = false;
				//Toast.makeText(AugmateWarehouseOrder.this, "The sku product no match",Toast.LENGTH_LONG).show();
				mp = MediaPlayer.create(AugmateWarehouseOrder.this, R.raw.buzzer);  
				mp.start();
				
	    		final AlertMessageDialogFragment alert = AlertMessageDialogFragment.newInstance("WRONG ITEM SCANNED", R.drawable.redalert, Color.RED);
	    		alert.show(getSupportFragmentManager(), "");
	    		Timer timer = new Timer();
	    		timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							
							public void run() {
								alert.dismiss();
							}
						});
					}
				}, 2000);				
	    	}
		}
		this.mReturningFromScanResult = false;
	}
	// get the scan result
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == IntentIntegrator.REQUEST_CODE) {
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, intent);
			if (scanResult.getContents() != null) {
				String result = scanResult.getContents();
			
				ArrayList<Product> products = order.products;
				Product lookingFor = products.get(currentProductIndex);
			
				this.mReturningFromScanResult = true;
				if (result.contentEquals(lookingFor.sku)) {
					this.mScanResultWasSuccessful = true;
					mp = MediaPlayer.create(AugmateWarehouseOrder.this, R.raw.ding); 
					mp.setVolume(1.0f, 1.0f);
					mp.start();

					Product currentProduct = products.get(currentProductIndex);
					currentProduct.quantityPicked = currentProduct.quantityToPick;

				    String token = user.username + ":" + user.password;
				    String command = String.format("pickProduct/%d?n=%d", currentProduct.idProduct, currentProduct.quantityPicked);
		    		WebServiceCallHelper webServiceCallHelper = new WebServiceCallHelper(this, updateQtyPickedRequestCode, command, token);
		    		webServiceCallHelper.execute();
				}
				else
				{
					this.mScanResultWasSuccessful = false;
					//Toast.makeText(AugmateWarehouseOrder.this, "The sku product no match",Toast.LENGTH_LONG).show();
					mp = MediaPlayer.create(AugmateWarehouseOrder.this, R.raw.buzzer);  
					mp.start();			
		    	}
			}
		} else if (requestCode == CONFIRM_DELIVERY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
			    String token = user.username + ":" + user.password;
			    String command = String.format("confirmDelivery/%d", this.order.idOrder);
				WebServiceCallHelper webServiceCallHelper = new WebServiceCallHelper(
						this, confirmDeliveryRequestCode, command , token);
	    		webServiceCallHelper.execute();
			}
		}
	}
	
	private boolean orderComplete() {
		boolean orderComplete = true;
		
		ArrayList<Product> products = order.products;
		for (int i=0; i<products.size(); i++) {
			Product currentProduct = products.get(i);
			if (currentProduct.quantityPicked != currentProduct.quantityToPick) {
				orderComplete = false;
				break;
			}
		}

		return orderComplete;
	}

	private void populate() {
		
		ArrayList<Product> products = order.products;
		Product nextProduct = products.get(currentProductIndex);
		Log.d("product", nextProduct.toString());

		// For Zoetis, we are mapping:
		// Product name => Diagnosis
		// Quantity => Weight
		// Shelf Unit => Pen Lot
		// Aisle => Days since treatment
		// Shelf => Cattle ID
		TextView nameTextView = (TextView) findViewById(R.id.NameTextView);
		nameTextView.setText(nextProduct.name);

		TextView qtyToPickTextView = (TextView) findViewById(R.id.qtyToPickTextView);
		qtyToPickTextView.setText(String.format("%d lbs", nextProduct.quantityToPick));
		
		TextView aisleTextView = (TextView) findViewById(R.id.aisleValueTextView);
		aisleTextView.setText(String.format("%s", nextProduct.aisle));

		TextView shelfUnitTextView = (TextView) findViewById(R.id.shelfUnitValueTextView);
		shelfUnitTextView.setText(String.format("%s", nextProduct.shelfUnit));
			
		TextView shelfTextView = (TextView) findViewById(R.id.shelfValueTextView);
		shelfTextView.setText(String.format("%s", nextProduct.shelf));
			
		TextView latestDiagnosisLabel = (TextView) findViewById(R.id.latestDiagnosis);
		latestDiagnosisLabel.setVisibility(View.VISIBLE);

		LinearLayout ll = (LinearLayout) findViewById(R.id.productLayout);
		ll.setVisibility(View.VISIBLE);
		
		LinearLayout llProductRight = (LinearLayout) findViewById(R.id.productRightLayout);
		llProductRight.setVisibility(View.VISIBLE);
	}
	
	public void webServiceCallback(int requestCode, int status, JSONObject jObj) {
		
		// HO-R still need to check for exceptions from server
		if(status == 200) {
			if (requestCode == orderRequestCode || requestCode == confirmDeliveryRequestCode) {
				Log.d("AugmateWarehouseOrder.webServiceCallback:",
						jObj.toString());

				this.order = new Order();
				order.parseJson(jObj);
				currentProductIndex = 0;
				this.populate();
			} else if (requestCode == completingOrderRequestCode) {
				String packingLocation = "";
				try {
					packingLocation = (jObj.has("packagingLocation"))?jObj.getString("packagingLocation"):"";
				}catch(JSONException e){
					Log.e(User.class.toString(), "Failed to parse"+e.toString());
				}

	    		Intent confirmPickIntent = new Intent(this, AugmateWarehouseConfirmDelivery.class);
				confirmPickIntent.putExtra("packingLocation", packingLocation);
				confirmPickIntent.putExtra("Order", this.order);
				startActivityForResult(confirmPickIntent, CONFIRM_DELIVERY_REQUEST_CODE);										
			}
		}
		else if(status == 401){
		}
		else{
		}
	}

	public boolean shouldDisplayImage(String tag) {
		ArrayList<Product> products = order.products;
		Product nextProduct = products.get(currentProductIndex);
		if (nextProduct.sku.equals(tag))
		{
			return true;
		}
		return false;
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
    
    public boolean onMenuItemClick(MenuItem item) 
    {
    	return this.handleMenuItemClick(item.getItemId());
    }
    	    
    private boolean handleMenuItemClick(int itemId)
    {
        switch (itemId) {
//        case R.id.action_scan_item:
//			IntentIntegrator xzingIntegrator = new IntentIntegrator(this);
//			xzingIntegrator.initiateScan(codeTypes);
//            return true;
        case R.id.action_report_cattle:
    		this.selectedReportCattle = true;
    		new Timer().schedule(new TimerTask() {
				
				@Override
				public void run() {
					runOnUiThread(new Runnable() {						
						public void run() {
							invalidateOptionsMenu();
							openOptionsMenu();
						}
					});
				}
			}, 100);
    		
            return true;
//        case R.id.action_call_manager:
//        	SkypeHelper.initiateSkype(this);
//            return true;
        case R.id.action_needs_treatment:
			Intent illnessLibraryIntent = new Intent(this, AugmateZoetisIllnessLibrary.class);
			startActivity(illnessLibraryIntent);
			return true;

        case R.id.action_to_market:
        case R.id.action_dead:
        	this.showConfirmation = true;
        	new Timer().schedule(new TimerTask() {
				
				@Override
				public void run() {
					runOnUiThread(new Runnable() {						
						public void run() {
							invalidateOptionsMenu();
							openOptionsMenu();
						}
					});
				}
			}, 100);
    		
            return true;
        case R.id.action_cattle_reported:
        	finish();
        	return true;
        	
        case R.id.action_logout:
        	finish();
        	return true;
        default:
            return false;
        }
    }
    
	public void menuPagerMenuItemSelected(MenuPagerMenuItem item) {
    	this.handleMenuItemClick(item.getItemId());		
	}
}
