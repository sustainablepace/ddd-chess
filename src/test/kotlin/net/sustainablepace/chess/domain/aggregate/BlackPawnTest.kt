package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
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
            ValidMove(E7, E6),
            ValidMove(E7, E5)
        )
    }

    @Test
    fun `finds valid pawn movements on empty board`() {
        val position = Position(mapOf(
            E5 to BlackPawn
        ))
        val moves = position.moveOptionsIgnoringCheck(E5)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(E5, E4)
        )
    }

    @Test
    fun `finds en passant capture moves to the right`() {
        val position = Position(mapOf(
            F7 to BlackPawn,
            E5 to WhitePawn
        ))
        val updatedPosition = position.movePiece(ValidMove(F7, F5))

        Assertions.assertThat(updatedPosition.moveOptions(White)).contains(
            ValidMove(E5, F6)
        )
    }

    @Test
    fun `finds en passant capture moves to the left`() {
        val position = Position(mapOf(
            D7 to BlackPawn,
            E5 to WhitePawn
        ))
        val updatedPosition = position.movePiece(ValidMove(D7, D5))

        Assertions.assertThat(updatedPosition.moveOptions(White)).contains(
            ValidMove(E5, D6)
        )
    }

    @Test
    fun `promotion to queen`() {
        val position = Position(mapOf(
            F2 to BlackPawn
        ))
        val updatedPosition = position.movePiece(ValidMove(F2, F1))

        Assertions.assertThat(updatedPosition.pieceOn(F1)).isEqualTo(BlackQueen)
    }

    @Test
    fun `moving left rook makes castling unavailable`() {
        val position = Position()
            .movePiece(ValidMove(A7, A5))
            .movePiece(ValidMove(A2, A3))
            .movePiece(ValidMove(A8, A6))

        Assertions.assertThat(position.blackCastlingOptions.queenSide).isFalse()
        Assertions.assertThat(position.blackCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `moving right rook makes castling unavailable`() {
        val position = Position()
            .movePiece(ValidMove(H7, H5))
            .movePiece(ValidMove(H2, H3))
            .movePiece(ValidMove(H8, H6))

        Assertions.assertThat(position.blackCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(position.blackCastlingOptions.kingSide).isFalse()
    }
}