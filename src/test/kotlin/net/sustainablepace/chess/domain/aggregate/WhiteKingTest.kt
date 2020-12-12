package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhiteKingTest {
    @Test
    fun `finds valid king movements on empty board`() {
        val chessGame = ChessGame(
            mapOf(
                E4 to WhiteKing
            )
        )
        val moves = chessGame.moveOptions(E4)
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
        val chessGame = ChessGame(
            mapOf(
                E4 to WhiteKing,
                D5 to BlackPawn,
                E5 to WhiteQueen
            )
        )
        val moves = chessGame.moveOptions(E4)
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
        val chessGame = ChessGame(
            mapOf(
                E1 to WhiteKing,
                A1 to WhiteRook
            )
        )
        assertThat(chessGame.whiteCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.whiteCastlingOptions.queenSide).isTrue()

        val updatedGame = chessGame.movePiece(ValidMove(E1, C1))
        assertThat(updatedGame.pieceOn(C1)).isEqualTo(WhiteKing)
        assertThat(updatedGame.pieceOn(D1)).isEqualTo(WhiteRook)
        assertThat(updatedGame.whiteCastlingOptions.kingSide).isFalse()
        assertThat(updatedGame.whiteCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `castling kingside on empty board`() {
        val chessGame = ChessGame(
            mapOf(
                E1 to WhiteKing,
                H1 to WhiteRook
            )
        )
        assertThat(chessGame.whiteCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.whiteCastlingOptions.queenSide).isTrue()

        val updatedGame = chessGame.movePiece(ValidMove(E1, G1))
        assertThat(updatedGame.pieceOn(G1)).isEqualTo(WhiteKing)
        assertThat(updatedGame.pieceOn(F1)).isEqualTo(WhiteRook)
        assertThat(updatedGame.whiteCastlingOptions.kingSide).isFalse()
        assertThat(updatedGame.whiteCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `moving king makes castling unavailable`() {
        val chessGame = ChessGame()
            .movePiece(ValidMove(E2, E4))
            .movePiece(ValidMove(E7, E6))
            .movePiece(ValidMove(E1, E2))

        assertThat(chessGame.whiteCastlingOptions.queenSide).isFalse()
        assertThat(chessGame.whiteCastlingOptions.kingSide).isFalse()
    }

}