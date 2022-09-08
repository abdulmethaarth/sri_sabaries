package affinity.com.srisabaries.network;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.gson.JsonObject;

import affinity.com.srisabaries.models.request.ReqAddToCart;
import affinity.com.srisabaries.models.request.ReqAddUserAddress;
import affinity.com.srisabaries.models.request.ReqBase;
import affinity.com.srisabaries.models.request.ReqCancelOrder;
import affinity.com.srisabaries.models.request.ReqCancellationDetails;
import affinity.com.srisabaries.models.request.ReqCashbackHistory;
import affinity.com.srisabaries.models.request.ReqChangePassword;
import affinity.com.srisabaries.models.request.ReqConnectDisconnectApps;
import affinity.com.srisabaries.models.request.ReqContests;
import affinity.com.srisabaries.models.request.ReqCoupons;
import affinity.com.srisabaries.models.request.ReqCouponsDetail;
import affinity.com.srisabaries.models.request.ReqCouponsList;
import affinity.com.srisabaries.models.request.ReqDeleteAllNotification;
import affinity.com.srisabaries.models.request.ReqDeleteUserAddresses;
import affinity.com.srisabaries.models.request.ReqDeliveryStatus;
import affinity.com.srisabaries.models.request.ReqFavouriteCoupons;
import affinity.com.srisabaries.models.request.ReqFavouriteProducts;
import affinity.com.srisabaries.models.request.ReqFitbitToken;
import affinity.com.srisabaries.models.request.ReqForgotPassword;
import affinity.com.srisabaries.models.request.ReqFsPoints;
import affinity.com.srisabaries.models.request.ReqFsStore;
import affinity.com.srisabaries.models.request.ReqFsStoreProductsList;
import affinity.com.srisabaries.models.request.ReqGetContestDetails;
import affinity.com.srisabaries.models.request.ReqGetFilters;
import affinity.com.srisabaries.models.request.ReqGetOrderDetail;
import affinity.com.srisabaries.models.request.ReqGetPushNotification;
import affinity.com.srisabaries.models.request.ReqGetUserDetails;
import affinity.com.srisabaries.models.request.ReqGetUserPointsByTime;
import affinity.com.srisabaries.models.request.ReqJoinChallenge;
import affinity.com.srisabaries.models.request.ReqLeaveChallenge;
import affinity.com.srisabaries.models.request.ReqLogin;
import affinity.com.srisabaries.models.request.ReqMyCart;
import affinity.com.srisabaries.models.request.ReqMyOrders;
import affinity.com.srisabaries.models.request.ReqMyWallet;
import affinity.com.srisabaries.models.request.ReqPlaceOrder;
import affinity.com.srisabaries.models.request.ReqPrizeMoneyDetails;
import affinity.com.srisabaries.models.request.ReqProceed;
import affinity.com.srisabaries.models.request.ReqProductDetail;
import affinity.com.srisabaries.models.request.ReqProductsCategory;
import affinity.com.srisabaries.models.request.ReqProductsList;
import affinity.com.srisabaries.models.request.ReqRedeemPointsAffiliate;
import affinity.com.srisabaries.models.request.ReqRemoveCart;
import affinity.com.srisabaries.models.request.ReqRepeatOrder;
import affinity.com.srisabaries.models.request.ReqRunkeeperToken;
import affinity.com.srisabaries.models.request.ReqSendToBank;
import affinity.com.srisabaries.models.request.ReqSetFavouriteCoupon;
import affinity.com.srisabaries.models.request.ReqSetFavouriteProduct;
import affinity.com.srisabaries.models.request.ReqShareAndInvite;
import affinity.com.srisabaries.models.request.ReqSignUp;
import affinity.com.srisabaries.models.request.ReqTrendingCoupons;
import affinity.com.srisabaries.models.request.ReqTrendingProducts;
import affinity.com.srisabaries.models.request.ReqUnFavouriteCoupon;
import affinity.com.srisabaries.models.request.ReqUnFavouriteProduct;
import affinity.com.srisabaries.models.request.ReqUpdateCart;
import affinity.com.srisabaries.models.request.ReqUserCalorieDistance;
import affinity.com.srisabaries.models.request.ReqWalletHistory;
import affinity.com.srisabaries.models.response.ResAddUserAddress;
import affinity.com.srisabaries.models.response.ResBase;
import affinity.com.srisabaries.models.response.ResCancellationDetails;
import affinity.com.srisabaries.models.response.ResCashBackHistory;
import affinity.com.srisabaries.models.response.ResContactUs;
import affinity.com.srisabaries.models.response.ResCouponsDetail;
import affinity.com.srisabaries.models.response.ResDeliveryStatus;
import affinity.com.srisabaries.models.response.ResForgotPassword;
import affinity.com.srisabaries.models.response.ResFsStore;
import affinity.com.srisabaries.models.response.ResFsStoreProductsList;
import affinity.com.srisabaries.models.response.ResGetContestDetails;
import affinity.com.srisabaries.models.response.ResGetContests;
import affinity.com.srisabaries.models.response.ResGetCouponData;
import affinity.com.srisabaries.models.response.ResGetCouponList;
import affinity.com.srisabaries.models.response.ResGetFavouriteCoupons;
import affinity.com.srisabaries.models.response.ResGetFavouriteProducts;
import affinity.com.srisabaries.models.response.ResGetFilters;
import affinity.com.srisabaries.models.response.ResGetFsPoints;
import affinity.com.srisabaries.models.response.ResGetOrderDetail;
import affinity.com.srisabaries.models.response.ResGetProductDetail;
import affinity.com.srisabaries.models.response.ResGetProductsCategories;
import affinity.com.srisabaries.models.response.ResGetProductsList;
import affinity.com.srisabaries.models.response.ResGetTrendingCoupons;
import affinity.com.srisabaries.models.response.ResGetTrendingProducts;
import affinity.com.srisabaries.models.response.ResGetUserAddresses;
import affinity.com.srisabaries.models.response.ResGetUserDetails;
import affinity.com.srisabaries.models.response.ResGetUserPointsByTime;
import affinity.com.srisabaries.models.response.ResLeaveContest;
import affinity.com.srisabaries.models.response.ResLoginSignUpSocial;
import affinity.com.srisabaries.models.response.ResMyCart;
import affinity.com.srisabaries.models.response.ResMyOrders;
import affinity.com.srisabaries.models.response.ResMyWallet;
import affinity.com.srisabaries.models.response.ResPlaceOrder;
import affinity.com.srisabaries.models.response.ResPrizeMoneyDetails;
import affinity.com.srisabaries.models.response.ResPushNotification;
import affinity.com.srisabaries.models.response.ResRunkeeperData;
import affinity.com.srisabaries.models.response.ResShareAndInvite;
import affinity.com.srisabaries.models.response.ResSignUp;
import affinity.com.srisabaries.models.response.ResUpdateProfile;
import affinity.com.srisabaries.models.response.ResUserCalorieDistance;
import affinity.com.srisabaries.models.response.ResWalletHistoryPaid;
import affinity.com.srisabaries.models.response.ResWalletHistoryReceived;
import affinity.com.srisabaries.ui.fragments.ResGetAllProductsDatas;
import affinity.com.srisabaries.utils.Banners;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by root on 6/14/16.
 */
