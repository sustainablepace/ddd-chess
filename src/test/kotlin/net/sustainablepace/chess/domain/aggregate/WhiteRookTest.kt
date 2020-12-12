package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhiteRookTest {

    @Test
    fun `finds valid rook movements`() {
        val chessGame = ChessGame(
            mapOf(
                C3 to WhiteRook,
                C7 to BlackPawn,
                E3 to WhitePawn
            )
        )

        val moves = chessGame.moveOptions(C3)
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(C3, C4),
            ValidMove(C3, C5),
            ValidMove(C3, C6),
            ValidMove(C3, C7),
            ValidMove(C3, B3),
            ValidMove(C3, A3),
            ValidMove(C3, C2),
            ValidMove(C3, C1),
            ValidMove(C3, D3)
        )
    }

    @Test
    fun `moving left rook makes castling unavailable`() {
        val chessGame = ChessGame()
            .movePiece(ValidMove(A2, A4))
            .movePiece(ValidMove(A7, A6))
            .movePiece(ValidMove(A1, A3))

        assertThat(chessGame.whiteCastlingOptions.queenSide).isFalse()
        assertThat(chessGame.whiteCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `moving right rook makes castling unavailable`() {
        val chessGame = ChessGame()
            .movePiece(ValidMove(H2, H4))
            .movePiece(ValidMove(H7, H6))
            .movePiece(ValidMove(H1, H3))

        assertThat(chessGame.whiteCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.whiteCastlingOptions.kingSide).isFalse()
    }
}