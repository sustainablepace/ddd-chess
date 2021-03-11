package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.position
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhiteRookTest {

    @Test
    fun `finds valid rook movements`() {
        val position = position(mapOf(
                c3 to WhiteRook,
                c7 to BlackPawn,
                e3 to WhitePawn
            )
        )

        assertThat(position.moveOptions.filter { it.departureSquare == c3 }).containsExactlyInAnyOrder(
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
        val position = position().movePiece(Move(a2, a4))
            .movePiece(Move(a7, a6))
            .movePiece(Move(a1, a3))

        assertThat(position.whiteCastlingOptions.queenSide).isFalse()
        assertThat(position.whiteCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `moving right rook makes castling unavailable`() {
        val position = position().movePiece(Move(h2, h4))
            .movePiece(Move(h7, h6))
            .movePiece(Move(h1, h3))

        assertThat(position.whiteCastlingOptions.queenSide).isTrue()
        assertThat(position.whiteCastlingOptions.kingSide).isFalse()
    }
}