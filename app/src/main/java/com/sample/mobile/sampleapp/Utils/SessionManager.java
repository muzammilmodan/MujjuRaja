package com.sample.mobile.sampleapp.Utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import com.example.mobile.pregmmaapp.Activities.BookAppointmentActivity;
import com.example.mobile.pregmmaapp.Model.BookAppointmentModel;
import com.example.mobile.pregmmaapp.Model.DoctorData;
import com.example.mobile.pregmmaapp.Model.DoctorRememberDetail;
import com.example.mobile.pregmmaapp.Model.EmergencyCalllistModel;
import com.example.mobile.pregmmaapp.Model.MedicineAlertlistModel;
import com.example.mobile.pregmmaapp.Model.PatientData;
import com.example.mobile.pregmmaapp.Model.PatientRememberDetail;
import com.example.mobile.pregmmaapp.Model.UserAppointmentlistModel;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Mobile on 6/13/2017.
 */

public class SessionManager {

    private static final String PREF_NAME = "Pregmma_Session";

    //    private static final String KEY_MODE_ID = "mode";
    private static final String KEY_USER_ID= "userid";
    private static final String KEY_USER_NAME= "User_name";
    private static final String KEY_USER_PROFILE_PIC= "userprofile_pic";
    private static final String KEY_PROFILE_PIC= "Profilepic";
    private static final String KEY_PASSWORD= "Password";
    //Doctor
    private static final String KEY_DOCTOR_ID= "doctorid";
    private static final String KEY_DOCTOR_NAME= "Doctorname";
    private static final String KEY_DOCTOR_PROFILE_PIC= "Doctor_profilepic";
    private static final String KEY_DOCTOR_PROFESSION= "Profession";
    private static final String KEY_DOCTOR_GENDER= "Gender";
    private static final String KEY_DOCTOR_WORKAT= "workat";
    private static final String KEY_DOCTOR_CHARGES= "Charges";
    private static final String KEY_DOCTOR_YEAR_OF_EXP= "Yearofexperiance";
    private static final String KEY_DOCTOR_MNG_TM= "Mor_timing";
    private static final String KEY_DOCTOR_NOON_TM= "Noon_timing";
    private static final String KEY_DATE= "Date";
    private static final String KEY_TIME= "Time";
    private static final String KEY_APPOINTMENT_ID= "appointment id";
    private static final String KEY_APPOINT_DOCTOR_ID= "Doctorid";

    private static final String KEY_ALERT_ITEM_ID= "AlertItem_id";
    private static final String KEY_ALERT_ITEM= "Item";
    private static final String KEY_APPOINT_ID= "Appointment_Id";
    private static final String KEY_ADD_CONTACT_ID= "contactid";
//    private static final String KEY_ALERT_ITEM_ID= "AlertItem_id";



    //    Remember
    public static final String KEY_IS_REM_PATIENT = "isRememberPatient";
    public static final String KEY_IS_REM_DOCTOR = "isRememberDoctor";

    public static final String KEY_USER_NAME_NEW = "email";
    public static final String KEY_USER_PASS_NEW = "password";

    public static final String KEY_USER_REMEMBER_NEW = "User Remember";
    public static final String KEY_DOCTOR_REMEMBER_NEW = "Doctor Remember";


    private static final String KEY_USER_TYPE= "Usertype";
    private static final String KEY_FIRST_NAME= "Firstname";
    private static final String KEY_LAST_NAME= "Lastname";
    private static final String KEY_BIRTH_DATE= "Birthdate";
    private static final String KEY_DUE_DATE= "Duedate";
    private static final String KEY_ADDRESS= "Address";
    private static final String KEY_EMAIL_ID= "Emailid";
    private static final String KEY_MOBILE_NO= "Mobileno";
    private static final String KEY_NAME= "Name";
    private static final String KEY_CONTACT_ID= "contact_id id";

    public static final String KEY_IS_LOGIN_PATIENT = "isLoginPatient";
    public static final String KEY_IS_LOGIN_DR= "isLoginDoctor";

    public static final String KEY_PASSWORD_PATIENT= "passwordPatient";
    public static final String KEY_PASSWORD_DR= "passwordDoctor";

