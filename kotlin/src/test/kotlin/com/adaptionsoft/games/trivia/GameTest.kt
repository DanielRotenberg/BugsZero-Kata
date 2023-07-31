package com.adaptionsoft.games.trivia

/**
 * Where to start?
 * Pick any of the listed problems
 *
 * A Game could have less than two players - make sure it always has at least two. - Done
 * Use a compiled language or a static type checker like flowtype
 *
 * A Game could have 7 players, make it have at most 6.
 * or slightly easier allow for 7 players or more
 *
 * A player that get’s into prison always stays there
 * Other than just fixing the bug, try to understand what’s wrong with the design and fix the root cause
 *
 * The deck could run out of questions
 * Make sure that can’t happen (a deck with 1 billion questions is cheating :)
 *
 * Introducing new categories of questions seems like tricky business.
 *
 * Could you make sure all places have the “right” question and that the distribution is always correct?
 *
 * Similarly changing the board size greatly affects the questions distribution
 *
 *
 */
import com.adaptionsoft.games.trivia.runner.GameRunner
import com.adaptionsoft.games.uglytrivia.Game
import org.approvaltests.Approvals
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.io.*
import java.util.*
import java.util.stream.IntStream

class GameTest {

    @Test
    @Throws(Exception::class)
    fun itsLockedDown() {

        val randomizer = Random(123455)
        val resultStream = ByteArrayOutputStream()
        System.setOut(PrintStream(resultStream))

        IntStream.range(1, 15).forEach { i -> GameRunner.playGame(randomizer) }

        Approvals.verify(resultStream.toString())

    }

    @Test
    fun `game must have at least 2 players`() {
        val game = createGameWithPlayers(numOfPlayers = 1)
        assertThrows<IllegalArgumentException> {
            game.roll(4)
        }
    }

    @Test
    fun `game can have maximum 6 players`() {
        assertThrows<IllegalArgumentException> {
            createGameWithPlayers(numOfPlayers = 7)
        }
    }

    @Test
    fun `game can have 6 players`() {
       val game = createGameWithPlayers(6)
        assertThat(game.players.size).isEqualTo(6)
    }
}


fun createGameWithPlayers(numOfPlayers: Int): Game {
    return Game().apply {
        repeat(numOfPlayers) {
            add("it")
        }
    }

}
