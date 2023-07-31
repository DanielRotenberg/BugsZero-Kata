package com.adaptionsoft.games.uglytrivia

data class Player(val position: Int = -1, val name: String, var coins: Int = 0, var inPenaltyBox: Boolean = false)
