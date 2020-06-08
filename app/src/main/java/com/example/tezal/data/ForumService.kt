

package com.example.tezal.data

import com.example.tezal.model.*

import retrofit2.Call
import retrofit2.http.*


interface ForumService {

    //@GET("/api/index.php")
    //fun getDate(): Call<MutableList<LoggedInUser>>


   /* @POST("/api/authenticate")
    @FormUrlEncoded
    fun setDate(
            @Field("username") username: String?,
            @Field("password") password: String?

    ):Call<JwtResponse>*/

    @Headers("Content-Type: application/json")
    @POST("/api/authenticate")
    fun setDate(
        @Body request: AuthModel): Call<JwtResponse> // body data

    @Headers("Content-Type: application/json")
    @POST("/api/client")
    fun setDate(
        @Body request: ClientInfo): Call<Client> // body data

    @Headers("Content-Type: application/json")
    @POST("/api/clientDevice")
    fun setDatePassword(
        @Body request: ClientSetPassword): Call<ClientResponseAfterSetPassword> // body data

    @GET("/api/orgCategory/all")
    fun getCategory():Call<MutableList<OrgCategoryItem>>

    @GET("/api/organization/list/category/{id}")
    fun getOrgListCategory(//@Header("Authorization") token: String,
                           @Path("id")
                           id: Int?):Call<MutableList<OrgListCategoryItem>>

    @GET("api/rate/model/list/{orgId}")
    fun getRawList(//@Header("Authorization") token: String,
        @Path("orgId") orgId: Int?):Call<MutableList<RawListItem>>

    @POST("api/order")
    fun setDate(
        @Body request: Order): Call<OrderList> // body data

    @POST("api/order_material")
    fun setDate(
        @Body request: Order_material): Call<ResponseOrder_material> // body data

    @GET("api/clientDevice/byClientId/{id}")
    fun getUser(//@Header("Authorization") token: String,
        @Path("id") id: Long?):Call<ClientResponseAfterSetPassword>

    @PUT("api/client/{id}")
    fun setDate(
        @Path("id") id: Long?, @Body request: ClientInfo): Call<ClientResponseAfterSetPassword> // body data

    @PUT("api/clientDevice/{id}")
    fun setDate(
        @Path("id") id: Long?, @Body request: ClientDevice): Call<ClientResponseAfterSetPassword> // body data

    @GET("api/order/all/client/{id}")
    fun getHistory(//@Header("Authorization") token: String,
        @Path("id") id: Long?):Call<List<OrderHistoryItem>>

    @GET("api/order_material/all")
    fun getHistoryDetails(//@Header("Authorization") token: String,
        @Query("orderId") orderId: Long?):Call<RawHistory>



}