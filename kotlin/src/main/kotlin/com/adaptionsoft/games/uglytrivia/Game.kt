package com.adaptionsoft.games.uglytrivia

class Game(private val players: List<Player>) {


    private val questions: Map<Category, MutableList<String>> = mapOf(
        createQuestionsOf(Category.Pop, 49),
        createQuestionsOf(Category.Science, 49),
        createQuestionsOf(Category.Sports, 49),
        createQuestionsOf(Category.Rock, 49)
    )
    private var player = players[0]


    private fun List<Player>.moveToNextPLayer(currentPlayer: Player): Player {
        val currentIndex = players.indexOf(currentPlayer)

        var newIndex = currentIndex + 1
        newIndex %= size

        /* currentPlayer++
         if (currentPlayer == players.size) currentPlayer = 0*/

        return this[newIndex]
    }


    /*  val isPlayable: Boolean
          get() = howManyPlayers() >= 2*/


    private fun createQuestionsOf(type: Category, questions: Int): Pair<Category, MutableList<String>> {
        return type to List(questions) { index -> "$type Question $index" }.toMutableList()
    }


    private fun Int.isEven() = this % 2 == 0
    private fun Int.isOdd() = !isEven()

    fun roll(roll: Int) {
        println(player.name + " is the current player")
        println("They have rolled a $roll")

        when {
            player.inPenaltyBox && roll.isEven() -> {
                println(player.name + " is not getting out of the penalty box")
            }

            player.inPenaltyBox && roll.isOdd() -> {
                player.inPenaltyBox = false
                println(player.name + " is getting out of the penalty box")

                updateCategoryAndAskQuestion(roll)
            }

            else -> {
                updateCategoryAndAskQuestion(roll)

            }
        }


    }

    private fun updateCategoryAndAskQuestion(roll: Int) {
        player.place = player.updatePlace(roll)

        println("${player.name}'s new location is ${player.place}")
        println("The category is ${categoryByIndex(player.place)}")

        val question = askQuestion(player.place)
        println(question)
    }


    private fun askQuestion(category: Int): String {
        return questions[Category.valueOf(categoryByIndex(category))]!!.removeFirst()

    }


    fun wasCorrectlyAnswered(): Boolean {
        return if (player.inPenaltyBox) {
            player = players.moveToNextPLayer(player)
            return true
        } else {
            return checkForWinner()
        }

    }

    private fun checkForWinner(): Boolean {
        println("Answer was correct!!!!")
        player.coins++

        val playerCoins = "${player.name} now has ${player.coins} Gold Coins."
        println(playerCoins)

        return if (player.didWin()) {
            false
        } else {
            player = players.moveToNextPLayer(player)
            true
        }
    }


    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(player.name + " was sent to the penalty box")

        player.inPenaltyBox = true
        player = players.moveToNextPLayer(player)
        return true
    }

}


fun gameWith(vararg playersNames: String): Game {
    require(playersNames.size in (2..6))

    playersNames.forEachIndexed { index, name ->
        println("$name was added")
        println("They are player number ${index.inc()}")
    }

    val players = playersNames.map { name -> Player(name) }.toMutableList()
    return Game(players)


}

/*
* Existing BUG: A player that gets into prison always stays there. ** Other than just fixing the bug,
* try to understand what's wrong with the design and fix the root cause
*
Existing BUG: coins are added to the wrong player.
* Try to understand what made this bug likely and fix the design so that it becomes very unlikely.
*
* */