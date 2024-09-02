package com.adaptionsoft.games.uglytrivia

class Player(val name: String, var place: Int = 0, var coins: Int = 0, var inPenaltyBox: Boolean = false) {

}

class Game {
    val players1 = mutableListOf<Player>()
    val players = mutableListOf<String>()
    val places = IntArray(6) { 0 }
    val purses = IntArray(6) { 0 }
//    var inPenaltyBox = BooleanArray(6) { false }

    private val questions: Map<Category, MutableList<String>> = mapOf(
        createQuestionsOf(Category.Pop, 49),
        createQuestionsOf(Category.Science, 49),
        createQuestionsOf(Category.Sports, 49),
        createQuestionsOf(Category.Rock, 49)
    )
    var currentPlayer = 0
    var isGettingOutOfPenaltyBox: Boolean = false

    fun setPlaces(index: Int, roll: Int) {
        places[index] = places[index] + roll
        if (places[index] > 11) places[index]  -= 12
        players1[index].place += roll //?? carefully not to introduce bug
        if (players1[index].place > 11) players1[index].place -= 12
    }

    fun getPlaces(index: Int): Int {
        return places[index]
    }

    /*  val isPlayable: Boolean
          get() = howManyPlayers() >= 2*/


    private fun createQuestionsOf(type: Category, questions: Int): Pair<Category, MutableList<String>> {
        return type to List(questions) { index -> "$type Question $index" }.toMutableList()
    }

    fun add(playerName: String): Boolean {

        players1.add(Player(name = playerName))
        players.add(playerName)

        println("$playerName was added")
        println("They are player number " + players.size)

        return true
    }

    fun setIsInPenalty(value: Boolean, position: Int) {

    }

    fun roll(roll: Int) {
        require(players.size > 1)
        require(players1.size > 1)
        println(players[currentPlayer] + " is the current player")
        println("They have rolled a $roll")

        if (players1[currentPlayer].inPenaltyBox /*inPenaltyBox[currentPlayer]*/) {
            if (roll % 2 == 0) {
                println(players[currentPlayer] + " is not getting out of the penalty box")
                isGettingOutOfPenaltyBox = false
            } else {
                isGettingOutOfPenaltyBox = true

                println(players[currentPlayer] + " is getting out of the penalty box")
                movePlayerAndAskQuestion(roll)
            }

        } else {

            movePlayerAndAskQuestion(roll)
        }

    }

    private fun movePlayerAndAskQuestion(roll: Int) {
//        places[currentPlayer] = places[currentPlayer] + roll
        setPlaces(currentPlayer, roll)


        println(
//            "${players[currentPlayer]}'s new location is ${places[currentPlayer]}"
            "${players[currentPlayer]}'s new location is ${getPlaces(currentPlayer)}"
        )
        // can group this together and extract pure function?
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
        val currentCategory = places[currentPlayer]
        return when (currentCategory) {
            0, 4, 8 -> "Pop"
            1, 5, 9 -> "Science"
            2, 6, 10 -> "Sports"
            else -> "Rock"
        }

    }

    fun wasCorrectlyAnswered(): Boolean {
        if (players1[currentPlayer].inPenaltyBox /*inPenaltyBox[currentPlayer]*/) {
            if (isGettingOutOfPenaltyBox) {
                println("Answer was correct!!!!")
                moveToNextPlayer()
                purses[currentPlayer]++
                val playerCoins = currentPlayerGoldCoins(players[currentPlayer], purses[currentPlayer])
                println(playerCoins)

                return didPlayerWin()
            } else {
                moveToNextPlayer()
                return true
            }


        } else {

            println("Answer was corrent!!!!")
            purses[currentPlayer]++
            val playerCoins = currentPlayerGoldCoins(players[currentPlayer], purses[currentPlayer])
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
        println(players[currentPlayer] + " was sent to the penalty box")
        putPlayerInPenaltyBox(currentPlayer)
//        inPenaltyBox[currentPlayer] = true

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
        return purses[currentPlayer] != 6
    }

    private fun putPlayerInPenaltyBox(index: Int) {
//        inPenaltyBox[index] = true
        players1[index].inPenaltyBox = true
    }
}

