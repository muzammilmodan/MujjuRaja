package com.sample.mobile.sampleapp.activity;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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
import com.jobseekerapp.Adapters.JobseekerPrevEmploymentAdapter;
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
import com.jobseekerapp.Dao.SkillDao;
import com.jobseekerapp.Dao.StateDao;
import com.jobseekerapp.Global.GlobalMethod;
import com.jobseekerapp.Model.JobseekerDataMdl;
import com.jobseekerapp.Model.JobseekerEducationDtlModel;
import com.jobseekerapp.Model.JobseekerPrevEmploymentModel;
import com.jobseekerapp.Model.SpinnerModel;
import com.jobseekerapp.R;
import com.jobseekerapp.R.anim;
import com.jobseekerapp.R.color;
import com.jobseekerapp.R.id;
import com.jobseekerapp.R.layout;
import com.jobseekerapp.R.mipmap;
import com.jobseekerapp.WebService.GetJsonWithAndroidNetworkingLib;
import com.jobseekerapp.WebService.OnUpdateListener;
import com.jobseekerapp.WebService.WebField;
import com.jobseekerapp.WebService.WebField.SET_PERSONAL_DETAIL;
import com.jobseekerapp.WebService.WebField.SET_PHOTO_VDO;
import com.jobseekerapp.WebService.WebField.SET_RESUME;

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

public class SignUpJobseekerDetailsActivity extends Activity implements OnClickListener {

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
    private Spinner spnrPersnlDtlCity, spnrLakhs, spnrThousand, spnrNoticePeriod, spnrGender, spnrEthnicCity;

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
    private static final String TAG = SignUpJobseekerDetailsActivity.class.getSimpleName();
    WakeLock wakeLock;
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
    public static ArrayList<JobseekerPrevEmploymentModel> alprevEmplymentDetail;
    private LinearLayoutManager prevEmplymentLinearLayoutManager;
    private JobseekerPrevEmploymentAdapter prevEmplymntAdapter;

