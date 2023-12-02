package com.nikhilosatwal.matchlist.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nikhilosatwal.matchlist.MatchesListApplication
import com.nikhilosatwal.matchlist.models.MatchResponse
import com.nikhilosatwal.matchlist.repository.MatchListRepository
import com.nikhilosatwal.matchlist.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MatchesViewModel(app : Application,
    val matchesRepository: MatchListRepository) : AndroidViewModel(app) {

    val matches: MutableLiveData<Resource<MatchResponse>> = MutableLiveData()
    var matchesResponse : MatchResponse?= null

    init {
        getMatchesList()
    }

    fun getMatchesList() =viewModelScope.launch {
        safeMatchesListCall()
    }

    private suspend fun safeMatchesListCall() {
        matches.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = matchesRepository.getMatches()
                matches.postValue(handleMatchesResponse(response))
            } else {
                matches.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t : Throwable) {
            when(t){
                is IOException -> matches.postValue(Resource.Error("Network Failure"))
                else -> matches.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleMatchesResponse(response: Response<MatchResponse>): Resource<MatchResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (matchesResponse == null) {
                    matchesResponse = resultResponse
                }
                return Resource.Success(matchesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =  getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork?:return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}