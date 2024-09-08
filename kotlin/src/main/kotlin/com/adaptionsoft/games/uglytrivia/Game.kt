package com.adaptionsoft.games.uglytrivia

class Game(private val players: List<Player>) {

    private val questions: Map<Category, MutableList<String>> = mapOf(
        createQuestionsOf(Category.Pop, 49),
        createQuestionsOf(Category.Science, 49),
        createQuestionsOf(Category.Sports, 49),
        createQuestionsOf(Category.Rock, 49)
    )

    fun moveToNextPLayer(currentPlayer: Player): Player {
        val currentIndex = players.indexOf(currentPlayer)

        var newIndex = currentIndex + 1
        newIndex %= players.size

        return players[newIndex]
    }

    private var currentPlayer1: Player = players[0]


    /*
    *
    private val playersTest = listOf<Player>()

    private fun currentPlayer() {

    }

    private var player = Player("hello")

    // pure function
    private fun List<Player>.moveToNextPlayer(currentPlayer: Player): Player {
        val currentPosition = indexOf(currentPlayer)

        var newPosition = currentPosition + 1
        newPosition %= size

        return this[newPosition]

    }
    *
    *
    *
    * */

    private fun moveToNextPlayer(currentPosition: Int): Int {

        var newPosition = currentPosition + 1
        newPosition %= players.size

        return newPosition

        /* currentPlayer++
         if (currentPlayer == players.size) currentPlayer = 0*/
    }

    /*  val isPlayable: Boolean
          get() = howManyPlayers() >= 2*/


    private fun createQuestionsOf(type: Category, questions: Int): Pair<Category, MutableList<String>> {
        return type to List(questions) { index -> "$type Question $index" }.toMutableList()
    }


    private fun Int.isEven() = this % 2 == 0
    private fun Int.isOdd() = !isEven()

    fun roll(roll: Int) {
        require(players.size > 1)
        println(currentPlayer1.name + " is the current player")
        println("They have rolled a $roll")

        when {
            currentPlayer1.inPenaltyBox && roll.isEven() -> {
                println(currentPlayer1.name + " is not getting out of the penalty box")
                currentPlayer1.isGettingOutOfPenaltyBox = false
            }

            currentPlayer1.inPenaltyBox && roll.isOdd() -> {
                currentPlayer1.isGettingOutOfPenaltyBox = true

                println(currentPlayer1.name + " is getting out of the penalty box")
                movePlayerAndAskQuestion(roll)
            }

            else -> {
                movePlayerAndAskQuestion(roll)

            }
        }


    }

    private fun movePlayerAndAskQuestion(roll: Int) {
        currentPlayer1.updatePlace(roll)

        println("${currentPlayer1.name}'s new location is ${currentPlayer1.place}")
        println("The category is ${categoryByIndex(currentPlayer1.place)}")

        val question = askQuestion(currentPlayer1.place)
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
            currentPlayer1.inPenaltyBox && currentPlayer1.isGettingOutOfPenaltyBox -> {
                println("Answer was correct!!!!")
                val newPosition = moveToNextPlayer(players.indexOf(currentPlayer1))
                currentPlayer1 = players[newPosition]
                currentPlayer1.addCoin()
                val playerCoins = "${currentPlayer1.name} now has ${currentPlayer1.coins} Gold Coins."
                println(playerCoins)

                return currentPlayer1.didPlayerWin()
            }

            currentPlayer1.inPenaltyBox && !currentPlayer1.isGettingOutOfPenaltyBox -> {
                val newPosition = moveToNextPlayer(players.indexOf(currentPlayer1))
                currentPlayer1 = players[newPosition]
                return true
            }

            else -> {
                println("Answer was corrent!!!!")
                currentPlayer1.addCoin()

                val playerCoins = "${currentPlayer1.name} now has ${currentPlayer1.coins} Gold Coins."
                println(playerCoins)
                val winner = currentPlayer1.didPlayerWin()

                val newPosition = moveToNextPlayer(players.indexOf(currentPlayer1))
                currentPlayer1 = players[newPosition]

                return winner
            }
        }

    }


    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(currentPlayer1.name + " was sent to the penalty box")
        currentPlayer1.putInPenaltyBox()

        val newPosition = moveToNextPlayer(players.indexOf(currentPlayer1))
        currentPlayer1 = players[newPosition]
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