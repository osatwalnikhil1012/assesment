package com.nikhilosatwal.matchlist.models

data class Matches(
    val mEventId: String,
    val eventId: String,
    val eventName: String,
    val marketId: String,
    val marketName: String,
    val competitionId: String,
    val competitionName: String,
    val sportId: String,
    val sportName: String,
    val oddsProvider: String,
    val fancyProvider: String,
    val openDate: String,
    val type: String,
    val isActive: Boolean,
    val isResult: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val unixDate: Long,
)
