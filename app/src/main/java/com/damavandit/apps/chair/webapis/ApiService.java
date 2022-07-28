package com.damavandit.apps.chair.webapis;

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
import com.damavandit.apps.chair.dbModels.ProductGroupMeasurementUnit;
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

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    String API_BASE_URL = "http://api.chair-app.com";

    @POST("/api/Account/Login")
    Call<UserId> login(@Body LoginBindingModel model);

    @POST("/api/Account/Register")
    Call<UserId> register(@Body RegisterBindingModel model);

    @POST("/api/Account/VerifyCode")
    Call<ResponseBody> VerifyCode(@Body VerifyCodeModel model);

    @POST("/api/Account/UpdatePusheId")
    Call<ResponseBody> updatePushe(@Query("userId") String userId, @Query("pusheId") String pusheId);

    @POST("/api/Account/ForgotPassword")
    Call<CodeForgetPassword> forgetPassword(@Body ForgotPasswordModel model);

    @POST("/api/Account/Register")
    Call<CodeForgetPassword> simpleRegister(@Body ForgotPasswordModel model);

//    @POST("/Account/ForgetPasswordSecondCode")
//    Call<UserId> sendSecondCode(@Body String secondCode);

    @POST("/api/Account/ResetPassword")
    Call<ResponseBody> sendResetPassword(@Body ResetPasswordModel resetPassword);

    @POST("/api/Data/PlaceOrder")
    Call<ResponseBody> SendOrderedBasket(@Body Order basketList);

    @POST("/api/Data/ConfirmOrder")
    Call<ResponseBody> sendConfirmPreBill(@Body ConfirmPreBill confirmPreBill);

    @POST("/api/Data/CancelOrder")
    Call<ResponseBody> sendCancelPreBill(@Query("orderId") int orderId);

    @Multipart
    @POST("/api/Data/UploadOrderFile")
    Call<ResponseBody> uploadOrderImage(@Part MultipartBody.Part image, @Query("userId") String userId,@Query("fileId") int fileId);

    @GET("/api/Data/IsLastOrderFileUploaded")
    Call<ResponseBody> isLastOrderFileUploaded(@Query("userId") String userId);

    @POST("/api/Data/SendMessage")
    Call<ResponseBody> sendMessage(@Body MessageModel messageModel);

    @POST("/api/Account/SendUserFullInfo")
    Call<ResponseBody> SendUserFullInfo(@Query("userId") String userId,@Body UserFullInfo userFullInfo);

    @POST("/api/Account/DeleteUserAccount")
    Call<ResponseBody> deleteUser(@Query("userName") String userName);

    @FormUrlEncoded
    @POST("/Token")
    Call<AccessToken> getAccessToken(
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("/Token")
    Call<AccessToken> getRefreshToken(
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("refresh_token") String refreshToken);

    @GET("/api/Account/UserInfo")
    Call<UserInfo> getUserInfo();

    @GET("/api/Data/GetProductGroupManufacturerList")
    Call<List<ProductGroupManufacturerModel2>> getProductGroupManufacturerView();

    @GET("/api/Data/GetDailyProductPriceList")
    Call<List<DailyProductSubGroup>> getDailyProductSubGroupView(@Query("manufacturerId") int manufacturerId,
                                                                 @Query("productGroupId") int productGroupId,
                                                                 @Query("dateString") String date);

    @GET("/api/Data/GetNLastOrders")
    Call<List<OrderServer>> getLastTenOrders(@Query("userId") String userId, @Query("count") int count);

    @GET("/api/Data/GetOrderDetails")
    Call<PreBill> getPreBillList(@Query("OrderId") int orderId);

    @GET("/api/Data/GetMeasurementUnitList")
    Call<List<MeasurementUnit>> getMeasurement();

    @GET("/api/Data/GetOrderStatusList")
    Call<List<OrderStatus>> getOrderStatus();

    @GET("/api/Data/GetDeliveryPointList")
    Call<List<DeliveryPoint>> getDeliveryPoint();

    @GET("/api/Data/GetProductGroupMeasurementUnitList")
    Call<List<ProductGroupMeasurementUnit2>> getProductGroupMeasurementUnit();

    @GET("/api/Data/GetFinalPreInvoice")
    Call<FinalBill> getFinalBillList(@Query("OrderId") int orderId);

    @GET("/api/Account/GetUserInformation")
    Call<UserFullInfo> getUserInformationState(@Query("userId") String userId);

    @GET("/api/Data/GetNLastMessages")
    Call<List<MessageModel>> getMessages(@Query("userId") String userId, @Query("pageNumber") int pageNumber);

//    @POST("/api/Action/PlaceOrder")
//    Call<ResponseBody> placeOrder(OrderServer order);
}
