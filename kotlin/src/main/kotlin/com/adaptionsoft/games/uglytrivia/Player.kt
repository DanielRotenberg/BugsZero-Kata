package com.adaptionsoft.games.uglytrivia

class Player(
    val name: String,
    var place: Int = 0,
    var coins: Int = 0,
    var inPenaltyBox: Boolean = false,
    var isGettingOutOfPenaltyBox: Boolean = false
) {
    fun updatePlace(roll: Int) {
        // incrementing the index, carefully not to introduce bug
        place += roll
        if (place > 11) place -= 12
    }

     fun didPlayerWin(): Boolean {
        return coins != 6
    }


     fun putInPenaltyBox() {
        inPenaltyBox = true
    }


     fun addCoin() {
       coins++
    }

}
// to-do -> extract place/category since it's not part of the player, it's part of the game