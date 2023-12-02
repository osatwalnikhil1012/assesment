package com.nikhilosatwal.matchlist.api

import com.nikhilosatwal.matchlist.models.MatchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MatchesAPI {

    @GET("getLiveMatches")
    suspend fun getMatches(): Response<MatchResponse>
}