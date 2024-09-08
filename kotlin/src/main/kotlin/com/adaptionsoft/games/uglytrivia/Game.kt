package com.adaptionsoft.games.uglytrivia

class Game(private val players: List<Player>) {

    private val questions: Map<Category, MutableList<String>> = mapOf(
        createQuestionsOf(Category.Pop, 49),
        createQuestionsOf(Category.Science, 49),
        createQuestionsOf(Category.Sports, 49),
        createQuestionsOf(Category.Rock, 49)
    )
    private var currentPlayer = players[0]


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
        println(currentPlayer.name + " is the current player")
        println("They have rolled a $roll")

        when {
            currentPlayer.inPenaltyBox && roll.isEven() -> {
                println(currentPlayer.name + " is not getting out of the penalty box")
                currentPlayer.isGettingOutOfPenaltyBox = false
            }

            currentPlayer.inPenaltyBox && roll.isOdd() -> {
                currentPlayer.isGettingOutOfPenaltyBox = true

                println(currentPlayer.name + " is getting out of the penalty box")
                movePlayerAndAskQuestion(roll)
            }

            else -> {
                movePlayerAndAskQuestion(roll)

            }
        }


    }

    private fun movePlayerAndAskQuestion(roll: Int) {
        currentPlayer.updatePlace(roll)

        println("${currentPlayer.name}'s new location is ${currentPlayer.place}")
        println("The category is ${categoryByIndex(currentPlayer.place)}")

        val question = askQuestion(currentPlayer.place)
        println(question)
    }


    private fun askQuestion(category: Int): String {
        return questions[Category.valueOf(categoryByIndex(category))]!!.removeFirst()

    }


    fun wasCorrectlyAnswered(): Boolean {

        /*
        * 1. in penalty && getting out
        * 2. in penalty && not getting out
        * 3. not in penalty
        * */
        return when {
            currentPlayer.inPenaltyBox && currentPlayer.isGettingOutOfPenaltyBox -> {
                println("Answer was correct!!!!")

                currentPlayer = players.moveToNextPLayer(currentPlayer)
                currentPlayer.addCoin()
                val playerCoins = "${currentPlayer.name} now has ${currentPlayer.coins} Gold Coins."
                println(playerCoins)

                return currentPlayer.didPlayerWin()
            }

            currentPlayer.inPenaltyBox && !currentPlayer.isGettingOutOfPenaltyBox -> {
                currentPlayer = players.moveToNextPLayer(currentPlayer)
                return true
            }

            else -> {
                println("Answer was corrent!!!!")
                currentPlayer.addCoin()

                val playerCoins = "${currentPlayer.name} now has ${currentPlayer.coins} Gold Coins."
                println(playerCoins)
                val winner = currentPlayer.didPlayerWin()

                currentPlayer = players.moveToNextPLayer(currentPlayer)

                return winner
            }
        }

    }


    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(currentPlayer.name + " was sent to the penalty box")
        currentPlayer.putInPenaltyBox()

        currentPlayer = players.moveToNextPLayer(currentPlayer)
        return true
    }

}


//players >= 2 <= 6
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
* cardIndex = cardIndex + someStep
if (cardIndex > cards.length) {
cardIndex = 0
}
let card = cards[cardIndex]

// Use of a modulo would be simpler and less error-prone.

cardIndex = (cardIndex + someIndex) % deckOfCards.length
let card = deckOfCards[cardIndex]
*
* */