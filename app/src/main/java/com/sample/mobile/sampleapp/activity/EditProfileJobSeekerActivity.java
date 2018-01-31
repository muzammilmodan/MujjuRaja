package com.sample.mobile.sampleapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jobseekerapp.Adapters.CustomSpinnerVehicleAdapter;
import com.jobseekerapp.Adapters.EditJobseekerPrevEmploymentAdapter;
import com.jobseekerapp.Adapters.SkillListingAdapter;
import com.jobseekerapp.AppUtils.AlertDialogUtility;
import com.jobseekerapp.AppUtils.Applog;
import com.jobseekerapp.AppUtils.ConnectivityDetector;
import com.jobseekerapp.AppUtils.FileHandeling;
import com.jobseekerapp.AppUtils.Fonts;
import com.jobseekerapp.AppUtils.GlobalData;
import com.jobseekerapp.AppUtils.HorizontalListView;
import com.jobseekerapp.AppUtils.HttpHandler;
import com.jobseekerapp.AppUtils.ImageUtil;
import com.jobseekerapp.AppUtils.KeyboardUtility;
import com.jobseekerapp.AppUtils.MyClass;
import com.jobseekerapp.AppUtils.SessionManager;
import com.jobseekerapp.AppUtils.UploadResumeFilePath;
import com.jobseekerapp.Dao.CityDao;
import com.jobseekerapp.Dao.CountryDao;
import com.jobseekerapp.Dao.GetEducationDetail;
import com.jobseekerapp.Dao.GetEmploymentDao;
import com.jobseekerapp.Dao.SkillDao;
import com.jobseekerapp.Dao.StateDao;
import com.jobseekerapp.Global.GlobalMethod;
import com.jobseekerapp.Model.JobseekerDataMdl;
import com.jobseekerapp.Model.JobseekerEducationDtlModel;
import com.jobseekerapp.Model.SpinnerModel;
import com.jobseekerapp.R;
import com.jobseekerapp.WebService.GetJsonWithAndroidNetworkingLib;
import com.jobseekerapp.WebService.OnUpdateListener;
import com.jobseekerapp.WebService.WebField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static android.util.Base64.encodeToString;
import static com.jobseekerapp.activities.WelcomeScreenActivity.JobseekerUserType;

public class EditProfileJobSeekerActivity extends Activity implements View.OnClickListener {

    private ImageView imgVwPersnlDtl, imgVwEmplyMntDtl, imgVwEducatDtl;
    private RelativeLayout rlPersnlDtl, rlEmplyMntDtl, rlEducatDtl, rlDateOfBrth;
    private TextView txtVwPersnalTitle, txtVwUploadVdoImgTitle, txtVwUpldResumeTitle, txtVwEmplDtlTitle, txtVwEducDtlTitle, txtVwCreatProf;

    private LinearLayout llPersonalDtl;
    private RelativeLayout rlProfilePicVdo, rlFileUploadMain, rlMain,rlScrollMain, rlPersonalDetail, rlEducationDtl;
    private ImageView ivBack;

    //Personal Details
    private EditText edtVwFirstNm, edtVwLastNm, edtVwZipcode, edtVwContactNo;
    private Button btnNextPersnlDtl;
    private String strFirstNm, strLastNm, strContactNo, strDateOfBrth, strZipCode;
    private ImageView imgVwDateOfBrth;
    private TextView txtVwSkip, txtVwDteOfBrth, txtVwPersnlState, txtSelectCity;
    private Spinner spnrPersnlDetailState, spnrPersnlDtlCountry;
    int persnlDtlCountryId, persnlDtlStateId, persnlDtlCityId;
    private String strPersnlDtlStateName, strPersnlDtlCityName;

    //Set up Profile
    private int mYear, mMonth, mDay;
    private int strSkipValues = 0;

    //Spinner values set
    private SpinnerModel spModel;
    private CustomSpinnerVehicleAdapter spCustomAdapter;
    private ArrayList<SpinnerModel> alLakhs;
    private ArrayList<SpinnerModel> alThousand;
    private ArrayList<SpinnerModel> alNoticePeriod;
    private ArrayList<SpinnerModel> alGender;
    private ArrayList<SpinnerModel> alEthaniCity;

    private String strGender, strEthaniCity, strFrom, strTo, strNoticePeriod;
    private int emplyerLac = 1, emplyerThousand = 1000;
    private Spinner spnrPersnlDtlCity, spnrLakhs, spnrThousand, spnrGender, spnrEthnicCity;

    //Profile pic/Video
    private ImageView imgVwUploadPic, imgProfVdo;
    private Button btnNextProfilePic;
    private TextView txtVwChoosExisting, txtVwTakePic, txtVwCancel;
    private Uri mImageUri, mVideoUri;
    private Bitmap bitmap;
    private String strProPicBase64 = "";

    //Video Uploading
    private ArrayList<String> fileNameArr;
    private final FileHandeling fileHandel = new FileHandeling(this);
    private final MyClass myClassData = new MyClass();

    //    private Button btnOpen, btnRecord;
    private String strUploadingVideo;
    private String base64Video, strBase64Thumb, strStatus;
    private int offSet;
    private int spliteCount;
    private int totalSplite;

    int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 101;
    private static final int MY_PERMISSIONS_UPLOAD_RESUME_REQUEST = 99;
    private static final int REQUEST_GALARY_IMG = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int PIC_CROP = 3;
    private static final int REQUEST_VIDEO_CAMERA = 4;
    private static final int REQUEST_GALARY_VIDEO = 5;
    private static final int REQUEST_VIDEO_CAPTURE = 6;
    private static final int PICK_FILE_REQUEST = 7;
    private String filePath = "";

    //File upload
    private ImageView imgVwUploadFile;
    private TextView txtVwUploadFile;
    private Button btnNextFileUpload;

    //    private String selectedResumeFilePath;
    private String strResumeBase64 = "";
    private static final String TAG = EditProfileJobSeekerActivity.class.getSimpleName();
    PowerManager.WakeLock wakeLock;
    private String strselectedResumeFilePath;
    ProgressDialog dialog;

    //Emplyment detail
    private LinearLayout llEmployAllDtl;
    private RelativeLayout rlEmploymenDtl;
    private Button btnNextEmployDetail;
    private EditText edtVwCurrentCmpny, edtVwCurrentDesign;
    private ImageView imgVwTitleExp;
    String strCurrentDesign, strCurrentCompny;
    private RelativeLayout rlWrkngSncFm, rlVwWrkgSncTo;
    private TextView txtVwWrkgSncFrom, txtVwWrkgSncTo;
    private ImageView imgVwWrkgSncFrom, imgVwWrkgSncTo;
    private RecyclerView recyclrPrevEmplymnt;
    private LinearLayoutManager prevEmplymentLinearLayoutManager;
    private EditJobseekerPrevEmploymentAdapter prevEmplymntAdapter;
    int addMorePosition = 0;

    //Education details
    private TextView txtVwGraduDte, txtVwHighScGraduDte;
    private Button btnNextEducationDtl;
    private EditText edtVwHighSclGraduation, edtVwClgGraduat;
    private RelativeLayout rlHighScGraduDate, rlClgGraduatDate;
    private ImageView imgVwHighScGraduDate, imgVwClgGraduatDate;
    public ArrayList<JobseekerEducationDtlModel> alEducationDetail;
    ArrayList<GetEducationDetail.EducationDetailBean> alEducationListItem;
    //    String strEducationName;
//    String strEducationDate;
    String strEducationType;

    String strEducationGradName;
    String strEducationHighName;
    String strEducationHighDate;
    String strEducationGradDate;

    //Skill set Horizontal listview
    HorizontalListView hlvSkill;
    private ArrayList<SkillDao.DataBean.SkillsListBean> skillList;
    private ArrayList<SkillDao.DataBean.SkillsListBean> addSkillList = new ArrayList<>();

    private AutoCompleteTextView autoCmpltTxtVwSkill;
    private SkillListingAdapter skillListingAdapter;
    String strSkill;
    SkillDao.DataBean.SkillsListBean AddskillName;
    int skillId;
    ArrayAdapter<SkillDao.DataBean.SkillsListBean> skillAdapter;

//    ...............................End Skill..................................

    private RelativeLayout rlAddMoreExp;
    String strPrevEmplyrCmpnyDesig;
    String strPrevEmplyrCmpnyNm;
    String strPrevEmplyrCmpnyFrom;
    String strPrevEmplyrCmpnyTo;

    String strUserType;
    Intent in;

    private static final boolean IS_CHUNKED = true;

    //    Get contry list,state list and city list
    private TextView txtVwEmplyCity;
    private ProgressDialog pDialog;
    ArrayList<CountryDao.DataBean.CountryListBean> countryList;
    ArrayList<StateDao.DataBean.StateListBean> stateList;
    ArrayList<CityDao.DataBean.CityListBean> cityList;
    private Spinner spnrEmplymentCountry, spnrEmplymentState, spnrEmplymentCity;
    String strCityName;
    int emplyerCountryId, emplyerStateId, emplyerCityId;

    String editProfileId;
    ArrayList<GetEmploymentDao.CurrentEmploymentDetailsBean> currentEmploymentListItem;
    ArrayList<GetEmploymentDao.PreviousEmploymentDetailsBean> prevEmploymentListItem;


    private RelativeLayout rlEmplySlrLacs, rlEmplySlrThous, rlEmplyNoticPer;
    private TextView txtVwEmplSlrLacs, txtVwEmplSlrThous, txtVwEmplNoticePeriod;
    private Spinner spnrNoticePeriod, spnrSrlThousand, spnrSrlLakhs;

    String getPersonalEmploymentId;
    String getEmployerEmploymentId;
    String getEducationEmploymentId;
    public static String PrevEmplyrEmploymentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_job_seeker);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.app_bg));
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        editProfileId = getIntent().getStringExtra("EditProfile").toString();
        Applog.e(TAG, "editProfileId" + editProfileId);

        strUserType = JobseekerUserType;

        prevEmploymentListItem = new ArrayList();
        currentEmploymentListItem = new ArrayList();
        alEducationListItem = new ArrayList();

        getIds();
        setListners();
        setFonts();

        showEditProfileData(editProfileId);

        getCallPersonalDtls();
        getCallEmploymentDtls();
        getCallEducationDtls();

        SetSpinnerEthnCity();
