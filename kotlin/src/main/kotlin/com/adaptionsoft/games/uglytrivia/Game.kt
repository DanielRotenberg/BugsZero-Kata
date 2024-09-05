package com.adaptionsoft.games.uglytrivia

class Player(
    val name: String,
    var place: Int = 0,
    var purse: Int = 0,
    var inPenaltyBox: Boolean = false,
    var isGettingOutOfPenaltyBox: Boolean = false
) {
    fun updatePlace(roll: Int) {
        // incrementing the index, carefully not to introduce bug
        place += roll
        if (place > 11) place -= 12

    }
     fun didPlayerWin(): Boolean {
        return purse != 6
    }


     fun putPlayerInPenaltyBox1() {
        inPenaltyBox = true
    }


     fun setPurse1() {
       purse++
    }

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
        println("The category is ${currentCategory(currentPlayer1!!.place)}")

        val question = askQuestion(currentPlayer1!!.place)
        println(question)
    }


    private fun askQuestion(category: Int): String {
        return questions[Category.valueOf(currentCategory(category))]!!.removeFirst()

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


    private fun currentCategory(category: Int): String = when (category) {
        0, 4, 8 -> "Pop"
        1, 5, 9 -> "Science"
        2, 6, 10 -> "Sports"
        else -> "Rock"
    }

    fun wasCorrectlyAnswered(): Boolean {
        if (currentPlayer1!!.inPenaltyBox /*inPenaltyBox[currentPlayer]*/) {
            if (currentPlayer1!!.isGettingOutOfPenaltyBox) {
                println("Answer was correct!!!!")
                moveToNextPlayer()
                currentPlayer1!!.setPurse1()
//                setPurse1(currentPlayer1!!)
                val playerCoins = "${currentPlayer1!!.name} now has ${currentPlayer1!!.purse} Gold Coins."
                println(playerCoins)

                return currentPlayer1!!.didPlayerWin()
            } else {
                moveToNextPlayer()
                return true
            }


        } else {

            println("Answer was corrent!!!!")
            currentPlayer1!!.setPurse1()
//            setPurse1(currentPlayer1!!)

            val playerCoins = "${currentPlayer1!!.name} now has ${currentPlayer1!!.purse} Gold Coins."
            println(playerCoins)
            val winner = currentPlayer1!!.didPlayerWin()

            moveToNextPlayer()

            return winner
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

    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(currentPlayer1!!.name + " was sent to the penalty box")
        currentPlayer1!!.putPlayerInPenaltyBox1()
//        putPlayerInPenaltyBox1(currentPlayer1!!)

        moveToNextPlayer()
        return true
    }

}

