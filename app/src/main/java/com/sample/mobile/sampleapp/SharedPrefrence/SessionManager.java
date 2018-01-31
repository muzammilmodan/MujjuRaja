package com.sample.mobile.sampleapp.SharedPrefrence;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;

public class SessionManager
{
	private static final String PREF_NAME = "1DEASLLC";
	private static final String USER_ID = "userid";
	private static final String LOCATION_ID = "location";
	private static final String REMEMBER_PASS = "rememberpass";
	private static final String REMEMBER_EMAIL = "rememberemail";
	private static final String REMEMBER = "remember";
	private static final String EMAIL = "email";
	private static final String FNAME = "firstName";
	private static final String LNAME = "lastName";
	private static final String USER_TYPE = "userType";
	private static final String LOGIN_TYPE = "loginType";
	private static final String LOGIN = "login";
	private static final String BUSINESSNAME = "businessName";
    private static final String CURRENCY_ID = "currencyId";

	//Save card detail
	private static final String CARD_HOLDER_NAME = "strCardHolderName";
	private static final String CARD_NUMBER = "strCardNumber";
	private static final String CARD_CVV = "strCvv";
	private static final String CARD_EXP_MONTH = "strExpMonth";
	private static final String CARD_EXP_YEAR = "strExpYear";


	public static void setNormalUserDetails(Context context, int uId,String email,String fname,String lname,int userType,int loginType)
	{
		try
		{
			SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			Editor editor;
			editor = prefSignupData.edit();
			editor.putInt(USER_ID, uId);
			editor.putString(EMAIL, email);
			editor.putString(FNAME,fname);
			editor.putString(LNAME,lname);
			editor.putInt(USER_TYPE, userType);
			editor.putInt(LOGIN_TYPE, loginType);
			editor.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setBusinessUserDetails(Context context, int uId,String email,String businessName,int userType,String strCurrency) {
		try {
			SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			Editor editor;
			editor = prefSignupData.edit();
			editor.putInt(USER_ID, uId);
			editor.putString(EMAIL, email);
			editor.putString(BUSINESSNAME, businessName);
			editor.putInt(USER_TYPE, userType);
			editor.putString(CURRENCY_ID, strCurrency);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}





	public static void saveCardDetail(Context context, String strCardHolderName,String strCardNumber,String strCvv,String strExpMonth,String strExpYear) {
		try {
			SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			Editor editor;
			editor = prefSignupData.edit();
			editor.putString(CARD_HOLDER_NAME, strCardHolderName);
			editor.putString(CARD_NUMBER, strCardNumber);
			editor.putString(CARD_CVV, strCvv);
			editor.putString(CARD_EXP_MONTH, strExpMonth);
			editor.putString(CARD_EXP_YEAR, strExpYear);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	public static int getUsersId(Context context) {
		int intUserId = 0;
		try {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			intUserId = pref.getInt(USER_ID, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intUserId;
	}

	public static int getLoginType(Context context) {
		int intUserId = 0;
		try {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			intUserId = pref.getInt(LOGIN_TYPE, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intUserId;
	}



	public static String getCurrency(Context context) {
		String intUserId = "";
		try {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			intUserId = pref.getString(CURRENCY_ID, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intUserId;
	}

	public static String getFirstName(Context context) {
		String strFirstName = "";
		try {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			strFirstName = pref.getString(FNAME, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strFirstName;
	}

	public static String getLastName(Context context) {
		String strLastName = "";
		try {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			strLastName = pref.getString(LNAME, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strLastName;
	}

	public static String getEmail(Context context) {
		String strEmail = "";
		try {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			strEmail = pref.getString(EMAIL, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strEmail;
	}

	public static ArrayList<String> alGetCardDetail (Context context) {

		ArrayList<String> alCardDetail = new ArrayList<String>();

		SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

		String strCardHolderName = pref.getString(CARD_HOLDER_NAME, "");
		String strCardNumber  = pref.getString(CARD_NUMBER, "");
		String strCvv = pref.getString(CARD_CVV, "");
		String strExmMonth= pref.getString(CARD_EXP_MONTH, "");
		String strExpYear= pref.getString(CARD_EXP_YEAR, "");

		alCardDetail.add(strCardHolderName);
		alCardDetail.add(strCardNumber);
		alCardDetail.add(strCvv);
		alCardDetail.add(strExmMonth);
		alCardDetail.add(strExpYear);
		return alCardDetail;
	}



	public static void setCheckedInLocationId(Context context, int locationID)
	{
		try
		{
			SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			Editor editor;
			editor = prefSignupData.edit();
			editor.putInt(LOCATION_ID, locationID);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getCheckedInLocationId(Context context) {
		int intUserId = 0;
		try {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			intUserId = pref.getInt(LOCATION_ID, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intUserId;
	}

	public static int getUserType(Context context) {
		int isLogin = 0;
		try {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			isLogin = pref.getInt(USER_TYPE,0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isLogin;
	}

	public static void setRemember(Context context, int val)
	{
		try
		{
			SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			Editor editor;
			editor = prefSignupData.edit();
			editor.putInt(REMEMBER,val);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getRemember(Context context) {
		int strUserId =0;
		try {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			strUserId = pref.getInt(REMEMBER,0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strUserId;
	}

	public static void setRememberIdPass(Context context, String email,String pass)
	{
		try
		{
			SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			Editor editor;
			editor = prefSignupData.edit();
			editor.putString(REMEMBER_EMAIL, email);
			editor.putString(REMEMBER_PASS, pass);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getRememberEmail(Context context) {
		String strUserId = "";
		try {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			strUserId = pref.getString(REMEMBER_EMAIL, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strUserId;
	}

	public static String getRememberPass(Context context) {
		String strUserId = "";
		try {
			SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
			strUserId = pref.getString(REMEMBER_PASS, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strUserId;
	}

    public static void clearCredential(Context context)
    {
        try {
            Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
            editor.clear();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}