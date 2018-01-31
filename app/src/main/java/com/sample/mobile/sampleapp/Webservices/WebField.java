package com.sample.mobile.sampleapp.Webservices;

public class WebField
{
    public final static String BASE_URL = "http://dev2.ifuturz.com/core/oneideas/";
    public final static String BASE_URL_ASSETS = BASE_URL + "assets/";
    public final static String BASE_URL_WEBSERVICE = BASE_URL + "webservice/";
    public final static String BASE_URL_WEBSERVICE_USER = BASE_URL + "webservice/Users/";
    public final static String BASE_URL_INVOICE_ADVERISE = "http://dev2.ifuturz.com/core/oneideas/assets/uploads/advertisement/";

    public final static String IMAGE_URL_NORMAL = BASE_URL_ASSETS+"uploads/user_management/normal_user/";
    public final static String IMAGE_URL_NORMAL_THUMB = BASE_URL_ASSETS+"uploads/user_management/normal_user/thumb/";
    public final static String IMAGE_FORMATE = ".png";

    public final static String IMAGE_URL_BUSINESS = BASE_URL_ASSETS+"uploads/user_management/business_user/";
    public final static String IMAGE_URL_BUSINESS_THUMB = BASE_URL_ASSETS+"uploads/user_management/business_user/thumb/";

    public final static String COVER_URL_NORMAL = BASE_URL_ASSETS+"uploads/user_management/normal_user/coverimage/";
    public final static String COVER_URL_NORMAL_THUMB = BASE_URL_ASSETS+"uploads/user_management/normal_user/coverimage/thumb/";
    public final static String COVER_FORMATE = ".png";

    public final static String COVER_URL_BUSINESS = BASE_URL_ASSETS+"uploads/user_management/business_user/coverimage/";
    public final static String COVER_URL_BUSINESS_THUMB = BASE_URL_ASSETS+"uploads/user_management/business_user/coverimage/thumb/";

    public final static String BASE_URL_INVOICE_ADVERISE_IMAGE = BASE_URL_INVOICE_ADVERISE+"/thumb/";
    public final static String BASE_URL_INVOICE_ADVERISE_VIDEO = BASE_URL_INVOICE_ADVERISE+"/video_thumb/";



//    http://dev2.ifuturz.com/core/oneideas/assets/uploads/user_management/business_user/thumb/292.png


    /// MODE URLS
    public final static String URL_NORMAL_USER_REGISTER = BASE_URL_WEBSERVICE+"Users/normalUserRegistation";
    public final static String URL_NORMAL_USER_DETAILS = BASE_URL_WEBSERVICE+"Users/getNormalUserDetails";
    public final static String URL_CHECK_IN_LOCATION = BASE_URL_WEBSERVICE+"Location/checkInIntoLocation";
    public final static String URL_CHECK_OUT_FROM_LOCATION = BASE_URL_WEBSERVICE+"Location/checkOutFromLocation";
    public final static String URL_CREATE_POST = BASE_URL_WEBSERVICE+"Post/createPost";
    public final static String URL_CHECKIN_USER_DETAILS = BASE_URL_WEBSERVICE+"Location/checkInUserDetail";
    public final static String URL_CHECKIN_USER_LIST = BASE_URL_WEBSERVICE+"Location/checkedInUsersList";
    public final static String URL_BUSINESS_CATEGORY = BASE_URL_WEBSERVICE_USER+"businessCategoryList";
    public final static String URL_USER_LOGIN = BASE_URL_WEBSERVICE_USER+"login";
    public final static String URL_USER_LOGOUT = BASE_URL_WEBSERVICE_USER+"logout";
    public final static String URL_CHANGE_PASSWORD = BASE_URL_WEBSERVICE_USER+"changePassword";
    public final static String URL_FORGOT_PASSWORD = BASE_URL_WEBSERVICE_USER+"forgotPassword";
    public final static String URL_COUNTRY_LIST = BASE_URL_WEBSERVICE_USER+"countryList";
    public final static String URL_STATE_LIST = BASE_URL_WEBSERVICE_USER+"stateList";
    public final static String URL_CITY_LIST = BASE_URL_WEBSERVICE+"Users/cityList";
    public final static String URL_CANCEL_ITEM = BASE_URL_WEBSERVICE+"Post/cancelItem";
    public final static String URL_BUSINESS_USER_REGISTER = BASE_URL_WEBSERVICE_USER + "businessUserRegistration";
    public final static String URL_GET_POST_LIST = BASE_URL_WEBSERVICE + "Post/getPostListing";
    public final static String URL_GET_LIKES_OF_POST = BASE_URL_WEBSERVICE + "Post/getLikesOfPost";
    public final static String URL_GIVE_LIKE_ON_POST = BASE_URL_WEBSERVICE + "Post/giveLikeOnPost";
    public final static String URL_GET_COMMENTS_OF_POST = BASE_URL_WEBSERVICE + "Post/getCommentsOfPost";
    public final static String URL_GIVE_COMMENTS_ON_POST = BASE_URL_WEBSERVICE + "Post/giveCommentOnPost";
    public final static String URL_GET_SHARE_POST = BASE_URL_WEBSERVICE + "Post/sharePost";
    public final static String URL_GET_DELETE_OWN_POST = BASE_URL_WEBSERVICE + "Post/deletePost";
    public final static String URL_GET_BUSINESS_USER_DETAIL = BASE_URL_WEBSERVICE_USER + "getBusinessUserDetails";
    public final static String URL_UPDATE_BUSINESS_USER_DETAIL = BASE_URL_WEBSERVICE_USER + "updateBusinessUserRegistration";
    public final static String URL_UPDATE_NORMAL_USER_DETAIL = BASE_URL_WEBSERVICE_USER + "updateNormalUserRegistation";
    public final static String URL_GET_INVOICELIST = BASE_URL_WEBSERVICE + "Advertise/GetInvoiceList";
    public final static String URL_RECHARGE = BASE_URL_WEBSERVICE+ "Advertise/rechargeYourWallet";

