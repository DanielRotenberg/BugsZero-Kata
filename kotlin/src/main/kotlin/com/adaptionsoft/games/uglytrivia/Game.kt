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
                player.isGettingOutOfPenaltyBox = false
            }

            player.inPenaltyBox && roll.isOdd() -> {
                player.isGettingOutOfPenaltyBox = true

                println(player.name + " is getting out of the penalty box")
                movePlayerAndAskQuestion(roll)
            }

            else -> {
                movePlayerAndAskQuestion(roll)

            }
        }


    }

    private fun movePlayerAndAskQuestion(roll: Int) {
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

        /*
        * 1. in penalty && getting out
        * 2. in penalty && not getting out
        * 3. not in penalty
        * */
        return when {
            player.inPenaltyBox && player.isGettingOutOfPenaltyBox -> {
                println("Answer was correct!!!!")

                player = players.moveToNextPLayer(player)
                player.addCoin()
                val playerCoins = "${player.name} now has ${player.coins} Gold Coins."
                println(playerCoins)

                return player.didWin()
            }

            player.inPenaltyBox && !player.isGettingOutOfPenaltyBox -> {
                player = players.moveToNextPLayer(player)
                return true
            }

            else -> {
                println("Answer was corrent!!!!")
                player.addCoin()

                val playerCoins = "${player.name} now has ${player.coins} Gold Coins."
                println(playerCoins)
                val winner = player.didWin()

                player = players.moveToNextPLayer(player)

                return winner
            }
        }

    }


    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(player.name + " was sent to the penalty box")
        player.putInPenaltyBox()

        player = players.moveToNextPLayer(player)
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