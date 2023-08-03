package com.adaptionsoft.games.uglytrivia

class Game(private val questions: QuestionsProvider) {
    val players = mutableListOf<Player>()

    private var currentPlayerIndex = 0
    private val currentPlayer
        get() = players[currentPlayerIndex]

    fun add(playerName: String): Boolean {
        players.add(Player(name = playerName))
        require(players.size < 7)
        println("$playerName was added")
        println("They are player number " + players.size)
        return true
    }

    fun roll(roll: Int) {
        require(players.size in 2..<7)
        println(currentPlayer.name + " is the current player")
        println("They have rolled a $roll")

        when {
            currentPlayer.rollForbidden(roll) -> {
                updatePlayerGettingOutTo(false)
                return
            }

            currentPlayer.canRollWithPenalty(roll) -> {
                updatePlayerGettingOutTo(true)
            }
        }
        movePlayerAndAskQuestion(roll)
    }

    private fun Player.rollForbidden(num: Int) = inPenaltyBox && !canGetOut(num)

    private fun Player.canGetOut(num: Int) = num % 2 != 0

    private fun Player.canRollWithPenalty(num: Int) = inPenaltyBox && canGetOut(num)


    private fun updatePlayerGettingOutTo(gettingOut: Boolean) {
        currentPlayer.isGettingOutOfPenaltyBox = gettingOut
        val messageParam = if (gettingOut) "" else " not"
        println(currentPlayer.name + " is$messageParam getting out of the penalty box")
    }

    private fun movePlayerAndAskQuestion(roll: Int) {
        currentPlayer.place += roll
        if (currentPlayer.place > 11)
            currentPlayer.place -= 12

        println("${currentPlayer.name}'s new location is ${currentPlayer.place}")
        questions.askQuestion(currentPlayer.place)
    }


    // false result will terminate the game
    fun wasCorrectlyAnswered(): Boolean {
        return when {
            currentPlayer.inPenaltyBox && currentPlayer.isGettingOutOfPenaltyBox -> {
                updatePlayerIndex()
                println("Answer was correct!!!!")
                currentPlayer.coins++
                println("${currentPlayer.name} now has ${currentPlayer.coins} Gold Coins.")
                didPlayerWin()
            }

            // player blocked
            currentPlayer.inPenaltyBox && !currentPlayer.isGettingOutOfPenaltyBox -> {
                updatePlayerIndex()
                true
            }

            // !currentPlayer.inPenaltyBox
            else -> {
                println("Answer was correct!!!!")
                currentPlayer.coins++
                println("${currentPlayer.name} now has ${currentPlayer.coins} Gold Coins.")

                return didPlayerWin().also { updatePlayerIndex() }
            }
        }
    }

//    private fun Player.

    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(currentPlayer.name + " was sent to the penalty box")
        currentPlayer.inPenaltyBox = true
        return true.also { updatePlayerIndex() }
    }

    private fun updatePlayerIndex() {
        currentPlayerIndex++
        if (currentPlayerIndex == players.size) currentPlayerIndex = 0
    }

    private fun didPlayerWin(): Boolean = currentPlayer.coins != 6
}






