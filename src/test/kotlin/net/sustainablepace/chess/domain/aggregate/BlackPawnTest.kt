package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BlackPawnTest {
    @Test
    fun `finds valid pawn movements on empty board in initial position`() {
        val position = Position(mapOf(
            E7 to BlackPawn
        ))
        val moves = position.moveOptionsIgnoringCheck(E7)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            Move(E7, E6),
            Move(E7, E5)
        )
    }

    @Test
    fun `finds valid pawn movements on empty board`() {
        val position = Position(mapOf(
            E5 to BlackPawn
        ))
        val moves = position.moveOptionsIgnoringCheck(E5)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            Move(E5, E4)
        )
    }

    @Test
    fun `finds en passant capture moves to the right`() {
        val position = Position(mapOf(
            F7 to BlackPawn,
            E5 to WhitePawn
        ))
        val updatedPosition = position.movePiece(Move(F7, F5))

        Assertions.assertThat(updatedPosition.moveOptions(White)).contains(
            Move(E5, F6)
        )
    }

    @Test
    fun `finds en passant capture moves to the left`() {
        val position = Position(mapOf(
            d7 to BlackPawn,
            E5 to WhitePawn
        ))
        val updatedPosition = position.movePiece(Move(d7, d5))

        Assertions.assertThat(updatedPosition.moveOptions(White)).contains(
            Move(E5, d6)
        )
    }

    @Test
    fun `promotion to queen`() {
        val position = Position(mapOf(
            F2 to BlackPawn
        ))
        val updatedPosition = position.movePiece(Move(F2, F1))

        Assertions.assertThat(updatedPosition.pieceOn(F1)).isEqualTo(BlackQueen)
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
            .movePiece(Move(H7, H5))
            .movePiece(Move(H2, H3))
            .movePiece(Move(H8, H6))

        Assertions.assertThat(position.blackCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(position.blackCastlingOptions.kingSide).isFalse()
    }
}