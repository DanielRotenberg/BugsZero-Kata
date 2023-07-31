package com.adaptionsoft.games.uglytrivia

class Game {
    val players = mutableListOf<Player>()
    var places = IntArray(6)
    var inPenaltyBox = BooleanArray(6)

    var popQuestions = mutableListOf<String>()
    var scienceQuestions = mutableListOf<String>()
    var sportsQuestions = mutableListOf<String>()
    var rockQuestions = mutableListOf<String>()

    var currentPlayer = 0
    var isGettingOutOfPenaltyBox: Boolean = false

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

        /* places[howManyPlayers()-1] = 0
         purses[howManyPlayers()-1] = 0
         inPenaltyBox[howManyPlayers()-1] = false*/

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

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true

                println(players.get(currentPlayer).name + " is getting out of the penalty box")
                movePlayerAndAskQuestion(roll)
            } else {
                println(players.get(currentPlayer).name + " is not getting out of the penalty box")
                isGettingOutOfPenaltyBox = false
            }

        } else {

            movePlayerAndAskQuestion(roll)
        }

    }

    private fun movePlayerAndAskQuestion(roll: Int) {
        places[currentPlayer] = places[currentPlayer] + roll
        if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12

        println(
            players.get(currentPlayer).name
                    + "'s new location is "
                    + places[currentPlayer]
        )
        println("The category is " + currentCategory())
        askQuestion()
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
            places[currentPlayer] == 0 -> "Pop"
            places[currentPlayer] == 4 -> "Pop"
            places[currentPlayer] == 8 -> "Pop"
            places[currentPlayer] == 1 -> "Science"
            places[currentPlayer] == 5 -> "Science"
            places[currentPlayer] == 9 -> "Science"
            places[currentPlayer] == 2 -> "Sports"
            places[currentPlayer] == 6 -> "Sports"
            else -> if (places[currentPlayer] == 10) "Sports" else "Rock"
        }
    }

    fun wasCorrectlyAnswered(): Boolean {
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                showMessage(Messages.CORRECT_MESSAGE)
                currentPlayer++
                if (currentPlayer == players.size) currentPlayer = 0
                players.get(currentPlayer).coins++
                println(
                    players.get(currentPlayer).name
                            + " now has "
                            + players.get(currentPlayer).coins
                            + " Gold Coins."
                )

                return didPlayerWin()
            } else {
                currentPlayer++
                if (currentPlayer == players.size) currentPlayer = 0
                return true
            }


        } else {

            showMessage(Messages.CORRECT_MESSAGE)
            players.get(currentPlayer).coins++
            println(
                players.get(currentPlayer).name
                        + " now has "
                        + players.get(currentPlayer).coins
                        + " Gold Coins."
            )

            val winner = didPlayerWin()
            currentPlayer++
            if (currentPlayer == players.size) currentPlayer = 0

            return winner
        }
    }

    private fun showMessage(content: String) {
        println(content)
    }

    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(players.get(currentPlayer).name + " was sent to the penalty box")
        inPenaltyBox[currentPlayer] = true

        currentPlayer++
        if (currentPlayer == players.size) currentPlayer = 0
        return true
    }


    private fun didPlayerWin(): Boolean {
        return players.get(currentPlayer).coins != 6
//        return purses[currentPlayer] != 6
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