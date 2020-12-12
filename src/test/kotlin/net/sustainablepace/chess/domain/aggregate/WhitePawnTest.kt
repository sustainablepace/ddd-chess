package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.WhitePawn
import net.sustainablepace.chess.domain.aggregate.chessgame.WhiteQueen
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhitePawnTest {
    @Test
    fun `finds valid pawn movements on empty board in initial position`() {
        val chessGame = ChessGame(mapOf(
            "e2" to WhitePawn
        ))
        val moves = chessGame.moveOptions("e2")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e2-e3") as ValidMove,
            ValidMove("e2-e4") as ValidMove
        )
    }

    @Test
    fun `finds valid pawn movements on empty board`() {
        val chessGame = ChessGame(mapOf(
            "e4" to WhitePawn
        ))
        val moves = chessGame.moveOptions("e4")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-e5") as ValidMove
        )
    }

    @Test
    fun `finds valid pawn movements on crowded board`() {
        val chessGame = ChessGame(mapOf(
            "e4" to WhitePawn,
            "e5" to WhiteQueen,
            "d5" to BlackPawn,
            "f5" to BlackPawn
        ))
        val moves = chessGame.moveOptions("e4")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-d5") as ValidMove
        )
    }

    @Test
    fun `finds valid initial pawn movements on crowded board`() {
        val chessGame = ChessGame(mapOf(
            "a2" to WhitePawn,
            "a4" to BlackPawn
        ))
        val moves = chessGame.moveOptions("a2")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("a2-a3") as ValidMove
        )
    }
    @Test
    fun `finds valid initial pawn movements on crowded board when blocked`() {
        val chessGame = ChessGame(mapOf(
            "a2" to WhitePawn,
            "a3" to BlackPawn
        ))
        val moves = chessGame.moveOptions("a2")
        assertThat(moves).isEmpty()
    }
    @Test
    fun `finds en passant capture moves to the right`() {
        val chessGame = ChessGame(mapOf(
            "d4" to BlackPawn,
            "e2" to WhitePawn
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove("e2-e4") as ValidMove)

        assertThat(updatedChessGame.moveOptions()).contains(
            ValidMove("d4-e3") as ValidMove
        )
    }

    @Test
    fun `finds en passant capture moves to the left`() {
        val chessGame = ChessGame(mapOf(
            "f4" to BlackPawn,
            "e2" to WhitePawn
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove("e2-e4") as ValidMove)

        assertThat(updatedChessGame.moveOptions()).contains(
            ValidMove("f4-e3") as ValidMove
        )
    }

    @Test
    fun `promotion to queen`() {
        val chessGame = ChessGame(mapOf(
            "f7" to WhitePawn
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove("f7-f8") as ValidMove)

        assertThat(updatedChessGame.pieceOn("f8")).isEqualTo(WhiteQueen)
    }

    // TODO: Allow promotion to another piece

}