//        SetPrevEmployment();
        SetSpinnerGender();

    }

    private void getCallPersonalDtls() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCallEmploymentDtls() {

        rlEmplySlrLacs.setVisibility(View.VISIBLE);
        rlEmplySlrThous.setVisibility(View.VISIBLE);
        rlEmplyNoticPer.setVisibility(View.VISIBLE);

        spnrNoticePeriod.setVisibility(View.GONE);
        spnrSrlThousand.setVisibility(View.GONE);
        spnrSrlLakhs.setVisibility(View.GONE);

        try {
            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put(WebField.JOB_SEEKER_ID, "2");
//                        jsonObject.put(WebField.JOB_SEEKER_ID, SessionManager.getJobseekerData(this).getJobUserId());
                Applog.e("Get Employment Dtl", " Request" + jsonObject);
                new GetJsonWithAndroidNetworkingLib(this, jsonObject, WebField.SERV_GET_EMPLOYMENT_DETAIL,
                        GlobalData.intFlagShow,
                        new OnUpdateListener() {
                            @Override
                            public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                                Applog.e(TAG, "isSuccess" + isSuccess);
                                if (isSuccess) {

                                    try {
                                        if (jsonObject != null) {
                                            Applog.e(TAG, " jsonObject " + jsonObject);

                                            GetEmploymentDao getEmplyListDao = new Gson().fromJson(jsonObject.toString(), GetEmploymentDao.class);
                                            prevEmploymentListItem.addAll(getEmplyListDao.getPreviousEmploymentDetails());
                                            currentEmploymentListItem.add(getEmplyListDao.getCurrentEmploymentDetails());

                                            GetEmploymentDao.CurrentEmploymentDetailsBean currentEmpData = new GetEmploymentDao.CurrentEmploymentDetailsBean();
                                            GetEmploymentDao.PreviousEmploymentDetailsBean prevEmpData = new GetEmploymentDao.PreviousEmploymentDetailsBean();

                                            edtVwCurrentDesign.setText(getEmplyListDao.getCurrentEmploymentDetails().getDesignation());
                                            edtVwCurrentCmpny.setText(getEmplyListDao.getCurrentEmploymentDetails().getCompanyName());
                                            txtVwWrkgSncFrom.setText(getEmplyListDao.getCurrentEmploymentDetails().getWorkStartFrom());
                                            txtVwWrkgSncTo.setText(getEmplyListDao.getCurrentEmploymentDetails().getWorkEnd());
                                            txtVwEmplSlrThous.setText(getEmplyListDao.getCurrentEmploymentDetails().getAnnualSalaryThousands());
                                            txtVwEmplSlrLacs.setText(getEmplyListDao.getCurrentEmploymentDetails().getAnnualSalaryLacs());


                                            getEmplyListDao.getCurrentEmploymentDetails().getStateId();
                                            getEmplyListDao.getCurrentEmploymentDetails().getCityId();
                                            getEmplyListDao.getCurrentEmploymentDetails().getSkills();
                                            getEmployerEmploymentId = getEmplyListDao.getCurrentEmploymentDetails().getEmploymentId();
                                            Applog.e(" getEmployerEmploymentId ", " : Get : " + getEmployerEmploymentId);
                                            getEmplyListDao.getCurrentEmploymentDetails().getJobSeekerId();

                                            String getNoticePeriod = getEmplyListDao.getCurrentEmploymentDetails().getNoticePeriod();
                                            Applog.e("Notice per ::: ", "" + getNoticePeriod);

                                            if (getNoticePeriod.matches("15")) {
                                                strNoticePeriod = "15 Day";
                                                txtVwEmplNoticePeriod.setText(strNoticePeriod);
                                            } else if (getNoticePeriod.matches("30")) {
                                                strNoticePeriod = "1 Month";
                                                txtVwEmplNoticePeriod.setText(strNoticePeriod);
                                            } else if (getNoticePeriod.matches("60")) {
                                                strNoticePeriod = "2 Month";
                                                txtVwEmplNoticePeriod.setText(strNoticePeriod);
                                            } else if (getNoticePeriod.matches("90")) {
                                                strNoticePeriod = "3 Month";
                                                txtVwEmplNoticePeriod.setText(strNoticePeriod);
                                            } else {
                                                strNoticePeriod = ">3 Month";
                                                txtVwEmplNoticePeriod.setText(strNoticePeriod);
                                            }

                                            recyclrPrevEmplymnt.setHasFixedSize(true);
                                            recyclrPrevEmplymnt.setLayoutManager(new LinearLayoutManager(EditProfileJobSeekerActivity.this));

                                            if (prevEmploymentListItem.size() > 0) {
                                                addMorePosition = prevEmploymentListItem.size();
                                                prevEmplymntAdapter = new EditJobseekerPrevEmploymentAdapter(EditProfileJobSeekerActivity.this,
                                                        prevEmploymentListItem);
                                                recyclrPrevEmplymnt.setAdapter(prevEmplymntAdapter);
                                            }


                                            SessionManager.saveEditJobseekerData(EditProfileJobSeekerActivity.this,
                                                    currentEmpData, prevEmpData);


                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "Not Update profile");
                                }
                            }
                        }).execute();
            } else {
                AlertDialogUtility.SHOW_INTERNET_ALERT(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getCallEducationDtls() {
        try {
            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put(WebField.JOB_SEEKER_ID, "2");
//                        jsonObject.put(WebField.JOB_SEEKER_ID, SessionManager.getJobseekerData(this).getJobUserId());
                Applog.e("Get Employment Dtl", " Request" + jsonObject);
                new GetJsonWithAndroidNetworkingLib(this, jsonObject, WebField.SERV_GET_EDUCATION_DETAIL,
                        GlobalData.intFlagShow,
                        new OnUpdateListener() {
                            @Override
                            public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                                Applog.e(TAG, "isSuccess" + isSuccess);
                                if (isSuccess) {
                                    try {
                                        if (jsonObject != null) {
                                            Applog.e(TAG, " jsonObject " + jsonObject);

                                            GetEducationDetail getEducationListDao = new Gson().fromJson(jsonObject.toString(), GetEducationDetail.class);
                                            alEducationListItem.addAll(getEducationListDao.getEducationDetail());

                                            getEducationListDao.getJobSeekerId();
                                            getEducationListDao.getEducationDetail();

                                            GetEducationDetail.EducationDetailBean educationDtl = new GetEducationDetail.EducationDetailBean();
                                            educationDtl.setJobSeekerId(alEducationListItem.get(0).getJobSeekerId());
                                            educationDtl.setJobSeekerId(alEducationListItem.get(1).getJobSeekerId());

                                            educationDtl.setEducationId(alEducationListItem.get(0).getEducationId());
                                            educationDtl.setEducationId(alEducationListItem.get(1).getEducationId());

                                            educationDtl.setGraduationType(alEducationListItem.get(0).getGraduationType());
                                            educationDtl.setGraduationType(alEducationListItem.get(1).getGraduationType());

                                            educationDtl.setName(alEducationListItem.get(0).getName());
                                            educationDtl.setName(alEducationListItem.get(1).getName());

                                            educationDtl.setGraduationDate(alEducationListItem.get(0).getGraduationDate());
                                            educationDtl.setGraduationDate(alEducationListItem.get(1).getGraduationDate());

                                            String strHighEducatDate = getEducationListDao.getEducationDetail().get(0).getGraduationDate();
                                            String strGraduEducatDate = getEducationListDao.getEducationDetail().get(1).getGraduationDate();
                                            String strHighEducatName = getEducationListDao.getEducationDetail().get(0).getName();
                                            String strGraduEducatName = getEducationListDao.getEducationDetail().get(1).getName();

                                            edtVwHighSclGraduation.setText(strHighEducatName);
                                            txtVwHighScGraduDte.setText(strHighEducatDate);

                                            edtVwClgGraduat.setText(strGraduEducatName);
                                            txtVwGraduDte.setText(strGraduEducatDate);

                                            SessionManager.saveJobseekerEditEducationData(EditProfileJobSeekerActivity.this,
                                                    educationDtl);

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "Not Update profile");
                                }
                            }
                        }).execute();
            } else {
                AlertDialogUtility.SHOW_INTERNET_ALERT(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEditProfileData(String editProfileId) {
        Applog.e("Check Ids", " " + editProfileId);
        if (editProfileId.matches("0")) {
            Applog.e("check ", "0");
        } else if (editProfileId.matches("1")) {
            setProfPicVideo();
        } else if (editProfileId.matches("2")) {
            setEmploymentDtls();
        } else if (editProfileId.matches("3")) {
            setProfPicVideo();
        } else if (editProfileId.matches("4")) {
            setPersonalDtls();
        } else if (editProfileId.matches("5")) {
            setEmploymentDtls();
        } else if (editProfileId.matches("6")) {
            setEmploymentDtls();
        } else if (editProfileId.matches("7")) {
            setEducationDtls();
        }
    }

    private void setPersonalDtls() {
        KeyboardUtility.HideKeyboard(this, imgVwPersnlDtl);

        imgVwPersnlDtl.setImageResource(R.mipmap.active_son_icon);
        imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
        imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

        btnNextPersnlDtl.setVisibility(View.VISIBLE);
        btnNextProfilePic.setVisibility(View.GONE);
        btnNextFileUpload.setVisibility(View.GONE);
        btnNextEducationDtl.setVisibility(View.GONE);
        btnNextEmployDetail.setVisibility(View.GONE);

        txtVwPersnalTitle.setVisibility(View.VISIBLE);
        txtVwEmplDtlTitle.setVisibility(View.GONE);
        txtVwEducDtlTitle.setVisibility(View.GONE);
        txtVwUploadVdoImgTitle.setVisibility(View.GONE);
        txtVwUpldResumeTitle.setVisibility(View.GONE);

        rlPersonalDetail.setVisibility(View.VISIBLE);
        llPersonalDtl.setVisibility(View.VISIBLE);
        rlProfilePicVdo.setVisibility(View.GONE);
        rlFileUploadMain.setVisibility(View.GONE);
        rlEmploymenDtl.setVisibility(View.GONE);
        rlEducationDtl.setVisibility(View.GONE);
        txtVwSkip.setVisibility(View.VISIBLE);
        strSkipValues = 0;
    }

    private void setEmploymentDtls() {
        addSkill();
//                getSkillList();
        KeyboardUtility.HideKeyboard(this, imgVwEmplyMntDtl);
        imgVwPersnlDtl.setImageResource(R.mipmap.son_icon);
        imgVwEmplyMntDtl.setImageResource(R.mipmap.active_professional_icon);
        imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

        btnNextPersnlDtl.setVisibility(View.GONE);
        btnNextEducationDtl.setVisibility(View.GONE);
        btnNextEmployDetail.setVisibility(View.VISIBLE);

        txtVwUploadVdoImgTitle.setVisibility(View.GONE);
        txtVwUpldResumeTitle.setVisibility(View.GONE);
        txtVwEducDtlTitle.setVisibility(View.GONE);
        txtVwPersnalTitle.setVisibility(View.GONE);
        txtVwEmplDtlTitle.setVisibility(View.VISIBLE);

        llEmployAllDtl.setVisibility(View.VISIBLE);
        rlEmploymenDtl.setVisibility(View.VISIBLE);
        rlProfilePicVdo.setVisibility(View.GONE);
        rlFileUploadMain.setVisibility(View.GONE);
        rlPersonalDetail.setVisibility(View.GONE);
        rlEducationDtl.setVisibility(View.GONE);

        txtVwSkip.setVisibility(View.VISIBLE);

        strSkipValues = 4;
    }

    private void setEducationDtls() {
        KeyboardUtility.HideKeyboard(this, imgVwEducatDtl);
        imgVwPersnlDtl.setImageResource(R.mipmap.son_icon);
        imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
        imgVwEducatDtl.setImageResource(R.mipmap.active_book_icon);

        btnNextPersnlDtl.setVisibility(View.GONE);
        btnNextEducationDtl.setVisibility(View.VISIBLE);
        btnNextEmployDetail.setVisibility(View.GONE);

        txtVwUploadVdoImgTitle.setVisibility(View.GONE);
        txtVwUpldResumeTitle.setVisibility(View.GONE);
        txtVwPersnalTitle.setVisibility(View.GONE);
        txtVwEmplDtlTitle.setVisibility(View.GONE);
        txtVwEducDtlTitle.setVisibility(View.VISIBLE);

        rlProfilePicVdo.setVisibility(View.GONE);
        rlPersonalDetail.setVisibility(View.GONE);
        rlEmploymenDtl.setVisibility(View.GONE);
        rlEducationDtl.setVisibility(View.VISIBLE);
        txtVwSkip.setVisibility(View.VISIBLE);
        strSkipValues = 5;

    }

    private void setProfPicVideo() {
        rlProfilePicVdo.setVisibility(View.VISIBLE);
        rlFileUploadMain.setVisibility(View.GONE);

        btnNextPersnlDtl.setVisibility(View.GONE);
        btnNextProfilePic.setVisibility(View.VISIBLE);

        txtVwUpldResumeTitle.setVisibility(View.GONE);
        txtVwPersnalTitle.setVisibility(View.GONE);
        txtVwUploadVdoImgTitle.setVisibility(View.VISIBLE);

        llPersonalDtl.setVisibility(View.GONE);
        txtVwSkip.setVisibility(View.VISIBLE);

        imgVwPersnlDtl.setImageResource(R.mipmap.active_son_icon);
        imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
        imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

        strSkipValues = 1;
    }

    //.................................................Simple using data.................................................
    private void setListners() {
        try {
            llPersonalDtl.setOnClickListener(this);

            imgVwDateOfBrth.setOnClickListener(this);
            rlDateOfBrth.setOnClickListener(this);
            imgVwUploadPic.setOnClickListener(this);
            imgProfVdo.setOnClickListener(this);
            imgVwUploadFile.setOnClickListener(this);
            txtVwPersnlState.setOnClickListener(this);
            btnNextProfilePic.setOnClickListener(this);
            btnNextFileUpload.setOnClickListener(this);
            btnNextPersnlDtl.setOnClickListener(this);

            rlPersonalDetail.setOnClickListener(this);
            rlEmploymenDtl.setOnClickListener(this);
            rlAddMoreExp.setOnClickListener(this);

//Employment details
            rlWrkngSncFm.setOnClickListener(this);
            rlVwWrkgSncTo.setOnClickListener(this);
            txtVwEmplyCity.setOnClickListener(this);

            txtVwWrkgSncFrom.setOnClickListener(this);
            txtVwWrkgSncTo.setOnClickListener(this);

            imgVwWrkgSncFrom.setOnClickListener(this);
            imgVwWrkgSncTo.setOnClickListener(this);

//            imgVwAddSkill.setOnClickListener(this);
            btnNextEmployDetail.setOnClickListener(this);

            rlEmplySlrLacs.setOnClickListener(this);
            rlEmplySlrThous.setOnClickListener(this);
            rlEmplyNoticPer.setOnClickListener(this);

//Education details
            imgVwHighScGraduDate.setOnClickListener(this);
            imgVwClgGraduatDate.setOnClickListener(this);

            rlHighScGraduDate.setOnClickListener(this);
            rlClgGraduatDate.setOnClickListener(this);

            btnNextEducationDtl.setOnClickListener(this);

            ivBack.setOnClickListener(this);
            txtVwSkip.setOnClickListener(this);
            rlMain.setOnClickListener(this);
            rlScrollMain.setOnClickListener(this);

            imgVwPersnlDtl.setOnClickListener(this);
            imgVwEmplyMntDtl.setOnClickListener(this);
            imgVwEducatDtl.setOnClickListener(this);

            rlPersnlDtl.setOnClickListener(this);
            rlEmplyMntDtl.setOnClickListener(this);
            rlEducatDtl.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getIds() {
        try {
//Personal profile
            edtVwFirstNm = findViewById(R.id.edtVwFirstNm);
            edtVwLastNm = findViewById(R.id.edtVwLastNm);
            edtVwContactNo = findViewById(R.id.edtVwContactNo);
            txtVwCreatProf = findViewById(R.id.txtVwCreatProf);
            txtVwPersnlState = findViewById(R.id.txtVwPersnlState);
            btnNextPersnlDtl = findViewById(R.id.btnNextPersnlDtl);
            txtSelectCity = (TextView) findViewById(R.id.txtSelectCity);
            txtSelectCity.setVisibility(View.VISIBLE);

            spnrGender = findViewById(R.id.spnrGender);
            spnrEthnicCity = findViewById(R.id.spnrEthnicCity);
//SetUp profile
            imgVwDateOfBrth = findViewById(R.id.imgVwDateOfBrth);
            rlDateOfBrth = findViewById(R.id.rlDateOfBrth);
            txtVwDteOfBrth = findViewById(R.id.txtVwDteOfBrth);
            edtVwZipcode = findViewById(R.id.edtVwZipcode);
            spnrPersnlDtlCity = findViewById(R.id.spnrPersnlDtlCity);
            llPersonalDtl = findViewById(R.id.llPersonalDtl);

//Profile pic
            imgVwUploadPic = findViewById(R.id.imgVwUploadPic);
            imgProfVdo = findViewById(R.id.imgProfVdo);
            btnNextProfilePic = findViewById(R.id.btnNextProfilePic);
            rlProfilePicVdo = findViewById(R.id.rlProfilePicVdo);
            rlFileUploadMain = findViewById(R.id.rlFileUploadMain);

//File Uploading
            imgVwUploadFile = findViewById(R.id.imgVwUploadFile);
            btnNextFileUpload = findViewById(R.id.btnNextFileUpload);
            txtVwUploadFile = findViewById(R.id.txtVwUploadFile);

//All using values
            txtVwSkip = findViewById(R.id.txtVwSkip);
            ivBack = findViewById(R.id.ivBack);
            rlMain = findViewById(R.id.rlMain);
            rlScrollMain  = findViewById(R.id.rlScrollMain);
            rlPersonalDetail = findViewById(R.id.rlPersonalDetail);

            txtVwPersnalTitle = findViewById(R.id.txtVwPersnalTitle);
            txtVwUploadVdoImgTitle = findViewById(R.id.txtVwUploadVdoImgTitle);
            txtVwUpldResumeTitle = findViewById(R.id.txtVwUpldResumeTitle);
            txtVwEmplDtlTitle = findViewById(R.id.txtVwEmplDtlTitle);
            txtVwEducDtlTitle = findViewById(R.id.txtVwEducDtlTitle);

            imgVwPersnlDtl = findViewById(R.id.imgVwPersnlDtl);
            imgVwEmplyMntDtl = findViewById(R.id.imgVwEmplyMntDtl);
            imgVwEducatDtl = findViewById(R.id.imgVwEducatDtl);

            rlPersnlDtl = findViewById(R.id.rlPersnlDtl);
            rlEmplyMntDtl = findViewById(R.id.rlEmplyMntDtl);
            rlEducatDtl = findViewById(R.id.rlEducatDtl);
            rlEducationDtl = findViewById(R.id.rlEducationDtl);

//Emplyment details
            rlEmplySlrThous = findViewById(R.id.rlEmplySlrThous);
            rlEmplySlrLacs = findViewById(R.id.rlEmplySlrLacs);
            rlEmplyNoticPer = findViewById(R.id.rlEmplyNoticPer);
            txtVwEmplSlrLacs = findViewById(R.id.txtVwEmplSlrLacs);
            txtVwEmplSlrThous = findViewById(R.id.txtVwEmplSlrThous);
            txtVwEmplNoticePeriod = (TextView) findViewById(R.id.txtVwEmplNoticePeriod);
            spnrNoticePeriod = findViewById(R.id.spnrNoticePeriod);
            spnrSrlThousand = findViewById(R.id.spnrSrlThousand);
            spnrSrlLakhs = findViewById(R.id.spnrSrlLakhs);

            hlvSkill = findViewById(R.id.hlvSkill);
            rlWrkngSncFm = findViewById(R.id.rlWrkngSncFm);
            rlVwWrkgSncTo = findViewById(R.id.rlVwWrkgSncTo);
            txtVwWrkgSncFrom = findViewById(R.id.txtVwWrkgSncFrom);
            txtVwWrkgSncTo = findViewById(R.id.txtVwWrkgSncTo);
            imgVwWrkgSncFrom = findViewById(R.id.imgVwWrkgSncFrom);
            imgVwWrkgSncTo = findViewById(R.id.imgVwWrkgSncTo);
            txtVwEmplyCity = findViewById(R.id.txtVwEmplyCity);
            rlAddMoreExp = findViewById(R.id.rlAddMoreExp);
            recyclrPrevEmplymnt = findViewById(R.id.recyclrPrevEmplymnt);
            rlEmploymenDtl = findViewById(R.id.rlEmploymenDtl);

            spnrLakhs = findViewById(R.id.spnrSrlLakhs);
            spnrThousand = findViewById(R.id.spnrSrlThousand);
            spnrNoticePeriod = findViewById(R.id.spnrNoticePeriod);

            btnNextEmployDetail = findViewById(R.id.btnNextEmployDetail);
            edtVwCurrentCmpny = findViewById(R.id.edtVwCurrentCmpny);
            edtVwCurrentDesign = findViewById(R.id.edtVwCurrentDesign);
            imgVwTitleExp = findViewById(R.id.imgVwTitleExp);
            llEmployAllDtl = findViewById(R.id.llEmployAllDtl);
//            imgVwAddSkill = findViewById(R.id.imgVwAddSkill);

//Education Detils
            edtVwHighSclGraduation = findViewById(R.id.edtVwHighSclGraduation);
            edtVwClgGraduat = findViewById(R.id.edtVwClgGraduat);
            imgVwHighScGraduDate = findViewById(R.id.imgVwHighScGraduDate);
            imgVwClgGraduatDate = findViewById(R.id.imgVwClgGraduatDate);
            rlHighScGraduDate = findViewById(R.id.rlHighScGraduDate);
            rlClgGraduatDate = findViewById(R.id.rlClgGraduatDate);
            btnNextEducationDtl = findViewById(R.id.btnNextEducationDtl);
            txtVwGraduDte = findViewById(R.id.txtVwGraduDte);
            txtVwHighScGraduDte = findViewById(R.id.txtVwHighScGraduDte);
//Visibility
            imgVwPersnlDtl.setImageResource(R.mipmap.active_son_icon);
            imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
            imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

//first time open Register data Visible
            txtVwPersnalTitle.setVisibility(View.VISIBLE);
            rlPersonalDetail.setVisibility(View.VISIBLE);
            llPersonalDtl.setVisibility(View.VISIBLE);
            btnNextPersnlDtl.setVisibility(View.VISIBLE);
            txtVwSkip.setVisibility(View.VISIBLE);
            rlProfilePicVdo.setVisibility(View.GONE);
            rlFileUploadMain.setVisibility(View.GONE);
            rlEmploymenDtl.setVisibility(View.GONE);
            rlEducationDtl.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFonts() {
        try {
            txtVwPersnalTitle.setTypeface(Fonts.poppinsSemiBold(this));
            txtVwEducDtlTitle.setTypeface(Fonts.poppinsSemiBold(this));
            txtVwEmplDtlTitle.setTypeface(Fonts.poppinsSemiBold(this));
            txtVwUpldResumeTitle.setTypeface(Fonts.poppinsSemiBold(this));
            txtVwUploadVdoImgTitle.setTypeface(Fonts.poppinsSemiBold(this));
            txtVwCreatProf.setTypeface(Fonts.poppinsSemiBold(this));

            txtVwSkip.setTypeface(Fonts.poppinsRegular(this));

            btnNextEducationDtl.setTypeface(Fonts.poppinsMedium(this));
            btnNextEmployDetail.setTypeface(Fonts.poppinsMedium(this));
            btnNextFileUpload.setTypeface(Fonts.poppinsMedium(this));
            btnNextPersnlDtl.setTypeface(Fonts.poppinsMedium(this));
            btnNextProfilePic.setTypeface(Fonts.poppinsMedium(this));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rlEmplySlrLacs:
                rlEmplySlrLacs.setVisibility(View.GONE);
                spnrSrlLakhs.setVisibility(View.VISIBLE);

                SetSpinnerLakhs();
                break;
            case R.id.rlEmplySlrThous:
                rlEmplySlrThous.setVisibility(View.GONE);
                spnrSrlThousand.setVisibility(View.VISIBLE);
                SetSpinnerThousand();
                break;
            case R.id.rlEmplyNoticPer:
                rlEmplyNoticPer.setVisibility(View.GONE);
                spnrNoticePeriod.setVisibility(View.VISIBLE);
                SetSpinnerNoticePeriod();
                break;

            case R.id.txtVwSkip:

                if (strSkipValues == 0) {
                    rlProfilePicVdo.setVisibility(View.VISIBLE);
                    rlFileUploadMain.setVisibility(View.GONE);

                    btnNextPersnlDtl.setVisibility(View.GONE);
                    btnNextProfilePic.setVisibility(View.VISIBLE);

                    txtVwUpldResumeTitle.setVisibility(View.GONE);
                    txtVwPersnalTitle.setVisibility(View.GONE);
                    txtVwUploadVdoImgTitle.setVisibility(View.VISIBLE);

                    llPersonalDtl.setVisibility(View.GONE);
                    txtVwSkip.setVisibility(View.VISIBLE);

                    imgVwPersnlDtl.setImageResource(R.mipmap.active_son_icon);
                    imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
                    imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

                    strSkipValues = 1;
                } else if (strSkipValues == 1) {
                    //Personal dtl gone, Profile dtl Visible
                    imgVwPersnlDtl.setImageResource(R.mipmap.active_son_icon);
                    imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
                    imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

                    llPersonalDtl.setVisibility(View.GONE);
                    rlProfilePicVdo.setVisibility(View.VISIBLE);

                    txtVwPersnalTitle.setVisibility(View.GONE);
                    txtVwUploadVdoImgTitle.setVisibility(View.VISIBLE);

                    strSkipValues = 2;
                } else if (strSkipValues == 2) {
                    imgVwPersnlDtl.setImageResource(R.mipmap.active_son_icon);
                    imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
                    imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

                    llPersonalDtl.setVisibility(View.GONE);
                    rlProfilePicVdo.setVisibility(View.GONE);
                    rlFileUploadMain.setVisibility(View.VISIBLE);

                    txtVwUploadVdoImgTitle.setVisibility(View.GONE);
                    txtVwUpldResumeTitle.setVisibility(View.VISIBLE);

                    strSkipValues = 3;
                } else if (strSkipValues == 3) {
//                    Employment dtl
                    addSkill();
//                    getSkillList();
                    imgVwPersnlDtl.setImageResource(R.mipmap.son_icon);
                    imgVwEmplyMntDtl.setImageResource(R.mipmap.active_professional_icon);
                    imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

                    txtVwEmplDtlTitle.setVisibility(View.VISIBLE);
                    txtVwPersnalTitle.setVisibility(View.GONE);
                    txtVwUploadVdoImgTitle.setVisibility(View.GONE);
                    txtVwUpldResumeTitle.setVisibility(View.GONE);
                    txtVwEducDtlTitle.setVisibility(View.GONE);

                    btnNextFileUpload.setVisibility(View.GONE);
                    btnNextEmployDetail.setVisibility(View.VISIBLE);

                    llEmployAllDtl.setVisibility(View.VISIBLE);
                    rlEmploymenDtl.setVisibility(View.VISIBLE);
                    txtVwSkip.setVisibility(View.VISIBLE);
                    rlPersonalDetail.setVisibility(View.GONE);
                    rlEducationDtl.setVisibility(View.GONE);

                    strSkipValues = 4;

                } else if (strSkipValues == 4) {
                    imgVwPersnlDtl.setImageResource(R.mipmap.son_icon);
                    imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
                    imgVwEducatDtl.setImageResource(R.mipmap.active_book_icon);

                    txtVwEmplDtlTitle.setVisibility(View.GONE);
                    txtVwPersnalTitle.setVisibility(View.GONE);
                    txtVwUploadVdoImgTitle.setVisibility(View.GONE);
                    txtVwUpldResumeTitle.setVisibility(View.GONE);
                    txtVwEducDtlTitle.setVisibility(View.VISIBLE);

                    btnNextEmployDetail.setVisibility(View.GONE);
                    btnNextEducationDtl.setVisibility(View.VISIBLE);
                    rlEmploymenDtl.setVisibility(View.GONE);

                    rlEducationDtl.setVisibility(View.VISIBLE);
                    txtVwSkip.setVisibility(View.VISIBLE);
                    strSkipValues = 5;

                } else if (strSkipValues == 5) {

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    in = new Intent(this, HomeJobseekerActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                }
                break;

            case R.id.imgVwPersnlDtl:
                KeyboardUtility.HideKeyboard(this, imgVwPersnlDtl);

                imgVwPersnlDtl.setImageResource(R.mipmap.active_son_icon);
                imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
                imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

                btnNextPersnlDtl.setVisibility(View.VISIBLE);
                btnNextProfilePic.setVisibility(View.GONE);
                btnNextFileUpload.setVisibility(View.GONE);
                btnNextEducationDtl.setVisibility(View.GONE);
                btnNextEmployDetail.setVisibility(View.GONE);

                txtVwPersnalTitle.setVisibility(View.VISIBLE);
                txtVwEmplDtlTitle.setVisibility(View.GONE);
                txtVwEducDtlTitle.setVisibility(View.GONE);
                txtVwUploadVdoImgTitle.setVisibility(View.GONE);
                txtVwUpldResumeTitle.setVisibility(View.GONE);

                rlPersonalDetail.setVisibility(View.VISIBLE);
                llPersonalDtl.setVisibility(View.VISIBLE);
                rlProfilePicVdo.setVisibility(View.GONE);
                rlFileUploadMain.setVisibility(View.GONE);
                rlEmploymenDtl.setVisibility(View.GONE);
                rlEducationDtl.setVisibility(View.GONE);
                txtVwSkip.setVisibility(View.VISIBLE);
                strSkipValues = 0;
                break;
            case R.id.rlPersnlDtl:
                KeyboardUtility.HideKeyboard(this, rlPersnlDtl);
                imgVwPersnlDtl.setImageResource(R.mipmap.active_son_icon);
                imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
                imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

                btnNextPersnlDtl.setVisibility(View.VISIBLE);
                btnNextProfilePic.setVisibility(View.GONE);
                btnNextFileUpload.setVisibility(View.GONE);
                btnNextEducationDtl.setVisibility(View.GONE);
                btnNextEmployDetail.setVisibility(View.GONE);

                txtVwPersnalTitle.setVisibility(View.VISIBLE);
                txtVwEmplDtlTitle.setVisibility(View.GONE);
                txtVwEducDtlTitle.setVisibility(View.GONE);

                txtVwPersnalTitle.setVisibility(View.VISIBLE);
                rlPersonalDetail.setVisibility(View.VISIBLE);
                llPersonalDtl.setVisibility(View.VISIBLE);
                rlProfilePicVdo.setVisibility(View.GONE);
                rlFileUploadMain.setVisibility(View.GONE);
                rlEmploymenDtl.setVisibility(View.GONE);
                rlEducationDtl.setVisibility(View.GONE);
                txtVwSkip.setVisibility(View.VISIBLE);
                strSkipValues = 0;
                break;

            case R.id.imgVwEmplyMntDtl:
                addSkill();
//                getSkillList();
                KeyboardUtility.HideKeyboard(this, imgVwEmplyMntDtl);
                imgVwPersnlDtl.setImageResource(R.mipmap.son_icon);
                imgVwEmplyMntDtl.setImageResource(R.mipmap.active_professional_icon);
                imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

                btnNextPersnlDtl.setVisibility(View.GONE);
                btnNextEducationDtl.setVisibility(View.GONE);
                btnNextEmployDetail.setVisibility(View.VISIBLE);

                txtVwUploadVdoImgTitle.setVisibility(View.GONE);
                txtVwUpldResumeTitle.setVisibility(View.GONE);
                txtVwEducDtlTitle.setVisibility(View.GONE);
                txtVwPersnalTitle.setVisibility(View.GONE);
                txtVwEmplDtlTitle.setVisibility(View.VISIBLE);

                llEmployAllDtl.setVisibility(View.VISIBLE);
                rlEmploymenDtl.setVisibility(View.VISIBLE);
                rlProfilePicVdo.setVisibility(View.GONE);
                rlFileUploadMain.setVisibility(View.GONE);
                rlPersonalDetail.setVisibility(View.GONE);
                rlEducationDtl.setVisibility(View.GONE);

                txtVwSkip.setVisibility(View.VISIBLE);

                strSkipValues = 4;
                break;

            case R.id.rlEmplyMntDtl:
//                getSkillList();
                KeyboardUtility.HideKeyboard(this, rlEmplyMntDtl);
                imgVwPersnlDtl.setImageResource(R.mipmap.son_icon);
                imgVwEmplyMntDtl.setImageResource(R.mipmap.active_professional_icon);
                imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

                btnNextPersnlDtl.setVisibility(View.GONE);
                btnNextEducationDtl.setVisibility(View.GONE);
                btnNextEmployDetail.setVisibility(View.VISIBLE);

                txtVwUploadVdoImgTitle.setVisibility(View.GONE);
                txtVwUpldResumeTitle.setVisibility(View.GONE);
                txtVwEducDtlTitle.setVisibility(View.GONE);
                txtVwPersnalTitle.setVisibility(View.GONE);
                txtVwEmplDtlTitle.setVisibility(View.VISIBLE);

                rlProfilePicVdo.setVisibility(View.GONE);
                llEmployAllDtl.setVisibility(View.VISIBLE);
                rlEmploymenDtl.setVisibility(View.VISIBLE);
                rlFileUploadMain.setVisibility(View.GONE);
                rlPersonalDetail.setVisibility(View.GONE);
                rlEducationDtl.setVisibility(View.GONE);
                txtVwSkip.setVisibility(View.VISIBLE);
                strSkipValues = 4;
                break;

            case R.id.imgVwEducatDtl:
                KeyboardUtility.HideKeyboard(this, imgVwEducatDtl);
                imgVwPersnlDtl.setImageResource(R.mipmap.son_icon);
                imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
                imgVwEducatDtl.setImageResource(R.mipmap.active_book_icon);

                btnNextPersnlDtl.setVisibility(View.GONE);
                btnNextEducationDtl.setVisibility(View.VISIBLE);
                btnNextEmployDetail.setVisibility(View.GONE);

                txtVwUploadVdoImgTitle.setVisibility(View.GONE);
                txtVwUpldResumeTitle.setVisibility(View.GONE);
                txtVwPersnalTitle.setVisibility(View.GONE);
                txtVwEmplDtlTitle.setVisibility(View.GONE);
                txtVwEducDtlTitle.setVisibility(View.VISIBLE);

                rlProfilePicVdo.setVisibility(View.GONE);
                rlPersonalDetail.setVisibility(View.GONE);
                rlEmploymenDtl.setVisibility(View.GONE);
                rlEducationDtl.setVisibility(View.VISIBLE);
                txtVwSkip.setVisibility(View.VISIBLE);
                strSkipValues = 5;
                break;
            case R.id.rlEducatDtl:
                KeyboardUtility.HideKeyboard(this, rlEducatDtl);
                imgVwPersnlDtl.setImageResource(R.mipmap.son_icon);
                imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
                imgVwEducatDtl.setImageResource(R.mipmap.active_book_icon);

                btnNextPersnlDtl.setVisibility(View.GONE);
                btnNextEducationDtl.setVisibility(View.VISIBLE);
                btnNextEmployDetail.setVisibility(View.GONE);

                txtVwUploadVdoImgTitle.setVisibility(View.GONE);
                txtVwUpldResumeTitle.setVisibility(View.GONE);
                txtVwPersnalTitle.setVisibility(View.GONE);
                txtVwEmplDtlTitle.setVisibility(View.GONE);
                txtVwEducDtlTitle.setVisibility(View.VISIBLE);

                rlProfilePicVdo.setVisibility(View.GONE);
                rlPersonalDetail.setVisibility(View.GONE);
                rlEmploymenDtl.setVisibility(View.GONE);
                rlEducationDtl.setVisibility(View.VISIBLE);
                txtVwSkip.setVisibility(View.VISIBLE);
                strSkipValues = 5;
                break;
            case R.id.rlMain:
                KeyboardUtility.HideKeyboard(this, rlMain);
                break;


            case R.id.rlScrollMain:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                break;
            case R.id.ivBack:
                finish();
                break;

            case R.id.txtVwPersnlState:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                showDlgsPersnlGetState();
                break;

//NextButton Clicked  .........................................
            case R.id.btnNextPersnlDtl:
//                rlProfilePicVdo.setVisibility(View.VISIBLE);
//                rlFileUploadMain.setVisibility(View.GONE);
//
//                btnNextPersnlDtl.setVisibility(View.GONE);
//                btnNextProfilePic.setVisibility(View.VISIBLE);
//
//                txtVwUpldResumeTitle.setVisibility(View.GONE);
//                txtVwPersnalTitle.setVisibility(View.GONE);
//                txtVwUploadVdoImgTitle.setVisibility(View.VISIBLE);
//
//                llPersonalDtl.setVisibility(View.GONE);
//                txtVwSkip.setVisibility(View.VISIBLE);
//
//                imgVwPersnlDtl.setImageResource(mipmap.active_son_icon);
//                imgVwEmplyMntDtl.setImageResource(mipmap.professional_icon);
//                imgVwEducatDtl.setImageResource(mipmap.book_icon);
//
//                strSkipValues = 1;
                checkPersonalDetails();
                break;

            case R.id.btnNextProfilePic:
                checkProfilePic();
                break;

            case R.id.btnNextFileUpload:
                checkUploadResume();
                break;

            case R.id.btnNextEmployDetail:
                checkUpdateEmployeDetails();
                break;

            case R.id.btnNextEducationDtl:
                checkEducationDetails();
                break;
//*************************End***********************************

            case R.id.rlDateOfBrth:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                getBirthDate();
                break;
            case R.id.imgVwDateOfBrth:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                getBirthDate();
                break;

//Profile pic
            case R.id.imgVwUploadPic:
                checkPermissions();
                showDialogsImg();
                break;
            case R.id.imgProfVdo:
                checkPermissions();
                showDialogsVdo();
                break;


            case R.id.imgVwUploadFile:
                checkUploadResumePermissions();
                showResumeFileChooser();
                break;

            //Employment details
            case R.id.rlVwWrkgSncTo:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                getWorkSincTo();
                break;
            case R.id.rlWrkngSncFm:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                getWorkSincFrom();
                break;
            case R.id.imgVwWrkgSncTo:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                getWorkSincTo();
                break;
            case R.id.imgVwWrkgSncFrom:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                getWorkSincFrom();
                break;

            case R.id.txtVwEmplyCity:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                showDialogsEmployerGetCity();
                break;

            //Custom prev employemnt
            case R.id.rlAddMoreExp:
                try {
                    int pos = 0;
                    prevEmploymentListItem.add(new GetEmploymentDao.PreviousEmploymentDetailsBean());
                    prevEmplymntAdapter.notifyDataSetChanged();
                    recyclrPrevEmplymnt.scrollToPosition(pos);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            //Education details
            case R.id.imgVwHighScGraduDate:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                getHighSclGradDate();
                break;
            case R.id.rlHighScGraduDate:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                getHighSclGradDate();
                break;
            case R.id.imgVwClgGraduatDate:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                getClgGradDate();
                break;
            case R.id.rlClgGraduatDate:
                KeyboardUtility.HideKeyboard(this, rlScrollMain);
                getClgGradDate();
                break;
        }
    }

//.............................................Simple details data end...........................................................


//.............................................Employment details data.....................................

    //Employment GetCountry, GetState, GetCity ...............................
    private void showDialogsEmployerGetCity() {
        try {
            final Dialog dialog = new Dialog(EditProfileJobSeekerActivity.this);
            dialog.setContentView(R.layout.custom_get_country_state_city);

            RelativeLayout rlEmplymentState = dialog.findViewById(R.id.rlEmplymentState);
            RelativeLayout rlEmplymentCity = dialog.findViewById(R.id.rlEmplymentCity);
            RelativeLayout rlEmplymentCountry = dialog.findViewById(R.id.rlEmplymentCountry);
            TextView txtVwSaveCity = dialog.findViewById(R.id.txtVwSaveCity);

            spnrEmplymentState = dialog.findViewById(R.id.spnrEmplymentState);
            spnrEmplymentCity = dialog.findViewById(R.id.spnrEmplymentCity);
            spnrEmplymentCountry = dialog.findViewById(R.id.spnrEmplymentCountry);

//            if (countryList != null) {
            getCountryList();
//            }
            rlEmplymentCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spnrEmplymentCountry.performClick();
                }
            });

            rlEmplymentState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spnrEmplymentState.performClick();
                }
            });
//
            rlEmplymentCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spnrEmplymentCity.performClick();
                }
            });

            txtVwSaveCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCountryList() {
        try {
            countryList = new ArrayList<>();
            new GetCountryList().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GetCountryList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(EditProfileJobSeekerActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(WebField.SERV_GET_COUNTRY_LIST);

                if (jsonStr != null) {

                    CountryDao countryDao = new Gson().fromJson(jsonStr.toString(), CountryDao.class);

                    countryList.addAll(countryDao.getData().getCountryList());
                } else {
                    Applog.e(EditProfileJobSeekerActivity.TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialogUtility.SHOW_TOAST(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!");
                        }
                    });
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            try {
                SetSpinnerEmplyrCountry();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getStateList(int strCountryId) {
        try {

            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                JSONObject jsonObjectInput = new JSONObject();

                jsonObjectInput.put("countryId", strCountryId);

                Applog.e("Request", ":::" + jsonObjectInput);
                new GetJsonWithAndroidNetworkingLib(this,
                        jsonObjectInput, WebField.SERV_GET_STATE_LIST, GlobalData.intFlagShow, new OnUpdateListener() {
                    @Override
                    public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                        if (isSuccess) {
                            try {
                                if (jsonObject != null) {
                                    stateList = new ArrayList<>();

                                    StateDao stateDao = new Gson().fromJson(jsonObject.toString(), StateDao.class);
                                    stateList.addAll(stateDao.getData().getStateList());

                                    SetSpinnerEmplyrState();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
//                            AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "Your set profile not update");
                        }
                    }
                }).execute();
            } else {
                AlertDialogUtility.SHOW_INTERNET_ALERT(this);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCityList(int stateId) {
        try {

            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                JSONObject jsonObjectInput = new JSONObject();

                jsonObjectInput.put("stateId", stateId);

                Applog.e("Request", ":::" + jsonObjectInput);
                new GetJsonWithAndroidNetworkingLib(this,
                        jsonObjectInput, WebField.SERV_GET_CITY_LIST, GlobalData.intFlagShow, new OnUpdateListener() {
                    @Override
                    public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                        if (isSuccess) {
                            try {
                                if (jsonObject != null) {
                                    cityList = new ArrayList<>();

                                    CityDao cityDao = new Gson().fromJson(jsonObject.toString(), CityDao.class);
                                    cityList.addAll(cityDao.getData().getCityList());

                                    SetSpinnerEmplyrCity();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
//                            AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "Your set profile not update");
                        }
                    }
                }).execute();
            } else {
                AlertDialogUtility.SHOW_INTERNET_ALERT(this);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetSpinnerEmplyrCountry() {
        try {

            ArrayAdapter<CountryDao.DataBean.CountryListBean> adapter = new
                    ArrayAdapter<>(EditProfileJobSeekerActivity.this, R.layout.custom_country_data, R.id.txtVwCountryName, countryList);
            spnrEmplymentCountry.setAdapter(adapter);

            spnrEmplymentCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    KeyboardUtility.HideKeyboard(getApplicationContext(), spnrEmplymentCountry);
                    emplyerCountryId = countryList.get(position).getCountryId();
                    getStateList(countryList.get(position).getCountryId());

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetSpinnerEmplyrState() {
        try {
            ArrayAdapter<StateDao.DataBean.StateListBean> adapter = new
                    ArrayAdapter<>(EditProfileJobSeekerActivity.this, R.layout.custom_country_data, R.id.txtVwCountryName, stateList);
            spnrEmplymentState.setAdapter(adapter);

            spnrEmplymentState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    KeyboardUtility.HideKeyboard(getApplicationContext(), spnrEmplymentState);
                    emplyerStateId = stateList.get(position).getStateId();
                    getCityList(stateList.get(position).getStateId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetSpinnerEmplyrCity() {
        try {
            ArrayAdapter<CityDao.DataBean.CityListBean> adapter = new
                    ArrayAdapter<>(EditProfileJobSeekerActivity.this, R.layout.custom_country_data, R.id.txtVwCountryName, cityList);
            spnrEmplymentCity.setAdapter(adapter);

            spnrEmplymentCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    KeyboardUtility.HideKeyboard(getApplicationContext(), spnrEmplymentCity);
                    emplyerCityId = cityList.get(position).getCityId();
                    strCityName = cityList.get(position).getCityName();
                    txtVwEmplyCity.setText(strCityName);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //...................End...........................

    //Employment GetSkill........................................................

    private void getSkillList() {
        try {
            skillList = new ArrayList<>();
            new CallGetSkillList().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CallGetSkillList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(EditProfileJobSeekerActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(WebField.SERV_GET_SKILL_LIST);
                Applog.e(EditProfileJobSeekerActivity.TAG, "Response from skill url : " + jsonStr);

                if (jsonStr != null) {

                    SkillDao skillDao = new Gson().fromJson(jsonStr.toString(), SkillDao.class);
                    skillList.addAll(skillDao.getData().getSkillsList());
                } else {
                    Applog.e(EditProfileJobSeekerActivity.TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialogUtility.SHOW_TOAST(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!");
                        }
                    });
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            try {
                SetSkillData();

//                addSkillData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void SetSkillData() {
        try {
            skillAdapter = new ArrayAdapter<>
                    (getApplicationContext(), R.layout.custom_autotxtvw_add_skill_layout, R.id.tvAddHintSkill, skillList);

            autoCmpltTxtVwSkill.setAdapter(skillAdapter);//setting the adapter data into the AutoCompleteTextView


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSkill() {
        try {
            AddskillName = new SkillDao.DataBean.SkillsListBean();

            getSkillList();

            autoCmpltTxtVwSkill = findViewById(R.id.autoCmpltTxtVwSkill);
            autoCmpltTxtVwSkill.setThreshold(1);//will start working from first character
            autoCmpltTxtVwSkill.setTextColor(getResources().getColor(R.color.edt_vw_text));
            autoCmpltTxtVwSkill.setDropDownBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_bg)));


            autoCmpltTxtVwSkill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Applog.e("Skill : ", "Add ");
                    skillId = skillAdapter.getItem(position).getSkillId();

                    strSkill = autoCmpltTxtVwSkill.getText().toString();
                    AddskillName.setSkill(strSkill);
                    addSkillData(AddskillName, skillId);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSkillData(SkillDao.DataBean.SkillsListBean addskillName, int skillId) {
        try {
            Applog.e("Skill Array ", " Size : " + skillList.size());
            if (skillList.size() != 0) {
                Applog.e("Get Ids", ":::::" + skillId);

                addSkillList.add(addskillName);
                skillListingAdapter = new SkillListingAdapter(getApplicationContext(), addSkillList);
                hlvSkill.setAdapter(skillListingAdapter);//setting the adapter data into the AutoCompleteTextView

                autoCmpltTxtVwSkill.setText("");
                AddskillName = new SkillDao.DataBean.SkillsListBean();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //.........................End..................................................

    private void SetSpinnerLakhs() {

        try {
            alLakhs = new ArrayList<>();

            for (int i = 1; i <= 10; i++) {
                spModel = new SpinnerModel();
                spModel.setEmplyrLakhs(emplyerLac);
                alLakhs.add(spModel);
                emplyerLac++;
            }

            spCustomAdapter = new CustomSpinnerVehicleAdapter(this, alLakhs);
            spnrLakhs.setAdapter(spCustomAdapter);

            spnrLakhs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    KeyboardUtility.HideKeyboard(getApplicationContext(), spnrLakhs);
                    spnrLakhs.getSelectedItemPosition();
                    int intPostion = spnrLakhs.getSelectedItemPosition();
                    emplyerLac = alLakhs.get(intPostion).getEmplyrLakhs();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetSpinnerThousand() {
        try {
            alThousand = new ArrayList<>();

            for (int i = 1; i < 10; i++) {
                spModel = new SpinnerModel();
                spModel.setEmplyrThousand(emplyerThousand);
                alThousand.add(spModel);
                emplyerThousand = emplyerThousand + 1000;
            }


            spCustomAdapter = new CustomSpinnerVehicleAdapter(this, alThousand);
            spnrThousand.setAdapter(spCustomAdapter);

            spnrThousand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    KeyboardUtility.HideKeyboard(getApplicationContext(), spnrThousand);
                    spnrThousand.getSelectedItemPosition();
                    int intPostion = spnrThousand.getSelectedItemPosition();
                    emplyerThousand = alThousand.get(intPostion).getEmplyrThousand();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetSpinnerNoticePeriod() {
        try {
            alNoticePeriod = new ArrayList<>();

            // set First State name
            spModel = new SpinnerModel();
            spModel.setStrNoticePeriod("15 Day");
            alNoticePeriod.add(spModel);

            spModel = new SpinnerModel();
            spModel.setStrNoticePeriod("1 Month");
            alNoticePeriod.add(spModel);

            spModel = new SpinnerModel();
            spModel.setStrNoticePeriod("2 Month");
            alNoticePeriod.add(spModel);

            spModel = new SpinnerModel();
            spModel.setStrNoticePeriod("3 Month");
            alNoticePeriod.add(spModel);

            spModel = new SpinnerModel();
            spModel.setStrNoticePeriod(">3 Month");
            alNoticePeriod.add(spModel);

            spCustomAdapter = new CustomSpinnerVehicleAdapter(this, alNoticePeriod);
            spnrNoticePeriod.setAdapter(spCustomAdapter);

            spnrNoticePeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    KeyboardUtility.HideKeyboard(getApplicationContext(), spnrNoticePeriod);
                    spnrNoticePeriod.getSelectedItemPosition();
                    int intPostion = spnrNoticePeriod.getSelectedItemPosition();
                    strNoticePeriod = alNoticePeriod.get(intPostion).getStrNoticePeriod();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWorkSincFrom() {
        try {
            // Get Current Date
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;

                            txtVwWrkgSncFrom.setText(new StringBuilder().append(mMonth + 1).append("/").append(year).append(" "));
                            strFrom = txtVwWrkgSncFrom.getText().toString();
                        }
                    }, mDay, mMonth, mYear);

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWorkSincTo() {
        try {
            // Get Current Date
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;

                            txtVwWrkgSncTo.setText(new StringBuilder().append(mMonth + 1).append("/").append(year).append(" "));
                            strTo = txtVwWrkgSncTo.getText().toString();
                        }
                    }, mDay, mMonth, mYear);

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkUpdateEmployeDetails() {
        try {
            strCurrentDesign = edtVwCurrentDesign.getText().toString().trim();
            strCurrentCompny = edtVwCurrentCmpny.getText().toString().trim();
            strFrom = txtVwWrkgSncFrom.getText().toString();
            strTo = txtVwWrkgSncTo.getText().toString();

            if (strCurrentDesign.length() == 0) {
                GlobalMethod.showAlert(this, "Please enter current designation");
                edtVwCurrentDesign.requestFocus();
            } else if (strCurrentCompny.length() == 0) {
                GlobalMethod.showAlert(this, "Please enter current company name");
                edtVwCurrentCmpny.requestFocus();
            } else if (strFrom.length() == 0) {
                GlobalMethod.showAlert(this, "Please enter working since from");
//                edtVwCurrentCmpny.requestFocus();
            } else if (strTo.length() == 0) {
                GlobalMethod.showAlert(this, "Please enter working since to");
//                edtVwCurrentCmpny.requestFocus();
            } else {

                if (strNoticePeriod.matches("15 Day")) {
                    strNoticePeriod = "15";
                } else if (strNoticePeriod.matches("1 Month")) {
                    strNoticePeriod = "30";
                } else if (strNoticePeriod.matches("2 Month")) {
                    strNoticePeriod = "60";
                } else if (strNoticePeriod.matches("3 Month")) {
                    strNoticePeriod = "90";
                } else if (strNoticePeriod.matches(">3 Month")) {
                    strNoticePeriod = ">90";
                }
//Update Change
                callUpdateEmployeDetails();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String strEmplyrEmploymentId, strprevEmplyerEmploymntId;

    private void callUpdateEmployeDetails() {

        try {
            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                JSONObject jsonObjectRequest = new JSONObject();
                JSONObject jsnObjCurrntEmpDtl = new JSONObject();
                JSONArray jsnArryPrevEmpData = new JSONArray();

                Applog.e("getEducationEmploymentId", "" + getEducationEmploymentId);
                strEmplyrEmploymentId = "1";
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_EMPLOYMENT_ID, strEmplyrEmploymentId);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_DESIGNATION, strCurrentDesign);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_COMPANY_NAME, strCurrentCompny);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_ANNUAL_SLRY_LACS, emplyerLac);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_ANNUAL_SLRY_THOUSND, emplyerThousand);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_WORK_START_DATE, strFrom);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_WORK_END_DATE, strTo);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_COUNTRY_ID, emplyerCountryId);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_STATE_ID, emplyerStateId);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_CITY_ID, emplyerCityId);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_NOTICE_PERIOD, strNoticePeriod);

                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_SKILLS, "");

                for (int i = 0; i < prevEmploymentListItem.size(); i++) {
                    JSONObject jsonObjPrevEmplDetails = new JSONObject();
//                    strprevEmplyerEmploymntId = SessionManager.getEditJobseekerData(this).getPreviousEmploymentDetails().get(i).getEmploymentId();
                    PrevEmplyrEmploymentId = prevEmploymentListItem.get(i).getEmploymentId();

                    strPrevEmplyrCmpnyDesig = prevEmploymentListItem.get(i).getDesignation();
                    strPrevEmplyrCmpnyNm = prevEmploymentListItem.get(i).getCompanyName();
                    strPrevEmplyrCmpnyFrom = prevEmploymentListItem.get(i).getWorkStartFrom();
                    strPrevEmplyrCmpnyTo = prevEmploymentListItem.get(i).getWorkEnd();

                    Applog.e("getPrevEmplyrEmploymentId ", " :: " + PrevEmplyrEmploymentId);
                    jsonObjPrevEmplDetails.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_EMPLOYMENT_ID, PrevEmplyrEmploymentId);
                    jsonObjPrevEmplDetails.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_PREV_DESIGNATION, strPrevEmplyrCmpnyDesig);
                    jsonObjPrevEmplDetails.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_PREV_COMPANY_NAME, strPrevEmplyrCmpnyNm);
                    jsonObjPrevEmplDetails.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_PREV_FROM_DATE, strPrevEmplyrCmpnyFrom);
                    jsonObjPrevEmplDetails.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_PREV_TO_DATE, strPrevEmplyrCmpnyTo);

                    jsnArryPrevEmpData.put(jsonObjPrevEmplDetails);
                }

                Applog.e("EmpIDs ", " Emp Ids :: " + strEmplyrEmploymentId);

//                jsonObjectRequest.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_JOBSEEKER_ID, SessionManager.getJobseekerData(this).getJobUserId());
                jsonObjectRequest.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_JOBSEEKER_ID, "1");
                jsonObjectRequest.put("CurrentEmploymentDetails", jsnObjCurrntEmpDtl);
                jsonObjectRequest.put("PreviousEmploymentDetails", jsnArryPrevEmpData);

                Applog.e("Emplyment details", ":::::::" + jsonObjectRequest);

                Applog.e("Api", ":::::::" + WebField.SERV_SET_EMPLOYMENT_DETAIL);
                new GetJsonWithAndroidNetworkingLib(EditProfileJobSeekerActivity.this, jsonObjectRequest,
                        WebField.SERV_SET_EMPLOYMENT_DETAIL,
                        GlobalData.intFlagShow, new OnUpdateListener() {
                    @Override
                    public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                        Applog.e("jsonObjectn ", " Employment:::" + jsonObject);
                        Applog.e("isSuccess", " Employment:::" + isSuccess);
                        if (isSuccess) {

                            try {
                                if (jsonObject != null) {

                                    String status = "";
                                    String message = "";

                                    Applog.e("SetEmploymentDetails", ":::::" + jsonObject);

                                    rlEmploymenDtl.setVisibility(View.GONE);
                                    rlEducationDtl.setVisibility(View.VISIBLE);

                                    imgVwPersnlDtl.setImageResource(R.mipmap.son_icon);
                                    imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
                                    imgVwEducatDtl.setImageResource(R.mipmap.active_book_icon);

                                    txtVwUpldResumeTitle.setVisibility(View.GONE);
                                    txtVwPersnalTitle.setVisibility(View.GONE);
                                    txtVwUploadVdoImgTitle.setVisibility(View.GONE);

                                    txtVwSkip.setVisibility(View.VISIBLE);
                                    txtVwEmplDtlTitle.setVisibility(View.GONE);
                                    txtVwEducDtlTitle.setVisibility(View.VISIBLE);

                                    btnNextEmployDetail.setVisibility(View.GONE);
                                    btnNextEducationDtl.setVisibility(View.VISIBLE);
                                    strSkipValues = 4;

                                    AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "You have set your employment details.");

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "Please check your enter filde!!!");
                        }
                    }
                }).execute();

            } else {
                AlertDialogUtility.SHOW_INTERNET_ALERT(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//.............................................Employment details data end...........................................................


//.............................................Personal details data.....................................

    private void SetSpinnerEthnCity() {
        try {
            alEthaniCity = new ArrayList<>();

            // set First State name
            spModel = new SpinnerModel();
            spModel.setStrEthnCity("Amarican Indian");
            alEthaniCity.add(spModel);

            spModel = new SpinnerModel();
            spModel.setStrEthnCity("North Indian");
            alEthaniCity.add(spModel);

            spModel = new SpinnerModel();
            spModel.setStrEthnCity("Sauth Indian");
            alEthaniCity.add(spModel);

            spCustomAdapter = new CustomSpinnerVehicleAdapter(this, alEthaniCity);
            spnrEthnicCity.setAdapter(spCustomAdapter);

            spnrEthnicCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    KeyboardUtility.HideKeyboard(getApplicationContext(), spnrEthnicCity);
                    spnrEthnicCity.getSelectedItemPosition();
                    int intPostion = spnrEthnicCity.getSelectedItemPosition();
                    strEthaniCity = alEthaniCity.get(intPostion).getStrEthnCity();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetSpinnerGender() {
        try {
            alGender = new ArrayList<>();

            // set First State name
            spModel = new SpinnerModel();
            spModel.setStrGender("Male");
            alGender.add(spModel);

            spModel = new SpinnerModel();
            spModel.setStrGender("Female");
            alGender.add(spModel);

            spCustomAdapter = new CustomSpinnerVehicleAdapter(this, alGender);
            spnrGender.setAdapter(spCustomAdapter);

            spnrGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    KeyboardUtility.HideKeyboard(getApplicationContext(), spnrGender);
                    spnrGender.getSelectedItemPosition();
                    int intPostion = spnrGender.getSelectedItemPosition();
                    strGender = alGender.get(intPostion).getStrGender();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPersonalDetails() {
        try {
            strFirstNm = edtVwFirstNm.getText().toString().trim();
            strLastNm = edtVwLastNm.getText().toString().trim();
            strDateOfBrth = txtVwDteOfBrth.getText().toString().trim();
            strContactNo = edtVwContactNo.getText().toString().trim();
//            strEthaniCity =strEthaniCity;
            strZipCode = edtVwZipcode.getText().toString().trim();

            if (strFirstNm.length() == 0) {
                GlobalMethod.showAlert(this, "Please enter first name");
                edtVwFirstNm.requestFocus();
            } else if (strLastNm.length() == 0) {
                GlobalMethod.showAlert(this, "Please enter last name");
                edtVwLastNm.requestFocus();
            } else if (strGender.equalsIgnoreCase("Select Gender")) {
                GlobalMethod.showAlert(this, "Please select gender");
                spnrGender.requestFocus();
            } else if (strDateOfBrth.length() == 0) {
                GlobalMethod.showAlert(this, "Please enter date of birth");
                txtVwDteOfBrth.requestFocus();
            } else if (strZipCode.length() == 0) {
                GlobalMethod.showAlert(this, "Please enter zipcode");
                edtVwZipcode.requestFocus();
            } else if (strContactNo.length() == 0) {
                GlobalMethod.showAlert(this, "Please enter contact number");
                edtVwContactNo.requestFocus();
            } else if (strContactNo.length() < 8) {
                GlobalMethod.showAlert(this, "Contact number should be of minimum 8 characters");
                edtVwContactNo.requestFocus();
            } else {
                //Update Change
//                callSetPersonalDetail();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callSetPersonalDetail() {
        try {
            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                JSONObject jsonObjectInput = new JSONObject();

                jsonObjectInput.put(WebField.SET_PERSONAL_DETAIL.REQUEST_FIRST_NAME, strFirstNm);
                jsonObjectInput.put(WebField.SET_PERSONAL_DETAIL.REQUEST_LAST_NAME, strLastNm);
                jsonObjectInput.put(WebField.SET_PERSONAL_DETAIL.REQUEST_GENDER, strGender);
                jsonObjectInput.put(WebField.SET_PERSONAL_DETAIL.REQUEST_DATE_OF_BRTH, strDateOfBrth);
                jsonObjectInput.put(WebField.SET_PERSONAL_DETAIL.REQUEST_MOBILE_NO, strContactNo);
                jsonObjectInput.put(WebField.SET_PERSONAL_DETAIL.REQUEST_ETHINI_CITY, strEthaniCity);
                jsonObjectInput.put(WebField.SET_PERSONAL_DETAIL.REQUEST_CITY, strPersnlDtlCityName);
                jsonObjectInput.put(WebField.SET_PERSONAL_DETAIL.REQUEST_STATE, strPersnlDtlStateName);
                jsonObjectInput.put(WebField.SET_PERSONAL_DETAIL.REQUEST_PINCODE, strZipCode);
                jsonObjectInput.put(WebField.SET_PERSONAL_DETAIL.REQUEST_JOB_USER_ID, SessionManager.getJobseekerData(this).getJobUserId());
//                jsonObjectInput.put(WebField.SET_PERSONAL_DETAIL.REQUEST_JOB_USER_ID, "34");

                Applog.e("Request", ":::" + jsonObjectInput);
                new GetJsonWithAndroidNetworkingLib(this,
                        jsonObjectInput, WebField.SERV_JOB_SET_PERSONAL_DTL, GlobalData.intFlagShow, new OnUpdateListener() {
                    @Override
                    public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                        if (isSuccess) {
                            try {
                                if (jsonObject != null) {

                                    JobseekerDataMdl userData = new JobseekerDataMdl();
                                    userData.setstrIsSetProfile(jsonObject.getString(WebField.SET_PERSONAL_DETAIL.RESPONSE_IS_SET_PROF));
                                    userData.setJobUserId(SessionManager.getJobseekerData(EditProfileJobSeekerActivity.this).getJobUserId());
                                    userData.setJobEmailId(SessionManager.getJobseekerData(EditProfileJobSeekerActivity.this).getJobEmailId());

                                    String status = "";
                                    String message = jsonObject.getString(WebField.MESSAGE);

                                    SessionManager.saveJobseekerData(EditProfileJobSeekerActivity.this, userData);

                                    AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, message);

                                    rlProfilePicVdo.setVisibility(View.VISIBLE);
                                    rlFileUploadMain.setVisibility(View.GONE);

                                    btnNextPersnlDtl.setVisibility(View.GONE);
                                    btnNextProfilePic.setVisibility(View.VISIBLE);

                                    txtVwUpldResumeTitle.setVisibility(View.GONE);
                                    txtVwPersnalTitle.setVisibility(View.GONE);
                                    txtVwUploadVdoImgTitle.setVisibility(View.VISIBLE);

                                    llPersonalDtl.setVisibility(View.GONE);
                                    txtVwSkip.setVisibility(View.VISIBLE);

                                    imgVwPersnlDtl.setImageResource(R.mipmap.active_son_icon);
                                    imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
                                    imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

                                    strSkipValues = 1;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "Your set profile not update");
                        }
                    }
                }).execute();
            } else {
                AlertDialogUtility.SHOW_INTERNET_ALERT(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Personal details screen GetCountry, GetState, GetCity .......................
    private void showDlgsPersnlGetState() {
        try {
            final Dialog dialog = new Dialog(EditProfileJobSeekerActivity.this);
            dialog.setContentView(R.layout.custom_get_persnl_details_state);

            RelativeLayout rlPersnlDetailCountry = dialog.findViewById(R.id.rlPersnlDetailCountry);
            RelativeLayout rlPersnlDetailState = dialog.findViewById(R.id.rlPersnlDetailState);

            TextView txtVwSaveState = dialog.findViewById(R.id.txtVwSaveState);

            spnrPersnlDtlCountry = dialog.findViewById(R.id.spnrPersnlDetailCountry);
            spnrPersnlDetailState = dialog.findViewById(R.id.spnrPersnlDetailState);

            getPersonalDtlCountryList();

            rlPersnlDetailCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spnrPersnlDtlCountry.performClick();
                }
            });
            rlPersnlDetailState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spnrPersnlDetailState.performClick();
                }
            });

            txtVwSaveState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPersonalDtlCountryList() {
        try {
            countryList = new ArrayList<>();
            new GetPersonalDtlCountryList().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GetPersonalDtlCountryList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(EditProfileJobSeekerActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(WebField.SERV_GET_COUNTRY_LIST);
                Applog.e(EditProfileJobSeekerActivity.TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {

                    CountryDao countryDao = new Gson().fromJson(jsonStr.toString(), CountryDao.class);

                    countryList.addAll(countryDao.getData().getCountryList());
                } else {
                    Applog.e(EditProfileJobSeekerActivity.TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialogUtility.SHOW_TOAST(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!");
                        }
                    });
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            try {
                SetSpinnerPersonalDtlCountry();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void SetSpinnerPersonalDtlCountry() {
        try {
            ArrayAdapter<CountryDao.DataBean.CountryListBean> adapter = new
                    ArrayAdapter<>(EditProfileJobSeekerActivity.this, R.layout.custom_country_data, R.id.txtVwCountryName, countryList);
            spnrPersnlDtlCountry.setAdapter(adapter);

            spnrPersnlDtlCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    persnlDtlCountryId = countryList.get(position).getCountryId();
                    getPersonalDtlStateList(countryList.get(position).getCountryId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPersonalDtlStateList(int strCountryId) {
        try {
            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                JSONObject jsonObjectInput = new JSONObject();

                jsonObjectInput.put("countryId", strCountryId);

                Applog.e("Request", ":::" + jsonObjectInput);
                new GetJsonWithAndroidNetworkingLib(this,
                        jsonObjectInput, WebField.SERV_GET_STATE_LIST, GlobalData.intFlagShow, new OnUpdateListener() {
                    @Override
                    public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                        if (isSuccess) {
                            try {
                                if (jsonObject != null) {
                                    stateList = new ArrayList<>();

                                    StateDao stateDao = new Gson().fromJson(jsonObject.toString(), StateDao.class);
                                    stateList.addAll(stateDao.getData().getStateList());

                                    SetSpinnerPersonalDtlState();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
//                            AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "Your set profile not update");
                        }
                    }
                }).execute();
            } else {
                AlertDialogUtility.SHOW_INTERNET_ALERT(this);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetSpinnerPersonalDtlState() {
        try {
            ArrayAdapter<StateDao.DataBean.StateListBean> adapter = new
                    ArrayAdapter<>(EditProfileJobSeekerActivity.this, R.layout.custom_country_data, R.id.txtVwCountryName, stateList);
            spnrPersnlDetailState.setAdapter(adapter);

            spnrPersnlDetailState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    persnlDtlStateId = stateList.get(position).getStateId();
                    strPersnlDtlStateName = stateList.get(position).getStateName();
                    txtVwPersnlState.setText(strPersnlDtlStateName);

                    getPersonalDtlCityList(stateList.get(position).getStateId());
                    txtSelectCity.setVisibility(View.GONE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPersonalDtlCityList(int stateId) {
        try {
            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                JSONObject jsonObjectInput = new JSONObject();

                jsonObjectInput.put("stateId", stateId);

                Applog.e("Request", ":::" + jsonObjectInput);
                new GetJsonWithAndroidNetworkingLib(this,
                        jsonObjectInput, WebField.SERV_GET_CITY_LIST, GlobalData.intFlagShow, new OnUpdateListener() {
                    @Override
                    public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                        if (isSuccess) {
                            try {
                                if (jsonObject != null) {
                                    cityList = new ArrayList<>();

                                    CityDao cityDao = new Gson().fromJson(jsonObject.toString(), CityDao.class);
                                    cityList.addAll(cityDao.getData().getCityList());

                                    SetSpinnerPersonalDtlCity();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
//                            AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "Your set profile not update");
                        }
                    }
                }).execute();
            } else {
                AlertDialogUtility.SHOW_INTERNET_ALERT(this);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetSpinnerPersonalDtlCity() {
        try {
            ArrayAdapter<CityDao.DataBean.CityListBean> adapter = new
                    ArrayAdapter<>(EditProfileJobSeekerActivity.this, R.layout.custom_country_data,
                    R.id.txtVwStateName, cityList);
            spnrPersnlDtlCity.setAdapter(adapter);

            spnrPersnlDtlCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    persnlDtlCityId = cityList.get(position).getCityId();
                    strPersnlDtlCityName = cityList.get(position).getCityName();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //..........................End..........................................

    //.............................................Personal details data end...........................................................


    //.............................................Resume upload data.....................................
    private void checkUploadResumePermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, EditProfileJobSeekerActivity.MY_PERMISSIONS_UPLOAD_RESUME_REQUEST);
        }
    }

    private void checkUploadResume() {
        try {
            if (strselectedResumeFilePath != null) {
                uploadResumeFile(strselectedResumeFilePath);
            } else {
                AlertDialogUtility.SHOW_TOAST(this, "Please choose a File First");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int uploadResumeFile(String strselectedResumeFilePath) {

        int serverResponseCode = 0;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(strselectedResumeFilePath);

        String[] parts = strselectedResumeFilePath.split("/");
        String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            dialog.dismiss();
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                //Update Change
//                CallUploadResumeApi();
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    try {

                        //write the bytes read from inputstream
//                        dataOutputStream.write(buffer, 0, bufferSize);
                    } catch (OutOfMemoryError e) {
                        AlertDialogUtility.SHOW_TOAST(this, "Insufficient Memory!");
                    }
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                fileInputStream.close();

                if (wakeLock.isHeld()) {
                    wakeLock.release();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private void CallUploadResumeApi() {
        try {

            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                JSONObject jsonObjectInput = new JSONObject();

                if (strselectedResumeFilePath.endsWith(".doc") || strselectedResumeFilePath.endsWith(".DOC") ||
                        strselectedResumeFilePath.endsWith(".docx") || strselectedResumeFilePath.endsWith(".DOCX")
                        || strselectedResumeFilePath.endsWith(".PDF") || strselectedResumeFilePath.endsWith(".pdf")) {

                    if (strselectedResumeFilePath.endsWith(".doc")) {
                        jsonObjectInput.put(WebField.SET_RESUME.REQUEST_FILE_TYPE, ".DOCX");
                    } else if (strselectedResumeFilePath.endsWith(".DOC")) {
                        jsonObjectInput.put(WebField.SET_RESUME.REQUEST_FILE_TYPE, ".DOCX");
                    } else if (strselectedResumeFilePath.endsWith(".docx")) {
                        jsonObjectInput.put(WebField.SET_RESUME.REQUEST_FILE_TYPE, ".DOCX");
                    } else if (strselectedResumeFilePath.endsWith(".DOCX")) {
                        jsonObjectInput.put(WebField.SET_RESUME.REQUEST_FILE_TYPE, ".DOCX");
                    } else if (strselectedResumeFilePath.endsWith(".PDF")) {
                        jsonObjectInput.put(WebField.SET_RESUME.REQUEST_FILE_TYPE, ".PDF");
                    } else if (strselectedResumeFilePath.endsWith(".pdf")) {
                        jsonObjectInput.put(WebField.SET_RESUME.REQUEST_FILE_TYPE, ".PDF");
                    }
                }

                jsonObjectInput.put(WebField.SET_RESUME.REQUEST_USER_ID, SessionManager.getJobseekerData(this).getJobUserId());
//                jsonObjectInput.put(SET_RESUME.REQUEST_USER_ID, "63");
                jsonObjectInput.put(WebField.SET_RESUME.REQUEST_RESUME_BASE64, strResumeBase64);

                Applog.e("Resume Request", "::::" + jsonObjectInput);
                new GetJsonWithAndroidNetworkingLib(this,
                        jsonObjectInput, WebField.SERV_SET_RESUME, GlobalData.intFlagShow, new OnUpdateListener() {
                    @Override
                    public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                        if (isSuccess) {
                            try {
                                if (jsonObject != null) {
                                    String status = "";
                                    String message = "";

//                                        SessionManager.saveEmployerData(SignInEmployerActivity.this, userData);
                                    jsonObject.getString(WebField.SET_RESUME.RESPONSE_IS_SET_PROF);

                                    imgVwPersnlDtl.setImageResource(R.mipmap.son_icon);
                                    imgVwEmplyMntDtl.setImageResource(R.mipmap.active_professional_icon);
                                    imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

                                    txtVwPersnalTitle.setVisibility(View.GONE);
                                    txtVwUploadVdoImgTitle.setVisibility(View.GONE);
                                    txtVwUpldResumeTitle.setVisibility(View.GONE);
                                    txtVwEmplDtlTitle.setVisibility(View.VISIBLE);
                                    txtVwEducDtlTitle.setVisibility(View.GONE);

                                    btnNextFileUpload.setVisibility(View.GONE);
                                    btnNextEmployDetail.setVisibility(View.VISIBLE);

                                    llEmployAllDtl.setVisibility(View.VISIBLE);
                                    rlEmploymenDtl.setVisibility(View.VISIBLE);
                                    txtVwSkip.setVisibility(View.VISIBLE);
                                    rlPersonalDetail.setVisibility(View.GONE);
                                    rlEducationDtl.setVisibility(View.GONE);
                                    strSkipValues = 4;

                                    AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "You have set your resume.");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "You have entered wrong credentials.");
                        }
                    }
                }).execute();
            } else {
                AlertDialogUtility.SHOW_INTERNET_ALERT(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showResumeFileChooser() {
        try {

            File file = new File(Environment.getExternalStorageDirectory(),
                    "Report.pdf");
            Uri path = Uri.fromFile(file);

            Intent intentPickDocument = new Intent();
            //sets the select file to all types of files
//            intentPickDocument.setType("application/pdf");
//            intentPickDocument.setType("application/docx");
//            intentPickDocument.setAction(Intent.ACTION_GET_CONTENT);
            intentPickDocument.setAction(Intent.ACTION_GET_CONTENT);
            intentPickDocument.setDataAndType(path, "application/*");
            intentPickDocument.addCategory(Intent.CATEGORY_OPENABLE);

            //starts new activity to select file and return data
            startActivityForResult(intentPickDocument, EditProfileJobSeekerActivity.PICK_FILE_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//.............................................Resume details data end...........................................................


    //.............................................Education details data.....................................
    private void getClgGradDate() {
        try {
            // Get Current Date
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;

                            txtVwGraduDte.setText(new StringBuilder().append(mDay)
                                    .append("/").append(mMonth + 1).append("/").append(year).append(" "));

                            strEducationGradDate = txtVwGraduDte.getText().toString();
                        }
                    }, mDay, mMonth, mYear);

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getHighSclGradDate() {
        try {
            // Get Current Date
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;

                            txtVwHighScGraduDte.setText(new StringBuilder().append(mDay)
                                    .append("/").append(mMonth + 1).append("/").append(year).append(" "));

                            strEducationHighDate = txtVwHighScGraduDte.getText().toString();
                        }
                    }, mDay, mMonth, mYear);

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBirthDate() {
        try {
            // Get Current Date
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;

                            txtVwDteOfBrth.setText(new StringBuilder().append(mDay)
                                    .append("/").append(mMonth + 1).append("/").append(year).append(" "));

                        }
                    }, mDay, mMonth, mYear);

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkEducationDetails() {
        try {

            strEducationHighName = edtVwHighSclGraduation.getText().toString().trim();
            strEducationGradName = edtVwClgGraduat.getText().toString().trim();

            if (strEducationHighName.length() == 0) {
                GlobalMethod.showAlert(this, "Please enter high school name");
                edtVwHighSclGraduation.requestFocus();
            } else if (strEducationGradName.length() == 0) {
                GlobalMethod.showAlert(this, "Please enter college name");
                edtVwClgGraduat.requestFocus();
            } else {
                //Update Change
//                callEducationDetails();
            }

        } catch (Exception e) {

        }
    }

    private void callEducationDetails() {

        try {
            try {
                if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                    alEducationDetail = new ArrayList<>();

                    JSONObject jsonObjectRequest = new JSONObject();
                    JSONArray jsnArryEducationDtl = new JSONArray();

                    JSONObject jsonObjEducationHighScl = new JSONObject();
                    JSONObject jsonObjEducationGraduation = new JSONObject();

                    String strhighSclType = "1";
                    String strClgGraduatType = "2";

//                    HighSchool Data
                    JobseekerEducationDtlModel jobseekerEducationDtl = new JobseekerEducationDtlModel(strEducationHighName
                            , strhighSclType, strEducationHighDate);

                    alEducationDetail.add(jobseekerEducationDtl);

                    jsonObjEducationHighScl.put(WebField.SET_EDUCATIONAL_DTLS.REQUEST_GRADUATION_NAME, strEducationHighName);
                    jsonObjEducationHighScl.put(WebField.SET_EDUCATIONAL_DTLS.REQUEST_GRADUATION_TYPE, strhighSclType);
                    jsonObjEducationHighScl.put(WebField.SET_EDUCATIONAL_DTLS.REQUEST_GRADUATION_DATE, strEducationHighDate);

                    jsnArryEducationDtl.put(jsonObjEducationHighScl);

//                    Graduation Data
                    JobseekerEducationDtlModel jobseekerEducationDtl1 = new JobseekerEducationDtlModel(strEducationGradName
                            , strClgGraduatType, strEducationGradDate);

                    alEducationDetail.add(jobseekerEducationDtl1);

                    jsonObjEducationGraduation.put(WebField.SET_EDUCATIONAL_DTLS.REQUEST_GRADUATION_NAME, strEducationGradName);
                    jsonObjEducationGraduation.put(WebField.SET_EDUCATIONAL_DTLS.REQUEST_GRADUATION_TYPE, strClgGraduatType);
                    jsonObjEducationGraduation.put(WebField.SET_EDUCATIONAL_DTLS.REQUEST_GRADUATION_DATE, strEducationGradDate);

                    jsnArryEducationDtl.put(jsonObjEducationGraduation);
                    Applog.e("jsonArray", " :::: " + jsnArryEducationDtl);

//                jsonObjectRequest.put(WebField.SET_EDUCATIONAL_DTLS.REQUEST_JOBSEEKER_ID, SessionManager.getJobseekerData(this).getJobUserId());
                    jsonObjectRequest.put(WebField.SET_EDUCATIONAL_DTLS.REQUEST_JOBSEEKER_ID, "63");
                    jsonObjectRequest.put("EducationDetail", jsnArryEducationDtl);

                    Applog.e("Education details", ":::::::" + jsonObjectRequest);
                    Applog.e("Api", ":::::::" + WebField.SERV_SET_EDUCATION_DETAIL);


                    new GetJsonWithAndroidNetworkingLib(EditProfileJobSeekerActivity.this, jsonObjectRequest,
                            WebField.SERV_SET_EDUCATION_DETAIL,
                            GlobalData.intFlagShow, new OnUpdateListener() {
                        @Override
                        public void onUpdateComplete(JSONObject jsonObject, boolean isSuccess) {
                            Applog.e("jsonObjectn ", " Education:::" + jsonObject);
                            Applog.e("isSuccess", " Education:::" + isSuccess);
                            if (isSuccess) {

                                try {
                                    if (jsonObject != null) {

                                        String status = "";
                                        String message = "";

                                        Applog.e("SetEducation dtls", ":::::" + jsonObject);

                                        in = new Intent(EditProfileJobSeekerActivity.this, HomeJobseekerActivity.class);
                                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(in);
                                        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
                                        finish();

                                        AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "You have set your employment details.");

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, "Please check your enter filde!!!");
//
                            }
                        }
                    }).execute();

                } else {
                    AlertDialogUtility.SHOW_INTERNET_ALERT(this);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//.............................................Education details data end...........................................................

    //.............................................Profile pic and video uploading data.....................................
    private void checkProfilePic() {

        try {
            if (strProPicBase64 == null || strProPicBase64.isEmpty()) {
                GlobalMethod.showAlert(this, "Please select profile picture to upload");
            } else if (base64Video == null || base64Video.isEmpty()) {
                GlobalMethod.showAlert(this, "Please select upload to video");
            } else {
                //Update Change
//                callImgVideo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callImgVideo() {
        try {

            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                JSONObject jsonObjectInput = new JSONObject();

//Static values
                jsonObjectInput.put(WebField.SET_PHOTO_VDO.REQUEST_JOB_USER_ID, SessionManager.getJobseekerData(EditProfileJobSeekerActivity.this).getJobUserId());
//                jsonObjectInput.put(SET_PHOTO_VDO.REQUEST_JOB_USER_ID, "16");
                jsonObjectInput.put(WebField.SET_PHOTO_VDO.REQUEST_PROF_PIC, strProPicBase64);
                jsonObjectInput.put(WebField.SET_PHOTO_VDO.REQUEST_VIDEO_BASE64, base64Video);
                jsonObjectInput.put(WebField.SET_PHOTO_VDO.REQUEST_PROFILE_VDO_FILE_NM, "");
                jsonObjectInput.put(WebField.SET_PHOTO_VDO.REQUEST_IS_UPLOADING_END, false);

                Applog.e("FIRST TIME:::::::", "" + jsonObjectInput);

                new GetJsonWithAndroidNetworkingLib(this,
                        jsonObjectInput, WebField.SERV_SET_PHOTO_VDO, GlobalData.intFlagShow, new OnUpdateListener() {

                    @Override
                    public void onUpdateComplete(JSONObject jObjResponse, boolean isSuccess) {
                        if (isSuccess) {
                            try {

                                String status = "";
                                String message = "";
                                strUploadingVideo = jObjResponse.getString(WebField.SET_PHOTO_VDO.RESPONSE_PROF_VDO_FILE_NM);
                                jObjResponse.getString(WebField.SET_PHOTO_VDO.RESPONSE_IS_SET_PROFILE);


                                uploadingVideo();  // again api is calling to update remaining base64 file.

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).execute();
            } else {
                AlertDialogUtility.SHOW_INTERNET_ALERT(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadingVideo() {
        // TODO Auto-generated method stub
        JSONObject objectSecondTime = new JSONObject();
        try {
            filePath = "";
            //spliteCount  = chunk values count and convert base64
            if (spliteCount < totalSplite) {
                getBase64(spliteCount);
            }
        } catch (Exception e) {
            e.printStackTrace();// Offset IsUploadingEnd = true
        }

        try {
//Static values
            objectSecondTime.put(WebField.SET_PHOTO_VDO.REQUEST_JOB_USER_ID, SessionManager.getJobseekerData(EditProfileJobSeekerActivity.this).getJobUserId());
//            objectSecondTime.put(SET_PHOTO_VDO.REQUEST_JOB_USER_ID, "16");
            objectSecondTime.put(WebField.SET_PHOTO_VDO.REQUEST_PROF_PIC, "");

            //PostId and ItemId by responce get values
            objectSecondTime.put(WebField.SET_PHOTO_VDO.REQUEST_PROFILE_VDO_FILE_NM, strUploadingVideo);

            if (spliteCount == (totalSplite - 1)) {
                objectSecondTime.put(WebField.SET_PHOTO_VDO.REQUEST_IS_UPLOADING_END, true);
            } else {
                objectSecondTime.put(WebField.SET_PHOTO_VDO.REQUEST_IS_UPLOADING_END, false);
            }

            objectSecondTime.put(WebField.SET_PHOTO_VDO.REQUEST_VIDEO_BASE64, filePath);

            new GetJsonWithAndroidNetworkingLib(this,
                    objectSecondTime, WebField.SERV_SET_PHOTO_VDO, GlobalData.intFlagShow, new OnUpdateListener() {
                @Override
                public void onUpdateComplete(JSONObject jObjResponse, boolean isSuccess) {
                    Applog.e("Response checking...", " ?????" + jObjResponse);

                    if (isSuccess) {

                        if (jObjResponse.has(WebField.STATUS)) {
                            try {
                                strStatus = jObjResponse.getString(WebField.STATUS);

                                if (strStatus.equalsIgnoreCase("1")) {

                                    strStatus = "0";
                                    spliteCount++;
                                    offSet = offSet + (1024 * 1024);

                                    if (spliteCount < totalSplite) {
                                        uploadingVideo();

                                    } else {
                                        AlertDialogUtility.SHOW_TOAST(EditProfileJobSeekerActivity.this, " Post uploaded Successfully");

                                        fileNameArr.clear();
                                        totalSplite = 0;
                                        spliteCount = 0;
                                        filePath = "";
                                        offSet = 0;
                                        base64Video = "";

                                        File f = new File(Environment
                                                .getExternalStorageDirectory()
                                                + "/VideoChunkDemo/"
                                                + "out.mp4");

                                        if (f.exists()) {
                                            f.delete();
                                        }
                                        myClassData.clearCache(EditProfileJobSeekerActivity.this);  // delete chunk file after sucessfully updated to server.
                                    }


                                    imgVwPersnlDtl.setImageResource(R.mipmap.active_son_icon);
                                    imgVwEmplyMntDtl.setImageResource(R.mipmap.professional_icon);
                                    imgVwEducatDtl.setImageResource(R.mipmap.book_icon);

                                    txtVwSkip.setVisibility(View.VISIBLE);

                                    llPersonalDtl.setVisibility(View.GONE);
                                    rlProfilePicVdo.setVisibility(View.GONE);
                                    rlFileUploadMain.setVisibility(View.VISIBLE);
                                    txtVwUploadVdoImgTitle.setVisibility(View.GONE);
                                    txtVwUpldResumeTitle.setVisibility(View.VISIBLE);
                                    btnNextProfilePic.setVisibility(View.GONE);
                                    btnNextFileUpload.setVisibility(View.VISIBLE);

                                    strSkipValues = 3;
                                } else {

                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Video splite
    private void spliteFile(File f) {
        try {
            fileNameArr = fileHandel.splitFileTobase64(f);
            totalSplite = fileNameArr.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialogsVdo() {
        try {
            final Dialog dialog = new Dialog(this);

            dialog.setContentView(R.layout.custom_upload_img_layout);

            txtVwChoosExisting = dialog.findViewById(R.id.txt_choos_existing);
            txtVwTakePic = dialog.findViewById(R.id.txt_take_pic);
            txtVwCancel = dialog.findViewById(R.id.txt_cancel);

            txtVwChoosExisting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("video/*");
                        startActivityForResult(Intent.createChooser(intent, "Complete action using"), EditProfileJobSeekerActivity.REQUEST_GALARY_VIDEO);
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            txtVwTakePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivityForResult(intent, EditProfileJobSeekerActivity.REQUEST_VIDEO_CAPTURE);
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            txtVwCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GALARY_IMG: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, REQUEST_GALARY_IMG);
                }
                return;
            }

            case REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File file = new File(android.os.Environment.getExternalStorageDirectory(), "JobSeeker.jpg");
//                    filePath = file.getAbsolutePath();
//                    Uri photoURI = Uri.fromFile(file);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
                return;
            }
        }
    }

    private void showDialogsImg() {
        try {
            final Dialog dialog = new Dialog(this);

            dialog.setContentView(R.layout.custom_upload_img_layout);

            txtVwChoosExisting = dialog.findViewById(R.id.txt_choos_existing);
            txtVwTakePic = dialog.findViewById(R.id.txt_take_pic);
            txtVwCancel = dialog.findViewById(R.id.txt_cancel);

            txtVwChoosExisting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ActivityCompat.checkSelfPermission(EditProfileJobSeekerActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditProfileJobSeekerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_GALARY_IMG);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_GALARY_IMG);
                    }
                    dialog.dismiss();
                }
            });

            txtVwTakePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    camera();
                    dialog.dismiss();
                }
            });

            txtVwCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//.............................................Profile pic and video uploading end...........................................................

    //.............................................Other background working data...........................................................
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Applog.e("Request code ", "::; " + requestCode);
        Applog.e("Result code ", ":::; " + resultCode);

        if (requestCode == REQUEST_GALARY_IMG) {
            if (resultCode == Activity.RESULT_OK) {
                mImageUri = data.getData();
                try {
                    bitmap = getBitmapFromUri(mImageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imgVwUploadPic.setImageBitmap(bitmap);

                strProPicBase64 = EditProfileJobSeekerActivity.encodeTobase64(bitmap);
//                try {
////                    Uri selectedImage = data.getData();
////                    performCrop(selectedImage);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }

        } else if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imgVwUploadPic.setImageBitmap(photo);

                    strProPicBase64 = encodeTobase64(photo);

                } catch (Exception e) {
                    e.printStackTrace();
                }

//                try {
//                    ExifInterface exif = new ExifInterface(filePath);
//                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//                    int angle = 0;
//                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
//                        angle = 90;
//                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
//                        angle = 180;
//                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
//                        angle = 270;
//                    }
//                    Matrix mat = new Matrix();
//                    mat.postRotate(angle);
//
//                    Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(filePath), null, null);
//                    Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
//                    performCrop(Uri.parse("file://" + filePath));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        } else if (requestCode == PIC_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    ImageUtil imgUtil = new ImageUtil();
                    File f = new File(imgUtil.makeDir(this), "JobSeeker.png");
                    if (f.exists()) {
                        filePath = f.getAbsolutePath();
                        Bitmap thePic;
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        thePic = BitmapFactory.decodeFile(filePath, bitmapOptions);
                        bitmap = thePic;
                        imgVwUploadPic.setImageBitmap(bitmap);
                        strProPicBase64 = encodeTobase64(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == EditProfileJobSeekerActivity.REQUEST_GALARY_VIDEO) {
            if (resultCode == Activity.RESULT_OK) {

                Uri selectedImageUri = data.getData();

                String selectedImagePath = getPath(selectedImageUri);  // get video path from image uri

                base64Video = sendImagesAndVideos(selectedImagePath);

                File f = new File(selectedImagePath);

                spliteFile(f);
//Update Change
//                callImgVideo();

            }
        } else if (requestCode == EditProfileJobSeekerActivity.REQUEST_VIDEO_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
//                AlertDialogUtility.SHOW_TOAST(this, "Video saved to:\n" + data.getData());

                Uri vid = data.getData();
                String videoPath = getPath(vid);

                base64Video = sendImagesAndVideos(videoPath);

                File f = new File(videoPath);

                spliteFile(f);
//Update Change
//                callImgVideo();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                AlertDialogUtility.SHOW_TOAST(this, "Video recording cancelled.");
            }
        } else if (requestCode == EditProfileJobSeekerActivity.PICK_FILE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (data == null) {
                        //no data present
                        return;
                    }

                    PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, EditProfileJobSeekerActivity.TAG);
                    wakeLock.acquire();

                    Uri selectedFileUri = data.getData();
                    strselectedResumeFilePath = UploadResumeFilePath.getPath(this, selectedFileUri);
                    Applog.i(EditProfileJobSeekerActivity.TAG, "Selected File Path:" + strselectedResumeFilePath);

                    strResumeBase64 = encodeResumeTobase64(strselectedResumeFilePath);
                    Applog.e("strResumeBase64", "::::::::" + strResumeBase64);

                    if (strselectedResumeFilePath != null && !strselectedResumeFilePath.equals("")) {
                        txtVwUploadFile.setText(strselectedResumeFilePath);
                    } else {
                        AlertDialogUtility.SHOW_TOAST(this, "Cannot upload file to server");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode != EditProfileJobSeekerActivity.MY_PERMISSIONS_UPLOAD_RESUME_REQUEST) {
                //on attachment icon click

                showResumeFileChooser();
            }

        }

    }

    private void performCrop(Uri selectedImage) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(selectedImage, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            DisplayMetrics matrix = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(matrix);
            int x = matrix.widthPixels;
            cropIntent.putExtra("outputX", 500);
            cropIntent.putExtra("outputY", 500);
            File f = null;
            ImageUtil imgUtil = new ImageUtil();
            f = new File(imgUtil.makeDir(this), "JobSeeker.png");
            Uri photoURI = Uri.fromFile(f);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            cropIntent.putExtra("output", photoURI);
            cropIntent.putExtra("return-data", false);
            startActivityForResult(cropIntent, PIC_CROP);

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            AlertDialogUtility.showAlert(this, errorMessage);
        }
    }

    private String encodeResumeTobase64(String strselectedResumeFilePath) {

        byte[] byteArray = null;
        try {

            InputStream inputStream = new FileInputStream(strselectedResumeFilePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 11];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String resumeEncoded = encodeToString(byteArray, Base64.DEFAULT);
        return resumeEncoded;

//        return encodeToString(byteArray, Base64.NO_WRAP);
    }

    //Video uploading
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private String getBase64(int index) {
        String base64_ = null;
        try {
            File f = new File(myClassData.makeDir(this), fileNameArr.get(index).toString());
            filePath = fileHandel.getBase64FromByteArray(fileHandel.convertFileToByteArray(f));
//            Log.e("File Path is ", "====>" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64_;
    }

    private String sendImagesAndVideos(String selectedImagePath) {
        Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(selectedImagePath,
                MediaStore.Video.Thumbnails.MICRO_KIND);
        return encodeVdoTobase64(bmThumbnail);
    }

    private String encodeVdoTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    //Image uploading
    private void camera() {

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                File file = new File(android.os.Environment.getExternalStorageDirectory(), "JobSeeker.jpg");
//                filePath = file.getAbsolutePath();
//                Uri photoURI = Uri.fromFile(file);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Image and video image convert base64
    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    //Checkpermissions................................................................
    private void checkPermissions() {
        try {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.GET_ACCOUNTS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.GET_ACCOUNTS},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            } else {
                //            SessionManager.setLocationService(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
