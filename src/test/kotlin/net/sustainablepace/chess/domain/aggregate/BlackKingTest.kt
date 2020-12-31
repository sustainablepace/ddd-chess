package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BlackKingTest {
    @Test
    fun `castling queenside on empty board`() {
        val position = position(mapOf(
            e8 to BlackKing,
            a8 to BlackRook
        ))
        assertThat(position.blackCastlingOptions.kingSide).isTrue()
        assertThat(position.blackCastlingOptions.queenSide).isTrue()

        val updatedPosition = position.movePiece(Move(e8, c8))
        assertThat(updatedPosition.pieceOn(c8)).isEqualTo(BlackKing)
        assertThat(updatedPosition.pieceOn(d8)).isEqualTo(BlackRook)
        assertThat(updatedPosition.blackCastlingOptions.kingSide).isFalse()
        assertThat(updatedPosition.blackCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `castling kingside on empty board`() {
        val position = position(mapOf(
            e8 to BlackKing,
            h8 to BlackRook
        ))
        assertThat(position.blackCastlingOptions.kingSide).isTrue()
        assertThat(position.blackCastlingOptions.queenSide).isTrue()

        val updatedPosition = position.movePiece(Move(e8, g8))
        assertThat(updatedPosition.pieceOn(g8)).isEqualTo(BlackKing)
        assertThat(updatedPosition.pieceOn(f8)).isEqualTo(BlackRook)
        assertThat(updatedPosition.blackCastlingOptions.kingSide).isFalse()
        assertThat(updatedPosition.blackCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `moving king makes castling unavailable`() {
        val position = position()
            .movePiece(Move(e7, e5))
            .movePiece(Move(e2, e3))
            .movePiece(Move(e8, e7))

        assertThat(position.blackCastlingOptions.queenSide).isFalse()
        assertThat(position.blackCastlingOptions.kingSide).isFalse()
    }

    @Test
    fun `kings can check each other`() {
        val position = position(
            board = mapOf(
                e1 to WhiteKing,
                e3 to BlackKing
            )
        )

        assertThat(position.moveOptions).containsExactly(
            Move(e1, f1),
            Move(e1, d1)
        )
    }

}