    public static final String KEY_IS_SIGNUP_PATIENT= "isSignUpPatient";
    public static final String KEY_IS_SIGNUP_DR= "isSignUpDoctor";


    public static ProgressDialog progressDialog;

    static GoogleApiClient googleApiClient;
    static AlarmManager alarmManager;
    static PendingIntent pendingIntent;

    public static void clearSession(Context context) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
            editor.clear();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isLoginPatient(Context context) {
        try {
//            return context.getSharedPreferences(PREF_NAME, 0).getBoolean(KEY_IS_LOGIN_USER, false);
            // Integer val = 0;
            SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            boolean val = preferences.getBoolean(KEY_IS_LOGIN_PATIENT, false);

            Applog.e("val", "val  : " + val);
            return val;
//            if (context.getSharedPreferences(PREF_NAME, 0).getBoolean(KEY_IS_LOGIN_PATIENT, false) == true) {
//                return true;
//            } else {
//                return false;
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setIsPatientLoggedin(Context context, boolean val) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_IS_LOGIN_PATIENT, val);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isRememberPatient(Context context) {
        try {
//            SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0);
//            return preferences.getBoolean(KEY_IS_REM_USER, false);
            boolean blAutoSave = false;
            try {
                SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                blAutoSave = pref.getBoolean(KEY_IS_REM_PATIENT, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return blAutoSave;
//            if (context.getSharedPreferences(PREF_NAME, 0).getBoolean(KEY_IS_REM_PATIENT, false) == true)
//                return true;
//            else {
//                return false;
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isLoginDr(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            boolean val = preferences.getBoolean(KEY_IS_LOGIN_DR, false);

            Applog.e("val", "val  : " + val);
            return val;
//            if (context.getSharedPreferences(PREF_NAME, 0).getBoolean(KEY_IS_LOGIN_DR, false) == true) {
//                return true;
//            } else {
//                return false;
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setIsDrLoggedin(Context context, boolean val) {
        try {
            SharedPreferences preferences = context
                    .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_IS_LOGIN_DR, val);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isRememberDr(Context context) {
        try {
            boolean blAutoSave = false;
            try {
                SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                blAutoSave = pref.getBoolean(KEY_IS_REM_DOCTOR, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return blAutoSave;
//            if (context.getSharedPreferences(PREF_NAME, 0).getBoolean(KEY_IS_REM_DOCTOR, false) == true) {
//                return true;
//            } else {
//                return false;
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int setPassword(Context context, String status, String message) {
        int i = 0;
        try {
            SharedPreferences pref = context
                    .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor;

            editor = pref.edit();
            editor.putString(AppConstants.STATUS, status);
            editor.putString(AppConstants.MESSAGE, message);

            Applog.i("IsSuccess", "" + i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public static void setRememberPati(Context context, boolean value) {
        try {
            SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor;
            editor = prefSignupData.edit();
            editor.putBoolean(KEY_IS_REM_PATIENT, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRememberPatiData(Context context, String username, String password) {
        try {
            SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(KEY_USER_REMEMBER_NEW, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor;
            editor = prefSignupData.edit();
            editor.putString(KEY_USER_NAME_NEW, username);
            editor.putString(KEY_USER_PASS_NEW, password);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRememberDoctor(Context context, boolean value) {
        try {
            SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor;
            editor = prefSignupData.edit();
            editor.putBoolean(KEY_IS_REM_DOCTOR, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setRememberDrData(Context context, String username, String password) {
        try {
            SharedPreferences prefSignupData = context.getApplicationContext().getSharedPreferences(KEY_DOCTOR_REMEMBER_NEW, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor;
            editor = prefSignupData.edit();
            editor.putString(KEY_USER_NAME_NEW, username);
            editor.putString(KEY_USER_PASS_NEW, password);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PatientRememberDetail getLoginRememberPassUser(Context context) {
        PatientRememberDetail userRememberDetail = new PatientRememberDetail();
        try {
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences(KEY_USER_REMEMBER_NEW, Context.MODE_PRIVATE);
            userRememberDetail.setStrUserName(pref.getString(KEY_USER_NAME_NEW, ""));
            userRememberDetail.setStrPassword(pref.getString(KEY_USER_PASS_NEW, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userRememberDetail;
    }

    public static DoctorRememberDetail getLoginRememberPassDr(Context context) {
        DoctorRememberDetail userRememberDetail = new DoctorRememberDetail();
        try {
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences(KEY_DOCTOR_REMEMBER_NEW, Context.MODE_PRIVATE);
            userRememberDetail.setStrDoctorName(pref.getString(KEY_USER_NAME_NEW, ""));
            userRememberDetail.setStrPassword(pref.getString(KEY_USER_PASS_NEW, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userRememberDetail;
    }

    public static void ClearLoginRememberPassUser(Context context) {
        PatientRememberDetail userRememberDetail = new PatientRememberDetail();
        try {
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences(KEY_USER_REMEMBER_NEW, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor;
            editor = pref.edit();
            editor.clear();
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void ClearLoginRememberPassDoctor(Context context) {
        DoctorRememberDetail userRememberDetail = new DoctorRememberDetail();
        try {
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences(KEY_DOCTOR_REMEMBER_NEW, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor;
            editor = pref.edit();
            editor.clear();
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logOutPatient(Context context) {
        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public static void logOutPatient(Context context, Boolean isLogin) {
//        try {
//            SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
//            editor.putBoolean(KEY_IS_LOGIN_PATIENT, isLogin);
//            editor.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void logoutDocotr(Context context) {
        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//Login and Register

    public static void saveLoginPatienDetails(Context context, PatientData userData) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(SessionManager.KEY_USER_ID, userData.getPatientId());
            editor.putString(SessionManager.KEY_EMAIL_ID, userData.getPatiEmailid());
//            editor.putString(SessionManager.KEY_PASSWORD, userData.getPatientPassword());
            editor.putString(SessionManager.KEY_USER_PROFILE_PIC, userData.getImgUrl());

            Applog.e("URL", "URL USER" + userData.getImgUrl());
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveLoginDrDetails(Context context, DoctorData DrData) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(SessionManager.KEY_DOCTOR_ID, DrData.getDoctorId());
            editor.putString(SessionManager.KEY_DOCTOR_NAME, DrData.getDoctorName());
//            editor.putString(SessionManager.KEY_D, DrData.getDoctorPassword());
            editor.putString(SessionManager.KEY_DOCTOR_PROFILE_PIC, DrData.getDrImgUrl());

            Applog.e("URL", "URL USER" + DrData.getDrImgUrl());
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void savePatientDetail(Context context, String userName, String userPassword, Boolean sign_up) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
            editor.putString(KEY_PASSWORD_PATIENT, userPassword);
            editor.putString(KEY_EMAIL_ID, userName);
            editor.putBoolean(KEY_IS_SIGNUP_PATIENT, sign_up);
            editor.putBoolean(KEY_IS_LOGIN_PATIENT, sign_up);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveDoctorDetail(Context context, String proUserName, String userPassword, Boolean sign_up) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, 0).edit();
            editor.putString(KEY_PASSWORD_DR, userPassword);
            editor.putString(KEY_EMAIL_ID, proUserName);
            editor.putBoolean(KEY_IS_SIGNUP_DR, sign_up);
            editor.putBoolean(KEY_IS_LOGIN_DR, sign_up);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void savePatientData(Context context, PatientData userData) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(SessionManager.KEY_USER_ID, userData.getPatientId());
            editor.putString(SessionManager.KEY_FIRST_NAME, userData.getPatiFirstname());
            editor.putString(SessionManager.KEY_LAST_NAME, userData.getPatiLastname());
            editor.putString(SessionManager.KEY_BIRTH_DATE, userData.getPatiBirthdate());
            editor.putString(SessionManager.KEY_DUE_DATE, userData.getPatiDuedate());
            editor.putString(SessionManager.KEY_ADDRESS, userData.getPatiAddress());
            editor.putString(SessionManager.KEY_MOBILE_NO, userData.getPatiMobileno());
            editor.putString(SessionManager.KEY_EMAIL_ID, userData.getPatiEmailid());
            editor.putString(SessionManager.KEY_USER_PROFILE_PIC, userData.getImgUrl());
            editor.putString(SessionManager.KEY_USER_NAME, userData.getPatiUsername());
            editor.putString(SessionManager.KEY_USER_TYPE, userData.getPatiUserType());

            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PatientData getPatientData(Context context) {
        PatientData userData = new PatientData();
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            userData.setPatientId(preferences.getString(SessionManager.KEY_USER_ID, ""));
            userData.setPatiFirstname(preferences.getString(SessionManager.KEY_FIRST_NAME, ""));
            userData.setPatiLastname(preferences.getString(SessionManager.KEY_LAST_NAME, ""));
            userData.setPatiBirthdate(preferences.getString(SessionManager.KEY_BIRTH_DATE, ""));
            userData.setPatiDuedate(preferences.getString(SessionManager.KEY_DUE_DATE, ""));
            userData.setPatiAddress(preferences.getString(SessionManager.KEY_ADDRESS, ""));
            userData.setPatiEmailid(preferences.getString(SessionManager.KEY_EMAIL_ID, ""));
            userData.setPatiMobileno(preferences.getString(SessionManager.KEY_MOBILE_NO, ""));
            userData.setImgUrl(preferences.getString(SessionManager.KEY_USER_PROFILE_PIC, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userData;
    }

    public static void saveDoctorData(Context context, DoctorData userData) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(SessionManager.KEY_DOCTOR_ID, userData.getDoctorId());
            editor.putString(SessionManager.KEY_DOCTOR_NAME, userData.getDoctorName());
            editor.putString(SessionManager.KEY_DOCTOR_PROFESSION, userData.getDrProfession());
            editor.putString(SessionManager.KEY_DOCTOR_GENDER, userData.getDrgender());
            editor.putString(SessionManager.KEY_BIRTH_DATE, userData.getDrBirthdate());
            editor.putString(SessionManager.KEY_DOCTOR_WORKAT, userData.getDrworkat());
            editor.putString(SessionManager.KEY_ADDRESS, userData.getDrAddress());
            editor.putString(SessionManager.KEY_MOBILE_NO, userData.getDrMobileno());
            editor.putString(SessionManager.KEY_DOCTOR_CHARGES, userData.getDrcharge());
            editor.putString(SessionManager.KEY_DOCTOR_YEAR_OF_EXP, userData.getDrYearofExp());
            editor.putString(SessionManager.KEY_DOCTOR_MNG_TM, userData.getDrMortime());
            editor.putString(SessionManager.KEY_DOCTOR_NOON_TM, userData.getDrNoontime());
            editor.putString(SessionManager.KEY_EMAIL_ID, userData.getDrEmailid());
            editor.putString(SessionManager.KEY_DOCTOR_PROFILE_PIC, userData.getDrImgUrl());

            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DoctorData getDoctorData(Context context) {
        DoctorData userData = new DoctorData();
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            userData.setDoctorId(preferences.getString(SessionManager.KEY_DOCTOR_ID, ""));
            userData.setDoctorName(preferences.getString(SessionManager.KEY_DOCTOR_NAME, ""));
            userData.setDrEmailid(preferences.getString(SessionManager.KEY_EMAIL_ID, ""));
            userData.setDrProfession(preferences.getString(SessionManager.KEY_DOCTOR_PROFESSION, ""));
            userData.setDrgender(preferences.getString(SessionManager.KEY_DOCTOR_GENDER, ""));
            userData.setDrBirthdate(preferences.getString(SessionManager.KEY_BIRTH_DATE, ""));
            userData.setDrworkat(preferences.getString(SessionManager.KEY_DOCTOR_WORKAT, ""));
            userData.setDrAddress(preferences.getString(SessionManager.KEY_ADDRESS, ""));
            userData.setDrMobileno(preferences.getString(SessionManager.KEY_MOBILE_NO, ""));
            userData.setDrcharge(preferences.getString(SessionManager.KEY_DOCTOR_CHARGES, ""));
            userData.setDrYearofExp(preferences.getString(SessionManager.KEY_DOCTOR_YEAR_OF_EXP, ""));
            userData.setDrMortime(preferences.getString(SessionManager.KEY_DOCTOR_MNG_TM, ""));
            userData.setDrNoontime(preferences.getString(SessionManager.KEY_DOCTOR_NOON_TM, ""));
            userData.setDrImgUrl(preferences.getString(SessionManager.KEY_DOCTOR_PROFILE_PIC, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userData;
    }

    public static void saveAppointmentData(Context context, BookAppointmentModel appointModel) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(SessionManager.KEY_APPOINTMENT_ID, appointModel.getStrAppointId());
            editor.putString(SessionManager.KEY_USER_ID, appointModel.getStrUserId());
            editor.putString(SessionManager.KEY_APPOINT_DOCTOR_ID, appointModel.getStrDoctorId());
            editor.putString(SessionManager.KEY_DATE, appointModel.getStrDrAppointDate());
            editor.putString(SessionManager.KEY_TIME, appointModel.getStrDrAppointTime());

            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BookAppointmentModel getAppointmentData(Context context) {
        BookAppointmentModel userData = new BookAppointmentModel();
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            userData.setStrAppointId(preferences.getString(SessionManager.KEY_APPOINTMENT_ID, ""));
            userData.setStrUserId(preferences.getString(SessionManager.KEY_USER_ID, ""));
            userData.setStrDoctorId(preferences.getString(SessionManager.KEY_APPOINT_DOCTOR_ID, ""));
            userData.setStrDoctorName(preferences.getString(SessionManager.KEY_DOCTOR_NAME, ""));
            userData.setStrDrAppointDate(preferences.getString(SessionManager.KEY_DATE, ""));
            userData.setStrDrAppointTime(preferences.getString(SessionManager.KEY_TIME, ""));

        } catch (Exception e) {
        e.printStackTrace();
    }
    return userData;
    }

    public static void saveEmergencyCallData(Context context, EmergencyCalllistModel appointModel) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(SessionManager.KEY_USER_ID, appointModel.getEmgCallUserId());
            editor.putString(SessionManager.KEY_MOBILE_NO, appointModel.getEmgCallNo());
            editor.putString(SessionManager.KEY_NAME, appointModel.getEmgCallName());
            editor.putString(SessionManager.KEY_CONTACT_ID, appointModel.getEmgCallContactId());

            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EmergencyCalllistModel getEmergencyCallData(Context context) {
        EmergencyCalllistModel userData = new EmergencyCalllistModel();
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            userData.setEmgCallContactId(preferences.getString(SessionManager.KEY_CONTACT_ID, ""));
            userData.setEmgCallUserId(preferences.getString(SessionManager.KEY_USER_ID, ""));
            userData.setEmgCallName(preferences.getString(SessionManager.KEY_NAME, ""));
            userData.setEmgCallNo(preferences.getString(SessionManager.KEY_MOBILE_NO, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userData;
    }

    public static void saveMedicineAlertData(Context context, MedicineAlertlistModel appointModel) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(SessionManager.KEY_ALERT_ITEM_ID, appointModel.getMedicineAlrtId());
            editor.putString(SessionManager.KEY_ALERT_ITEM, appointModel.getMedicineAlrtItem());
            editor.putString(SessionManager.KEY_NAME, appointModel.getMedicineAlrtName());
            editor.putString(SessionManager.KEY_TIME, appointModel.getMedicineAlrtTime());

            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MedicineAlertlistModel getMedicineAlertData(Context context) {
        MedicineAlertlistModel userData = new MedicineAlertlistModel();
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            userData.setMedicineAlrtId(preferences.getString(SessionManager.KEY_ALERT_ITEM_ID, ""));
            userData.setMedicineAlrtItem(preferences.getString(SessionManager.KEY_ALERT_ITEM, ""));
            userData.setMedicineAlrtName(preferences.getString(SessionManager.KEY_NAME, ""));
            userData.setMedicineAlrtTime(preferences.getString(SessionManager.KEY_TIME, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userData;
    }

    public static void saveUserAppointmentList(Context context, UserAppointmentlistModel appointModel) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(SessionManager.KEY_APPOINT_ID, appointModel.getAppointId());
            editor.putString(SessionManager.KEY_USER_ID, appointModel.getUserId());
            editor.putString(SessionManager.KEY_NAME, appointModel.getAppointName());
            editor.putString(SessionManager.KEY_PROFILE_PIC, appointModel.getAppointProfPic());
            editor.putString(SessionManager.KEY_DOCTOR_ID, appointModel.getDoctorId());
            editor.putString(SessionManager.KEY_DOCTOR_NAME, appointModel.getAppointDrName());
            editor.putString(SessionManager.KEY_DATE, appointModel.getAppointDate());
            editor.putString(SessionManager.KEY_TIME, appointModel.getAppointTime());

            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserAppointmentlistModel getUserAppointmentListData(Context context) {
        UserAppointmentlistModel userData = new UserAppointmentlistModel();
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            userData.setAppointId(preferences.getString(SessionManager.KEY_APPOINT_ID, ""));
            userData.setUserId(preferences.getString(SessionManager.KEY_USER_ID, ""));
            userData.setAppointName(preferences.getString(SessionManager.KEY_NAME, ""));
            userData.setAppointProfPic(preferences.getString(SessionManager.KEY_PROFILE_PIC, ""));
            userData.setDoctorId(preferences.getString(SessionManager.KEY_DOCTOR_ID, ""));
            userData.setAppointDrName(preferences.getString(SessionManager.KEY_DOCTOR_NAME, ""));
            userData.setAppointDate(preferences.getString(SessionManager.KEY_DATE, ""));
            userData.setAppointTime(preferences.getString(SessionManager.KEY_TIME, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userData;
    }

    public static void saveAddContact(Context context, EmergencyCalllistModel appointModel) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(SessionManager.KEY_ADD_CONTACT_ID, appointModel.getEmgCallContactId());
            editor.putString(SessionManager.KEY_NAME, appointModel.getEmgCallName());
            editor.putString(SessionManager.KEY_MOBILE_NO, appointModel.getEmgCallNo());

            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static EmergencyCalllistModel getAddContactData(Context context) {
        EmergencyCalllistModel userData = new EmergencyCalllistModel();
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            userData.setEmgCallContactId(preferences.getString(SessionManager.KEY_ADD_CONTACT_ID, ""));
            userData.setEmgCallName(preferences.getString(SessionManager.KEY_NAME, ""));
            userData.setEmgCallNo(preferences.getString(SessionManager.KEY_MOBILE_NO, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userData;
    }

//    ..............................
    //Lat and long

    public static String getlattitude(Context context) {
        String point = "";
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            point = preferences.getString(AppConstants.LATTITUDE, "0.0");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return point;
    }

    public static String getlongitude(Context context) {
        String point = "";
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            point = preferences.getString(AppConstants.LONGITUDE, "0.0");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return point;
    }

    public static void saveLocation(double lat, double lng, Context context) {
        try {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(AppConstants.LATTITUDE, "" + lat);
            editor.putString(AppConstants.LONGITUDE, "" + lng);
            editor.commit();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void updateLatLong(Activity activity) {
        try {
            progressDialog = new ProgressDialog(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            progressDialog.setMessage("Please Wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Applog.v("updateLatLong called", "*******************");
            long longCurrentTimeInMilli = Calendar.getInstance()
                    .getTimeInMillis();
            Intent intent = new Intent(activity,
                    MyLocationBroadcastReceiver.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			Bundle b = new Bundle();
//			b.putInt("CALLFROM", callFrom);
//			intent.putExtras(b);
            pendingIntent = PendingIntent.getBroadcast(activity, 0,
                    intent, 0);

            alarmManager = (AlarmManager) activity.getSystemService(
                    Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    longCurrentTimeInMilli, 30000, pendingIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setLocationService(final Activity activity) {
        // TODO Auto-generated method stub

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(activity).addApi(
                    LocationServices.API).build();
            googleApiClient.connect();
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                // final LocationSettingsStates state =
                // result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        updateLatLong(activity);
                        Applog.v("result", "Success");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed
                        // by showing the user
                        // a dialog.
                        Applog.v("result ",
                                "RESOLUTION_REQUIRED");
                        try {
                            // Show the dialog by calling
                            // startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Applog.v("result ",
                                "SETTINGS_CHANGE_UNAVAILABLE");
                        // Location settings are not satisfied. However, we have no
                        // way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    public static void stopLocationService(Context context) {
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            context.stopService(new Intent(context, MyLocationBroadcastReceiver.class));
            Applog.v("service stopped", "*************");
        }
    }

}
