package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BlackKingTest {
    @Test
    fun `castling queenside on empty board`() {
        val position = Position(mapOf(
            E8 to BlackKing,
            a8 to BlackRook
        ))
        assertThat(position.blackCastlingOptions.kingSide).isTrue()
        assertThat(position.blackCastlingOptions.queenSide).isTrue()

        val updatedPosition = position.movePiece(Move(E8, c8))
        assertThat(updatedPosition.pieceOn(c8)).isEqualTo(BlackKing)
        assertThat(updatedPosition.pieceOn(d8)).isEqualTo(BlackRook)
        assertThat(updatedPosition.blackCastlingOptions.kingSide).isFalse()
        assertThat(updatedPosition.blackCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `castling kingside on empty board`() {
        val position = Position(mapOf(
            E8 to BlackKing,
            H8 to BlackRook
        ))
        assertThat(position.blackCastlingOptions.kingSide).isTrue()
        assertThat(position.blackCastlingOptions.queenSide).isTrue()

        val updatedPosition = position.movePiece(Move(E8, G8))
        assertThat(updatedPosition.pieceOn(G8)).isEqualTo(BlackKing)
        assertThat(updatedPosition.pieceOn(F8)).isEqualTo(BlackRook)
        assertThat(updatedPosition.blackCastlingOptions.kingSide).isFalse()
        assertThat(updatedPosition.blackCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `moving king makes castling unavailable`() {
        val position = Position()
            .movePiece(Move(E7, E5))
            .movePiece(Move(E2, E3))
            .movePiece(Move(E8, E7))

        assertThat(position.blackCastlingOptions.queenSide).isFalse()
        assertThat(position.blackCastlingOptions.kingSide).isFalse()
    }

}