public interface IApiClient {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/rest/register")
    Call<ResSignUp> signup(/*@Body ReqSignUp reqSignUp*/@Header("Authorization") String auth,@Body JsonObject jsonBody);

    @POST("indexm")
    Call<ResLoginSignUpSocial> signupFb(@Body ReqSignUp reqSignUp);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/rest/login/")
    Call<ResLoginSignUpSocial> login(/*@Body ReqLogin reqLogin*/@Header("Authorization") String auth,@Body JsonObject jsonBody);

    @POST("indexm")
    Call<ResForgotPassword> forgotPassword(@Body ReqForgotPassword reqForgotPassword);

    @POST("indexm")
    Call<ResGetUserDetails> getUserDetails(@Body ReqGetUserDetails reqGetUserDetails);

    @POST("indexm")
    Call<ResAddUserAddress> addUserAddress(@Body ReqAddUserAddress reqAddUserAddress);

    @POST("indexm")
    Call<ResBase> changePassword(@Body ReqChangePassword reqChangePassword);

    @POST("indexm")
    Call<ResGetCouponData> getCoupons(@Body ReqCoupons reqCoupons);

    @POST("indexm")
    Call<ResGetFavouriteProducts> getFavouriteProducts(@Body ReqFavouriteProducts reqFavouriteProducts);

