package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.position
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhiteKingTest {
    @Test
    fun `finds valid king movements on empty board`() {
        val position = position(
            mapOf(
                e4 to WhiteKing
            )
        )
        assertThat(position.moveOptions).containsExactlyInAnyOrder(
            Move(e4, d3),
            Move(e4, e3),
            Move(e4, f3),
            Move(e4, f4),
            Move(e4, f5),
            Move(e4, e5),
            Move(e4, d5),
            Move(e4, d4)
        )
    }

    @Test
    fun `finds valid king movements on crowded board`() {
        val position = position(
            mapOf(
                e4 to WhiteKing,
                d5 to BlackPawn,
                e5 to WhiteQueen
            )
        )
        assertThat(position.moveOptions.filter { it.departureSquare == e4 }).containsExactlyInAnyOrder(
            Move(e4, d3),
            Move(e4, e3),
            Move(e4, f3),
            Move(e4, f4),
            Move(e4, f5),
            Move(e4, d5),
            Move(e4, d4)
        )
    }

    @Test
    fun `castling queenside on empty board`() {
        val position = position(
            mapOf(
                e1 to WhiteKing,
                a1 to WhiteRook
            )
        )
        assertThat(position.whiteCastlingOptions.kingSide).isTrue()
        assertThat(position.whiteCastlingOptions.queenSide).isTrue()

        val updatedPosition = position.movePiece(Move(e1, c1))
        assertThat(updatedPosition.pieceOn(c1)).isEqualTo(WhiteKing)
        assertThat(updatedPosition.pieceOn(d1)).isEqualTo(WhiteRook)
        assertThat(updatedPosition.whiteCastlingOptions.kingSide).isFalse()
        assertThat(updatedPosition.whiteCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `castling kingside on empty board`() {
        val position = position(
            mapOf(
                e1 to WhiteKing,
                h1 to WhiteRook
            )
        )
        assertThat(position.whiteCastlingOptions.kingSide).isTrue()
        assertThat(position.whiteCastlingOptions.queenSide).isTrue()

        val updatedPosition = position.movePiece(Move(e1, g1))
        assertThat(updatedPosition.pieceOn(g1)).isEqualTo(WhiteKing)
        assertThat(updatedPosition.pieceOn(f1)).isEqualTo(WhiteRook)
        assertThat(updatedPosition.whiteCastlingOptions.kingSide).isFalse()
        assertThat(updatedPosition.whiteCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `moving king makes castling unavailable`() {
        val position = position().movePiece(Move(e2, e4))
            .movePiece(Move(e7, e6))
            .movePiece(Move(e1, e2))

        assertThat(position.whiteCastlingOptions.queenSide).isFalse()
        assertThat(position.whiteCastlingOptions.kingSide).isFalse()
    }

}