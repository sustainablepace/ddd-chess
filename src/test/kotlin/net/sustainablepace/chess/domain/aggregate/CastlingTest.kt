package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.position
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.*
import net.sustainablepace.chess.domain.event.PieceNotMoved
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CastlingTest {

    @Test
    fun `castling sets the game stats accordingly`() {
        val chessGame = chessGame(
            position(
                mapOf(
                    e1 to WhiteKing,
                    a1 to WhiteRook,
                    e2 to WhitePawn,
                    d2 to WhitePawn,
                    c2 to WhitePawn,
                    b2 to WhitePawn,
                    e8 to BlackKing,
                    a8 to BlackRook
                )
            )
        )
        assertThat(chessGame.position.whiteCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.position.whiteCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.position.blackCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.position.blackCastlingOptions.queenSide).isTrue()

        val gameAfterWhiteCastling = chessGame
            .movePiece(Move(e1, c1))

        assertThat(gameAfterWhiteCastling.numberOfNextMove).isEqualTo(2)
        assertThat(gameAfterWhiteCastling.pieceOn(c1)).isEqualTo(WhiteKing)
        assertThat(gameAfterWhiteCastling.pieceOn(d1)).isEqualTo(WhiteRook)
        assertThat(gameAfterWhiteCastling.position.whiteCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterWhiteCastling.position.whiteCastlingOptions.queenSide).isFalse()
        assertThat(gameAfterWhiteCastling.position.blackCastlingOptions.kingSide).isTrue()
        assertThat(gameAfterWhiteCastling.position.blackCastlingOptions.queenSide).isTrue()

        val gameAfterBlackCastling = gameAfterWhiteCastling
            .movePiece(Move(e8, c8))
        assertThat(gameAfterBlackCastling.numberOfNextMove).isEqualTo(3)
        assertThat(gameAfterBlackCastling.pieceOn(c8)).isEqualTo(BlackKing)
        assertThat(gameAfterBlackCastling.pieceOn(d8)).isEqualTo(BlackRook)
        assertThat(gameAfterBlackCastling.position.whiteCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterBlackCastling.position.whiteCastlingOptions.queenSide).isFalse()
        assertThat(gameAfterBlackCastling.position.blackCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterBlackCastling.position.blackCastlingOptions.queenSide).isFalse()
    }


    @Test
    fun `white cannot castle kingside in checked position`() {
        val chessGame = chessGame(
            position(
                mapOf(
                    e1 to WhiteKing,
                    h1 to WhiteRook,
                    e7 to BlackRook
                )
            )
        ).movePiece(Move(e1, g1))

        assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        assertThat(chessGame.position.whiteCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.position.whiteCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `black cannot castle kingside in checked position`() {
        val chessGame = chessGame(
            position(
                board = mapOf(
                    e8 to BlackKing,
                    h8 to BlackRook,
                    e2 to WhiteRook
                ),
                turn = Black
            )
        ).movePiece(Move(e8, g8))

        assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        assertThat(chessGame.position.blackCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.position.blackCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `white cannot castle queenside in checked position`() {
        val chessGame = chessGame(
            position(
                mapOf(
                    e1 to WhiteKing,
                    a1 to WhiteRook,
                    e7 to BlackRook
                )
            )
        ).movePiece(Move(e1, c1))

        assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        assertThat(chessGame.position.whiteCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.position.whiteCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `black cannot castle queenside in checked position`() {
        val chessGame = chessGame(
            position(
                board = mapOf(
                    e8 to BlackKing,
                    a8 to BlackRook,
                    e2 to WhiteRook
                ),
                turn = Black
            )
        ).movePiece(Move(e8, c8))

        assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        assertThat(chessGame.position.blackCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.position.blackCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `white cannot castle kingside via threatened square`() {
        val chessGame = chessGame(
            position(
                mapOf(
                    e1 to WhiteKing,
                    h1 to WhiteRook,
                    a6 to BlackBishop
                )
            )
        ).movePiece(Move(e1, g1))

        assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        assertThat(chessGame.position.whiteCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.position.whiteCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `black cannot castle kingside via threatened square`() {
        val chessGame = chessGame(
            position(
                board = mapOf(
                    e8 to BlackKing,
                    h8 to BlackRook,
                    a3 to WhiteBishop
                ),
                turn = Black
            )
        ).movePiece(Move(e8, g8))

        assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        assertThat(chessGame.position.blackCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.position.blackCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `white cannot castle queenside via threatened square`() {
        val chessGame = chessGame(
            position(
                mapOf(
                    e1 to WhiteKing,
                    a1 to WhiteRook,
                    h5 to BlackBishop
                )
            )
        ).movePiece(Move(e1, c1))

        assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        assertThat(chessGame.position.whiteCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.position.whiteCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `black cannot castle queenside via threatened square`() {
        val chessGame = chessGame(
            position(
                board = mapOf(
                    e8 to BlackKing,
                    a8 to BlackRook,
                    h4 to WhiteBishop
                ),
                turn = Black
            )
        ).movePiece(Move(e8, c8))

        assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        assertThat(chessGame.position.blackCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.position.blackCastlingOptions.kingSide).isTrue()
    }


}