package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
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
        val moves = position.moveOptions(E4)
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(E4, D3),
            ValidMove(E4, E3),
            ValidMove(E4, F3),
            ValidMove(E4, F4),
            ValidMove(E4, F5),
            ValidMove(E4, E5),
            ValidMove(E4, D5),
            ValidMove(E4, D4)
        )
    }

    @Test
    fun `finds valid king movements on crowded board`() {
        val position = Position(
            mapOf(
                E4 to WhiteKing,
                D5 to BlackPawn,
                E5 to WhiteQueen
            )
        )
        val moves = position.moveOptions(E4)
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(E4, D3),
            ValidMove(E4, E3),
            ValidMove(E4, F3),
            ValidMove(E4, F4),
            ValidMove(E4, F5),
            ValidMove(E4, D5),
            ValidMove(E4, D4)
        )
    }

    @Test
    fun `castling queenside on empty board`() {
        val position = Position(
            mapOf(
                E1 to WhiteKing,
                A1 to WhiteRook
            )
        )
        assertThat(position.whiteCastlingOptions.kingSide).isTrue()
        assertThat(position.whiteCastlingOptions.queenSide).isTrue()

        val updatedPosition = position.movePiece(ValidMove(E1, C1))
        assertThat(updatedPosition.pieceOn(C1)).isEqualTo(WhiteKing)
        assertThat(updatedPosition.pieceOn(D1)).isEqualTo(WhiteRook)
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

        val updatedPosition = position.movePiece(ValidMove(E1, G1))
        assertThat(updatedPosition.pieceOn(G1)).isEqualTo(WhiteKing)
        assertThat(updatedPosition.pieceOn(F1)).isEqualTo(WhiteRook)
        assertThat(updatedPosition.whiteCastlingOptions.kingSide).isFalse()
        assertThat(updatedPosition.whiteCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `moving king makes castling unavailable`() {
        val position = Position().movePiece(ValidMove(E2, E4))
            .movePiece(ValidMove(E7, E6))
            .movePiece(ValidMove(E1, E2))

        assertThat(position.whiteCastlingOptions.queenSide).isFalse()
        assertThat(position.whiteCastlingOptions.kingSide).isFalse()
    }

}