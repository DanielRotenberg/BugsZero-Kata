package com.adaptionsoft.games.uglytrivia

data class Player(
    val name: String,
    var place: Int = 0,
    var coins: Int = 0,
    var inPenaltyBox: Boolean = false,
    var isGettingOutOfPenaltyBox: Boolean = false
) {

    fun putInPenaltyBox() {
        inPenaltyBox = true
    }

    fun addCoin() {
        coins++
    }

}

fun Player.updatePlace(roll:Int):Int{
    var newPlace = place + roll
    if (newPlace > 11) newPlace -= 12
    return newPlace
}

fun Player.didWin() = coins != 6

// to-do -> extract place/category since it's not part of the player, it's part of the game



