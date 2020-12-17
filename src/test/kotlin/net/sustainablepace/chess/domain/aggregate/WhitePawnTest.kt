package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhitePawnTest {
    @Test
    fun `finds valid pawn movements on empty board in initial position`() {
        val chessGame = Position(mapOf(
                e2 to WhitePawn
            )
        )
        val moves = chessGame.moveOptionsIgnoringCheck(e2)
        assertThat(moves).containsExactlyInAnyOrder(
            Move(e2, e3),
            Move(e2, e4)
        )
    }

    @Test
    fun `finds valid pawn movements on empty board`() {
        val chessGame = Position(mapOf(
                e4 to WhitePawn
            )
        )
        val moves = chessGame.moveOptionsIgnoringCheck(e4)
        assertThat(moves).containsExactlyInAnyOrder(
            Move(e4, e5)
        )
    }

    @Test
    fun `finds valid pawn movements on crowded board`() {
        val chessGame = Position(mapOf(
                e4 to WhitePawn,
                e5 to WhiteQueen,
                d5 to BlackPawn,
                f5 to BlackPawn
            )
        )
        val moves = chessGame.moveOptionsIgnoringCheck(e4)
        assertThat(moves).containsExactlyInAnyOrder(
            Move(e4, f5),
            Move(e4, d5)
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
                e2 to WhitePawn
            )
        )
        val position = chessGame.movePiece(Move(e2, e4))

        assertThat(position.moveOptions(Black)).contains(
            Move(d4, e3)
        )
    }

    @Test
    fun `finds en passant capture moves to the left`() {
        val chessGame = Position(mapOf(
                f4 to BlackPawn,
                e2 to WhitePawn
            )
        )
        val updatedPosition = chessGame.movePiece(Move(e2, e4))

        assertThat(updatedPosition.moveOptions(Black)).contains(
            Move(f4, e3)
        )
    }

    @Test
    fun `promotion to queen`() {
        val chessGame = Position(mapOf(
                f7 to WhitePawn
            )
        )
        val updatedPosition = chessGame.movePiece(Move(f7, f8))

        assertThat(updatedPosition.pieceOn(f8)).isEqualTo(WhiteQueen)
    }

    // TODO: Allow promotion to another piece

}