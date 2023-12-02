package com.nikhilosatwal.matchlist.models

data class MatchResponse(
    val message : String,
    val data : MutableList<Matches>
)