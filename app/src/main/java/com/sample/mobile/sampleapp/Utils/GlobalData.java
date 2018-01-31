package com.sample.mobile.sampleapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;


public class GlobalData {

  public static final int intFlagShow = 1;
    public static final int intFlagHide = 0;

    public static final String STR_INTERNET_ALERT_TITLE = "Network Error!";
    public static final String APP_NAME = "HOWDY_CROWDY";
    public static final String STR_INETRNET_ALERT_MESSAGE = "Please check your Internet connection.";

    public static final String BUSINESS_NAME = "businessName";
    public static final String BUSINESS_CATOGARY = "businessCatogary";
    public static final String BUSINESS_EMAIL = "businessEmail";
    public static final String BUSINESS_WEB = "businessWeb";
    public static final String BUSINESS_PASSWORD = "businessPassword";
    public static final String BUSINESS_DESC = "businessDesc";
    public static final String BUSINESS_PROFILE_PIC = "profilePic";
    public static final String BUSINESS_USER_TYPE = "userType";

    public static final String INTENT_KEY_PROFILE = "Profile";
    public static final String INTENT_KEY_OWNPROFILE = "OwnProfile";
    public static final String INTENT_KEY_OTHER_USERPROFILE = "OtherUserProfile";
    public static final String INTENT_KEY_POST_MODEL = "PostModel";
    public static final String INTENT_KEY_TERMSANDCONDITIONS = "TermsAndConditions";

    public static final String TYPE_VIDEO = "4";
    public static final String TYPE_AUDIO = "3";
    public static final String TYPE_IMAGE = "2";
    public static final String TYPE_TEXT = "1";

    public static final String INTENT_KEY_INVOICE_ID = "invoiceId";
    public static final String INTENT_KEY_LOCATION_ID = "locationId";
    public static final String INTENT_KEY_ADD_NAME = "addName";
    public static final String INTENT_KEY_ADD_PRICE = "addPrice";
    public static final String INTENT_KEY_LOCATION_NAME = "locationName";
    public static final String INTENT_KEY_ADDED_TIME = "addedTime";
    public static final String INTENT_KEY_AD_ID = "advertiseId";
    public static final String INTENT_KEY_AD_START_TIME = "addStartTime";
    public static final String INTENT_KEY_AD_END_TIME = "addEndTime";
    public static final String INTENT_KEY_ITEM_ID = "itemId";
    public static final String INTENT_KEY_ITEM_TYPE = "itemType";
    public static final String INTENT_KEY_ADD_ID = "addId";
    public static final String INTENT_KEY_DESCRIPTION = "description";

    public static final String INTENT_KEY_PLAY_URL = "URL";

    public static final String USER_BLOCK_ALERT = "Your profile has been blocked by admin. Please contact to administrator.";
    public static final String USER_NOT_EXIST = "User does not exist. Please contact to administrator for more info!";


    public static  Boolean isUserBlockedByAdmin = false; // If user block by admin then condition will be true.
    public static  Boolean isUserNotExist = false; // if user delete by admin then condition will be true.



	
	
	public static final String APPLICATION_NAME = "Pregmma app";

	public static final int intDeviceType = 1;
	public static int checkData = 1;
	//public static int setBackButton = 0;
	public static int isNinja = 0;
	
	public static ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
			ScaleAnimation.RELATIVE_TO_SELF, .5f,
			ScaleAnimation.RELATIVE_TO_SELF, .5f);
	public static final int SCALE_TIME = 140;


	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String SENDER_ID = "953358091857";   //563133514454


	// Sharing Message
	public static final String FACEBOOK_SUCCESS = "Your message has been posted successfully!";
	public static final String FACEBOOK_FAIL = "Failed to post!";
	public static final String TWEET_SUCCESS = "Your twitt has been posted successfully!";
	public static final String TWEET_FAIL = "You have already twitted the same Tweet!";

	public static final String STR_PROGRESS_DIALOG_MESSAGE = "Please Wait...";
//	public static final String STR_INTERNET_ALERT_TITLE = "Network Error!";
//	public static final String STR_INETRNET_ALERT_MESSAGE = "Internet is not available. Please try later.";
	public static final String STR_FORGOT_PASSWORD_ALERT_MESSAGE = "Forgot Password";
	public static final String STR_ENTER_EMAIL_ALERT_MESSAGE = "Please enter your email id";

	// Change Password Validation
	public static final String STR_CHANGEPASS_OLD_EMPTY = "Enter old password";
	public static final String STR_CHANGEPASS_NEW_EMPTY = "Enter new password";
	public static final String STR_CHANGEPASS_CONFIRM_EMPTY = "Enter confirm password";
	public static final String STR_CHANGEPASS_OLD_NEW_MATCH = "Old password and new password are same";
	public static final String STR_CHANGEPASS_NEW_CONFIRM_NOT_MATCH = "New passowrd and confirm password are not same";
	public static final String STR_CHANGEPASS_SUCCESS = "Password has been changed successfully";
	
	public static final String TAB_PROFILE = "Profile";
	public static final String TAB_VENUE = "Venue";	
	public static final String TAB_KEEN_LIST = "KeenList";
	public static final String TAB_FAVOURITES = "Favourites";
	public static final String TAB_SETTING = "Setting";
	public static final String TAB_NOTIFICATION = "Notification";
	
	public static final int TAB_PROFILE_INDEX = 0;
	public static final int TAB_VENUE_INDEX = 1;
	public static final int TAB_KEEN_LIST_INDEX = 2;
	public static final int TAB_FAVOURITES_INDEX = 3;
	public static final int TAB_SETTING_INDEX = 4;
	public static final int TAB_NOTIFICATION_INDEX = 5;
	
	public static int LAST_TAB_INDEX = 1;
	
	public static ArrayList<String> keenFoursquareId = new ArrayList<String>();
	public static ArrayList<String> keenDBId = new ArrayList<String>();
	public static ArrayList<String> favouriteFoursquareId = new ArrayList<String>();
	public static ArrayList<String> favouriteDBId = new ArrayList<String>();
	
	public static String MENU_RESTAURANT = "VENUE";
	public static String MENU_KEEN = "KEEN";
	public static String MENU_FAVOURITE = "FAVORITE";
	
	public static boolean isFROMNotification = false;
	
	public static int getScreenWidth(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.x;
	}
	
	public static int getScreenHeight(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.y;
	}
	
	/** Method to turn on GPS **/

	@SuppressWarnings("deprecation")
	public static void turnGPSOn(Context context) {
		try {

			String provider = Settings.Secure.getString(context.getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

			if (!provider.contains("gps")) { // if gps is disabled
				final Intent poke = new Intent();
				poke.setClassName("com.android.settings",
						"com.android.settings.widget.SettingsAppWidgetProvider");
				poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
				poke.setData(Uri.parse("3"));
				context.sendBroadcast(poke);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Method to turn off GPS **/
	
	@SuppressWarnings("deprecation")
	public static void turnGPSOff(Context context){
	    String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(provider.contains("gps")){ //if gps is enabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        context.sendBroadcast(poke);
	    }
	}
}
