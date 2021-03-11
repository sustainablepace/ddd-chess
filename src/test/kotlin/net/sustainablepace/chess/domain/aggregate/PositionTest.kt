package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.White
import net.sustainablepace.chess.domain.aggregate.chessgame.position
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.*
import net.sustainablepace.chess.domain.event.PieceNotMovedOnBoard
import net.sustainablepace.chess.domain.move.Move
import net.sustainablepace.chess.domain.move.rules.MoveRuleSet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PositionTest {
    @Test
    fun `initial position is not in check (white)`() {
        assertThat(position().isInCheck).isFalse()
    }

    @Test
    fun `initial position is not in check (black)`() {
        assertThat(position(turn = Black).isInCheck).isFalse()
    }

    @Test
    fun `black is in check by bishop`() {
        val position = position()
            .movePiece(Move(e2, e4))
            .movePiece(Move(d7, d6))
            .movePiece(Move(f1, b5))

        assertThat(position.isInCheck).isTrue()
    }

    @Test
    fun `en passant (white)`() {
        val position = position()
        assertThat(position.enPassantSquare).isNull()

        position.movePiece(Move(e2, e4)).let {
            assertThat(it.enPassantSquare).isEqualTo(e4)
        }

        position.movePiece(Move(e2, e3)).let {
            assertThat(it.enPassantSquare).isNull()
        }
    }

    @Test
    fun `en passant (black)`() {
        val position = position()
        assertThat(position.enPassantSquare).isNull()

        position.movePiece(Move(e7, e6)).let {
            assertThat(it.enPassantSquare).isNull()
        }

        position.movePiece(Move(e7, e5)).let {
            assertThat(it.enPassantSquare).isEqualTo(e5)
        }

    }

    @Test
    fun `find moves for white in default position`() {
        assertThat(position().moveOptions).containsExactlyInAnyOrder(
            Move(a2, a3),
            Move(b2, b3),
            Move(c2, c3),
            Move(d2, d3),
            Move(e2, e3),
            Move(f2, f3),
            Move(g2, g3),
            Move(h2, h3),
            Move(a2, a4),
            Move(b2, b4),
            Move(c2, c4),
            Move(d2, d4),
            Move(e2, e4),
            Move(f2, f4),
            Move(g2, g4),
            Move(h2, h4),
            Move(b1, a3),
            Move(b1, c3),
            Move(g1, f3),
            Move(g1, h3)
        )
    }

    @Test
    fun `find moves for black in default position`() {
        assertThat(position(turn = Black).moveOptions).containsExactlyInAnyOrder(
            Move(a7, a6),
            Move(b7, b6),
            Move(c7, c6),
            Move(d7, d6),
            Move(e7, e6),
            Move(f7, f6),
            Move(g7, g6),
            Move(h7, h6),
            Move(a7, a5),
            Move(b7, b5),
            Move(c7, c5),
            Move(d7, d5),
            Move(e7, e5),
            Move(f7, f5),
            Move(g7, g5),
            Move(h7, h5),
            Move(b8, a6),
            Move(b8, c6),
            Move(g8, f6),
            Move(g8, h6)
        )
    }

    @Test
    fun `position not updated after move`() {
        val position = position().movePiece(Move(a3, a4))

        assertThat(position).isInstanceOf(PieceNotMovedOnBoard::class.java)
    }

    @Test
    fun `initial position is not a dead position`() {
        val position = position()
        assertThat(position.isDeadPosition).isFalse()
    }

    @Test
    fun `dead position (only two kings)`() {
        val position = position(
            mapOf(
                e1 to WhiteKing,
                e8 to BlackKing
            )
        )
        assertThat(position.isDeadPosition).isTrue()
    }

    @Test
    fun `dead position (only two kings and a white bishop)`() {
        val position = position(
            mapOf(
                e1 to WhiteKing,
                c1 to WhiteBishop,
                e8 to BlackKing
            )
        )
        assertThat(position.isDeadPosition).isTrue()
    }

    @Test
    fun `dead position (only two kings and a black bishop)`() {
        val position = position(
            mapOf(
                e1 to WhiteKing,
                c8 to BlackBishop,
                e8 to BlackKing
            )
        )
        assertThat(position.isDeadPosition).isTrue()
    }

    @Test
    fun `dead position (only two kings and two bishops on different coloured squares)`() {
        val position = position(
            mapOf(
                e1 to WhiteKing,
                c1 to WhiteBishop,
                e8 to BlackKing,
                c8 to BlackBishop
            )
        )
        assertThat(position.isDeadPosition).isFalse()
    }

    @Test
    fun `dead position (only two kings and two bishops on identical coloured squares)`() {
        val position = position(
            mapOf(
                e1 to WhiteKing,
                c1 to WhiteBishop,
                e8 to BlackKing,
                f8 to BlackBishop
            )
        )
        assertThat(position.isDeadPosition).isTrue()
    }

    @Test
    fun `dead position (only two kings and a white knight)`() {
        val position = position(
            mapOf(
                e1 to WhiteKing,
                b1 to WhiteKnight,
                e8 to BlackKing
            )
        )
        assertThat(position.isDeadPosition).isTrue()
    }

    @Test
    fun `dead position (only two kings and a black knight)`() {
        val position = position(
            mapOf(
                e1 to WhiteKing,
                b8 to BlackKnight,
                e8 to BlackKing
            )
        )
        assertThat(position.isDeadPosition).isTrue()
    }

    @Test
    fun `pawn advance does not threaten square ahead`() {
        val position = position(
            mapOf(
                e5 to WhitePawn,
                e7 to BlackKing
            )
        )
        assertThat(position.moveOptions).contains(Move(e5, e6))
        assertThat(MoveRuleSet.isSquareThreatenedBy(e6, White, position)).isFalse()
    }

    @Test
    fun `bishop threatens rook`() {
        val position = position(
            board = mapOf(
                g7 to BlackBishop
            ),
            turn = Black
        )
        assertThat(position.moveOptions).contains(Move(g7, a1))
        assertThat(MoveRuleSet.isSquareThreatenedBy(a1, Black, position)).isTrue()
    }
}