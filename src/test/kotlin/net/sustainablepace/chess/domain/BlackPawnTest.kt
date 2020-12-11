package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.BlackPieces
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackQueen
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhitePawn
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BlackPawnTest {
    @Test
    fun `finds valid pawn movements on empty board in initial position`() {
        val chessGame = ChessGame(mapOf(
            "e7" to BlackPawn()
        ))
        val moves = chessGame.findMoves("e7")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e7-e6") as ValidMove,
            ValidMove("e7-e5") as ValidMove
        )
    }

    @Test
    fun `finds valid pawn movements on empty board`() {
        val chessGame = ChessGame(mapOf(
            "e5" to BlackPawn()
        ))
        val moves = chessGame.findMoves("e5")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e5-e4") as ValidMove
        )
    }

    @Test
    fun `finds en passant capture moves to the right`() {
        val chessGame = ChessGame(BlackPieces, mapOf(
            "f7" to BlackPawn(),
            "e5" to WhitePawn()
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove("f7-f5") as ValidMove)

        Assertions.assertThat(updatedChessGame.findMoves()).contains(
            ValidMove("e5-f6") as ValidMove
        )
    }

    @Test
    fun `finds en passant capture moves to the left`() {
        val chessGame = ChessGame(BlackPieces, mapOf(
            "d7" to BlackPawn(),
            "e5" to WhitePawn()
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove("d7-d5") as ValidMove)

        Assertions.assertThat(updatedChessGame.findMoves()).contains(
            ValidMove("e5-d6") as ValidMove
        )
    }

    @Test
    fun `promotion to queen`() {
        val chessGame = ChessGame(BlackPieces, mapOf(
            "f2" to BlackPawn()
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove("f2-f1") as ValidMove)

        Assertions.assertThat(updatedChessGame.pieceOn("f1")).isEqualTo(BlackQueen())
    }

    @Test
    fun `moving left rook makes castling unavailable`() {
        val chessGame = ChessGame(BlackPieces)
            .movePiece(ValidMove("a7-a5") as ValidMove)
            .movePiece(ValidMove("a2-a3") as ValidMove)
            .movePiece(ValidMove("a8-a6") as ValidMove)

        Assertions.assertThat(chessGame.blackCastlingOptions.queenSide).isFalse()
        Assertions.assertThat(chessGame.blackCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `moving right rook makes castling unavailable`() {
        val chessGame = ChessGame(BlackPieces)
            .movePiece(ValidMove("h7-h5") as ValidMove)
            .movePiece(ValidMove("h2-h3") as ValidMove)
            .movePiece(ValidMove("h8-h6") as ValidMove)

        Assertions.assertThat(chessGame.blackCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(chessGame.blackCastlingOptions.kingSide).isFalse()
    }
}