    @POST("indexm")
    Call<ResGetUserAddresses> getUserAddresses(@Body ReqGetUserDetails reqAddress);

    @POST("indexm")
    Call<ResBase> deleteUserAddresses(@Body ReqDeleteUserAddresses reqDeleteUserAddresses);

    @POST("indexm")
    Call<ResGetFavouriteCoupons> getFavouriteCoupons(@Body ReqFavouriteCoupons reqFavouriteCoupons);

    @POST("indexm")
    Call<ResBase> setFavouriteCoupon(@Body ReqSetFavouriteCoupon reqSetFavouriteCoupon);

    @POST("indexm")
    Call<ResBase> setFavouriteProduct(@Body ReqSetFavouriteProduct reqSetFavouriteProduct);

    @POST("indexm")
    Call<ResBase> unFavouriteCoupon(@Body ReqUnFavouriteCoupon reqUnFavouriteCoupon);

    @POST("indexm")
    Call<ResBase> unFavouriteProduct(@Body ReqUnFavouriteProduct reqUnFavouriteProduct);

    @POST("indexm")
    Call<ResGetCouponList> getCouponsList(@Body ReqCouponsList reqCoupons);

    /*@POST("api/rest/categories")
    Call<ResGetProductsCategories> getProductsCategory(@Body ReqProductsCategory reqProductsCategory);*/

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/rest/oauth2/token/client_credentials")
    Call<ResGetToken> getAccesToken(/*@Query("id") String id,*/ @Header("Authorization") String auth);