    //Education details
    private TextView txtVwGraduDte, txtVwHighScGraduDte;
    private Button btnNextEducationDtl;
    private EditText edtVwHighSclGraduation, edtVwClgGraduat;
    private RelativeLayout rlHighScGraduDate, rlClgGraduatDate;
    private ImageView imgVwHighScGraduDate, imgVwClgGraduatDate;
    public ArrayList<JobseekerEducationDtlModel> alEducationDetail;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_jobseeker_details);


        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(color.app_bg));
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        strUserType = JobseekerUserType;

        getIds();
        setListners();
        setFonts();

        SetSpinnerGender("");
        SetSpinnerEthnCity("");
        SetSpinnerLakhs();
        SetSpinnerThousand();
        SetSpinnerNoticePeriod();
        SetPrevEmployment();

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
            rlMain = (RelativeLayout) findViewById(R.id.rlMain);
            rlScrollMain = (RelativeLayout) findViewById(R.id.rlScrollMain);
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
            imgVwPersnlDtl.setImageResource(mipmap.active_son_icon);
            imgVwEmplyMntDtl.setImageResource(mipmap.professional_icon);
            imgVwEducatDtl.setImageResource(mipmap.book_icon);

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
            case id.txtVwSkip:

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

                    imgVwPersnlDtl.setImageResource(mipmap.active_son_icon);
                    imgVwEmplyMntDtl.setImageResource(mipmap.professional_icon);
                    imgVwEducatDtl.setImageResource(mipmap.book_icon);

                    strSkipValues = 1;
                } else if (strSkipValues == 1) {
                    //Personal dtl gone, Profile dtl Visible
                    imgVwPersnlDtl.setImageResource(mipmap.active_son_icon);
                    imgVwEmplyMntDtl.setImageResource(mipmap.professional_icon);
                    imgVwEducatDtl.setImageResource(mipmap.book_icon);

                    llPersonalDtl.setVisibility(View.GONE);
                    rlProfilePicVdo.setVisibility(View.VISIBLE);

                    txtVwPersnalTitle.setVisibility(View.GONE);
                    txtVwUploadVdoImgTitle.setVisibility(View.VISIBLE);

                    strSkipValues = 2;
                } else if (strSkipValues == 2) {
                    imgVwPersnlDtl.setImageResource(mipmap.active_son_icon);
                    imgVwEmplyMntDtl.setImageResource(mipmap.professional_icon);
                    imgVwEducatDtl.setImageResource(mipmap.book_icon);

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
                    imgVwPersnlDtl.setImageResource(mipmap.son_icon);
                    imgVwEmplyMntDtl.setImageResource(mipmap.active_professional_icon);
                    imgVwEducatDtl.setImageResource(mipmap.book_icon);

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
                    imgVwPersnlDtl.setImageResource(mipmap.son_icon);
                    imgVwEmplyMntDtl.setImageResource(mipmap.professional_icon);
                    imgVwEducatDtl.setImageResource(mipmap.active_book_icon);

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

                    getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    in = new Intent(this, HomeJobseekerActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    overridePendingTransition(anim.trans_right_in, anim.trans_left_out);
                }
                break;

            case id.imgVwPersnlDtl:
                KeyboardUtility.HideKeyboard(this, imgVwPersnlDtl);

                imgVwPersnlDtl.setImageResource(mipmap.active_son_icon);
                imgVwEmplyMntDtl.setImageResource(mipmap.professional_icon);
                imgVwEducatDtl.setImageResource(mipmap.book_icon);

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
            case id.rlPersnlDtl:
                KeyboardUtility.HideKeyboard(this, rlPersnlDtl);
                imgVwPersnlDtl.setImageResource(mipmap.active_son_icon);
                imgVwEmplyMntDtl.setImageResource(mipmap.professional_icon);
                imgVwEducatDtl.setImageResource(mipmap.book_icon);

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

            case id.imgVwEmplyMntDtl:
                addSkill();
//                getSkillList();
                KeyboardUtility.HideKeyboard(this, imgVwEmplyMntDtl);
                imgVwPersnlDtl.setImageResource(mipmap.son_icon);
                imgVwEmplyMntDtl.setImageResource(mipmap.active_professional_icon);
                imgVwEducatDtl.setImageResource(mipmap.book_icon);

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

            case id.rlEmplyMntDtl:
//                getSkillList();
                KeyboardUtility.HideKeyboard(this, rlEmplyMntDtl);
                imgVwPersnlDtl.setImageResource(mipmap.son_icon);
                imgVwEmplyMntDtl.setImageResource(mipmap.active_professional_icon);
                imgVwEducatDtl.setImageResource(mipmap.book_icon);

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

            case id.imgVwEducatDtl:
                KeyboardUtility.HideKeyboard(this, imgVwEducatDtl);
                imgVwPersnlDtl.setImageResource(mipmap.son_icon);
                imgVwEmplyMntDtl.setImageResource(mipmap.professional_icon);
                imgVwEducatDtl.setImageResource(mipmap.active_book_icon);

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
            case id.rlEducatDtl:
                KeyboardUtility.HideKeyboard(this, rlEducatDtl);
                imgVwPersnlDtl.setImageResource(mipmap.son_icon);
                imgVwEmplyMntDtl.setImageResource(mipmap.professional_icon);
                imgVwEducatDtl.setImageResource(mipmap.active_book_icon);

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
            case id.rlMain:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
                break;


            case id.rlScrollMain:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlScrollMain);
                break;
            case id.ivBack:
                finish();
                break;

            case R.id.txtVwPersnlState:
                showDlgsPersnlGetState();
                break;

//NextButton Clicked  .........................................
            case id.btnNextPersnlDtl:
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

            case id.btnNextProfilePic:
                checkProfilePic();
                break;

            case id.btnNextFileUpload:
                checkUploadResume();
                break;

            case id.btnNextEmployDetail:
                checkEmployeDetails();
                break;

            case id.btnNextEducationDtl:
                checkEducationDetails();
                break;
//*************************End***********************************

            case id.rlDateOfBrth:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
                getBirthDate();
                break;
            case id.imgVwDateOfBrth:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
                getBirthDate();
                break;

