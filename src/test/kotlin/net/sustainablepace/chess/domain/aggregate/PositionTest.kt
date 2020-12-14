package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
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
            .movePiece(ValidMove(E2, E4))
            .movePiece(ValidMove(D7, D6))
            .movePiece(ValidMove(F1, B5))

        assertThat(position.moveOptionsIgnoringCheck(B5)).contains(ValidMove(B5, E8))
        assertThat(position.isInCheck(Black)).isTrue()
    }

    @Test
    fun `en passant (white)`() {
        val position = Position()
        assertThat(position.enPassantSquare).isNull()

        position.movePiece(ValidMove(E2, E4)).let {
            assertThat(it.enPassantSquare).isEqualTo(E4)
        }

        position.movePiece(ValidMove(E2, E3)).let {
            assertThat(it.enPassantSquare).isNull()
        }
    }

    @Test
    fun `en passant (black)`() {
        val position = Position()
        assertThat(position.enPassantSquare).isNull()

        position.movePiece(ValidMove(E7, E6)).let {
            assertThat(it.enPassantSquare).isNull()
        }

        position.movePiece(ValidMove(E7, E5)).let {
            assertThat(it.enPassantSquare).isEqualTo(E5)
        }

    }

    @Test
    fun `find moves for white in default position`() {
        val position = Position()

        val moves = position.moveOptions(White)

        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(A2, A3),
            ValidMove(B2, B3),
            ValidMove(C2, C3),
            ValidMove(D2, D3),
            ValidMove(E2, E3),
            ValidMove(F2, F3),
            ValidMove(G2, G3),
            ValidMove(H2, H3),
            ValidMove(A2, A4),
            ValidMove(B2, B4),
            ValidMove(C2, C4),
            ValidMove(D2, D4),
            ValidMove(E2, E4),
            ValidMove(F2, F4),
            ValidMove(G2, G4),
            ValidMove(H2, H4),
            ValidMove(B1, A3),
            ValidMove(B1, C3),
            ValidMove(G1, F3),
            ValidMove(G1, H3)
        )
    }

    @Test
    fun `find moves for black in default position`() {
        val position = Position()

        val moves = position.moveOptions(Black)

        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(A7, A6),
            ValidMove(B7, B6),
            ValidMove(C7, C6),
            ValidMove(D7, D6),
            ValidMove(E7, E6),
            ValidMove(F7, F6),
            ValidMove(G7, G6),
            ValidMove(H7, H6),
            ValidMove(A7, A5),
            ValidMove(B7, B5),
            ValidMove(C7, C5),
            ValidMove(D7, D5),
            ValidMove(E7, E5),
            ValidMove(F7, F5),
            ValidMove(G7, G5),
            ValidMove(H7, H5),
            ValidMove(B8, A6),
            ValidMove(B8, C6),
            ValidMove(G8, F6),
            ValidMove(G8, H6)
        )
    }
}