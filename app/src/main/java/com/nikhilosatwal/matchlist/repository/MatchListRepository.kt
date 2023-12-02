package com.nikhilosatwal.matchlist.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.nikhilosatwal.matchlist.api.RetrofitInstance
import com.nikhilosatwal.matchlist.models.MatchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchListRepository {

    suspend fun getMatches() = RetrofitInstance.api.getMatches()
}