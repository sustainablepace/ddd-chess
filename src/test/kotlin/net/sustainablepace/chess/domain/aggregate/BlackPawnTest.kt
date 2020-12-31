package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import net.sustainablepace.chess.domain.move.PromotionMove
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BlackPawnTest {
    @Test
    fun `finds valid pawn movements on empty board in initial position`() {
        val position = position(
            board = mapOf(
                e7 to BlackPawn
            ),
            turn = Black
        )
        val moves = position.moveOptions
        assertThat(moves).containsExactlyInAnyOrder(
            Move(e7, e6),
            Move(e7, e5)
        )
    }

    @Test
    fun `finds valid pawn movements on empty board`() {
        val position = position(
            board = mapOf(
                e5 to BlackPawn
            ),
            turn = Black
        )
        assertThat(position.moveOptions).containsExactlyInAnyOrder(
            Move(e5, e4)
        )
    }

    @Test
    fun `finds en passant capture moves to the right`() {
        val position = position(
            board = mapOf(
                f7 to BlackPawn,
                e5 to WhitePawn
            ),
            turn = Black
        )
        val updatedPosition = position.movePiece(Move(f7, f5))

        assertThat(updatedPosition.moveOptions).contains(
            Move(e5, f6)
        )
    }

    @Test
    fun `finds en passant capture moves to the left`() {
        val position = position(
            board = mapOf(
                d7 to BlackPawn,
                e5 to WhitePawn
            ),
            turn = Black
        )
        val updatedPosition = position.movePiece(Move(d7, d5))

        assertThat(updatedPosition.moveOptions).contains(
            Move(e5, d6)
        )
    }


    @Test
    fun `promotion to all options`() {
        val chessGame = position(
            board = mapOf(
                f2 to BlackPawn
            ),
            turn = Black
        )
        assertThat(chessGame.moveOptions).containsExactly(
            PromotionMove(f2, f1, BlackQueen),
            PromotionMove(f2, f1, BlackRook),
            PromotionMove(f2, f1, BlackBishop),
            PromotionMove(f2, f1, BlackKnight),
        )

        val positionAfterQueenPromotion = chessGame.movePiece(PromotionMove(f2, f1, BlackQueen))
        assertThat(positionAfterQueenPromotion.pieceOn(f1)).isEqualTo(BlackQueen)
        val positionAfterRookPromotion = chessGame.movePiece(PromotionMove(f2, f1, BlackRook))
        assertThat(positionAfterRookPromotion.pieceOn(f1)).isEqualTo(BlackRook)

        val positionAfterKnightPromotion = chessGame.movePiece(PromotionMove(f2, f1, BlackKnight))
        assertThat(positionAfterKnightPromotion.pieceOn(f1)).isEqualTo(BlackKnight)

        val positionAfterBishopPromotion = chessGame.movePiece(PromotionMove(f2, f1, BlackBishop))
        assertThat(positionAfterBishopPromotion.pieceOn(f1)).isEqualTo(BlackBishop)
    }

    @Test
    fun `moving left rook makes castling unavailable`() {
        val position = position()
            .movePiece(Move(a7, a5))
            .movePiece(Move(a2, a3))
            .movePiece(Move(a8, a6))

        assertThat(position.blackCastlingOptions.queenSide).isFalse()
        assertThat(position.blackCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `moving right rook makes castling unavailable`() {
        val position = position()
            .movePiece(Move(h7, h5))
            .movePiece(Move(h2, h3))
            .movePiece(Move(h8, h6))

        assertThat(position.blackCastlingOptions.queenSide).isTrue()
        assertThat(position.blackCastlingOptions.kingSide).isFalse()
    }
}