     @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/rest/categoriess")
    Call<ResGetProductsCategories> getProductsCategory(/*@Query("id") String id,*/ @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/rest/latest")
    Call<ResGetTrendingProducts> getTrendingProducts(@Header("Authorization") String auth/*@Body ReqTrendingProducts reqTrendingProducts*/);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/rest/products//")
    Call<ResGetAllProductsDatas> getAllProductsData(@Header("Authorization") String auth);

    //new one banners

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/rest/slideshows")
    Call<Banners> getBanners(@Header("Authorization") String auth/*@Field("userID") String userID,
                             @Field("method") String method,
                             @Field("service_key") String service_key,
                             @Field("search") String search,
                             @Field("page") String page*/);


/*    @FormUrlEncoded
    @POST("indexm")
    Call<Products> getAllProCat(@Field("userID") String userID,
                                @Field("method") String method,
                                @Field("service_key") String service_key,
                                @Field("search") String search,
                                @Field("page") String page);*/


    @POST("indexm")
    Call<ResGetTrendingProducts> banner(@Body ReqTrendingProducts reqTrendingProducts);

    @POST("indexm")
    Call<ResGetTrendingCoupons> getTrendingCoupons(@Body ReqTrendingCoupons reqTrendingCoupons);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("products/category/")
    Call<ResGetProductsList> getProductsList(@Header("Authorization") String auth, @Query("category_id") String category_id/*@Body ReqProductsList reqProductsList*/);

    @POST("indexm")
    Call<ResGetProductDetail> getProductDetail(@Body ReqProductDetail reqProductDetail);

    @POST("indexm")
    Call<ResGetFilters> getFilters(@Body ReqGetFilters reqGetFilters);

    @POST("indexm")
    Call<ResCouponsDetail> getCouponDetail(@Body ReqCouponsDetail ReqCouponsDetail);

    @POST("indexm")
    Call<ResShareAndInvite> shareAndInvite(@Body ReqShareAndInvite reqShareAndInvite);

    @POST("indexm")
    Call<ResGetContests> getContests(@Body ReqContests reqContests);

    @POST("indexm")
    Call<ResGetContests> joinChallenge(@Body ReqJoinChallenge reqJoinChallenge);

    @POST("indexm")
    Call<ResLeaveContest> joinChallengeFromDetail(@Body ReqJoinChallenge reqJoinChallenge);

    @POST("indexm")
    Call<ResLeaveContest> leaveChallenge(@Body ReqLeaveChallenge reqLeaveChallenge);

    @POST("indexm")
    Call<ResGetContestDetails> getContestDetails(@Body ReqGetContestDetails reqGetContestDetails);

    @POST("indexm")
    Call<ResFsStoreProductsList> getFsStoreProductsList(@Body ReqFsStoreProductsList reqFsStoreProductsList);

    @POST("indexm")
    Call<ResFsStore> getFsStore(@Body ReqFsStore reqFsStore);

    @POST("indexm")
    Call<ResGetFsPoints> getFsStorePoints(@Body ReqFsPoints reqFsPoints);

    @POST("indexm")
    Call<ResUserCalorieDistance> getUserCalorieDistance(@Body ReqUserCalorieDistance reqUserCalorieDistance);

    @POST("indexm")
    Call<ResDeliveryStatus> getDeliveryStatus(@Body ReqDeliveryStatus reqDeliveryStatus);

    @POST("indexm")
    Call<ResMyCart> getCartDetails(@Body ReqMyCart reqMyCart);

    @POST("indexm")
    Call<ResBase> removeCart(@Body ReqRemoveCart reqRemoveCart);

    @POST("indexm")
    Call<ResBase> addCart(@Body ReqAddToCart reqAddToCart);

    @POST("indexm")
    Call<ResPlaceOrder> placeOrder(@Body ReqPlaceOrder reqPlaceOrder);

    @POST("indexm")
    Call<ResMyOrders> getMyOrders(@Body ReqMyOrders reqMyOrders);

    @POST("indexm")
    Call<ResGetOrderDetail> getOrderDetail(@Body ReqGetOrderDetail reqGetOrderDetail);

    @POST("indexm")
    Call<ResBase> cancelOrder(@Body ReqCancelOrder reqCancelOrder);

    @POST("indexm")
    Call<ResBase> proceedOrder(@Body ReqProceed reqProceed);

    @POST("indexm")
    Call<ResBase> repeatOrder(@Body ReqRepeatOrder reqRepeatOrder);

    @POST("indexm")
    Call<ResContactUs> conatctUs(@Body ReqBase reqBase);

    @POST("indexm")
    Call<ResMyWallet> myWallet(@Body ReqMyWallet reqMyWallet);

    @POST("indexm")
    Call<ResBase> sendToBank(@Body ReqSendToBank reqSendToBank);

    @POST("indexm")
    Call<ResWalletHistoryReceived> getWalletHistoryReceived(@Body ReqWalletHistory reqWalletHistory);

    @POST("indexm")
    Call<ResWalletHistoryPaid> getWalletHistoryPaid(@Body ReqWalletHistory reqWalletHistory);

    @POST("indexm")
    Call<ResCashBackHistory> getCashbackHistory(@Body ReqCashbackHistory reqCashbackHistory);

    @POST("indexm")
    Call<ResCancellationDetails> getCancellationDetails(@Body ReqCancellationDetails reqCancellationDetails);

    @POST("indexm")
    Call<ResPrizeMoneyDetails> getPrizeMoneyDetails(@Body ReqPrizeMoneyDetails reqPrizeMoneyDetails);

    @POST("indexm")
    Call<ResRunkeeperData> getRunkeeperData(@Body ReqRunkeeperToken reqRunkeeperToken);

    @POST("indexm")
    Call<ResRunkeeperData> getFitbitData(@Body ReqFitbitToken reqFitbitToken);

    @POST("indexm")
    Call<ResPushNotification> getPushNotification(@Body ReqGetPushNotification reqGetPushNotification);

    @POST("indexm")
    Call<ResGetUserPointsByTime> getUserPointsByTime(@Body ReqGetUserPointsByTime reqGetUserPointsByTime);

    @POST("indexm")
    Call<ResBase> deleteAllNotification(@Body ReqDeleteAllNotification reqDeleteAllNotification);

    @POST("indexm")
    Call<ResBase> connectDisconnectApp(@Body ReqConnectDisconnectApps reqConnectDisconnectApps);

    @POST("indexm")
    Call<ResBase> redeemPointsAffiliate(@Body ReqRedeemPointsAffiliate reqRedeemPointsAffiliate);

    @POST("indexm")
    Call<ResBase> updateCartDetails(@Body ReqUpdateCart reqUpdateCart);

    @Multipart
    @POST("indexm")
    Call<ResUpdateProfile> updateProfile(@Part("method") RequestBody method,
                                         @Part("service_key") RequestBody serviceKey,
                                         @Part("userID") RequestBody userID,
                                         @Part("name") RequestBody name,
                                         @Part("phone") RequestBody phone,
                                         @Part("dob") RequestBody dob,
                                         @Part("locationName") RequestBody locationName,
                                         @Part("height") RequestBody height,
                                         @Part("weight") RequestBody weight,
                                         @Part MultipartBody.Part image);

    @Multipart
    @POST("indexm")
    Call<ResUpdateProfile> updateProfileWithoutImage(@Part("method") RequestBody method,
                                                     @Part("service_key") RequestBody serviceKey,
                                                     @Part("userID") RequestBody userID,
                                                     @Part("name") RequestBody name,
                                                     @Part("phone") RequestBody phone,
                                                     @Part("dob") RequestBody dob,
                                                     @Part("locationName") RequestBody locationName,
                                                     @Part("height") RequestBody height,
                                                     @Part("weight") RequestBody weight,
                                                     @Part("image") RequestBody image);

    @Multipart
    @POST("indexm")
    Call<ResUpdateProfile> updateSettings(@Part("method") RequestBody method,
                                          @Part("service_key") RequestBody serviceKey,
                                          @Part("userID") RequestBody userID,
                                          @Part("unit") RequestBody unit,
                                          @Part("notification") RequestBody notification,
                                          @Part("resetImage") RequestBody resetImage,
                                          @Part MultipartBody.Part homeScreen);

    @Multipart
    @POST("indexm")
    Call<ResUpdateProfile> updateSettingsWithoutImage(@Part("method") RequestBody method,
                                                      @Part("service_key") RequestBody serviceKey,
                                                      @Part("userID") RequestBody userID,
                                                      @Part("unit") RequestBody unit,
                                                      @Part("notification") RequestBody notification,
                                                      @Part("resetImage") RequestBody resetImage,
                                                      @Part("homeScreen") RequestBody homeScreen);

    @POST("indexm")
    Call<ResBase> setFavouriteCouponDetail(@Body ReqCouponsDetail reqCouponsDetail);

    @GET("http://api.runkeeper.com/records")
    Call<ResponseBody> getTotalDistance(@Header("Authorization") String authorization,
                                        @Header("Accept") String accept);

    @POST("https://runkeeper.com/apps/token")
    Call<ResponseBody> getAccessToken(@Query("grant_type") String grantKey,
                                      @Query("code") String code,
                                      @Query("client_id") String clientId,
                                      @Query("client_secret") String clientSecret,
                                      @Query("redirect_uri") String redirectUri);
}
