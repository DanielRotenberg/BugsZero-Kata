package com.adaptionsoft.games.uglytrivia

class Game {
    val players = mutableListOf<Player>()

    var popQuestions = mutableListOf<String>()
    var scienceQuestions = mutableListOf<String>()
    var sportsQuestions = mutableListOf<String>()
    var rockQuestions = mutableListOf<String>()

    private var currentPlayerIndex = 0
    private val currentPlayer
        get() = players[currentPlayerIndex]


    init {
        for (i in 0..49) {
            popQuestions.addLast("Pop Question " + i)
            scienceQuestions.addLast("Science Question " + i)
            sportsQuestions.addLast("Sports Question " + i)
            rockQuestions.addLast(createRockQuestion(i))
        }
    }

    fun createRockQuestion(index: Int): String {
        return "Rock Question " + index
    }

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
            currentPlayer.place = currentPlayer.place - 12

        playerLocationMessage(currentPlayer)
        println("The category is " + currentCategory())
        askQuestion()
    }

    private fun playerLocationMessage(player: Player) {
        println("${player.name}'s new location is ${currentPlayer.place}")
    }

    private fun askQuestion() {
        when (currentCategory()) {
            "Pop" -> println(popQuestions.removeFirst())
            "Science" -> println(scienceQuestions.removeFirst())
            "Sports" -> println(sportsQuestions.removeFirst())
            "Rock" -> println(rockQuestions.removeFirst())
        }
    }

    private fun currentCategory(): String {
        return when (currentPlayer.place) {
            0, 4, 8 -> "Pop"
            1, 5, 9 -> "Science"
            2, 6 -> "Sports"
            else -> if (currentPlayer.place == 10) "Sports" else "Rock"
        }
    }

    fun wasCorrectlyAnswered(): Boolean {
        if (currentPlayer.inPenaltyBox) {
            return if (currentPlayer.isGettingOutOfPenaltyBox) {
                showMessage(Messages.CORRECT_MESSAGE)
                setPlayerIndex()
                currentPlayer.coins++
                currentPlayer.numberOfCoinsMessage()
                didPlayerWin()
            } else {
                setPlayerIndex()
                true
            }

        } else {

            showMessage(Messages.CORRECT_MESSAGE)
            currentPlayer.coins++
            currentPlayer.numberOfCoinsMessage()
            val winner = didPlayerWin()
            setPlayerIndex()

            return winner
        }
    }
    private fun Player.numberOfCoinsMessage() {
        println("$name now has $coins Gold Coins.")
    }

    private fun showMessage(content: String) {
        println(content)
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


object Messages {
    const val CORRECT_MESSAGE = "Answer was correct!!!!"
}


fun MutableList<String>.removeFirst(): String {
    return this.removeAt(0)
}

fun MutableList<String>.addLast(element: String) {
    this.add(element)
}

