package com.herpace.data.remote

import com.herpace.data.remote.dto.CreateRaceRequest
import com.herpace.data.remote.dto.GeneratePlanRequest
import com.herpace.data.remote.dto.LoginRequest
import com.herpace.data.remote.dto.LoginResponse
import com.herpace.data.remote.dto.RaceResponse
import com.herpace.data.remote.dto.RunnerProfileRequest
import com.herpace.data.remote.dto.RunnerProfileResponse
import com.herpace.data.remote.dto.SignupRequest
import com.herpace.data.remote.dto.SignupResponse
import com.herpace.data.remote.dto.TrainingPlanDetailResponse
import com.herpace.data.remote.dto.TrainingPlanResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HerPaceApiService {

    // Auth
    @POST("api/auth/signup")
    suspend fun signup(@Body request: SignupRequest): SignupResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // Profile
    @GET("api/profiles/me")
    suspend fun getProfile(): RunnerProfileResponse?

    @POST("api/profiles/me")
    suspend fun saveProfile(@Body request: RunnerProfileRequest): RunnerProfileResponse

    // Races
    @GET("api/races")
    suspend fun getRaces(): List<RaceResponse>

    @POST("api/races")
    suspend fun createRace(@Body request: CreateRaceRequest): RaceResponse

    @GET("api/races/{id}")
    suspend fun getRace(@Path("id") raceId: String): RaceResponse

    // Training Plans
    @POST("api/plans")
    suspend fun generatePlan(@Body request: GeneratePlanRequest): TrainingPlanResponse

    @GET("api/plans/active")
    suspend fun getActivePlan(): TrainingPlanDetailResponse?
}
