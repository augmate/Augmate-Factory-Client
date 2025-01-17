package com.augmate;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

public class SkypeHelper {
	/**
	 * Determine whether the Skype for Android client is installed on this device.
	 */
	public static boolean isSkypeClientInstalled(Context myContext) {
	  PackageManager myPackageMgr = myContext.getPackageManager();
	  try {
	    myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
	  }
	  catch (PackageManager.NameNotFoundException e) {
	    return (false);
	  }
	  return (true);
	}
	
	/**
	 * Install the Skype client through the market: URI scheme.
	 */
	public static void goToMarket(Context myContext) {
	  Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
	  Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
	  myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	  myContext.startActivity(myIntent);

	  return;
	}

	public static void initiateSkypeUri(Context myContext, String mySkypeUri) {

		  // Make sure the Skype for Android client is installed
		  if (!isSkypeClientInstalled(myContext)) {
//		    goToMarket(myContext);
		    return;
		  }

		  // Create the Intent from our Skype URI
		  Uri skypeUri = Uri.parse(mySkypeUri);
		  Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

		  // Restrict the Intent to being handled by the Skype for Android client only
		  myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
		  myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		  // Initiate the Intent. It should never fail since we've already established the
		  // presence of its handler (although there is an extremely minute window where that
		  // handler can go away...)
		  myContext.startActivity(myIntent);

		  return;
		}
	
	public static void initiateSkype(Context myContext) {
//      initiateSkypeUri(myContext,"skype:holahreiken?call&video=true");
//      initiateSkypeUri(myContext,"skype:pete.wassell?call&video=true");
	      initiateSkypeUri(myContext,"skype:augsuper?call&video=true");
//	      initiateSkypeUri(myContext,"skype:vuzixcorp?call&video=true");
//      initiateSkypeUri(myContext,"skype:drew_austin1?call&video=true");
	}

}
