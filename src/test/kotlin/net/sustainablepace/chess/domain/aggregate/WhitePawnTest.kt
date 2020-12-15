package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
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
            Move(E2, E3),
            Move(E2, E4)
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
            Move(E4, E5)
        )
    }

    @Test
    fun `finds valid pawn movements on crowded board`() {
        val chessGame = Position(mapOf(
                E4 to WhitePawn,
                E5 to WhiteQueen,
                d5 to BlackPawn,
                F5 to BlackPawn
            )
        )
        val moves = chessGame.moveOptionsIgnoringCheck(E4)
        assertThat(moves).containsExactlyInAnyOrder(
            Move(E4, F5),
            Move(E4, d5)
        )
    }

    @Test
    fun `finds valid initial pawn movements on crowded board`() {
        val chessGame = Position(mapOf(
                a2 to WhitePawn,
                a4 to BlackPawn
            ))

        val moves = chessGame.moveOptionsIgnoringCheck(a2)
        assertThat(moves).containsExactlyInAnyOrder(
            Move(a2, a3)
        )
    }

    @Test
    fun `finds valid initial pawn movements on crowded board when blocked`() {
        val chessGame = Position(mapOf(
                a2 to WhitePawn,
                a3 to BlackPawn
            )
        )
        val moves = chessGame.moveOptionsIgnoringCheck(a2)
        assertThat(moves).isEmpty()
    }

    @Test
    fun `finds en passant capture moves to the right`() {
        val chessGame = Position(mapOf(
                d4 to BlackPawn,
                E2 to WhitePawn
            )
        )
        val position = chessGame.movePiece(Move(E2, E4))

        assertThat(position.moveOptions(Black)).contains(
            Move(d4, E3)
        )
    }

    @Test
    fun `finds en passant capture moves to the left`() {
        val chessGame = Position(mapOf(
                F4 to BlackPawn,
                E2 to WhitePawn
            )
        )
        val updatedPosition = chessGame.movePiece(Move(E2, E4))

        assertThat(updatedPosition.moveOptions(Black)).contains(
            Move(F4, E3)
        )
    }

    @Test
    fun `promotion to queen`() {
        val chessGame = Position(mapOf(
                F7 to WhitePawn
            )
        )
        val updatedPosition = chessGame.movePiece(Move(F7, F8))

        assertThat(updatedPosition.pieceOn(F8)).isEqualTo(WhiteQueen)
    }

    // TODO: Allow promotion to another piece

}