package com.adaptionsoft.games.uglytrivia

class Player(val name: String, var place: Int = 0, var purse: Int = 0, var inPenaltyBox: Boolean = false) {

}

class Game {
    private val players = mutableListOf<Player>()

    private val questions: Map<Category, MutableList<String>> = mapOf(
        createQuestionsOf(Category.Pop, 49),
        createQuestionsOf(Category.Science, 49),
        createQuestionsOf(Category.Sports, 49),
        createQuestionsOf(Category.Rock, 49)
    )
    var currentPlayer = 0
    var isGettingOutOfPenaltyBox: Boolean = false

    private fun setPlaces(index: Int, roll: Int) {
        players[index].place += roll //?? carefully not to introduce bug
        if (players[index].place > 11) players[index].place -= 12
    }

    private fun getPlaces(index: Int): Int {
        return players[index].place

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
        println(players[currentPlayer].name + " is the current player")
        println("They have rolled a $roll")

        if (players[currentPlayer].inPenaltyBox ) {
            if (roll % 2 == 0) {
                println(players[currentPlayer].name + " is not getting out of the penalty box")
                isGettingOutOfPenaltyBox = false
            } else {
                isGettingOutOfPenaltyBox = true

                println(players[currentPlayer].name + " is getting out of the penalty box")
                movePlayerAndAskQuestion(roll)
            }

        } else {

            movePlayerAndAskQuestion(roll)
        }

    }

    private fun movePlayerAndAskQuestion(roll: Int) {
        setPlaces(currentPlayer, roll)


        println(
            "${players[currentPlayer].name}'s new location is ${getPlaces(currentPlayer)}"
        )
        println("The category is ${currentCategory()}")
        val question = askQuestion()
        println(question)
    }


    private fun askQuestion(): String {
        return removeFirstQuestionOf(Category.valueOf(currentCategory()))

    }

    private fun removeFirstQuestionOf(category: Category): String {
        return questions[category]!!.removeFirst()
    }

    enum class Category {
        Pop, Science, Sports, Rock;

        /*     fun typeOf(category: String): Category {
                 return when (category) {
                     "Pop" -> Pop
                     "Science" -> Science
                     "Sports" -> Sports
                     "Rock" -> Rock
                     else -> throw Exception("unknown category")
                 }
             }

             fun textForType(category: Category): String = when (category) {
                 Pop -> "Pop Question"
                 Science -> "Science Question"
                 Sports -> "Sports Question"
                 Rock -> "Rock Question"
             }*/
    }


    private fun currentCategory(): String {
        val currentCategory = getPlaces(currentPlayer)
        return when (currentCategory) {
            0, 4, 8 -> "Pop"
            1, 5, 9 -> "Science"
            2, 6, 10 -> "Sports"
            else -> "Rock"
        }

    }

    fun wasCorrectlyAnswered(): Boolean {
        if (players[currentPlayer].inPenaltyBox /*inPenaltyBox[currentPlayer]*/) {
            if (isGettingOutOfPenaltyBox) {
                println("Answer was correct!!!!")
                moveToNextPlayer()
                setPurse(currentPlayer)
                val playerCoins = currentPlayerGoldCoins(players[currentPlayer].name, getPurse(currentPlayer))
                println(playerCoins)

                return didPlayerWin()
            } else {
                moveToNextPlayer()
                return true
            }


        } else {

            println("Answer was corrent!!!!")
            setPurse(currentPlayer)

            val playerCoins = currentPlayerGoldCoins(players[currentPlayer].name, getPurse(currentPlayer))
            println(playerCoins)
            val winner = didPlayerWin()

            moveToNextPlayer()

            return winner
        }
    }

    private fun currentPlayerGoldCoins(player: String, purse: Int): String {
        return "$player now has $purse Gold Coins."
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

    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(players[currentPlayer].name + " was sent to the penalty box")
        putPlayerInPenaltyBox(currentPlayer)

        moveToNextPlayer()
        return true
    }

    private fun moveToNextPlayer() {
        currentPlayer++
        currentPlayer %= players.size
        /* currentPlayer++
         if (currentPlayer == players.size) currentPlayer = 0*/
    }


    private fun didPlayerWin(): Boolean {
        return getPurse(currentPlayer) != 6
    }

    private fun putPlayerInPenaltyBox(index: Int) {
        players[index].inPenaltyBox = true
    }

    private fun setPurse(index: Int) {
        players[index].purse++

    }

    private fun getPurse(index: Int): Int {
        return players[index].purse
    }
}

