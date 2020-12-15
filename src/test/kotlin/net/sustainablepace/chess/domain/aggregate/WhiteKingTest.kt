package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhiteKingTest {
    @Test
    fun `finds valid king movements on empty board`() {
        val position = Position(
            mapOf(
                E4 to WhiteKing
            )
        )
        val moves = position.moveOptionsIgnoringCheck(E4)
        assertThat(moves).containsExactlyInAnyOrder(
            Move(E4, d3),
            Move(E4, E3),
            Move(E4, F3),
            Move(E4, F4),
            Move(E4, F5),
            Move(E4, E5),
            Move(E4, d5),
            Move(E4, d4)
        )
    }

    @Test
    fun `finds valid king movements on crowded board`() {
        val position = Position(
            mapOf(
                E4 to WhiteKing,
                d5 to BlackPawn,
                E5 to WhiteQueen
            )
        )
        val moves = position.moveOptionsIgnoringCheck(E4)
        assertThat(moves).containsExactlyInAnyOrder(
            Move(E4, d3),
            Move(E4, E3),
            Move(E4, F3),
            Move(E4, F4),
            Move(E4, F5),
            Move(E4, d5),
            Move(E4, d4)
        )
    }

    @Test
    fun `castling queenside on empty board`() {
        val position = Position(
            mapOf(
                E1 to WhiteKing,
                a1 to WhiteRook
            )
        )
        assertThat(position.whiteCastlingOptions.kingSide).isTrue()
        assertThat(position.whiteCastlingOptions.queenSide).isTrue()

        val updatedPosition = position.movePiece(Move(E1, c1))
        assertThat(updatedPosition.pieceOn(c1)).isEqualTo(WhiteKing)
        assertThat(updatedPosition.pieceOn(d1)).isEqualTo(WhiteRook)
        assertThat(updatedPosition.whiteCastlingOptions.kingSide).isFalse()
        assertThat(updatedPosition.whiteCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `castling kingside on empty board`() {
        val position = Position(
            mapOf(
                E1 to WhiteKing,
                H1 to WhiteRook
            )
        )
        assertThat(position.whiteCastlingOptions.kingSide).isTrue()
        assertThat(position.whiteCastlingOptions.queenSide).isTrue()

        val updatedPosition = position.movePiece(Move(E1, G1))
        assertThat(updatedPosition.pieceOn(G1)).isEqualTo(WhiteKing)
        assertThat(updatedPosition.pieceOn(F1)).isEqualTo(WhiteRook)
        assertThat(updatedPosition.whiteCastlingOptions.kingSide).isFalse()
        assertThat(updatedPosition.whiteCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `moving king makes castling unavailable`() {
        val position = Position().movePiece(Move(E2, E4))
            .movePiece(Move(E7, E6))
            .movePiece(Move(E1, E2))

        assertThat(position.whiteCastlingOptions.queenSide).isFalse()
        assertThat(position.whiteCastlingOptions.kingSide).isFalse()
    }

}