package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.WhiteKing
import net.sustainablepace.chess.domain.aggregate.chessgame.WhiteQueen
import net.sustainablepace.chess.domain.aggregate.chessgame.WhiteRook
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhiteKingTest {
    @Test
    fun `finds valid king movements on empty board`() {
        val chessGame = ChessGame(mapOf(
            "e4" to WhiteKing
        ))
        val moves = chessGame.moveOptions("e4")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-e3") as ValidMove,
            ValidMove("e4-f3") as ValidMove,
            ValidMove("e4-f4") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-e5") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-d4") as ValidMove
        )
    }

    @Test
    fun `finds valid king movements on crowded board`() {
        val chessGame = ChessGame(mapOf(
            "e4" to WhiteKing,
            "d5" to BlackPawn,
            "e5" to WhiteQueen
        ))
        val moves = chessGame.moveOptions("e4")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-e3") as ValidMove,
            ValidMove("e4-f3") as ValidMove,
            ValidMove("e4-f4") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-d4") as ValidMove
        )
    }

    @Test
    fun `castling queenside on empty board`() {
        val chessGame = ChessGame(mapOf(
            "e1" to WhiteKing,
            "a1" to WhiteRook
        ))
        assertThat(chessGame.whiteCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.whiteCastlingOptions.queenSide).isTrue()

        val updatedGame = chessGame.movePiece(ValidMove("e1-c1") as ValidMove)
        assertThat(updatedGame.pieceOn("c1")).isEqualTo(WhiteKing)
        assertThat(updatedGame.pieceOn("d1")).isEqualTo(WhiteRook)
        assertThat(updatedGame.whiteCastlingOptions.kingSide).isFalse()
        assertThat(updatedGame.whiteCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `castling kingside on empty board`() {
        val chessGame = ChessGame(mapOf(
            "e1" to WhiteKing,
            "h1" to WhiteRook
        ))
        assertThat(chessGame.whiteCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.whiteCastlingOptions.queenSide).isTrue()

        val updatedGame = chessGame.movePiece(ValidMove("e1-g1") as ValidMove)
        assertThat(updatedGame.pieceOn("g1")).isEqualTo(WhiteKing)
        assertThat(updatedGame.pieceOn("f1")).isEqualTo(WhiteRook)
        assertThat(updatedGame.whiteCastlingOptions.kingSide).isFalse()
        assertThat(updatedGame.whiteCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `moving king makes castling unavailable`() {
        val chessGame = ChessGame()
            .movePiece(ValidMove("e2-e4") as ValidMove)
            .movePiece(ValidMove("e7-e6") as ValidMove)
            .movePiece(ValidMove("e1-e2") as ValidMove)

        assertThat(chessGame.whiteCastlingOptions.queenSide).isFalse()
        assertThat(chessGame.whiteCastlingOptions.kingSide).isFalse()
    }

}