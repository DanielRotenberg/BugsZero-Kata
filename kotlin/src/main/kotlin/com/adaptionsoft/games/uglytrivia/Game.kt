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

        if (currentPlayer.inPenaltyBox) {
            if (roll % 2 != 0) {
                currentPlayer.isGettingOutOfPenaltyBox = true
                println(currentPlayer.name + " is getting out of the penalty box")
                movePlayerAndAskQuestion(roll)
            } else {
                println(currentPlayer.name + " is not getting out of the penalty box")
                currentPlayer.isGettingOutOfPenaltyBox = false
            }
        } else {
            movePlayerAndAskQuestion(roll)
        }

    }

    private fun movePlayerAndAskQuestion(roll: Int) {
        currentPlayer.place += roll
        if (currentPlayer.place > 11)
            currentPlayer.place -= 12

        playerLocationMessage(currentPlayer)
        questions.askQuestion(currentPlayer.place)
    }

    private fun playerLocationMessage(player: Player) {
        println("${player.name}'s new location is ${currentPlayer.place}")
    }


    fun wasCorrectlyAnswered(): Boolean {
        if (currentPlayer.inPenaltyBox) {
            return if (currentPlayer.isGettingOutOfPenaltyBox) {
                setPlayerIndex()
                println("Answer was correct!!!!")
                currentPlayer.coins++
                println("${currentPlayer.name} now has ${currentPlayer.coins} Gold Coins.")
                didPlayerWin()
            } else {
                setPlayerIndex()
                true
            }

        } else {

            println("Answer was correct!!!!")
            currentPlayer.coins++
            println("${currentPlayer.name} now has ${currentPlayer.coins} Gold Coins.")
            val winner = didPlayerWin()
            setPlayerIndex()

            return winner
        }
    }

    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(currentPlayer.name + " was sent to the penalty box")
        currentPlayer.inPenaltyBox = true

        setPlayerIndex()
        return true
    }

    private fun setPlayerIndex() {
        currentPlayerIndex++
        if (currentPlayerIndex == players.size) currentPlayerIndex = 0
    }

    private fun didPlayerWin(): Boolean = currentPlayer.coins != 6
}






