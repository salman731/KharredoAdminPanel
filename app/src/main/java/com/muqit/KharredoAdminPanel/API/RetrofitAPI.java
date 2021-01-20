package com.muqit.KharredoAdminPanel.API;

import com.muqit.KharredoAdminPanel.Fragments.DeleteBannerResponse;
import com.muqit.KharredoAdminPanel.Models.AffiliateMarketersResponse;
import com.muqit.KharredoAdminPanel.Models.BannersResponse;
import com.muqit.KharredoAdminPanel.Models.BlogResponse;
import com.muqit.KharredoAdminPanel.Models.CartData;
import com.muqit.KharredoAdminPanel.Models.CategoriesResponse;
import com.muqit.KharredoAdminPanel.Models.ChatHistoryData;
import com.muqit.KharredoAdminPanel.Models.ChatResponse;
import com.muqit.KharredoAdminPanel.Models.ChattingData;
import com.muqit.KharredoAdminPanel.Models.CommonResponse;
import com.muqit.KharredoAdminPanel.Models.CustomerResposne;
import com.muqit.KharredoAdminPanel.Models.EmailFeedResponse;
import com.muqit.KharredoAdminPanel.Models.EmployeeDetailData;
import com.muqit.KharredoAdminPanel.Models.EmployeesResponse;
import com.muqit.KharredoAdminPanel.Models.FeaturedProductsData;
import com.muqit.KharredoAdminPanel.Models.LoginResponse;
import com.muqit.KharredoAdminPanel.Models.NewUserResponse;
import com.muqit.KharredoAdminPanel.Models.OrderItemResponse;
import com.muqit.KharredoAdminPanel.Models.OrderResponse;
import com.muqit.KharredoAdminPanel.Models.ProductsResponse;
import com.muqit.KharredoAdminPanel.Models.SalesDetail;
import com.muqit.KharredoAdminPanel.Models.SalesResponse;
import com.muqit.KharredoAdminPanel.Models.UsersResponse;
import com.muqit.KharredoAdminPanel.Models.VendorDetailResponse;
import com.muqit.KharredoAdminPanel.Models.VendorsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @FormUrlEncoded
    @POST("/android/slugify.php")
    Call<CommonResponse> AddCategory(@Field("name") String str, @Field("category_id") String str2, @Field("photo") String str3);

    @FormUrlEncoded
    @POST("/android/empinsert.php")
    Call<NewUserResponse> AddEmployee(@Field("firstname") String str, @Field("lastname") String str2, @Field("email") String str3, @Field("password") String str4, @Field("phone") String str5, @Field("address") String str6, @Field("photo") String str7);

    @GET("/android/featuredprod.php")
    Call<CommonResponse> AddFeaturedProduct(@Query("id") String str);

    @FormUrlEncoded
    @POST("/android/add-product.php")
    Call<CommonResponse> AddProduct(@Field("name") String str, @Field("category_id") String str2, @Field("price") String str3, @Field("description") String str4, @Field("photo") String str5, @Field("qty") String str6, @Field("weight") String str7, @Field("dimension") String str8, @Field("shipping-price") String str9, @Field("sale_start") String str10, @Field("sale_end") String str11, @Field("sale") String str12);

    @FormUrlEncoded
    @POST("/android/userinsert.php")
    Call<NewUserResponse> AddUser(@Field("firstname") String str, @Field("lastname") String str2, @Field("email") String str3, @Field("password") String str4, @Field("phone") String str5, @Field("address") String str6, @Field("photo") String str7);

    @FormUrlEncoded
    @POST("/android/veninsert.php")
    Call<CommonResponse> AddVendor(@Field("fname") String str, @Field("lname") String str2, @Field("email") String str3, @Field("password") String str4, @Field("repassword") String str5, @Field("phone") String str6, @Field("address") String str7, @Field("logo") String str8, @Field("cnicImage") String str9, @Field("city") String str10, @Field("country") String str11, @Field("state") String str12, @Field("company") String str13);

    @GET("/android/prodblock.php")
    Call<CommonResponse> BlockProduct(@Query("id") String str, @Query("status") int i);

    @GET("/android/empblock.php")
    Call<CommonResponse> BlockUser(@Query("id") String str, @Query("status") int i);

    @GET("/android/blog.php")
    Call<BlogResponse> Blog();

    @GET("/android/orderstatus.php")
    Call<CommonResponse> ChangeOrderStatus(@Query("id") String str, @Query("status") String str2);

    @GET("/android/chat.php")
    Call<ChatResponse> Chat();

    @GET("/android/delcategory.php")
    Call<CommonResponse> DeleteCategory(@Query("id") String str);

    @GET("/android/feeddel.php")
    Call<EmailFeedResponse> DeleteEmailFeed(@Query("id") int i);

    @GET("/android/delbanner.php")
    Call<DeleteBannerResponse> Deletebanner(@Query("id") int i);

    @FormUrlEncoded
    @POST("/android/editproduct.php")
    Call<CommonResponse> EditProduct(@Field("id") String str, @Field("name") String str2, @Field("category") String str3, @Field("price") String str4, @Field("description") String str5, @Field("sale_start") String str6, @Field("sale_end") String str7, @Field("sale") String str8);

    @GET("/android/adminlogin.php")
    Call<LoginResponse> Login(@Query("email") String str, @Query("password") String str2);

    @GET("/android/brand.php")
    Call<CommonResponse> MakeBrand(@Query("id") String str);

    @GET("/android/wholesale.php")
    Call<CommonResponse> MakeFeatured(@Query("id") String str);

    @GET("/android/shop.php")
    Call<CommonResponse> MakeShop(@Query("id") String str);

    @GET("/android/wholesale.php")
    Call<CommonResponse> MakeWholeSale(@Query("id") String str);

    @GET("/android/orderitems.php")
    Call<OrderItemResponse> OrderItemDetails(@Query("id") String str);

    @GET("/android/catgegoryupd.php")
    Call<CommonResponse> UpdateCategory(@Query("id") String str, @Query("commission") String str2);

    @GET("/android/empupd.php")
    Call<CommonResponse> UpdateEmployee(@Query("id") String str, @Query("firstname") String str2, @Query("lastname") String str3, @Query("password") String str4, @Query("address") String str5, @Query("phone") String str6, @Query("email") String str7);

    @FormUrlEncoded
    @POST("/android/empimage.php")
    Call<CommonResponse> UpdateEmployeeImage(@Field("id") String str, @Field("photo") String str2);

    @GET("/android/venupd.php")
    Call<CommonResponse> UpdateVendor(@Query("id") String str, @Query("firstname") String str2, @Query("lastname") String str3, @Query("email") String str4, @Query("password") String str5, @Query("phone") String str6, @Query("address") String str7, @Query("city") String str8, @Query("country") String str9, @Query("state") String str10, @Query("company") String str11);

    @GET("/android/vendetail.php")  
    Call<VendorDetailResponse> VendorDetail(@Query("id") String str);

    @GET("/android/affilatemarketers.php")
    Call<AffiliateMarketersResponse> getAffiliateMarketersDetails();

    @GET("/android/banners.php")
    Call<BannersResponse> getBannersDetails();

    @GET("/android/categories.php")
    Call<CategoriesResponse> getCategoriesDetails();

    @GET("/android/cusdetail.php")
    Call<CustomerResposne> getCustomerInfo(@Query("id") String str);

    @GET("/android/searchemail.php")
    Call<EmailFeedResponse> getEmailFeedData();

    @GET("/android/empdetail.php")
    Call<EmployeeDetailData> getEmployeeInfo(@Query("id") String str);

    @GET("/android/employees.php")
    Call<EmployeesResponse> getEmployeesDetails();

    @GET("/android/featured-products.php")
    Call<ArrayList<FeaturedProductsData>> getFeaturedProductDetails();

    @GET("/android/allsales.php")
    Call<OrderResponse> getOrdersDetails();

    @GET("/android/all-products.php")
    Call<ProductsResponse> getProductsDetails();

    @GET("/android/sales.php")
    Call<SalesResponse> getSalesDetails();

    @GET("/android/userdetail.php")
    Call<EmployeeDetailData> getUserInfo(@Query("id") String str);

    @GET("/android/users.php")
    Call<UsersResponse> getUsersDetails();

    @GET("/android/vendors.php")
    Call<VendorsResponse> getVendorsDetails();

    @POST("/android/sale.php")
    @FormUrlEncoded
    Call<ArrayList<SalesDetail>> getSaleDetail(@Field("id") String ID);

    @POST("/android/blog_insertion.php")
    @FormUrlEncoded
    Call<CommonResponse> InsertBlog(@Field("title") String Title,@Field("t_image") String TitleImage,@Field("b_image") String BannerImage,@Field("t_des") String TitleDescription,@Field("des") String Description);

    @POST("/android/Add_new_banner.php")
    @FormUrlEncoded
    Call<CommonResponse> InsertBanner(@Field("cover") String BannerPhoto,@Field("heading") String Heading,@Field("description") String Description);

    @POST("/android/blog_update.php")
    @FormUrlEncoded
    Call<CommonResponse> UpdateBlog(@Field("title") String Title,@Field("t_image") String TitleImage,@Field("b_image") String BannerImage,@Field("t_des") String TitleDescription,@Field("des") String Description,@Field("id") String ID);

    @POST("/android/blog_delete.php")
    @FormUrlEncoded
    Call<CommonResponse> DeleteBlog(@Field("id") String ID);

    @POST("/android/newcart.php")
    @FormUrlEncoded
    Call<CommonResponse> AddCartProduct(@Field("user_id") String UID,@Field("product_id") String PID,@Field("quantity") String Quantity);

    @POST("/android/fetch_cart.php")
    @FormUrlEncoded
    Call<ArrayList<CartData>> getUserCartProducts(@Field("user_id") String UID);

    @POST("/android/delete_cart.php")
    @FormUrlEncoded
    Call<CommonResponse> deleteCartProduct(@Field("id") String ID);

    @POST("/android/update_cart.php")
    @FormUrlEncoded
    Call<CommonResponse> updateCartProduct(@Field("id") String ID,@Field("product_id") String ProductID,@Field("quantity") String Quantity);

    @GET("/android/fetch_chat.php")
    Call<ArrayList<ChatHistoryData>> getChatHistory();

    @POST("/android/chat2.php")
    @FormUrlEncoded
    Call<CommonResponse> sendMessage(@Field("user_id") String UID,@Field("message") String Message);

    @POST("/android/fetch_chating.php")
    @FormUrlEncoded
    Call<ArrayList<ChattingData>> getChattingData(@Field("user_id") String UID);
}