    public final static String STATUS = "status";
    public final static String MESSAGE = "message";
    public final static String DEVICE_TYPE_ANDROID = "1";
    public final static String LOGIN_TYPE_NORMAL = "0";
    public final static String LOGIN_TYPE_FB = "1";
    public final static String LOGIN_TYPE_GOOGLE = "2";
    public final static String LOCATION_NAME = "locationName";
    public final static String USER_ID = "userId";
    public final static String EMAIL = "email";
    public final static String FIRST_NAME = "firstName";
    public final static String LAST_NAME = "lastName";
    public final static String PASSWORD = "password";
    public final static String POST_ID = "postId";
    public final static String COVER_PIC = "coverPic";
    public final static String POST_LISTING = "postListing";
    public final static String USER_NAME = "userName";
    public final static String POST_TEXT = "postText";
    public final static String POST_TOTAL_LIKES = "postTotalLikes";
    public final static String POST_TOTAL_COMMENTS = "postTotalComments";
    public final static String IS_LIKE = "islike";
    public final static String COMMENT = "comment";
    public final static String POST_ADDED_DATE = "postAddedDate";
    public final static String ADDED_DATE = "addedDate";
    public final static String IMAGE_LIST = "imageList";
    public final static String VIDEO_LIST = "videoList";
    public final static String AUDIO_LIST = "audioList";
    public final static String ITEM_ID = "itemId";
    public final static String SOURCE_URL = "sourceUrl";
    public final static String THUMB = "thumb";
    public final static String LOCATION_ID = "locationId";
    public final static String IS_RECENTLY_CHAT = "isRecentlyChat";
    public final static String GET_LIKES_OF_POST = "getLikesOfPost";
    public final static String GET_COMMENTS_OF_POST = "commentOfPost";
    public final static String COMMENT_TEXT = "commentText";
    public final static String GENDER = "gender";
    public final static String DOB = "DOB";
    public final static String DEVICE_TYPE = "deviceType";
    public final static String DEVICE_TOKEN = "deviceToken";
    public final static String REGISTER_TYPE = "registerType";
    public final static String PROFILE_PIC = "profilePic";
    public final static String POSTWEBURL = "postWebURL";

    public final static class GOOGLE_API
    {
        public final static String URL_WITHOUT_LAT_LONG = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
        public final static String URL_USING_LAT_LONG = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
        public final static String URL_USING_NAME = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
        public final static String URL_LOAD_MORE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=";
        public final static String URL_GET_PHOTO_FROM_REFERENCE = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
        public final static String RESPONSE_RESULT = "results";
        public final static String RESPONSE_RESULT_OBJ = "result";
        public final static String RESPONSE_NAME = "name";
        public final static String RESPONSE_ID = "place_id";
        public final static String RESPONSE_VICINITY = "vicinity";
        public final static String RESPONSE_PHOTOS = "photos";
        public final static String RESPONSE_PHOTO_REFERENCE = "photo_reference";
        public final static String RESPONSE_ICON = "icon";
        public final static String RESPONSE_NEXT_PAGE = "next_page_token";
    }

