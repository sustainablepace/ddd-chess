package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BlackPawnTest {
    @Test
    fun `finds valid pawn movements on empty board in initial position`() {
        val chessGame = ChessGame(Black, mapOf(
            E7 to BlackPawn
        ))
        val moves = chessGame.moveOptions(E7)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(E7, E6),
            ValidMove(E7, E5)
        )
    }

    @Test
    fun `finds valid pawn movements on empty board`() {
        val chessGame = ChessGame(Black, mapOf(
            E5 to BlackPawn
        ))
        val moves = chessGame.moveOptions(E5)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(E5, E4)
        )
    }

    @Test
    fun `finds en passant capture moves to the right`() {
        val chessGame = ChessGame(Black, mapOf(
            F7 to BlackPawn,
            E5 to WhitePawn
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove(F7, F5))

        Assertions.assertThat(updatedChessGame.moveOptions()).contains(
            ValidMove(E5, F6)
        )
    }

    @Test
    fun `finds en passant capture moves to the left`() {
        val chessGame = ChessGame(Black, mapOf(
            D7 to BlackPawn,
            E5 to WhitePawn
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove(D7, D5))

        Assertions.assertThat(updatedChessGame.moveOptions()).contains(
            ValidMove(E5, D6)
        )
    }

    @Test
    fun `promotion to queen`() {
        val chessGame = ChessGame(Black, mapOf(
            F2 to BlackPawn
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove(F2, F1))

        Assertions.assertThat(updatedChessGame.pieceOn(F1)).isEqualTo(BlackQueen)
    }

    @Test
    fun `moving left rook makes castling unavailable`() {
        val chessGame = ChessGame(Black)
            .movePiece(ValidMove(A7, A5))
            .movePiece(ValidMove(A2, A3))
            .movePiece(ValidMove(A8, A6))

        Assertions.assertThat(chessGame.blackCastlingOptions.queenSide).isFalse()
        Assertions.assertThat(chessGame.blackCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `moving right rook makes castling unavailable`() {
        val chessGame = ChessGame(Black)
            .movePiece(ValidMove(H7, H5))
            .movePiece(ValidMove(H2, H3))
            .movePiece(ValidMove(H8, H6))

        Assertions.assertThat(chessGame.blackCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(chessGame.blackCastlingOptions.kingSide).isFalse()
    }
}