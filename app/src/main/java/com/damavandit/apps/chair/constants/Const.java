package com.damavandit.apps.chair.constants;

import android.widget.ScrollView;

public final class Const {
    public static final class shared_pref {
        public static final String NAME_ACCESS_TOKEN = "accessToken";
        public static final String KEY_ACCESS_TOKEN_VALUE = "accessTokenValue";
        public static final String KEY_ACCESS_TOKEN_TYPE = "accessTokenType";
        public static final String KEY_ACCESS_REFRESH_TOKEN = "accessRefreshToken";
        public static final String KEY_ACCESS_USER_NAME = "accessUserName";
    }

    public static final class action {
        public static final String ONE_SIGNAL_NOTIFICATION_RECEIVED = "com.damavandit.chairapp.onesignal.notificationreceived";
        public static final String ACCESS_TOKEN_RECEIVED = "com.damavandit.chairapp.accesstokenreceived";
        public static final String ACCESS_TOKEN_FAILED = "com.damavandit.chairapp.accesstokenfailed";
        public static final String CONFLICT_USERNAME = "com.damavandit.conflictusername";
        public static final String USER_INFO_RECEIVED = "com.damavandit.chairapp.userinforeceived";
        public static final String ERROR_IN_LOGIN = "com.damavandit.errorinlogin";
        public static final String ERROR_IN_REGISTER = "com.damavandit.errorinregister";
        public static final String VERIFICATION_CODE_WAS_CORRECT = "com.damavandit.verificationcodewascorrect";
        public static final String PRODUCT_GROUP_MANUFACTURER_LIST_RECEIVED = "com.damavandit.chairapp.productgroupmanufacturerviewreceived";
        public static final String PRODUCT_GROUP_MANUFACTURER_LIST_DID_NOT_RECEIVED = "com.damavandit.chairapp.productgroupmanufacturerviewdidnotreceived";
        public static final String DAILY_PRODUCT_SUBGROUP_LIST_RECEIVED = "com.damavandit.dailyproductsubgrouplistreceived";
        public static final String THERE_IS_NO_PRODUCT = "com.damavandit.thereisnoproduct";
        public static final String DAILY_PRODUCT_SUBGROUP_LIST_DID_NOT_RECEIVED = "com.damavandit.dailyproductsubgrouplistdidnotreceived";
        public static final String NOTIFICATION_COUNT_INCREASED = "com.damavandit.notificationcountincreased";
        public static final String NOTIFICATION_COUNT_DECREASED = "com.damavandit.notificationcountdecreased";
        public static final String NOTIFICATION_COUNT_CHANGED = "com.damavandit.notificationcountchanged";
        public static final String ORDERED_BASKET_HAS_BEEN_SENT_SUCCESSFULLY = "com.damavandit.orderedbaskethasbeensentsuccessfully";
        public static final String LAST_TEN_ORDERS_LIST_RECEIVED = "com.damavandit.lasttenorderslistreceived";
        public static final String LAST_TEN_ORDERS_DID_NOT_RECIEVE = "com.damavandit.lasttenordersdidnotreceive";
        public static final String LAST_TEN_ORDERS_IS_EMPTY = "com.damavandit.lasttenordersisempty";
        public static final String PRE_BILL_LIST_RECEIVED = "com.damavandit.prebilllistreceived";
        public static final String PRE_BILL_LIST_DID_NOT_RECEIVE = "com.damavandit.prebilllidtdidnotreceive";
        public static final String PRE_BILL_HAS_CHANGED = "com.damavandit.prebillhaschanged";
        public static final String CONFIRM_PRE_BILL_HAS_BEEN_SENT_SUCCESSFULLY = "com.damavandit.confirmprebillhasbeensentsuccessfully";
        public static final String CANCEL_PRE_BILL_HAS_BEEN_SENT_SUCCESSFULLY = "com.damavandit.cancelprebillhasbeensentsuccessfully";
        public static final String ONE_MESSAGE_ADDED = "com.damavandit.onemessageadded";
        public static final String ORDER_FILE_HAS_BEEN_SENT_SUCCESSFULLY = "com.damavandit.orderfilehasbeensentsuccessfully";
        public static final String ORDER_FILE_DID_NOT_SEND = "com.damavandit.ordefiledidnotsend";
        public static final String MEASUREMENT_UNIT_LIST_RECEIVED = "com.damavandit.measurementunitlistreceived";
        public static final String ORDER_STATUS_LIST_RECEIVED = "com.damavandit.orderstatuslistreceived";
        public static final String DELIVERY_POINT_LIST_RECEIVED = "com.damavandit.deloiverypointlistreceived";
        public static final String PRODUCT_GROUP_MEASUREMENT_UNIT_LIST_RECEIVED = "com.damavandit.produstgroupmeasurementunitlistreceived";
        public static final String FINAL_BILL_LIST_RECEIVED = "com.damavandit.finalbilllistreceived";
        public static final String FINAL_BILL_LIST_NOT_RECEIVED ="com.damavandit.finalbilllistnotreceived";
        public static final String MESSAGE_LIST_RECEIVED = "com.damavandit.messagelistreceived";
        public static final String MESSAGE_LIST_DID_NOT_RECIEVE = "com.damavandit.messagelistdidnotreceive";
        public static final String MESSAGE_SENT_SUCCESSFULLY = "com.damavandit.messagesentsuccessfully";
        public static final String PUSHEID_WAS_UPDATED = "com.damavandit.pusheidwasupdated";
        public static final String PUSHE_ID_DID_NOT_UPDATE = "com.damavandit.pusheiddidnotupdate";
        public static final String USERNAME_IS_EXIST_SENT_WITH_CODE = "com.damavandit.usernameisexistsentwithcode";
        public static final String SECOND_CODE_WAS_CORRECT = "com.damavandit.secondcodewascorrect";
        public static final String PASSWORD_HAS_BEEN_RESETED = "com.damavandit.passwordhasbeenreseted";
        public static final String LOGED_IN = "com.damavandit.logedin";
        public static final String CAN_NOT_LOGIN = "com.damavandit.cannotlogedin";
        public static final String FAIL_VERIFY = "com.damavandit.failverify";
        public static final String FAIL_VERIFY_SERVER = "com.damavandit.failverifyserver";
        public static final String REGISTERE_FOR_SHOPPING = "com.damavandit.registeredforshopping";
        public static final String REGISTERE_FOR_CHATTING = "com.damavandit.registeredforchatting";
        public static final String USER_INFO_HAS_BEEN_SENT_SUCCESSFULLY = "com.damavandit.userinfosent";
        public static final String USER_INFO_DID_NOT_SEND = "com.damavandit.userinfodidnotsend";
        public static final String USER_INFO_STATE_DID_NOT_RECEIVE = "com.damavandit.userinfostatedidnotreceive";
        public static final String EDIT = "com.damavandit.edit";
    }

    public static final class response_code {
        public static final int OK = 200;
        public static final int CREATED = 201;
        public static final int UNAUTHORIZED = 401;
        public static final int CONFLICT = 409;
    }

    public static final class grant_type {
        public static final String PASSWORD = "password";
        public static final String REFRESH_TOKEN = "refresh_token";
    }
}