    public final static class NORMAL_USER_REGISTER
    {
        public final static String RESPONSE_USER_DETAILS = "UserDetail";
    }

    public final static class NORMAL_USER_DETAILS
    {
        public final static String RESPONSE_USER_DETAILS = "userDetails";
    }

    public final static class CHECK_IN_LOCATION
    {
        public final static String REQUEST_PLACE_ID = "placeId";
    }

    public final static class CREATE_POST
    {
        public final static String REQUEST_IS_COMPLETED = "isComleted";
        public final static String REQUEST_POST_TYPE = "postType";
        public final static String REQUEST_POST_SOURCE_DATA = "postSourceData";
    }

    public final static class CHECKIN_USER_DETAILS
    {
        public final static String RESPONSE_CHECKEDIN_USER_DETAILS = "checkInUserDetail";
        public final static String RESPONSE_EMAIL = "Email-id";
        public final static String RESPONSE_DOB = "DateofBirth";
    }

    public final static class CHECKIN_USER_LIST
    {
        public final static String RESPONSE_CHECKEDIN_USER_LIST = "checkInUserList";
    }

    public final static class BUSINESS_CATEGORY
    {
        public final static String RESPONSE_BUSINESS_CATOGARY_LIST = "businessCategoryList";
        public final static String RESPONSE_BUSINESS_CATOGARY_ID = "categoryId";
        public final static String RESPONSE_BUSINESS_CATOGARY_NAME = "categoryName";
    }

    public final static class USER_LOGIN
    {
        public final static String REQUEST_LGOIN_TYPE = "loginType";

        public final static String RESPONSE_USER_TYPE= "userType";
        public final static String RESPONSE_CURRENCY_ID= "currencyId";
        public final static String RESPONSE_LOGIN_TYPE = "loginType";
    }

    public final static class CHANGE_PASSWORD
    {
        public final static String REQUEST_OLD_PASSWORD = "oldPassword";
        public final static String REQUEST_NEW_PASSWORD = "newPassword";
    }

    public final static class FORGOT_PASSWORD
    {
        public final static String REQUEST_EMAIL = "emailAddress";
    }


    public final static class COUNTRY_LIST
    {
        public final static String RESPONSE_COUNTRY_LIST = "countryList";
        public final static String RESPONSE_COUNTRY_ID = "countryId";
        public final static String RESPONSE_COUNTRY_NAME = "countryName";
        public final static String RESPONSE_COUNTRY_CURRENCY = "currencyCode";
    }

    public final static class STATE_LIST
    {
        public final static String REQUEST_COUNTRY_ID = "countryId";
        public final static String RESPONSE_STATE_LIST = "stateList";
        public final static String RESPONSE_STATE_ID = "stateId";
        public final static String RESPONSE_STATE_NAME = "stateName";
    }

    public final static class CITY_LIST
    {
        public final static String REQUEST_COUNTRY_ID = "countryId";
        public final static String REQUEST_STATE_ID = "stateId";
        public final static String RESPONSE_CITY_LIST = "cityList";
        public final static String RESPONSE_CITY_ID = "cityId";
        public final static String RESPONSE_CITY_NAME = "cityName";
    }

    public final static class BUSINESS_USER_REGISTER
    {
        public final static String REQUEST_BUSINESS_NAME = "businessName";
        public final static String REQUEST_CATOGARY = "businessCategory";
        public final static String REQUEST_DESC = "businessDesc";
        public final static String REQUEST_WEB = "website";
        public final static String REQUEST_ADDRESS = "address";
        public final static String REQUEST_ADDRESS_TWO = "address2";
        public final static String REQUEST_COUNTRY= "country";

        public final static String REQUEST_STATE = "state";
        public final static String REQUEST_CITY = "city";
        public final static String REQUEST_ZIP= "zipcode";
        public final static String REQUEST_PHONE = "phone";

        public final static String REQUEST_CURRENCY = "currency";

