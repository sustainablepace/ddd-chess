package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.InProgress
import net.sustainablepace.chess.domain.aggregate.chessgame.ThreefoldRepetition
import net.sustainablepace.chess.domain.aggregate.chessgame.position
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ThreefoldRepetitionTest {

    @Test
    fun `Threefold repetition`() {
        val chessGame = chessGame(
            position(
                mapOf(
                    e1 to WhiteKing,
                    e8 to BlackKing,
                    g1 to WhiteKnight,
                    g8 to BlackKnight,
                    e4 to WhitePawn // so it's not a dead position
                )
            )
        )
        val chessGameAfterMove6 = chessGame
            .movePiece(Move(g1, h3))
            .movePiece(Move(g8, h6))
            .movePiece(Move(h3, g1))
            .movePiece(Move(h6, g8))
            .movePiece(Move(g1, h3))
            .movePiece(Move(g8, h6))
            .movePiece(Move(h3, g1))

        assertThat(chessGameAfterMove6.numberOfNextMove).isEqualTo(8)
        assertThat(chessGameAfterMove6.status).isEqualTo(InProgress)

        val chessGameAfterMove7 = chessGameAfterMove6.movePiece(Move(h6, g8))


        assertThat(chessGameAfterMove7.numberOfNextMove).isEqualTo(9)
        assertThat(chessGameAfterMove7.status).isEqualTo(ThreefoldRepetition)
    }

    @Test
    fun `Threefold repetition (with castling options changing)`() {
        val chessGame = chessGame(
            position(
                mapOf(
                    e1 to WhiteKing,
                    e8 to BlackKing,
                    f3 to WhiteKnight,
                    g3 to WhiteKnight,
                    e4 to WhitePawn // so it's not a dead position
                )
            )
        )
        val chessGameAfterMove9 = chessGame
            .movePiece(Move(e1, f1)) // eliminates castling option for white
            .movePiece(Move(e8, f8)) // eliminates castling option for black
            .movePiece(Move(f1, e1))
            .movePiece(Move(f8, e8))
            .movePiece(Move(e1, f1))
            .movePiece(Move(e8, f8))
            .movePiece(Move(f1, e1))
            .movePiece(Move(f8, e8))
            .movePiece(Move(e1, f1))

        assertThat(chessGameAfterMove9.numberOfNextMove).isEqualTo(10)
        assertThat(chessGameAfterMove9.status).isEqualTo(InProgress)

        val chessGameAfterMove10 = chessGameAfterMove9.movePiece(Move(e8, f8))

        assertThat(chessGameAfterMove10.numberOfNextMove).isEqualTo(11)
        assertThat(chessGameAfterMove10.status).isEqualTo(ThreefoldRepetition)
    }

    @Test
    fun `Threefold repetition (taking en passant into account)`() {
        val chessGame = chessGame(
            position(
                board = mapOf(
                    e1 to WhiteKing,
                    e8 to BlackKing,
                    g1 to WhiteKnight,
                    g8 to BlackKnight,
                    e7 to BlackPawn // so it's not a dead position
                ),
                turn = Black
            )
        )
        val chessGameAfterMove9 = chessGame
            .movePiece(Move(e7, e5)) // en passant square is e5
            .movePiece(Move(g1, h3)) // only now is en passant square null
            .movePiece(Move(g8, h6))
            .movePiece(Move(h3, g1))
            .movePiece(Move(h6, g8))
            .movePiece(Move(g1, h3))
            .movePiece(Move(g8, h6))
            .movePiece(Move(h3, g1))
            .movePiece(Move(h6, g8))

        assertThat(chessGameAfterMove9.numberOfNextMove).isEqualTo(10)
        assertThat(chessGameAfterMove9.status).isEqualTo(InProgress)

        val chessGameAfterMove10 = chessGameAfterMove9.movePiece(Move(g1, h3))

        assertThat(chessGameAfterMove10.numberOfNextMove).isEqualTo(11)
        assertThat(chessGameAfterMove10.status).isEqualTo(ThreefoldRepetition)
    }

}