//Profile pic
            case id.imgVwUploadPic:
                checkPermissions();
                showDialogsImg();
                break;
            case id.imgProfVdo:
                checkPermissions();
                showDialogsVdo();
                break;


            case id.imgVwUploadFile:
                checkUploadResumePermissions();
                showResumeFileChooser();
                break;

            //Employment details
            case id.rlVwWrkgSncTo:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
                getWorkSincTo();
                break;
            case id.rlWrkngSncFm:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
                getWorkSincFrom();
                break;
            case id.imgVwWrkgSncTo:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
                getWorkSincTo();
                break;
            case id.imgVwWrkgSncFrom:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
                getWorkSincFrom();
                break;

            case id.txtVwEmplyCity:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
                showDialogsEmployerGetCity();
                break;

            //Custom prev employemnt
            case id.rlAddMoreExp:
                try {
                    Applog.e("Add more", ":::::::::::");
                    int position = 0;
                    alprevEmplymentDetail.add(new JobseekerPrevEmploymentModel());
                    prevEmplymntAdapter.notifyDataSetChanged();
                    recyclrPrevEmplymnt.scrollToPosition(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            //Education details
            case id.imgVwHighScGraduDate:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
                getHighSclGradDate();
                break;
            case id.rlHighScGraduDate:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
                getHighSclGradDate();
                break;
            case id.imgVwClgGraduatDate:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
                getClgGradDate();
                break;
            case id.rlClgGraduatDate:
                KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
                getClgGradDate();
                break;
        }
    }

//.............................................Simple details data end...........................................................


//.............................................Employment details data.....................................

    //Employment GetCountry, GetState, GetCity ...............................
    private void showDialogsEmployerGetCity() {
        try {
            final Dialog dialog = new Dialog(SignUpJobseekerDetailsActivity.this);
            dialog.setContentView(R.layout.custom_get_country_state_city);

            RelativeLayout rlEmplymentState = dialog.findViewById(R.id.rlEmplymentState);
            RelativeLayout rlEmplymentCity = dialog.findViewById(R.id.rlEmplymentCity);
            RelativeLayout rlEmplymentCountry = dialog.findViewById(R.id.rlEmplymentCountry);
            TextView txtVwSaveCity = dialog.findViewById(R.id.txtVwSaveCity);

            spnrEmplymentState = dialog.findViewById(id.spnrEmplymentState);
            spnrEmplymentCity = dialog.findViewById(id.spnrEmplymentCity);
            spnrEmplymentCountry = dialog.findViewById(id.spnrEmplymentCountry);

//            if (countryList != null) {
            getCountryList();
//            }
            rlEmplymentCountry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    spnrEmplymentCountry.performClick();
                }
            });

            rlEmplymentState.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    spnrEmplymentState.performClick();
                }
            });