        public final static String RESPONSE_BUSINESS_DETAILS= "businessUserDetail";
    }

    // Load business user profile detail.
    public static final class RESPONSE_BUSINESS_USER_DETAIL_FOR_UPDATE_PROFILE{

        //Business User detail response for update Edit profile.
        public final static String RESPONSE_BUSINESS_USER_DETAIL_OBJ = "userDetails";
        public final static String RESPONSE_BUSINESS_USER_NAME = "businessName";
        public final static String RESPONSE_BUSINESS_USER_WEBSITE = "website";
        public final static String RESPONSE_BUSINESS_USER_EMAIL = "email";
        public final static String RESPONSE_BUSINESS_USER_ADDRESS = "address";
        public final static String RESPONSE_BUSINESS_USER_ADDRESS2="address2";
        public final static String RESPONSE_BUSINESS_USER_COUNTRY = "country";
        public final static String RESPONSE_BUSINESS_USER_STATE = "state";
        public final static String RESPONSE_BUSINESS_USER_CITY = "city";
        public final static String RESPONSE_BUSINESS_USER_ZIP = "zipcode";
        public final static String RESPONSE_BUSINESS_USER_PHONE_NUMBER = "phone";
        public final static String RESPONSE_BUSINESS_USER_DESCRIPTION = "businessDesc";
        public final static String RESPONSE_BUSINESS_USER_BUSINESS_CATEGORY = "businessCategory";
        public final static String RESPONSE_BUSINESS_USER_CURRENCY = "currency";

    }

    // Update business user profile
    public static final class REQUEST_BUSINESS_USER_UDPATE_PROFILE{
        public final static String REQUEST_USER_NAME = "businessName";
        public final static String REQUEST_BUSINESS_CATEGORY = "businessCategory";
        public final static String REQUEST__BUSINESS_DESCRIPTION = "businessDesc";
        public final static String REQUEST_WEBSITE = "website";
        public final static String REQUEST_ADDRESS = "address";
        public final static String REQUEST_ADDRESS2 = "address2";
        public final static String REQUEST_COUNTRY_ID = "countryId";
        public final static String REQUEST_STATE_ID = "stateId";
        public final static String REQUEST_CITY_ID = "cityId";
        public final static String REQUEST_ZIP_CODE = "zipcode";
        public final static String REQUEST_PHONE_NUMBER = "phone";
        public final static String REQUEST_CURRENCY = "currency";
    }

    public static final class REQUEST_RECHARGE {
        public final static String REQUEST_USER_ID = "userId";
        public final static String REQUEST_CURRENCY_ID = "currencyId";
        public final static String REQUEST_RECHARGE_AMOUNT = "rechargeAmount";
        public final static String REQUEST_NAME_ON_CARD = "nameOnCard";
        public final static String REQUEST_CARD_NUMBER = "cardNo";
        public final static String REQUEST_EXPIRY_MONTH = "expiryMonth";
        public final static String REQUEST_EXPIRY_YEAR = "expiryYear";
        public final static String REQUEST_CVV = "cvv";
        public final static String REQUEST_MININUM_AMOUNT_FOR_BALANCE = "MinAmountForBalance";



        // For intent
        public final static String RECHARGE_AMOUNT_AUTO_RECHARGE = "auto_recharge_amount";
        public final static String MINIMUM_AMOUNT_FOR_BALANCE = "strMinimumAmountForBalance";



    }


    public final static class GET_INVOICELIST
    {
        public final static String RESPONSE_GET_INVOICELIST = "GetInvoiceList";
        public final static String RESPONSE_INVOICE_ID = "invoiceId";
        public final static String RESPONSE_GET_ADD_NAME = "addName";
        public final static String RESPONSE_GET_ADD_PRICE = "addPrice";
        public final static String RESPONSE_GET_LOCATION_NAME = "locationName";
        public final static String RESPONSE_GET_ADDED_TIME = "addedTime";
        public final static String RESPONSE_GET_ADVERTISE_ID = "advertiseId";
        public final static String RESPONSE_GET_AD_START_TIME = "addStartTime";
        public final static String RESPONSE_GET_AD_END_TIME = "addEndTime";
        public final static String RESPONSE_GET_DESCRIPTION= "description";
        public final static String RESPONSE_GET_ITEM_TYPE= "itemType";
        public final static String RESPONSE_GET_ADD_ID= "addId";


    }




}