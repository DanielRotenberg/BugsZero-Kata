package com.adaptionsoft.games.uglytrivia

class Game {
    private val players = mutableListOf<Player>()

    private val questions: Map<Category, MutableList<String>> = mapOf(
        createQuestionsOf(Category.Pop, 49),
        createQuestionsOf(Category.Science, 49),
        createQuestionsOf(Category.Sports, 49),
        createQuestionsOf(Category.Rock, 49)
    )
    var currentPlayer = 0
    var currentPlayer1: Player? = null
        get() = players[currentPlayer]


    private fun moveToNextPlayer() {
        currentPlayer++
        currentPlayer %= players.size
        /* currentPlayer++
         if (currentPlayer == players.size) currentPlayer = 0*/
    }

    /*  val isPlayable: Boolean
          get() = howManyPlayers() >= 2*/


    private fun createQuestionsOf(type: Category, questions: Int): Pair<Category, MutableList<String>> {
        return type to List(questions) { index -> "$type Question $index" }.toMutableList()
    }

    fun add(playerName: String): Boolean {

        players.add(Player(name = playerName))

        println("$playerName was added")
        println("They are player number " + players.size)

        return true
    }


    fun roll(roll: Int) {
        require(players.size > 1)
        println(currentPlayer1!!.name + " is the current player")
        println("They have rolled a $roll")

        if (currentPlayer1!!.inPenaltyBox) {
            if (roll % 2 == 0) {
                println(currentPlayer1!!.name + " is not getting out of the penalty box")
                currentPlayer1!!.isGettingOutOfPenaltyBox = false
            } else {
                currentPlayer1!!.isGettingOutOfPenaltyBox = true

                println(currentPlayer1!!.name + " is getting out of the penalty box")
                movePlayerAndAskQuestion(roll)
            }

        } else {

            movePlayerAndAskQuestion(roll)
        }

    }

    private fun movePlayerAndAskQuestion(roll: Int) {
        currentPlayer1!!.updatePlace(roll)

        println("${currentPlayer1!!.name}'s new location is ${currentPlayer1!!.place}")
        println("The category is ${categoryByIndex(currentPlayer1!!.place)}")

        val question = askQuestion(currentPlayer1!!.place)
        println(question)
    }


    private fun askQuestion(category: Int): String {
        return questions[Category.valueOf(categoryByIndex(category))]!!.removeFirst()

    }





    fun wasCorrectlyAnswered(): Boolean {
        if (currentPlayer1!!.inPenaltyBox ) {
            if (currentPlayer1!!.isGettingOutOfPenaltyBox) {
                println("Answer was correct!!!!")
                moveToNextPlayer()
                currentPlayer1!!.addCoin()
                val playerCoins = "${currentPlayer1!!.name} now has ${currentPlayer1!!.coins} Gold Coins."
                println(playerCoins)

                return currentPlayer1!!.didPlayerWin()
            } else {
                moveToNextPlayer()
                return true
            }


        } else {

            println("Answer was corrent!!!!")
            currentPlayer1!!.addCoin()

            val playerCoins = "${currentPlayer1!!.name} now has ${currentPlayer1!!.coins} Gold Coins."
            println(playerCoins)
            val winner = currentPlayer1!!.didPlayerWin()

            moveToNextPlayer()

            return winner
        }
    }




    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(currentPlayer1!!.name + " was sent to the penalty box")
        currentPlayer1!!.putInPenaltyBox()

        moveToNextPlayer()
        return true
    }

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