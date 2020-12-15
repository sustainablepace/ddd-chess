package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhiteRookTest {

    @Test
    fun `finds valid rook movements`() {
        val position = Position(mapOf(
                c3 to WhiteRook,
                c7 to BlackPawn,
                E3 to WhitePawn
            )
        )

        val moves = position.moveOptionsIgnoringCheck(c3)
        assertThat(moves).containsExactlyInAnyOrder(
            Move(c3, c4),
            Move(c3, c5),
            Move(c3, c6),
            Move(c3, c7),
            Move(c3, b3),
            Move(c3, a3),
            Move(c3, c2),
            Move(c3, c1),
            Move(c3, d3)
        )
    }

    @Test
    fun `moving left rook makes castling unavailable`() {
        val position = Position().movePiece(Move(a2, a4))
            .movePiece(Move(a7, a6))
            .movePiece(Move(a1, a3))

        assertThat(position.whiteCastlingOptions.queenSide).isFalse()
        assertThat(position.whiteCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `moving right rook makes castling unavailable`() {
        val position = Position().movePiece(Move(H2, H4))
            .movePiece(Move(H7, H6))
            .movePiece(Move(H1, H3))

        assertThat(position.whiteCastlingOptions.queenSide).isTrue()
        assertThat(position.whiteCastlingOptions.kingSide).isFalse()
    }
}