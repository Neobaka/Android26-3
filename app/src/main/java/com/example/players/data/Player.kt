package com.example.players.data

data class FieldSpot(val x: Float, val y: Float, val isCurrent: Boolean = false)

data class PlayerStats(
    val games: Int,
    val goals: Int,
    val assists: Int,
    val yellow: Int,
    val red: Int
)

data class Player(
    val id: Int,
    val name: String,
    val number: Int,
    val age: Int,
    val position: String,
    val team: String,
    val socialUrl: String,
    val resumeUrl: String,
    val stats: PlayerStats,
    val fieldSpots: List<FieldSpot>,
    val comments: List<String>
)
