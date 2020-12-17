package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.event.PositionNotChanged
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PositionTest {
    @Test
    fun `initial position is not in check`() {
        val position = Position()

        assertThat(position.isInCheck(White)).isFalse()
        assertThat(position.isInCheck(Black)).isFalse()
    }


    @Test
    fun `black is in check by bishop`() {
        val position = Position()
            .movePiece(Move(e2, e4))
            .movePiece(Move(d7, d6))
            .movePiece(Move(f1, b5))

        assertThat(position.moveOptionsIgnoringCheck(b5)).contains(Move(b5, e8))
        assertThat(position.isInCheck(Black)).isTrue()
    }

    @Test
    fun `en passant (white)`() {
        val position = Position()
        assertThat(position.enPassantSquare).isNull()

        position.movePiece(Move(e2, e4)).let {
            assertThat(it.enPassantSquare).isEqualTo(e4)
        }

        position.movePiece(Move(e2, e3)).let {
            assertThat(it.enPassantSquare).isNull()
        }
    }

    @Test
    fun `en passant (black)`() {
        val position = Position()
        assertThat(position.enPassantSquare).isNull()

        position.movePiece(Move(e7, e6)).let {
            assertThat(it.enPassantSquare).isNull()
        }

        position.movePiece(Move(e7, e5)).let {
            assertThat(it.enPassantSquare).isEqualTo(e5)
        }

    }

    @Test
    fun `find moves for white in default position`() {
        val position = Position()

        val moves = position.moveOptions(White)

        assertThat(moves).containsExactlyInAnyOrder(
            Move(a2, a3),
            Move(b2, b3),
            Move(c2, c3),
            Move(d2, d3),
            Move(e2, e3),
            Move(f2, f3),
            Move(g2, g3),
            Move(h2, h3),
            Move(a2, a4),
            Move(b2, b4),
            Move(c2, c4),
            Move(d2, d4),
            Move(e2, e4),
            Move(f2, f4),
            Move(g2, g4),
            Move(h2, h4),
            Move(b1, a3),
            Move(b1, c3),
            Move(g1, f3),
            Move(g1, h3)
        )
    }

    @Test
    fun `find moves for black in default position`() {
        val position = Position()

        val moves = position.moveOptions(Black)

        assertThat(moves).containsExactlyInAnyOrder(
            Move(a7, a6),
            Move(b7, b6),
            Move(c7, c6),
            Move(d7, d6),
            Move(e7, e6),
            Move(f7, f6),
            Move(g7, g6),
            Move(h7, h6),
            Move(a7, a5),
            Move(b7, b5),
            Move(c7, c5),
            Move(d7, d5),
            Move(e7, e5),
            Move(f7, f5),
            Move(g7, g5),
            Move(h7, h5),
            Move(b8, a6),
            Move(b8, c6),
            Move(g8, f6),
            Move(g8, h6)
        )
    }

    @Test
    fun `position not updated after move`() {
        val position = Position().movePiece(Move(a3, a4))

        assertThat(position).isInstanceOf(PositionNotChanged::class.java)
    }
}