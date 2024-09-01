package com.adaptionsoft.games.uglytrivia

class Game {
    val players = mutableListOf<String>()
    val places = IntArray(6)
    val purses = IntArray(6)
    var inPenaltyBox = BooleanArray(6)

    var popQuestions = createQuestionsOf(Category.Pop, 49)
    var scienceQuestions = createQuestionsOf(Category.Science, 49)
    var sportsQuestions = createQuestionsOf(Category.Sports, 49)
    var rockQuestions = createQuestionsOf(Category.Rock, 49)

    var currentPlayer = 0
    var isGettingOutOfPenaltyBox: Boolean = false

    /*  val isPlayable: Boolean
          get() = howManyPlayers() >= 2*/


    private fun createQuestionsOf(type: Category, questions: Int): MutableList<String> {
        return List(questions) { index -> "$type Question $index" }.toMutableList()


    }

    fun add(playerName: String): Boolean {


        players.add(playerName)
        places[howManyPlayers()] = 0
        purses[howManyPlayers()] = 0
        inPenaltyBox[howManyPlayers()] = false

        println(playerName + " was added")
        println("They are player number " + players.size)
        return true
    }

    fun howManyPlayers(): Int {
        return players.size
    }

    fun roll(roll: Int) {
        require(players.size > 1)
        println(players.get(currentPlayer) + " is the current player")
        println("They have rolled a " + roll)

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true

                println(players.get(currentPlayer) + " is getting out of the penalty box")
                movePlayerAndAskQuestion(roll)
            } else {
                println(players.get(currentPlayer) + " is not getting out of the penalty box")
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
            players[currentPlayer]
                    + "'s new location is "
                    + places[currentPlayer]
        )
        // can group this together and extract pure function?
        println("The category is " + currentCategory())
        askQuestion()
    }

    //Map<Category,List<Question>>


    private fun askQuestion() {
        /*
        * 1. remove the element
        * 2. print the element
        * */

        if (currentCategory() === "Pop")
            println(popQuestions.removeFirst())
        if (currentCategory() === "Science")
            println(scienceQuestions.removeFirst())
        if (currentCategory() === "Sports")
            println(sportsQuestions.removeFirst())
        if (currentCategory() === "Rock")
            println(rockQuestions.removeFirst())
    }

    enum class Category {
        Pop, Science, Sports, Rock;

        fun typeOf(category: String): Category {
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
        }
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
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                println("Answer was correct!!!!")
                currentPlayer++
                if (currentPlayer == players.size) currentPlayer = 0
                purses[currentPlayer]++
                println(
                    players.get(currentPlayer)
                            + " now has "
                            + purses[currentPlayer]
                            + " Gold Coins."
                )

                return didPlayerWin()
            } else {
                currentPlayer++
                if (currentPlayer == players.size) currentPlayer = 0
                return true
            }


        } else {

            println("Answer was corrent!!!!")
            purses[currentPlayer]++
            println(
                players.get(currentPlayer)
                        + " now has "
                        + purses[currentPlayer]
                        + " Gold Coins."
            )

            val winner = didPlayerWin()
            currentPlayer++
            if (currentPlayer == players.size) currentPlayer = 0

            return winner
        }
    }

    fun wrongAnswer(): Boolean {
        println("Question was incorrectly answered")
        println(players.get(currentPlayer) + " was sent to the penalty box")
        inPenaltyBox[currentPlayer] = true

        currentPlayer++
        if (currentPlayer == players.size) currentPlayer = 0
        return true
    }


    private fun didPlayerWin(): Boolean {
        return purses[currentPlayer] != 6
    }
}

/*fun MutableList<String>.removeFirst(): String {
    return this.removeAt(0)
}*/

/*
fun MutableList<String>.addLast(element: String) {
    this.add(element)
}*/
