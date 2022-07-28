package com.damavandit.apps.chair.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.controllers.TokenManager;
import com.damavandit.apps.chair.dbModels.CodeForgetPassword;
import com.damavandit.apps.chair.dbModels.ConfirmPreBill;
import com.damavandit.apps.chair.dbModels.DailyProductSubGroup;
import com.damavandit.apps.chair.dbModels.DeliveryPoint;
import com.damavandit.apps.chair.dbModels.FinalBill;
import com.damavandit.apps.chair.dbModels.ForgotPasswordModel;
import com.damavandit.apps.chair.dbModels.MeasurementUnit;
import com.damavandit.apps.chair.dbModels.MessageModel;
import com.damavandit.apps.chair.dbModels.Order;
import com.damavandit.apps.chair.dbModels.OrderServer;
import com.damavandit.apps.chair.dbModels.OrderStatus;
import com.damavandit.apps.chair.dbModels.PreBill;
import com.damavandit.apps.chair.dbModels.ProductGroupManufacturerModel2;
import com.damavandit.apps.chair.dbModels.ProductGroupMeasurementUnit2;
import com.damavandit.apps.chair.dbModels.RejectPreBill;
import com.damavandit.apps.chair.dbModels.ResetPasswordModel;
import com.damavandit.apps.chair.dbModels.UserFullInfo;
import com.damavandit.apps.chair.dbModels.UserId;
import com.damavandit.apps.chair.dbModels.UserInfoState;
import com.damavandit.apps.chair.models.AccessToken;
import com.damavandit.apps.chair.models.LoginBindingModel;
import com.damavandit.apps.chair.models.RegisterBindingModel;
import com.damavandit.apps.chair.models.UserInfo;
import com.damavandit.apps.chair.models.VerifyCodeModel;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.webapis.ApiService;
import com.damavandit.apps.chair.webapis.ServiceGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.damavandit.apps.chair.constants.Const.action.ACCESS_TOKEN_FAILED;
import static com.damavandit.apps.chair.constants.Const.action.ACCESS_TOKEN_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.CANCEL_PRE_BILL_HAS_BEEN_SENT_SUCCESSFULLY;
import static com.damavandit.apps.chair.constants.Const.action.CONFIRM_PRE_BILL_HAS_BEEN_SENT_SUCCESSFULLY;
import static com.damavandit.apps.chair.constants.Const.action.CONFLICT_USERNAME;
import static com.damavandit.apps.chair.constants.Const.action.DAILY_PRODUCT_SUBGROUP_LIST_DID_NOT_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.DAILY_PRODUCT_SUBGROUP_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.DELIVERY_POINT_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.ERROR_IN_LOGIN;
import static com.damavandit.apps.chair.constants.Const.action.ERROR_IN_REGISTER;
import static com.damavandit.apps.chair.constants.Const.action.FAIL_VERIFY;
import static com.damavandit.apps.chair.constants.Const.action.FAIL_VERIFY_SERVER;
import static com.damavandit.apps.chair.constants.Const.action.FINAL_BILL_LIST_NOT_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.FINAL_BILL_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.LAST_TEN_ORDERS_DID_NOT_RECIEVE;
import static com.damavandit.apps.chair.constants.Const.action.LAST_TEN_ORDERS_IS_EMPTY;
import static com.damavandit.apps.chair.constants.Const.action.LAST_TEN_ORDERS_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.MEASUREMENT_UNIT_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.MESSAGE_LIST_DID_NOT_RECIEVE;
import static com.damavandit.apps.chair.constants.Const.action.MESSAGE_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.MESSAGE_SENT_SUCCESSFULLY;
import static com.damavandit.apps.chair.constants.Const.action.NOTIFICATION_COUNT_CHANGED;
import static com.damavandit.apps.chair.constants.Const.action.ORDERED_BASKET_HAS_BEEN_SENT_SUCCESSFULLY;
import static com.damavandit.apps.chair.constants.Const.action.ORDER_FILE_DID_NOT_SEND;
import static com.damavandit.apps.chair.constants.Const.action.ORDER_FILE_HAS_BEEN_SENT_SUCCESSFULLY;
import static com.damavandit.apps.chair.constants.Const.action.ORDER_STATUS_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.PRE_BILL_LIST_DID_NOT_RECEIVE;
import static com.damavandit.apps.chair.constants.Const.action.PRE_BILL_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.PRODUCT_GROUP_MANUFACTURER_LIST_DID_NOT_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.PRODUCT_GROUP_MANUFACTURER_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.PRODUCT_GROUP_MEASUREMENT_UNIT_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.PUSHEID_WAS_UPDATED;
import static com.damavandit.apps.chair.constants.Const.action.PUSHE_ID_DID_NOT_UPDATE;
import static com.damavandit.apps.chair.constants.Const.action.THERE_IS_NO_PRODUCT;
import static com.damavandit.apps.chair.constants.Const.action.USERNAME_IS_EXIST_SENT_WITH_CODE;
import static com.damavandit.apps.chair.constants.Const.action.USER_INFO_DID_NOT_SEND;
import static com.damavandit.apps.chair.constants.Const.action.USER_INFO_HAS_BEEN_SENT_SUCCESSFULLY;
import static com.damavandit.apps.chair.constants.Const.action.USER_INFO_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.USER_INFO_STATE_DID_NOT_RECEIVE;
import static com.damavandit.apps.chair.constants.Const.action.VERIFICATION_CODE_WAS_CORRECT;
import static com.damavandit.apps.chair.constants.Const.response_code.CONFLICT;
import static com.damavandit.apps.chair.constants.Const.response_code.CREATED;
import static com.damavandit.apps.chair.constants.Const.response_code.OK;
import static com.damavandit.apps.chair.constants.Const.shared_pref.KEY_ACCESS_REFRESH_TOKEN;
import static com.damavandit.apps.chair.constants.Const.shared_pref.KEY_ACCESS_TOKEN_TYPE;
import static com.damavandit.apps.chair.constants.Const.shared_pref.KEY_ACCESS_TOKEN_VALUE;
import static com.damavandit.apps.chair.constants.Const.shared_pref.KEY_ACCESS_USER_NAME;
import static com.damavandit.apps.chair.constants.Const.shared_pref.NAME_ACCESS_TOKEN;

