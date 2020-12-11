package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhitePawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhiteQueen
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhitePawnTest {
    @Test
    fun `finds valid pawn movements on empty board in initial position`() {
        val chessGame = ChessGame(mutableMapOf(
            "e2" to WhitePawn()
        ))
        val moves = chessGame.findMoves("e2")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e2-e3") as ValidMove,
            ValidMove("e2-e4") as ValidMove
        )
    }

    @Test
    fun `finds valid pawn movements on empty board`() {
        val chessGame = ChessGame(mutableMapOf(
            "e4" to WhitePawn()
        ))
        val moves = chessGame.findMoves("e4")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-e5") as ValidMove
        )
    }

    @Test
    fun `finds valid pawn movements on crowded board`() {
        val chessGame = ChessGame(mutableMapOf(
            "e4" to WhitePawn(),
            "e5" to WhiteQueen(),
            "d5" to BlackPawn(),
            "f5" to BlackPawn()
        ))
        val moves = chessGame.findMoves("e4")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-d5") as ValidMove
        )
    }

    @Test
    fun `finds valid initial pawn movements on crowded board`() {
        val chessGame = ChessGame(mutableMapOf(
            "a2" to WhitePawn(),
            "a4" to BlackPawn()
        ))
        val moves = chessGame.findMoves("a2")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("a2-a3") as ValidMove
        )
    }
    @Test
    fun `finds valid initial pawn movements on crowded board when blocked`() {
        val chessGame = ChessGame(mutableMapOf(
            "a2" to WhitePawn(),
            "a3" to BlackPawn()
        ))
        val moves = chessGame.findMoves("a2")
        Assertions.assertThat(moves).isEmpty()
    }
    @Test
    fun `finds en passant capture moves to the right`() {
        val chessGame = ChessGame(mutableMapOf(
            "d4" to BlackPawn(),
            "e2" to WhitePawn()
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove("e2-e4") as ValidMove)

        assertThat(updatedChessGame.findMoves()).contains(
            ValidMove("d4-e3") as ValidMove
        )
    }

    @Test
    fun `finds en passant capture moves to the left`() {
        val chessGame = ChessGame(mutableMapOf(
            "f4" to BlackPawn(),
            "e2" to WhitePawn()
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove("e2-e4") as ValidMove)

        assertThat(updatedChessGame.findMoves()).contains(
            ValidMove("f4-e3") as ValidMove
        )
    }

    @Test
    fun `promotion to queen`() {
        val chessGame = ChessGame(mutableMapOf(
            "f7" to WhitePawn()
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove("f7-f8") as ValidMove)

        assertThat(updatedChessGame.position.get("f8")).isEqualTo(WhiteQueen())
    }

    // TODO: Allow promotion to another piece

}