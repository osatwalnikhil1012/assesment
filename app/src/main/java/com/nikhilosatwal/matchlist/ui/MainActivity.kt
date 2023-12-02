package com.nikhilosatwal.matchlist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikhilosatwal.matchlist.R
import com.nikhilosatwal.matchlist.adapter.MatchesAdapter
import com.nikhilosatwal.matchlist.databinding.ActivityMainBinding
import com.nikhilosatwal.matchlist.models.Matches
import com.nikhilosatwal.matchlist.repository.MatchListRepository
import com.nikhilosatwal.matchlist.utils.Resource
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var matchesViewModel: MatchesViewModel
    lateinit var cricketAdapter : MatchesAdapter
    lateinit var tennisAdapter : MatchesAdapter
    lateinit var soccerAdapter : MatchesAdapter
    private var cricketList : ArrayList<Matches> = ArrayList()
    private var soccerList : ArrayList<Matches> = ArrayList()
    private var tennisList : ArrayList<Matches> = ArrayList()
    private var allMatchList : MutableList<Matches> = ArrayList()
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)

        val matchesRepository = MatchListRepository()
        val viewModelProviderFactory = MatchesViewModelProviderFactory(application, matchesRepository)
        matchesViewModel = ViewModelProvider(this, viewModelProviderFactory).get(MatchesViewModel::class.java)
        matchesViewModel.matches.observe(this, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    response.data?.let { matchesResponse ->
                        allMatchList = matchesResponse.data
                        for (data in matchesResponse.data) {
                            when(data.sportId) {
                                "1" -> soccerList.add(data)
                                "2" -> tennisList.add(data)
                                "4" -> cricketList.add(data)
                            }
                        }
                        binding.cricketRv.layoutManager = LinearLayoutManager(this)
                        binding.tennisRv.layoutManager = LinearLayoutManager(this)
                        binding.soccerRv.layoutManager = LinearLayoutManager(this)

                        cricketAdapter = MatchesAdapter(this, cricketList)
                        tennisAdapter = MatchesAdapter(this, tennisList)
                        soccerAdapter = MatchesAdapter(this, soccerList)

                        binding.cricketRv.adapter = cricketAdapter
                        binding.tennisRv.adapter = tennisAdapter
                        binding.soccerRv.adapter = soccerAdapter
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(this, "Error Occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                }
            }
        })

        binding.toggle.addOnButtonCheckedListener { group, checkedId, isChecked ->

            if (isChecked) {
                cricketList.clear()
                tennisList.clear()
                soccerList.clear()
                when(checkedId) {
                    R.id.inPlayButton -> {
                        for (data in allMatchList) {
                            val openDate = data.openDate
                            val openDateMilli = convertDateStringToMilliseconds(openDate)
                            val currentTime = System.currentTimeMillis()
                            if (openDateMilli <= currentTime) {
                                when(data.sportId) {
                                    "1" -> soccerList.add(data)
                                    "2" -> tennisList.add(data)
                                    "4" -> cricketList.add(data)
                                }
                            }
                        }
                        println()
                    }
                    R.id.todayButton -> {
                        for (data in allMatchList) {
                            val openDate = data.openDate
                            val openDateMilli = convertDateStringToMilliseconds(openDate)
                            val currentTime = System.currentTimeMillis()
                            if (openDateMilli > currentTime && openDateMilli<getTomorrowInMillis()) {
                                when(data.sportId) {
                                    "1" -> soccerList.add(data)
                                    "2" -> tennisList.add(data)
                                    "4" -> cricketList.add(data)
                                }
                            }
                        }
                    }
                    R.id.tomorrowButton -> {
                        for (data in allMatchList) {
                            val openDate = data.openDate
                            val openDateMilli = convertDateStringToMilliseconds(openDate)
                            val currentTime = System.currentTimeMillis()
                            if (openDateMilli>getTomorrowInMillis()) {
                                when(data.sportId) {
                                    "1" -> soccerList.add(data)
                                    "2" -> tennisList.add(data)
                                    "4" -> cricketList.add(data)
                                }
                            }
                        }
                    }
                }
            }
            tennisAdapter.notifyDataSetChanged()
            cricketAdapter.notifyDataSetChanged()
            soccerAdapter.notifyDataSetChanged()
//            binding.cricketRv.adapter = MatchesAdapter(this, cricketList)
//            binding.tennisRv.adapter = MatchesAdapter(this, tennisList)
//            binding.soccerRv.adapter = MatchesAdapter(this, soccerList)
        }
    }
    fun convertDateStringToMilliseconds(dateString: String): Long {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
        val date = dateFormat.parse(dateString)

        return date?.time ?: 0L
    }

    fun getTomorrowInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1) // Add 1 day to get tomorrow's date

        return calendar.timeInMillis
    }
}