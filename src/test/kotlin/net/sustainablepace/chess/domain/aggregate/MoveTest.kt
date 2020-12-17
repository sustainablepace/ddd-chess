package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.e2
import net.sustainablepace.chess.domain.aggregate.chessgame.e4
import net.sustainablepace.chess.domain.move.InvalidMove
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test

class MoveTest {
    @Test
    fun `ignore invalid move`() {
        val result = Move("invalid")

        assertThat(result).isInstanceOf(InvalidMove::class.java)
    }

    @Test
    fun `ignore move to same square`() {
        val result = Move("e2-e2")

        assertThat(result).isInstanceOf(InvalidMove::class.java)
    }

    @Test
    fun `ignore moves with invalid square`() {
        val result = Move("e2-e9")

        assertThat(result).isInstanceOf(InvalidMove::class.java)
    }

    @Test
    fun `e2-e4 is a valid move`() {
        val move = Move("e2-e4")

        if(move is Move) {
            assertThat(move.departureSquare).isEqualTo(e2)
            assertThat(move.arrivalSquare).isEqualTo(e4)
        } else {
            fail("Move is not valid.")
        }
    }
}