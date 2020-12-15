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
            .movePiece(Move(E2, E4))
            .movePiece(Move(d7, d6))
            .movePiece(Move(F1, b5))

        assertThat(position.moveOptionsIgnoringCheck(b5)).contains(Move(b5, E8))
        assertThat(position.isInCheck(Black)).isTrue()
    }

    @Test
    fun `en passant (white)`() {
        val position = Position()
        assertThat(position.enPassantSquare).isNull()

        position.movePiece(Move(E2, E4)).let {
            assertThat(it.enPassantSquare).isEqualTo(E4)
        }

        position.movePiece(Move(E2, E3)).let {
            assertThat(it.enPassantSquare).isNull()
        }
    }

    @Test
    fun `en passant (black)`() {
        val position = Position()
        assertThat(position.enPassantSquare).isNull()

        position.movePiece(Move(E7, E6)).let {
            assertThat(it.enPassantSquare).isNull()
        }

        position.movePiece(Move(E7, E5)).let {
            assertThat(it.enPassantSquare).isEqualTo(E5)
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
            Move(E2, E3),
            Move(F2, F3),
            Move(G2, G3),
            Move(H2, H3),
            Move(a2, a4),
            Move(b2, b4),
            Move(c2, c4),
            Move(d2, d4),
            Move(E2, E4),
            Move(F2, F4),
            Move(G2, G4),
            Move(H2, H4),
            Move(b1, a3),
            Move(b1, c3),
            Move(G1, F3),
            Move(G1, H3)
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
            Move(E7, E6),
            Move(F7, F6),
            Move(G7, G6),
            Move(H7, H6),
            Move(a7, a5),
            Move(b7, b5),
            Move(c7, c5),
            Move(d7, d5),
            Move(E7, E5),
            Move(F7, F5),
            Move(G7, G5),
            Move(H7, H5),
            Move(b8, a6),
            Move(b8, c6),
            Move(G8, F6),
            Move(G8, H6)
        )
    }

    @Test
    fun `position not updated after move`() {
        val position = Position().movePiece(Move(a3, a4))

        assertThat(position).isInstanceOf(PositionNotChanged::class.java)
    }
}