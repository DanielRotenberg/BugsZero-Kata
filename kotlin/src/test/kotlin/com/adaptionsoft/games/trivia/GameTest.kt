package com.adaptionsoft.games.trivia


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
    fun `in given game must be at least 2 players`() {
        val game = Game()
        game.add("a")
        assertThrows<IllegalArgumentException> {
            game.roll(4)
        }
    }
}
