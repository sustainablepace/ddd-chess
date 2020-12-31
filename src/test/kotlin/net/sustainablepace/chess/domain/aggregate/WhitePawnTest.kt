package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import net.sustainablepace.chess.domain.move.PromotionMove
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhitePawnTest {
    @Test
    fun `finds valid pawn movements on empty board in initial position`() {
        val chessGame = position(mapOf(
                e2 to WhitePawn
            )
        )
        assertThat(chessGame.moveOptions).containsExactlyInAnyOrder(
            Move(e2, e3),
            Move(e2, e4)
        )
    }

    @Test
    fun `finds valid pawn movements on empty board`() {
        val chessGame = position(mapOf(
                e4 to WhitePawn
            )
        )
        assertThat(chessGame.moveOptions).containsExactlyInAnyOrder(
            Move(e4, e5)
        )
    }

    @Test
    fun `finds valid pawn movements on crowded board`() {
        val chessGame = position(mapOf(
                e4 to WhitePawn,
                e5 to WhiteQueen,
                d5 to BlackPawn,
                f5 to BlackPawn
            )
        )
        assertThat(chessGame.moveOptions.filter { it.departureSquare == e4 }).containsExactlyInAnyOrder(
            Move(e4, f5),
            Move(e4, d5)
        )
    }

    @Test
    fun `finds valid initial pawn movements on crowded board`() {
        val chessGame = position(mapOf(
                a2 to WhitePawn,
                a4 to BlackPawn
            ))

        assertThat(chessGame.moveOptions).containsExactlyInAnyOrder(
            Move(a2, a3)
        )
    }

    @Test
    fun `finds valid initial pawn movements on crowded board when blocked`() {
        val chessGame = position(mapOf(
                a2 to WhitePawn,
                a3 to BlackPawn
            )
        )
        assertThat(chessGame.moveOptions).isEmpty()
    }

    @Test
    fun `finds en passant capture moves to the right`() {
        val chessGame = position(mapOf(
                d4 to BlackPawn,
                e2 to WhitePawn
            )
        )
        val position = chessGame.movePiece(Move(e2, e4))

        assertThat(position.moveOptions).contains(
            Move(d4, e3)
        )
    }

    @Test
    fun `finds en passant capture moves to the left`() {
        val chessGame = position(mapOf(
                f4 to BlackPawn,
                e2 to WhitePawn
            )
        )
        val updatedPosition = chessGame.movePiece(Move(e2, e4))

        assertThat(updatedPosition.moveOptions).contains(
            Move(f4, e3)
        )
    }

    @Test
    fun `promotion to all options`() {
        val chessGame = position(mapOf(
                f7 to WhitePawn
            )
        )
        assertThat(chessGame.moveOptions).containsExactly(
            PromotionMove(f7, f8, WhiteQueen),
            PromotionMove(f7, f8, WhiteRook),
            PromotionMove(f7, f8, WhiteBishop),
            PromotionMove(f7, f8, WhiteKnight),
        )

        val positionAfterQueenPromotion = chessGame.movePiece(PromotionMove(f7, f8, WhiteQueen))
        assertThat(positionAfterQueenPromotion.pieceOn(f8)).isEqualTo(WhiteQueen)
        val positionAfterRookPromotion = chessGame.movePiece(PromotionMove(f7, f8, WhiteRook))
        assertThat(positionAfterRookPromotion.pieceOn(f8)).isEqualTo(WhiteRook)

        val positionAfterKnightPromotion = chessGame.movePiece(PromotionMove(f7, f8, WhiteKnight))
        assertThat(positionAfterKnightPromotion.pieceOn(f8)).isEqualTo(WhiteKnight)

        val positionAfterBishopPromotion = chessGame.movePiece(PromotionMove(f7, f8, WhiteBishop))
        assertThat(positionAfterBishopPromotion.pieceOn(f8)).isEqualTo(WhiteBishop)
    }
}