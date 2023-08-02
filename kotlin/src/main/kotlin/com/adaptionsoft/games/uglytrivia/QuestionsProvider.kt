package com.adaptionsoft.games.uglytrivia

class QuestionsProvider {


    var popQuestions = mutableListOf<String>()
    var scienceQuestions = mutableListOf<String>()
    var sportsQuestions = mutableListOf<String>()
    var rockQuestions = mutableListOf<String>()


    init {
        for (i in 0..49) {
            popQuestions.addLast("Pop Question " + i)
            scienceQuestions.addLast("Science Question " + i)
            sportsQuestions.addLast("Sports Question " + i)
            rockQuestions.addLast("Rock Question " + i)
        }
    }

     fun askQuestion(playerPlace: Int) {
        println("The category is " + currentCategory(playerPlace))
        when (currentCategory(playerPlace)) {
            "Pop" -> println(popQuestions.removeFirst())
            "Science" -> println(scienceQuestions.removeFirst())
            "Sports" -> println(sportsQuestions.removeFirst())
            "Rock" -> println(rockQuestions.removeFirst())
        }
    }

    private fun currentCategory(selector: Int): String {
        return when (selector) {
            0, 4, 8 -> "Pop"
            1, 5, 9 -> "Science"
            2, 6 -> "Sports"
            else -> if (selector == 10) "Sports" else "Rock"
        }
    }
}
fun MutableList<String>.removeFirst(): String {
    return this.removeAt(0)
}

fun MutableList<String>.addLast(element: String) {
    this.add(element)
}