package com.nikhilosatwal.matchlist.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikhilosatwal.matchlist.repository.MatchListRepository

class MatchesViewModelProviderFactory(
    val app: Application,
    val newsRepository: MatchListRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MatchesViewModel(app, newsRepository) as T
    }
}