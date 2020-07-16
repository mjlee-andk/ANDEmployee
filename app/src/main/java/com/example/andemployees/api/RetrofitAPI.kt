package com.example.andemployees.api

import com.example.andemployees.models.Result
import okhttp3.Interceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

public interface RetrofitAPI {
    // 로그인
    @FormUrlEncoded
    @POST("/api/user/login")
    fun login(
            @Field("account") account: String,
            @Field("password") password: String
    )
    : Call<Result.ResultLogin>

    // 비밀번호 변경하기
    @FormUrlEncoded
    @POST("/api/user/changepassword")
    fun changePassword(
            @Field("user_id") user_id: String,
            @Field("change_password") change_password: String
    )
    : Call<Result.ResultChangePassword>

    // 직원 목록 조회하기
    @GET("/api/employees")
    fun getEmployees(
            @Query("search") search: String,
            @Query("division_id") division_id: String,
            @Query("department_id") department_id: String
    )
    : Call<Result.ResultEmployees>

    // 직원 조회하기
    @GET("/api/employee")
    fun getEmployee(
        @Query("employee_id") employee_id: String
    )
    : Call<Result.ResultEmployee>

    // 메모 조회하기
    @GET("/api/memo")
    fun getMemo(
        @Query("user_id") user_id: String,
        @Query("employee_id") employee_id: String
    )
    : Call<Result.ResultMemo>

    // 메모 추가하기
    @FormUrlEncoded
    @POST("/api/memo/add")
    fun addMemo(
        @Field("user_id") user_id: String,
        @Field("employee_id") employee_id: String,
        @Field("memo") memo: String
    )
    : Call<Result.ResultMemo>

    // 메모 수정하기
    @FormUrlEncoded
    @POST("/api/memo/update")
    fun updateMemo(
        @Field("memo_id") memo_id: String,
        @Field("memo") memo: String
    )
    : Call<Result.ResultBasic>

    // 부서 조회하기
    @GET("/api/departments")
    fun getDepartments(
//        @Query("division_id") division_id: String
    )
    : Call<Result.ResultDepartments>

    companion object {
        private const val BASE_URL = "http://121.126.225.132:3000"
//        private const val CLIENT_ID = "네이버_개발자센터_아이디"
//        private const val CLIENT_SECRET = "네이버_개발자센터_비밀번호"

        fun create(): RetrofitAPI {
//            val httpLoggingInterceptor = HttpLoggingInterceptor()
//            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            // 고정 헤더값에 사용
            val headerInterceptor = Interceptor {
                val request = it.request()
                        .newBuilder()
//                        .addHeader("X-Naver-Client-Id", CLIENT_ID)
//                        .addHeader("X-Naver-Client-Secret", CLIENT_SECRET)
                        .build()
                return@Interceptor it.proceed(request)
            }

//            val client = OkHttpClient.Builder()
//                    .addInterceptor(headerInterceptor)
////                    .addInterceptor(httpLoggingInterceptor)
//                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RetrofitAPI::class.java)
        }
    }
}
