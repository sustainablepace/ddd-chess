package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhitePawnTest {
    @Test
    fun `finds valid pawn movements on empty board in initial position`() {
        val chessGame = Position(mapOf(
                E2 to WhitePawn
            )
        )
        val moves = chessGame.moveOptionsIgnoringCheck(E2)
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(E2, E3),
            ValidMove(E2, E4)
        )
    }

    @Test
    fun `finds valid pawn movements on empty board`() {
        val chessGame = Position(mapOf(
                E4 to WhitePawn
            )
        )
        val moves = chessGame.moveOptionsIgnoringCheck(E4)
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(E4, E5)
        )
    }

    @Test
    fun `finds valid pawn movements on crowded board`() {
        val chessGame = Position(mapOf(
                E4 to WhitePawn,
                E5 to WhiteQueen,
                D5 to BlackPawn,
                F5 to BlackPawn
            )
        )
        val moves = chessGame.moveOptionsIgnoringCheck(E4)
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(E4, F5),
            ValidMove(E4, D5)
        )
    }

    @Test
    fun `finds valid initial pawn movements on crowded board`() {
        val chessGame = Position(mapOf(
                A2 to WhitePawn,
                A4 to BlackPawn
            ))

        val moves = chessGame.moveOptionsIgnoringCheck(A2)
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(A2, A3)
        )
    }

    @Test
    fun `finds valid initial pawn movements on crowded board when blocked`() {
        val chessGame = Position(mapOf(
                A2 to WhitePawn,
                A3 to BlackPawn
            )
        )
        val moves = chessGame.moveOptionsIgnoringCheck(A2)
        assertThat(moves).isEmpty()
    }

    @Test
    fun `finds en passant capture moves to the right`() {
        val chessGame = Position(mapOf(
                D4 to BlackPawn,
                E2 to WhitePawn
            )
        )
        val position = chessGame.movePiece(ValidMove(E2, E4))

        assertThat(position.moveOptions(Black)).contains(
            ValidMove(D4, E3)
        )
    }

    @Test
    fun `finds en passant capture moves to the left`() {
        val chessGame = Position(mapOf(
                F4 to BlackPawn,
                E2 to WhitePawn
            )
        )
        val updatedPosition = chessGame.movePiece(ValidMove(E2, E4))

        assertThat(updatedPosition.moveOptions(Black)).contains(
            ValidMove(F4, E3)
        )
    }

    @Test
    fun `promotion to queen`() {
        val chessGame = Position(mapOf(
                F7 to WhitePawn
            )
        )
        val updatedPosition = chessGame.movePiece(ValidMove(F7, F8))

        assertThat(updatedPosition.pieceOn(F8)).isEqualTo(WhiteQueen)
    }

    // TODO: Allow promotion to another piece

}