//
            rlEmplymentCity.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    spnrEmplymentCity.performClick();
                }
            });

            txtVwSaveCity.setOnClickListener(new OnClickListener() {
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
            pDialog = new ProgressDialog(SignUpJobseekerDetailsActivity.this);
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

//                    CountryDao.DataBean.CountryListBean countryListBean = new CountryDao.DataBean.CountryListBean();
//
//                    countryListBean.getCountryId();
//                    countryListBean.getCountryCode();
//                    countryListBean.getCountryISOCode();
//                    countryListBean.getCountrySTDCode();
//                    countryListBean.getCountryName();
//
//                    Applog.e(" county Data check 1","::" +countryDao.getData().getCountryList());
//                    Applog.e(" county Data check 2","::" +countryListBean.getCountryId());
//                    Applog.e(" county Data check 3","::" +countryListBean.getCountryName());
//
//                    SessionManager.saveCountry(SignUpJobseekerDetailsActivity.this, countryListBean);

                } else {
                    Applog.e(SignUpJobseekerDetailsActivity.TAG, "Couldn't get json from server.");
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

    private void SetSpinnerEmplyrCountry() {
        try {

            ArrayAdapter<CountryDao.DataBean.CountryListBean> adapter = new
                    ArrayAdapter<>(SignUpJobseekerDetailsActivity.this, layout.custom_country_data, id.txtVwCountryName,
                    countryList);
            spnrEmplymentCountry.setAdapter(adapter);

            spnrEmplymentCountry.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    emplyerCountryId = countryList.get(position).getCountryId();
                    getStateList(countryList.get(position).getCountryId());

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            for (int i = 0; i < countryList.size(); i++) {
                if (countryList.get(i).getCountryId() == (emplyerCountryId)) {
                    spnrEmplymentCountry.setSelection(i);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
//                            AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, "Your set profile not update");
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

    private void SetSpinnerEmplyrState() {
        try {
            ArrayAdapter<StateDao.DataBean.StateListBean> adapter = new
                    ArrayAdapter<>(SignUpJobseekerDetailsActivity.this, layout.custom_country_data, id.txtVwCountryName, stateList);
            spnrEmplymentState.setAdapter(adapter);

            spnrEmplymentState.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
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
//                            AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, "Your set profile not update");
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

    private void SetSpinnerEmplyrCity() {
        try {
            ArrayAdapter<CityDao.DataBean.CityListBean> adapter = new
                    ArrayAdapter<>(SignUpJobseekerDetailsActivity.this, R.layout.custom_country_data, id.txtVwCountryName, cityList);
            spnrEmplymentCity.setAdapter(adapter);

            spnrEmplymentCity.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
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
            pDialog = new ProgressDialog(SignUpJobseekerDetailsActivity.this);
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
                Applog.e(SignUpJobseekerDetailsActivity.TAG, "Response from skill url : " + jsonStr);

                if (jsonStr != null) {

                    SkillDao skillDao = new Gson().fromJson(jsonStr.toString(), SkillDao.class);
                    skillList.addAll(skillDao.getData().getSkillsList());
                } else {
                    Applog.e(SignUpJobseekerDetailsActivity.TAG, "Couldn't get json from server.");
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
            autoCmpltTxtVwSkill.setTextColor(getResources().getColor(color.edt_vw_text));
            autoCmpltTxtVwSkill.setDropDownBackgroundDrawable(new ColorDrawable(getResources().getColor(color.title_bg)));


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

    private void SetPrevEmployment() {
        try {
            alprevEmplymentDetail = new ArrayList<>();
            alprevEmplymentDetail.add(new JobseekerPrevEmploymentModel());
            prevEmplymentLinearLayoutManager = new LinearLayoutManager(this);
            recyclrPrevEmplymnt.setLayoutManager(prevEmplymentLinearLayoutManager);


            prevEmplymntAdapter = new JobseekerPrevEmploymentAdapter(this, alprevEmplymentDetail);
            recyclrPrevEmplymnt.setAdapter(prevEmplymntAdapter);


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

            spnrLakhs.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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

            spnrThousand.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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

            spnrNoticePeriod.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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
                    new OnDateSetListener() {
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
                    new OnDateSetListener() {
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

    private void checkEmployeDetails() {
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

                String strNoticePeriodDays = null;
                if (strNoticePeriod.equals("15 Day")) {
                    strNoticePeriodDays = "15";
                } else if (strNoticePeriod.equals("1 Month")) {
                    strNoticePeriodDays = "30";
                } else if (strNoticePeriod.equals("2 Month")) {
                    strNoticePeriodDays = "60";
                } else if (strNoticePeriod.equals("3 Month")) {
                    strNoticePeriodDays = "90";
                } else if (strNoticePeriod.equals(">3 Month")) {
                    strNoticePeriodDays = ">90";
                }

                callEmployeDetails(strNoticePeriodDays);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callEmployeDetails(String strNoticePeriodDays) {
        try {
            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                JSONObject jsonObjectRequest = new JSONObject();
                JSONObject jsnObjCurrntEmpDtl = new JSONObject();
                JSONArray jsnArryPrevEmpData = new JSONArray();

                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_DESIGNATION, strCurrentDesign);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_COMPANY_NAME, strCurrentCompny);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_ANNUAL_SLRY_LACS, emplyerLac);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_ANNUAL_SLRY_THOUSND, emplyerThousand);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_WORK_START_DATE, strFrom);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_WORK_END_DATE, strTo);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_COUNTRY_ID, emplyerCountryId);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_STATE_ID, emplyerStateId);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_CITY_ID, emplyerCityId);
                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_NOTICE_PERIOD, strNoticePeriodDays);

                jsnObjCurrntEmpDtl.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_SKILLS, "");

                for (int i = 0; i < alprevEmplymentDetail.size(); i++) {
                    JSONObject jsonObjPrevEmplDetails = new JSONObject();

                    strPrevEmplyrCmpnyDesig = alprevEmplymentDetail.get(i).getPrevEmplyDesignation();
                    strPrevEmplyrCmpnyNm = alprevEmplymentDetail.get(i).getPrevEmplyCompnyName();
                    strPrevEmplyrCmpnyFrom = alprevEmplymentDetail.get(i).getPrevEmploymentFrom();
                    strPrevEmplyrCmpnyTo = alprevEmplymentDetail.get(i).getPrevEmploymentTo();

                    jsonObjPrevEmplDetails.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_PREV_DESIGNATION, strPrevEmplyrCmpnyDesig);
                    jsonObjPrevEmplDetails.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_PREV_COMPANY_NAME, strPrevEmplyrCmpnyNm);
                    jsonObjPrevEmplDetails.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_PREV_FROM_DATE, strPrevEmplyrCmpnyFrom);
                    jsonObjPrevEmplDetails.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_PREV_TO_DATE, strPrevEmplyrCmpnyTo);

                    jsnArryPrevEmpData.put(jsonObjPrevEmplDetails);
                }

//                jsonObjectRequest.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_JOBSEEKER_ID, SessionManager.getJobseekerData(this).getJobUserId());
                jsonObjectRequest.put(WebField.SET_EMPLOYMENT_DETAILS.REQUEST_JOBSEEKER_ID, "63");
                jsonObjectRequest.put("CurrentEmploymentDetails", jsnObjCurrntEmpDtl);
                jsonObjectRequest.put("PreviousEmploymentDetails", jsnArryPrevEmpData);

                Applog.e("Emplyment details", ":::::::" + jsonObjectRequest);

                Applog.e("Api", ":::::::" + WebField.SERV_SET_EMPLOYMENT_DETAIL);
                new GetJsonWithAndroidNetworkingLib(SignUpJobseekerDetailsActivity.this, jsonObjectRequest,
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

                                    AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, "You have set your employment details.");

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, "Please check your enter filde!!!");
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
    }

//.............................................Employment details data end...........................................................


//.............................................Personal details data.....................................

    private void SetSpinnerEthnCity(String sEthnCity) {
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

            spnrEthnicCity.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
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

    private void SetSpinnerGender(String sGender) {
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

            spnrGender.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
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
                callSetPersonalDetail();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callSetPersonalDetail() {
        try {
            if (ConnectivityDetector.IS_INTERNET_AVAILABLE(this)) {
                JSONObject jsonObjectInput = new JSONObject();

                jsonObjectInput.put(SET_PERSONAL_DETAIL.REQUEST_FIRST_NAME, strFirstNm);
                jsonObjectInput.put(SET_PERSONAL_DETAIL.REQUEST_LAST_NAME, strLastNm);
                jsonObjectInput.put(SET_PERSONAL_DETAIL.REQUEST_GENDER, strGender);
                jsonObjectInput.put(SET_PERSONAL_DETAIL.REQUEST_DATE_OF_BRTH, strDateOfBrth);
                jsonObjectInput.put(SET_PERSONAL_DETAIL.REQUEST_MOBILE_NO, strContactNo);
                jsonObjectInput.put(SET_PERSONAL_DETAIL.REQUEST_ETHINI_CITY, strEthaniCity);
                jsonObjectInput.put(SET_PERSONAL_DETAIL.REQUEST_CITY, strPersnlDtlCityName);
                jsonObjectInput.put(SET_PERSONAL_DETAIL.REQUEST_STATE, strPersnlDtlStateName);
                jsonObjectInput.put(SET_PERSONAL_DETAIL.REQUEST_PINCODE, strZipCode);
                jsonObjectInput.put(SET_PERSONAL_DETAIL.REQUEST_JOB_USER_ID, SessionManager.getJobseekerData(this).getJobUserId());
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
                                    userData.setstrIsSetProfile(jsonObject.getString(SET_PERSONAL_DETAIL.RESPONSE_IS_SET_PROF));
                                    userData.setJobUserId(SessionManager.getJobseekerData(SignUpJobseekerDetailsActivity.this).getJobUserId());
                                    userData.setJobEmailId(SessionManager.getJobseekerData(SignUpJobseekerDetailsActivity.this).getJobEmailId());

                                    String status = "";
                                    String message = jsonObject.getString(WebField.MESSAGE);

                                    SessionManager.saveJobseekerData(SignUpJobseekerDetailsActivity.this, userData);

                                    AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, message);

                                    rlProfilePicVdo.setVisibility(View.VISIBLE);
                                    rlFileUploadMain.setVisibility(View.GONE);

                                    btnNextPersnlDtl.setVisibility(View.GONE);
                                    btnNextProfilePic.setVisibility(View.VISIBLE);

                                    txtVwUpldResumeTitle.setVisibility(View.GONE);
                                    txtVwPersnalTitle.setVisibility(View.GONE);
                                    txtVwUploadVdoImgTitle.setVisibility(View.VISIBLE);

                                    llPersonalDtl.setVisibility(View.GONE);
                                    txtVwSkip.setVisibility(View.VISIBLE);

                                    imgVwPersnlDtl.setImageResource(mipmap.active_son_icon);
                                    imgVwEmplyMntDtl.setImageResource(mipmap.professional_icon);
                                    imgVwEducatDtl.setImageResource(mipmap.book_icon);

                                    strSkipValues = 1;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, "Your set profile not update");
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
            final Dialog dialog = new Dialog(SignUpJobseekerDetailsActivity.this);
            dialog.setContentView(R.layout.custom_get_persnl_details_state);

            RelativeLayout rlPersnlDetailCountry = dialog.findViewById(id.rlPersnlDetailCountry);
            RelativeLayout rlPersnlDetailState = dialog.findViewById(R.id.rlPersnlDetailState);

            TextView txtVwSaveState = dialog.findViewById(R.id.txtVwSaveState);

            spnrPersnlDtlCountry = dialog.findViewById(R.id.spnrPersnlDetailCountry);
            spnrPersnlDetailState = dialog.findViewById(R.id.spnrPersnlDetailState);

            getPersonalDtlCountryList();

            rlPersnlDetailCountry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    spnrPersnlDtlCountry.performClick();
                }
            });
            rlPersnlDetailState.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    spnrPersnlDetailState.performClick();
                }
            });

            txtVwSaveState.setOnClickListener(new OnClickListener() {
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
            pDialog = new ProgressDialog(SignUpJobseekerDetailsActivity.this);
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
                Applog.e(SignUpJobseekerDetailsActivity.TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    Applog.e("Country length ", " :::: " + jsonStr.length());
                    CountryDao countryDao = new Gson().fromJson(jsonStr.toString(), CountryDao.class);
                    countryList.addAll(countryDao.getData().getCountryList());

                    Applog.e("Country Size ", " :::: " + countryList.size());

                } else {
                    Applog.e(SignUpJobseekerDetailsActivity.TAG, "Couldn't get json from server.");
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
                    ArrayAdapter<>(SignUpJobseekerDetailsActivity.this, R.layout.custom_country_data, id.txtVwCountryName, countryList);
            spnrPersnlDtlCountry.setAdapter(adapter);

            spnrPersnlDtlCountry.setOnItemSelectedListener(new OnItemSelectedListener() {
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
//                            AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, "Your set profile not update");
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
                    ArrayAdapter<>(SignUpJobseekerDetailsActivity.this, R.layout.custom_country_data, id.txtVwCountryName, stateList);
            spnrPersnlDetailState.setAdapter(adapter);

            spnrPersnlDetailState.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    KeyboardUtility.HideKeyboard(SignUpJobseekerDetailsActivity.this, rlMain);
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
//                            AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, "Your set profile not update");
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
                    ArrayAdapter<>(SignUpJobseekerDetailsActivity.this, R.layout.custom_country_data,
                    R.id.txtVwPersnlDtlCityName, cityList);
            spnrPersnlDtlCity.setAdapter(adapter);

            spnrPersnlDtlCity.setOnItemSelectedListener(new OnItemSelectedListener() {
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
                permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission.GET_ACCOUNTS, permission.WRITE_EXTERNAL_STORAGE,
                            permission.READ_EXTERNAL_STORAGE}, SignUpJobseekerDetailsActivity.MY_PERMISSIONS_UPLOAD_RESUME_REQUEST);
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
                CallUploadResumeApi();
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
                        jsonObjectInput.put(SET_RESUME.REQUEST_FILE_TYPE, ".DOCX");
                    } else if (strselectedResumeFilePath.endsWith(".DOC")) {
                        jsonObjectInput.put(SET_RESUME.REQUEST_FILE_TYPE, ".DOCX");
                    } else if (strselectedResumeFilePath.endsWith(".docx")) {
                        jsonObjectInput.put(SET_RESUME.REQUEST_FILE_TYPE, ".DOCX");
                    } else if (strselectedResumeFilePath.endsWith(".DOCX")) {
                        jsonObjectInput.put(SET_RESUME.REQUEST_FILE_TYPE, ".DOCX");
                    } else if (strselectedResumeFilePath.endsWith(".PDF")) {
                        jsonObjectInput.put(SET_RESUME.REQUEST_FILE_TYPE, ".PDF");
                    } else if (strselectedResumeFilePath.endsWith(".pdf")) {
                        jsonObjectInput.put(SET_RESUME.REQUEST_FILE_TYPE, ".PDF");
                    }
                }

                jsonObjectInput.put(SET_RESUME.REQUEST_USER_ID, SessionManager.getJobseekerData(this).getJobUserId());
//                jsonObjectInput.put(SET_RESUME.REQUEST_USER_ID, "63");
                jsonObjectInput.put(SET_RESUME.REQUEST_RESUME_BASE64, strResumeBase64);

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
                                    jsonObject.getString(SET_RESUME.RESPONSE_IS_SET_PROF);

                                    imgVwPersnlDtl.setImageResource(mipmap.son_icon);
                                    imgVwEmplyMntDtl.setImageResource(mipmap.active_professional_icon);
                                    imgVwEducatDtl.setImageResource(mipmap.book_icon);

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

                                    AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, "You have set your resume.");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, "You have entered wrong credentials.");
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
            startActivityForResult(intentPickDocument, SignUpJobseekerDetailsActivity.PICK_FILE_REQUEST);
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
                    new OnDateSetListener() {
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
                    new OnDateSetListener() {
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
                    new OnDateSetListener() {
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
                callEducationDetails();
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


                    new GetJsonWithAndroidNetworkingLib(SignUpJobseekerDetailsActivity.this, jsonObjectRequest,
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

                                        in = new Intent(SignUpJobseekerDetailsActivity.this, HomeJobseekerActivity.class);
                                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(in);
                                        overridePendingTransition(anim.trans_right_in, anim.trans_left_out);
                                        finish();

                                        AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, "You have set your employment details.");

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, "Please check your enter filde!!!");
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
                callImgVideo();
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
                jsonObjectInput.put(WebField.SET_PHOTO_VDO.REQUEST_JOB_USER_ID, SessionManager.getJobseekerData(SignUpJobseekerDetailsActivity.this).getJobUserId());
//                jsonObjectInput.put(SET_PHOTO_VDO.REQUEST_JOB_USER_ID, "16");
                jsonObjectInput.put(SET_PHOTO_VDO.REQUEST_PROF_PIC, strProPicBase64);
                jsonObjectInput.put(SET_PHOTO_VDO.REQUEST_VIDEO_BASE64, base64Video);
                jsonObjectInput.put(SET_PHOTO_VDO.REQUEST_PROFILE_VDO_FILE_NM, "");
                jsonObjectInput.put(SET_PHOTO_VDO.REQUEST_IS_UPLOADING_END, false);

                Applog.e("FIRST TIME:::::::", "" + jsonObjectInput);

                new GetJsonWithAndroidNetworkingLib(this,
                        jsonObjectInput, WebField.SERV_SET_PHOTO_VDO, GlobalData.intFlagShow, new OnUpdateListener() {

                    @Override
                    public void onUpdateComplete(JSONObject jObjResponse, boolean isSuccess) {
                        if (isSuccess) {
                            try {

                                String status = "";
                                String message = "";
                                strUploadingVideo = jObjResponse.getString(SET_PHOTO_VDO.RESPONSE_PROF_VDO_FILE_NM);
                                jObjResponse.getString(SET_PHOTO_VDO.RESPONSE_IS_SET_PROFILE);

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
            objectSecondTime.put(WebField.SET_PHOTO_VDO.REQUEST_JOB_USER_ID, SessionManager.getJobseekerData(SignUpJobseekerDetailsActivity.this).getJobUserId());
//            objectSecondTime.put(SET_PHOTO_VDO.REQUEST_JOB_USER_ID, "16");
            objectSecondTime.put(SET_PHOTO_VDO.REQUEST_PROF_PIC, "");

            //PostId and ItemId by responce get values
            objectSecondTime.put(SET_PHOTO_VDO.REQUEST_PROFILE_VDO_FILE_NM, strUploadingVideo);

            if (spliteCount == (totalSplite - 1)) {
                objectSecondTime.put(SET_PHOTO_VDO.REQUEST_IS_UPLOADING_END, true);
            } else {
                objectSecondTime.put(SET_PHOTO_VDO.REQUEST_IS_UPLOADING_END, false);
            }

            objectSecondTime.put(SET_PHOTO_VDO.REQUEST_VIDEO_BASE64, filePath);

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
                                        AlertDialogUtility.SHOW_TOAST(SignUpJobseekerDetailsActivity.this, " Post uploaded Successfully");

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
                                        myClassData.clearCache(SignUpJobseekerDetailsActivity.this);  // delete chunk file after sucessfully updated to server.
                                    }


                                    imgVwPersnlDtl.setImageResource(mipmap.active_son_icon);
                                    imgVwEmplyMntDtl.setImageResource(mipmap.professional_icon);
                                    imgVwEducatDtl.setImageResource(mipmap.book_icon);

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

            txtVwChoosExisting = dialog.findViewById(id.txt_choos_existing);
            txtVwTakePic = dialog.findViewById(id.txt_take_pic);
            txtVwCancel = dialog.findViewById(id.txt_cancel);

            txtVwChoosExisting.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent intent = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("video/*");
                        startActivityForResult(Intent.createChooser(intent, "Complete action using"), SignUpJobseekerDetailsActivity.REQUEST_GALARY_VIDEO);
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            txtVwTakePic.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivityForResult(intent, SignUpJobseekerDetailsActivity.REQUEST_VIDEO_CAPTURE);
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            txtVwCancel.setOnClickListener(new OnClickListener() {
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
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GALARY_IMG: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
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

            txtVwChoosExisting = dialog.findViewById(id.txt_choos_existing);
            txtVwTakePic = dialog.findViewById(id.txt_take_pic);
            txtVwCancel = dialog.findViewById(id.txt_cancel);

            txtVwChoosExisting.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ActivityCompat.checkSelfPermission(SignUpJobseekerDetailsActivity.this,
                            permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SignUpJobseekerDetailsActivity.this, new String[]{permission.READ_EXTERNAL_STORAGE,
                                permission.WRITE_EXTERNAL_STORAGE}, REQUEST_GALARY_IMG);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_GALARY_IMG);
                    }
                    dialog.dismiss();
                }
            });

            txtVwTakePic.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    camera();
                    dialog.dismiss();
                }
            });

            txtVwCancel.setOnClickListener(new OnClickListener() {
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

                strProPicBase64 = SignUpJobseekerDetailsActivity.encodeTobase64(bitmap);
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
        } else if (requestCode == SignUpJobseekerDetailsActivity.REQUEST_GALARY_VIDEO) {
            if (resultCode == Activity.RESULT_OK) {

                Uri selectedImageUri = data.getData();

                String selectedImagePath = getPath(selectedImageUri);  // get video path from image uri

                base64Video = sendImagesAndVideos(selectedImagePath);

                File f = new File(selectedImagePath);

                spliteFile(f);

//                callImgVideo();

            }
        } else if (requestCode == SignUpJobseekerDetailsActivity.REQUEST_VIDEO_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
//                AlertDialogUtility.SHOW_TOAST(this, "Video saved to:\n" + data.getData());

                Uri vid = data.getData();
                String videoPath = getPath(vid);

                base64Video = sendImagesAndVideos(videoPath);

                File f = new File(videoPath);

                spliteFile(f);

//                callImgVideo();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                AlertDialogUtility.SHOW_TOAST(this, "Video recording cancelled.");
            }
        } else if (requestCode == SignUpJobseekerDetailsActivity.PICK_FILE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (data == null) {
                        //no data present
                        return;
                    }

                    PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, SignUpJobseekerDetailsActivity.TAG);
                    wakeLock.acquire();

                    Uri selectedFileUri = data.getData();
                    strselectedResumeFilePath = UploadResumeFilePath.getPath(this, selectedFileUri);
                    Applog.i(SignUpJobseekerDetailsActivity.TAG, "Selected File Path:" + strselectedResumeFilePath);

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
            } else if (resultCode != SignUpJobseekerDetailsActivity.MY_PERMISSIONS_UPLOAD_RESUME_REQUEST) {
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
        String[] projection = {Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(Images.Media.DATA);
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
                Thumbnails.MICRO_KIND);
        return encodeVdoTobase64(bmThumbnail);
    }

    private String encodeVdoTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    //Image uploading
    private void camera() {

        try {
            if (ActivityCompat.checkSelfPermission(this, permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permission.CAMERA}, REQUEST_CAMERA);
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
        immagex.compress(CompressFormat.JPEG, 100, baos);
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
                    permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    permission.GET_ACCOUNTS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{permission.WRITE_EXTERNAL_STORAGE, permission.CAMERA, permission.GET_ACCOUNTS},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            } else {
                //            SessionManager.setLocationService(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