public class AppMainService extends Service {

    private final IBinder mBinder = new MyBinder();
    private AccessToken mAccessToken = null;
    private int nLogin = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void initAccessToken() {

        Session session = new Session(getApplicationContext());
        TokenManager.getAccessToken(session.getUserName(), "$h@red.P@$$w0rd.4.@ll.U$er$");

        SharedPreferences sharedPref = getSharedPreferences(NAME_ACCESS_TOKEN, MODE_PRIVATE);

        if (sharedPref != null) {
            mAccessToken = new AccessToken(
                    sharedPref.getString(KEY_ACCESS_TOKEN_VALUE, ""),
                    sharedPref.getString(KEY_ACCESS_TOKEN_TYPE, ""),
                    0,
                    sharedPref.getString(KEY_ACCESS_REFRESH_TOKEN, ""),
                    sharedPref.getString(KEY_ACCESS_USER_NAME, ""),
                    "",
                    "");
        }
    }

    public void deleteUser(final String userName) {
        ApiService webService = ServiceGenerator.createService(ApiService.class);

        Call<ResponseBody> call = webService.deleteUser(userName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == OK) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void register(final String userName, final String password, final String confirm, final String hint) {
        ApiService webService = ServiceGenerator.createService(ApiService.class);
        RegisterBindingModel model = new RegisterBindingModel(userName, password, confirm, hint);

        Call<UserId> call = webService.register(model);
        call.enqueue(new Callback<UserId>() {
            @Override
            public void onResponse(Call<UserId> call, Response<UserId> response) {
                int responseCode = response.code();
                UserId body = response.body();
                if (responseCode == OK && body != null) {
                    Session session = new Session(getApplicationContext());
                    session.setUserId(body.getUserId());
                    session.setUserName(userName);
                    Intent intent = new Intent();
                    intent.setAction(ACCESS_TOKEN_RECEIVED);
                    sendBroadcast(intent);
//                    TokenManager.getAccessToken(userName, password);
                } else if (responseCode == CONFLICT) {
                    Intent intent = new Intent();
                    intent.setAction(CONFLICT_USERNAME);
                    sendBroadcast(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(ERROR_IN_REGISTER);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<UserId> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), getString(R.string.error_register), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void login(final String userName, final String password) {
        ApiService webService = ServiceGenerator.createService(ApiService.class);
        LoginBindingModel model = new LoginBindingModel(userName, password);
        Call<UserId> call = webService.login(model);
        call.enqueue(new Callback<UserId>() {
            @Override
            public void onResponse(Call<UserId> call, Response<UserId> response) {
                int responseCode = response.code();
                UserId userId = response.body();
                if (responseCode == OK && userId != null) {
                    Session session = new Session(getApplicationContext());
                    session.setUserId(userId.getUserId());
                    session.setUserName(userName);
                    Intent intent = new Intent();
                    intent.setAction(ACCESS_TOKEN_RECEIVED);
                    sendBroadcast(intent);
//                    TokenManager.getAccessToken(userName, password);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(ERROR_IN_LOGIN);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<UserId> call, Throwable t) {
                call.cancel();
                Intent intent = new Intent();
                intent.setAction(ERROR_IN_LOGIN);
                sendBroadcast(intent);
            }
        });
    }

    public void verifyCode(final String phone, final String verificationCode) {
        ApiService webService = ServiceGenerator.createService(ApiService.class);

        VerifyCodeModel model = new VerifyCodeModel(phone, verificationCode);
        Call<ResponseBody> call = webService.VerifyCode(model);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int responseCode = response.code();
                if (responseCode == OK) {
                    Intent intent = new Intent();
                    intent.setAction(VERIFICATION_CODE_WAS_CORRECT);
                    sendBroadcast(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(FAIL_VERIFY);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                Intent intent = new Intent();
                intent.setAction(FAIL_VERIFY_SERVER);
                sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), getString(R.string.error_verify), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updatePusheId(String userId, String pusheId) {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());
            Call<ResponseBody> call = webService.updatePushe(userId, pusheId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int responseCode = response.code();
                    if (responseCode == OK) {
                        Intent intent = new Intent();
                        intent.setAction(PUSHEID_WAS_UPDATED);
                        sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(PUSHE_ID_DID_NOT_UPDATE);
                        sendBroadcast(intent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }

    public void forgetPassword(String userName) {
        ApiService webService = ServiceGenerator.createService(ApiService.class);
        ForgotPasswordModel model = new ForgotPasswordModel(userName);
        Call<CodeForgetPassword> call = webService.forgetPassword(model);
        call.enqueue(new Callback<CodeForgetPassword>() {
            @Override
            public void onResponse(Call<CodeForgetPassword> call, Response<CodeForgetPassword> response) {
                int responseCode = response.code();
                CodeForgetPassword codeForgetPassword = response.body();

                if (responseCode == OK && codeForgetPassword != null) {
                    Session session = new Session(getApplicationContext());
                    session.setSecondCode(codeForgetPassword.getCode());

                    Intent intent = new Intent();
                    intent.setAction(USERNAME_IS_EXIST_SENT_WITH_CODE);
                    sendBroadcast(intent);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_forgot_pass_no_user), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CodeForgetPassword> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), getString(R.string.error_forgot_pass), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendResetPassword(final String userName, final String password, String confirm, String code) {
        ApiService webService = ServiceGenerator.createService(ApiService.class);
        ResetPasswordModel resetPassword = new ResetPasswordModel(userName, password, confirm, code);
        Call<ResponseBody> call = webService.sendResetPassword(resetPassword);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int responseCode = response.code();
                if (responseCode == OK) {
                    TokenManager.getAccessToken(userName, password);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_reset_password), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                Toast.makeText(getApplicationContext(), getString(R.string.error_reset_password), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getProductGroupManufacturerView() {
//        initAccessToken();
//
//        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
//                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {
//
//            ApiService webService = ServiceGenerator.createService(ApiService.class,
//                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());

        ApiService webService = ServiceGenerator.createService(ApiService.class);

        Call<List<ProductGroupManufacturerModel2>> call = webService.getProductGroupManufacturerView();
        call.enqueue(new Callback<List<ProductGroupManufacturerModel2>>() {
            @Override
            public void onResponse(Call<List<ProductGroupManufacturerModel2>> call, Response<List<ProductGroupManufacturerModel2>> response) {
                int responseCode = response.code();
                if (responseCode == OK) {
                    ArrayList<ProductGroupManufacturerModel2> productGroupManufacturerModelList =
                            (ArrayList<ProductGroupManufacturerModel2>) response.body();

                    if (productGroupManufacturerModelList != null) {
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("productGroupManufacturerModelList", productGroupManufacturerModelList);
                        intent.setAction(PRODUCT_GROUP_MANUFACTURER_LIST_RECEIVED);
                        sendBroadcast(intent);
                    }
                } else {
                    Intent intent = new Intent();
                    intent.setAction(PRODUCT_GROUP_MANUFACTURER_LIST_DID_NOT_RECEIVED);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<List<ProductGroupManufacturerModel2>> call, Throwable t) {
                Intent intent = new Intent();
                intent.setAction(PRODUCT_GROUP_MANUFACTURER_LIST_DID_NOT_RECEIVED);
                sendBroadcast(intent);
                call.cancel();
                Toast.makeText(getApplicationContext(), getString(R.string.error_getting_product_manufacturer), Toast.LENGTH_LONG).show();
            }
        });
//        }
    }

    public void getDailyProductSubGroupView(int productGroupId, int manufacturerId, String date) {
//        initAccessToken();
//
//        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
//                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {
//
//            ApiService webService = ServiceGenerator.createService(ApiService.class,
//                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());

        ApiService webService = ServiceGenerator.createService(ApiService.class);

        Call<List<DailyProductSubGroup>> call = webService.getDailyProductSubGroupView(manufacturerId, productGroupId, date);
        call.enqueue(new Callback<List<DailyProductSubGroup>>() {
            @Override
            public void onResponse(Call<List<DailyProductSubGroup>> call, Response<List<DailyProductSubGroup>> response) {
                int responseCode = response.code();
                if (responseCode == OK) {
                    ArrayList<DailyProductSubGroup> dailyProductSubGroupList =
                            (ArrayList<DailyProductSubGroup>) response.body();

                    if (dailyProductSubGroupList != null && dailyProductSubGroupList.size() != 0) {
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("dailyProductSubGroupList", dailyProductSubGroupList);
                        intent.setAction(DAILY_PRODUCT_SUBGROUP_LIST_RECEIVED);
                        sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(THERE_IS_NO_PRODUCT);
                        sendBroadcast(intent);
                    }
                } else {
                    Intent intent = new Intent();
                    intent.setAction(DAILY_PRODUCT_SUBGROUP_LIST_DID_NOT_RECEIVED);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<List<DailyProductSubGroup>> call, Throwable t) {
                Intent intent = new Intent();
                intent.setAction(DAILY_PRODUCT_SUBGROUP_LIST_DID_NOT_RECEIVED);
                sendBroadcast(intent);
                call.cancel();
            }
        });
//        }
    }

    public void sendOrderedBasket(Order basketContentList) {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());

            Call<ResponseBody> call = webService.SendOrderedBasket(basketContentList);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int responseCode = response.code();
                    if (responseCode == OK) {
                        Intent intent = new Intent();
                        intent.setAction(ORDERED_BASKET_HAS_BEEN_SENT_SUCCESSFULLY);
                        sendBroadcast(intent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_send_shopping_cart), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void getLastTenOrders(String userId, int pageNumber) {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());

            Call<List<OrderServer>> call = webService.getLastTenOrders(userId, 20);
            call.enqueue(new Callback<List<OrderServer>>() {
                @Override
                public void onResponse(Call<List<OrderServer>> call, Response<List<OrderServer>> response) {
                    int responseCode = response.code();
                    if (responseCode == OK) {
                        ArrayList<OrderServer> orders =
                                (ArrayList<OrderServer>) response.body();

                        if (orders != null) {
                            Intent intent = new Intent();
                            intent.putParcelableArrayListExtra("lastTenOrders", orders);
                            intent.setAction(LAST_TEN_ORDERS_LIST_RECEIVED);
                            sendBroadcast(intent);
                        } else {
                            Intent intent = new Intent();
                            intent.setAction(LAST_TEN_ORDERS_IS_EMPTY);
                            sendBroadcast(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<OrderServer>> call, Throwable t) {
                    call.cancel();
                    Intent intent = new Intent();
                    intent.setAction(LAST_TEN_ORDERS_DID_NOT_RECIEVE);
                    sendBroadcast(intent);
                }
            });
        }
    }

    public void getPreBillList(int orderId) {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());

            Call<PreBill> call = webService.getPreBillList(orderId);
            call.enqueue(new Callback<PreBill>() {
                @Override
                public void onResponse(Call<PreBill> call, Response<PreBill> response) {
                    int responseCode = response.code();
                    if (responseCode == OK) {
                        PreBill preBillList = response.body();

                        if (preBillList != null) {
                            Intent intent = new Intent();
//                            intent.putExtra("tax",preBillList.getTax());
                            intent.putExtra("preBillList", preBillList);
                            Bundle bundle = new Bundle();
                            bundle.putDouble("tax", preBillList.getTax());
                            intent.setAction(PRE_BILL_LIST_RECEIVED);
                            sendBroadcast(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<PreBill> call, Throwable t) {
                    call.cancel();
                    Intent intent = new Intent();
                    intent.setAction(PRE_BILL_LIST_DID_NOT_RECEIVE);
                    sendBroadcast(intent);
                }
            });

        }
    }

    public void sendConfirmPreBill(ConfirmPreBill confirmPreBill) {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());


            Call<ResponseBody> call = webService.sendConfirmPreBill(confirmPreBill);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int responseCode = response.code();
                    if (responseCode == OK) {
                        Intent intent = new Intent();
                        intent.setAction(CONFIRM_PRE_BILL_HAS_BEEN_SENT_SUCCESSFULLY);
                        sendBroadcast(intent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_confirm), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void sendCancelPreBill(RejectPreBill rejectPreBill) {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());


            Call<ResponseBody> call = webService.sendCancelPreBill(rejectPreBill.getOrderId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int responseCode = response.code();
                    if (responseCode == OK) {
                        Intent intent = new Intent();
                        intent.setAction(CANCEL_PRE_BILL_HAS_BEEN_SENT_SUCCESSFULLY);
                        sendBroadcast(intent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_cancel), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void uploadOrderImage(final String path, final int rand) {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());

            File file = new File(path);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            final Session session = new Session(getApplicationContext());
            Call<ResponseBody> call = webService.uploadOrderImage(body, session.getUserId(), rand);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int responseCode = response.code();
                    if (responseCode == OK || responseCode == CREATED) {
                        Intent intent = new Intent();
                        intent.setAction(ORDER_FILE_HAS_BEEN_SENT_SUCCESSFULLY);
                        sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(ORDER_FILE_DID_NOT_SEND);
                        sendBroadcast(intent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                    String s = t.getMessage();
                    uploadOrderImage(path, rand);
//                    isLastOrderFileUploaded(session.getUserId());
                }
            });

        }
    }

    public void isLastOrderFileUploaded(String userId) {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());

            Call<ResponseBody> call = webService.isLastOrderFileUploaded(userId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int responseCode = response.code();
                    if (responseCode == OK || responseCode == CREATED) {
                        Intent intent = new Intent();
                        intent.setAction(ORDER_FILE_HAS_BEEN_SENT_SUCCESSFULLY);
                        sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(ORDER_FILE_DID_NOT_SEND);
                        sendBroadcast(intent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Intent intent = new Intent();
                    intent.setAction(ORDER_FILE_DID_NOT_SEND);
                    sendBroadcast(intent);
                }
            });
        }
    }


    public void getAllConstantTables() {
//        initAccessToken();
//
//        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
//                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {
//
//            ApiService webService = ServiceGenerator.createService(ApiService.class,
//                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());

        ApiService webService = ServiceGenerator.createService(ApiService.class);

        Call<List<MeasurementUnit>> call = webService.getMeasurement();
        call.enqueue(new Callback<List<MeasurementUnit>>() {
            @Override
            public void onResponse(Call<List<MeasurementUnit>> call, Response<List<MeasurementUnit>> response) {
                int responseCode = response.code();

                if (responseCode == OK) {
                    ArrayList<MeasurementUnit> measurementUnits =
                            (ArrayList<MeasurementUnit>) response.body();

                    if (measurementUnits != null) {
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("measurementUnitList", measurementUnits);
                        intent.setAction(MEASUREMENT_UNIT_LIST_RECEIVED);
                        sendBroadcast(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MeasurementUnit>> call, Throwable t) {
                call.cancel();
            }
        });

        Call<List<OrderStatus>> call2 = webService.getOrderStatus();
        call2.enqueue(new Callback<List<OrderStatus>>() {
            @Override
            public void onResponse(Call<List<OrderStatus>> call, Response<List<OrderStatus>> response) {
                int responseCode = response.code();
                if (responseCode == OK) {
                    ArrayList<OrderStatus> orderStatuses =
                            (ArrayList<OrderStatus>) response.body();

                    if (orderStatuses != null) {
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("orderStatusList", orderStatuses);
                        intent.setAction(ORDER_STATUS_LIST_RECEIVED);
                        sendBroadcast(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<OrderStatus>> call, Throwable t) {
                call.cancel();
            }
        });

        Call<List<DeliveryPoint>> call3 = webService.getDeliveryPoint();
        call3.enqueue(new Callback<List<DeliveryPoint>>() {
            @Override
            public void onResponse(Call<List<DeliveryPoint>> call, Response<List<DeliveryPoint>> response) {
                int responseCode = response.code();
                if (responseCode == OK) {
                    ArrayList<DeliveryPoint> deliveryPoints =
                            (ArrayList<DeliveryPoint>) response.body();

                    if (deliveryPoints != null) {
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("deliveryPointList", deliveryPoints);
                        intent.setAction(DELIVERY_POINT_LIST_RECEIVED);
                        sendBroadcast(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DeliveryPoint>> call, Throwable t) {
                call.cancel();
            }
        });

        Call<List<ProductGroupMeasurementUnit2>> call4 = webService.getProductGroupMeasurementUnit();
        call4.enqueue(new Callback<List<ProductGroupMeasurementUnit2>>() {
            @Override
            public void onResponse(Call<List<ProductGroupMeasurementUnit2>> call, Response<List<ProductGroupMeasurementUnit2>> response) {
                int responseCode = response.code();
                if (responseCode == OK) {
                    ArrayList<ProductGroupMeasurementUnit2> productGroupMeasurementUnits =
                            (ArrayList<ProductGroupMeasurementUnit2>) response.body();

                    if (productGroupMeasurementUnits != null) {
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("productGroupMeasurementUnitList", productGroupMeasurementUnits);
                        intent.setAction(PRODUCT_GROUP_MEASUREMENT_UNIT_LIST_RECEIVED);
                        sendBroadcast(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ProductGroupMeasurementUnit2>> call, Throwable t) {
                call.cancel();
            }
        });
//        }
    }

    public void sendFullUserInfo(UserFullInfo userFullInfo) {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());

            Session session = new Session(getApplicationContext());
            Call<ResponseBody> call = webService.SendUserFullInfo(session.getUserId(), userFullInfo);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int responseCode = response.code();
                    if (responseCode == OK) {
                        Intent intent = new Intent();
                        intent.setAction(USER_INFO_HAS_BEEN_SENT_SUCCESSFULLY);
                        sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(USER_INFO_DID_NOT_SEND);
                        sendBroadcast(intent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                    Intent intent = new Intent();
                    intent.setAction(USER_INFO_DID_NOT_SEND);
                    sendBroadcast(intent);
                }
            });
        }
    }

    public void getUserInfoState() {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());


            final Session session = new Session(getApplicationContext());
            Call<UserFullInfo> call = webService.getUserInformationState(session.getUserId());
            call.enqueue(new Callback<UserFullInfo>() {
                @Override
                public void onResponse(Call<UserFullInfo> call, Response<UserFullInfo> response) {
                    int responseCode = response.code();
                    if (responseCode == OK) {
                        UserFullInfo userInfoState = response.body();

                        if (userInfoState != null) {
                            if (userInfoState.getName() == null &&
                                    userInfoState.getFamily() == null &&
                                    userInfoState.getEmail() == null &&
                                    userInfoState.getAddress() == null) {
                                session.setUserInfoComplete(false);
                            } else {
                                session.setUserInfoComplete(true);
                            }
                        }
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(USER_INFO_STATE_DID_NOT_RECEIVE);
                        sendBroadcast(intent);
                    }
                }

                @Override
                public void onFailure(Call<UserFullInfo> call, Throwable t) {
                    Intent intent = new Intent();
                    intent.setAction(USER_INFO_STATE_DID_NOT_RECEIVE);
                    sendBroadcast(intent);
                    call.cancel();
                }
            });

        }
    }

    public void getFinalBillList(int orderId) {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());
            Call<FinalBill> call = webService.getFinalBillList(orderId);
            call.enqueue(new Callback<FinalBill>() {
                @Override
                public void onResponse(Call<FinalBill> call, Response<FinalBill> response) {
                    int responseCode = response.code();
                    if (responseCode == OK) {
                        FinalBill finalBillList = response.body();

                        if (finalBillList != null) {
                            Intent intent = new Intent();
                            intent.putExtra("finalBillList", finalBillList);
                            intent.setAction(FINAL_BILL_LIST_RECEIVED);
                            sendBroadcast(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<FinalBill> call, Throwable t) {
                    call.cancel();
                    Intent intent = new Intent();
                    intent.setAction(FINAL_BILL_LIST_NOT_RECEIVED);
                    sendBroadcast(intent);
                    Toast.makeText(getApplicationContext(), getString(R.string.error_final_bill), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void getMessages(String userId, int pageNumber) {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());
            Call<List<MessageModel>> call = webService.getMessages(userId, pageNumber);
            call.enqueue(new Callback<List<MessageModel>>() {
                @Override
                public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                    int responseCode = response.code();
                    if (responseCode == OK) {
                        ArrayList<MessageModel> messageList =
                                (ArrayList<MessageModel>) response.body();

                        if (messageList != null) {
                            Intent intent = new Intent();
                            intent.putParcelableArrayListExtra("messageList", messageList);
                            intent.setAction(MESSAGE_LIST_RECEIVED);
                            sendBroadcast(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                    Intent intent = new Intent();
                    intent.setAction(MESSAGE_LIST_DID_NOT_RECIEVE);
                    sendBroadcast(intent);
                    call.cancel();
                }
            });
        }
    }

    public void sendMessage(MessageModel messageModel) {
        initAccessToken();

        if (!TextUtils.isEmpty(mAccessToken.getAccessToken())
                && !TextUtils.isEmpty(mAccessToken.getTokenType())) {

            ApiService webService = ServiceGenerator.createService(ApiService.class,
                    mAccessToken.getTokenType() + " " + mAccessToken.getAccessToken());


            Call<ResponseBody> call = webService.sendMessage(messageModel);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int responseCode = response.code();
                    if (responseCode == OK) {
                        Intent intent = new Intent();
                        intent.setAction(MESSAGE_SENT_SUCCESSFULLY);
                        sendBroadcast(intent);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }

    public class MyBinder extends Binder {
        public AppMainService getService() {
            return AppMainService.this;
        }
    }
}
