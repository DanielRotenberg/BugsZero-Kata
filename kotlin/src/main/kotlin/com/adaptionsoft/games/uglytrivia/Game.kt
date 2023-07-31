package com.adaptionsoft.games.uglytrivia

class Game {
    val players = mutableListOf<Player>()

    var popQuestions = mutableListOf<String>()
    var scienceQuestions = mutableListOf<String>()
    var sportsQuestions = mutableListOf<String>()
    var rockQuestions = mutableListOf<String>()

    fun currentPlayerObj(index:Int = 0) = players[index]
    var currentPlayer = 0

    fun Int.currentPlayerExt() = players[this]


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



        println(playerName + " was added")
        println("They are player number " + players.size)
        return true
    }

    /*    fun howManyPlayers(): Int {
            return players.size
        }*/

    fun roll(roll: Int) {
        require(players.size in 2..<7)
        println(players.get(currentPlayer).name + " is the current player")
        println("They have rolled a " + roll)

        if (players.get(currentPlayer).inPenaltyBox) {
            if (roll % 2 != 0) {
                players.get(currentPlayer).isGettingOutOfPenaltyBox = true

                println(players.get(currentPlayer).name + " is getting out of the penalty box")
                movePlayerAndAskQuestion(roll)
            } else {
                println(players.get(currentPlayer).name + " is not getting out of the penalty box")
                players.get(currentPlayer).isGettingOutOfPenaltyBox = false
            }

        } else {

            movePlayerAndAskQuestion(roll)
        }

    }

    private fun movePlayerAndAskQuestion(roll: Int) {
        currentPlayer.currentPlayerExt().place = currentPlayer.currentPlayerExt().place + roll
        if (currentPlayer.currentPlayerExt().place > 11)
            currentPlayer.currentPlayerExt().place = currentPlayer.currentPlayerExt().place - 12

        playerLocationMessage(players.get(currentPlayer))
        println("The category is " + currentCategory())
        askQuestion()
    }

    private fun playerLocationMessage(player: Player) {
        println(
            player.name
                    + "'s new location is "
                    + currentPlayer.currentPlayerExt().place
//                    + places[currentPlayer]
        )
    }

    private fun askQuestion() {
        when {
            currentCategory() === "Pop" -> println(popQuestions.removeFirst())
            currentCategory() === "Science" -> println(scienceQuestions.removeFirst())
            currentCategory() === "Sports" -> println(sportsQuestions.removeFirst())
            currentCategory() === "Rock" -> println(rockQuestions.removeFirst())
        }
    }


    private fun currentCategory(): String {
        return when {
            currentPlayer.currentPlayerExt().place == 0 -> "Pop"
            currentPlayer.currentPlayerExt().place == 4 -> "Pop"
            currentPlayer.currentPlayerExt().place == 8 -> "Pop"
            currentPlayer.currentPlayerExt().place == 1 -> "Science"
            currentPlayer.currentPlayerExt().place == 5 -> "Science"
            currentPlayer.currentPlayerExt().place == 9 -> "Science"
            currentPlayer.currentPlayerExt().place == 2 -> "Sports"
            currentPlayer.currentPlayerExt().place == 6 -> "Sports"
            else -> if (currentPlayer.currentPlayerExt().place == 10) "Sports" else "Rock"
        }
    }

    fun wasCorrectlyAnswered(): Boolean {
        if (currentPlayer.currentPlayerExt().inPenaltyBox) {
            return if (currentPlayer.currentPlayerExt().isGettingOutOfPenaltyBox) {
                showMessage(Messages.CORRECT_MESSAGE)
                currentPlayer++
                if (currentPlayer == players.size) currentPlayer = 0
                currentPlayer.currentPlayerExt().coins++
                numberOfCoinsMessage(players[currentPlayer])

                didPlayerWin()
            } else {
                currentPlayer++
                if (currentPlayer == players.size) currentPlayer = 0
                true
            }


        } else {

            showMessage(Messages.CORRECT_MESSAGE)
            players.get(currentPlayer).coins++
            numberOfCoinsMessage(players[currentPlayer])

            val winner = didPlayerWin()
            currentPlayer++
            if (currentPlayer == players.size) currentPlayer = 0

            return winner
        }
    }

    // TODO: convert to extension function
    private fun numberOfCoinsMessage(player: Player) {
        println("${player.name} now has ${player.coins} Gold Coins.")
    }

    private fun showMessage(content: String) {
        println(content)
    }

    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(players.get(currentPlayer).name + " was sent to the penalty box")
        currentPlayer.currentPlayerExt().inPenaltyBox = true

        currentPlayer++
        if (currentPlayer == players.size) currentPlayer = 0
        return true
    }


    private fun didPlayerWin(): Boolean {
        return players.get(currentPlayer).coins != 6
    }
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

