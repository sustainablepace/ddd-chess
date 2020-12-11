package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhiteKing
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhiteQueen
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhiteRook
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhiteKingTest {
    @Test
    fun `finds valid king movements on empty board`() {
        val chessGame = ChessGame(mutableMapOf(
            "e4" to WhiteKing()
        ))
        val moves = chessGame.findMoves("e4")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
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
        val chessGame = ChessGame(mutableMapOf(
            "e4" to WhiteKing(),
            "d5" to BlackPawn(),
            "e5" to WhiteQueen()
        ))
        val moves = chessGame.findMoves("e4")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
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
        val chessGame = ChessGame(mutableMapOf(
            "e1" to WhiteKing(),
            "a1" to WhiteRook()
        ))
        assertThat(chessGame.whiteCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.whiteCastlingOptions.queenSide).isTrue()

        val updatedGame = chessGame.movePiece(ValidMove("e1-c1") as ValidMove)
        assertThat(updatedGame.position.get("c1")).isEqualTo(WhiteKing())
        assertThat(updatedGame.position.get("d1")).isEqualTo(WhiteRook())
        assertThat(updatedGame.whiteCastlingOptions.kingSide).isFalse()
        assertThat(updatedGame.whiteCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `castling kingside on empty board`() {
        val chessGame = ChessGame(mutableMapOf(
            "e1" to WhiteKing(),
            "h1" to WhiteRook()
        ))
        assertThat(chessGame.whiteCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.whiteCastlingOptions.queenSide).isTrue()

        val updatedGame = chessGame.movePiece(ValidMove("e1-g1") as ValidMove)
        assertThat(updatedGame.position.get("g1")).isEqualTo(WhiteKing())
        assertThat(updatedGame.position.get("f1")).isEqualTo(WhiteRook())
        assertThat(updatedGame.whiteCastlingOptions.kingSide).isFalse()
        assertThat(updatedGame.whiteCastlingOptions.queenSide).isFalse()
    }


}