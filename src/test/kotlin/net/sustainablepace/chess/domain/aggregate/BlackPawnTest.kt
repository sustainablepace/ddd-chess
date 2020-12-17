package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BlackPawnTest {
    @Test
    fun `finds valid pawn movements on empty board in initial position`() {
        val position = Position(mapOf(
            e7 to BlackPawn
        ))
        val moves = position.moveOptionsIgnoringCheck(e7)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            Move(e7, e6),
            Move(e7, e5)
        )
    }

    @Test
    fun `finds valid pawn movements on empty board`() {
        val position = Position(mapOf(
            e5 to BlackPawn
        ))
        val moves = position.moveOptionsIgnoringCheck(e5)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            Move(e5, e4)
        )
    }

    @Test
    fun `finds en passant capture moves to the right`() {
        val position = Position(mapOf(
            f7 to BlackPawn,
            e5 to WhitePawn
        ))
        val updatedPosition = position.movePiece(Move(f7, f5))

        Assertions.assertThat(updatedPosition.moveOptions(White)).contains(
            Move(e5, f6)
        )
    }

    @Test
    fun `finds en passant capture moves to the left`() {
        val position = Position(mapOf(
            d7 to BlackPawn,
            e5 to WhitePawn
        ))
        val updatedPosition = position.movePiece(Move(d7, d5))

        Assertions.assertThat(updatedPosition.moveOptions(White)).contains(
            Move(e5, d6)
        )
    }

    @Test
    fun `promotion to queen`() {
        val position = Position(mapOf(
            f2 to BlackPawn
        ))
        val updatedPosition = position.movePiece(Move(f2, f1))

        Assertions.assertThat(updatedPosition.pieceOn(f1)).isEqualTo(BlackQueen)
    }

    @Test
    fun `moving left rook makes castling unavailable`() {
        val position = Position()
            .movePiece(Move(a7, a5))
            .movePiece(Move(a2, a3))
            .movePiece(Move(a8, a6))

        Assertions.assertThat(position.blackCastlingOptions.queenSide).isFalse()
        Assertions.assertThat(position.blackCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `moving right rook makes castling unavailable`() {
        val position = Position()
            .movePiece(Move(h7, h5))
            .movePiece(Move(h2, h3))
            .movePiece(Move(h8, h6))

        Assertions.assertThat(position.blackCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(position.blackCastlingOptions.kingSide).isFalse()
    }
}