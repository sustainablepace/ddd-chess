package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackQueen
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhitePawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.BlackPieces
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BlackPawnTest {
    @Test
    fun `finds valid pawn movements on empty board in initial position`() {
        val chessGame = ChessGame(mutableMapOf(
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
        val chessGame = ChessGame(mutableMapOf(
            "e5" to BlackPawn()
        ))
        val moves = chessGame.findMoves("e5")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e5-e4") as ValidMove
        )
    }

    @Test
    fun `finds en passant capture moves to the right`() {
        val chessGame = ChessGame(BlackPieces, mutableMapOf(
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
        val chessGame = ChessGame(BlackPieces, mutableMapOf(
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
        val chessGame = ChessGame(BlackPieces, mutableMapOf(
            "f2" to BlackPawn()
        ))
        val updatedChessGame = chessGame.movePiece(ValidMove("f2-f1") as ValidMove)

        Assertions.assertThat(updatedChessGame.position.get("f1")).isEqualTo(BlackQueen())
    }
}