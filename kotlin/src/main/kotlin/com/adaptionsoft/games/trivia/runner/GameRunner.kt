package com.adaptionsoft.games.trivia.runner

import java.util.Random

import com.adaptionsoft.games.uglytrivia.gameWith


object GameRunner {

    private var gameContinues: Boolean = false

    @JvmStatic
    fun main(args: Array<String>) {
        val rand = Random()
        playGame(rand)

    }

    fun playGame(rand: Random) {

        val aGame = gameWith("Chet","Pat","Sue")

        do {

            aGame.roll(rand.nextInt(5) + 1)

            if (rand.nextInt(9) == 7) {
                gameContinues = aGame.wrongAnswer()
            } else {
                gameContinues = aGame.wasCorrectlyAnswered()
            }


        } while (gameContinues)
    }
}
