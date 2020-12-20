package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.event.PieceNotMoved
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class CastlingTest {

    @Test
    fun `castling sets the game stats accordingly`() {
        val chessGame = ChessGame(
            Position(
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
        Assertions.assertThat(chessGame.position.whiteCastlingOptions.kingSide).isTrue()
        Assertions.assertThat(chessGame.position.whiteCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(chessGame.position.blackCastlingOptions.kingSide).isTrue()
        Assertions.assertThat(chessGame.position.blackCastlingOptions.queenSide).isTrue()

        val gameAfterWhiteCastling = chessGame
            .movePiece(Move(e1, c1))

        Assertions.assertThat(gameAfterWhiteCastling.numberOfNextMove).isEqualTo(2)
        Assertions.assertThat(gameAfterWhiteCastling.pieceOn(c1)).isEqualTo(WhiteKing)
        Assertions.assertThat(gameAfterWhiteCastling.pieceOn(d1)).isEqualTo(WhiteRook)
        Assertions.assertThat(gameAfterWhiteCastling.position.whiteCastlingOptions.kingSide).isFalse()
        Assertions.assertThat(gameAfterWhiteCastling.position.whiteCastlingOptions.queenSide).isFalse()
        Assertions.assertThat(gameAfterWhiteCastling.position.blackCastlingOptions.kingSide).isTrue()
        Assertions.assertThat(gameAfterWhiteCastling.position.blackCastlingOptions.queenSide).isTrue()

        val gameAfterBlackCastling = gameAfterWhiteCastling
            .movePiece(Move(e8, c8))
        Assertions.assertThat(gameAfterBlackCastling.numberOfNextMove).isEqualTo(3)
        Assertions.assertThat(gameAfterBlackCastling.pieceOn(c8)).isEqualTo(BlackKing)
        Assertions.assertThat(gameAfterBlackCastling.pieceOn(d8)).isEqualTo(BlackRook)
        Assertions.assertThat(gameAfterBlackCastling.position.whiteCastlingOptions.kingSide).isFalse()
        Assertions.assertThat(gameAfterBlackCastling.position.whiteCastlingOptions.queenSide).isFalse()
        Assertions.assertThat(gameAfterBlackCastling.position.blackCastlingOptions.kingSide).isFalse()
        Assertions.assertThat(gameAfterBlackCastling.position.blackCastlingOptions.queenSide).isFalse()
    }


    @Test
    fun `white cannot castle kingside in checked position`() {
        val chessGame = ChessGame(
            Position(
                mapOf(
                    e1 to WhiteKing,
                    h1 to WhiteRook,
                    e7 to BlackRook
                )
            )
        ).movePiece(Move(e1, g1))

        Assertions.assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        Assertions.assertThat(chessGame.position.whiteCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(chessGame.position.whiteCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `black cannot castle kingside in checked position`() {
        val chessGame = ChessGame(
            Position(
                board = mapOf(
                    e8 to BlackKing,
                    h8 to BlackRook,
                    e2 to WhiteRook
                ),
                turn = Black
            )
        ).movePiece(Move(e8, g8))

        Assertions.assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        Assertions.assertThat(chessGame.position.blackCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(chessGame.position.blackCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `white cannot castle queenside in checked position`() {
        val chessGame = ChessGame(
            Position(
                mapOf(
                    e1 to WhiteKing,
                    a1 to WhiteRook,
                    e7 to BlackRook
                )
            )
        ).movePiece(Move(e1, c1))

        Assertions.assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        Assertions.assertThat(chessGame.position.whiteCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(chessGame.position.whiteCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `black cannot castle queenside in checked position`() {
        val chessGame = ChessGame(
            Position(
                board = mapOf(
                    e8 to BlackKing,
                    a8 to BlackRook,
                    e2 to WhiteRook
                ),
                turn = Black
            )
        ).movePiece(Move(e8, c8))

        Assertions.assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        Assertions.assertThat(chessGame.position.blackCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(chessGame.position.blackCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `white cannot castle kingside via threatened square`() {
        val chessGame = ChessGame(
            Position(
                mapOf(
                    e1 to WhiteKing,
                    h1 to WhiteRook,
                    a6 to BlackBishop
                )
            )
        ).movePiece(Move(e1, g1))

        Assertions.assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        Assertions.assertThat(chessGame.position.whiteCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(chessGame.position.whiteCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `black cannot castle kingside via threatened square`() {
        val chessGame = ChessGame(
            Position(
                board = mapOf(
                    e8 to BlackKing,
                    h8 to BlackRook,
                    a3 to WhiteBishop
                ),
                turn = Black
            )
        ).movePiece(Move(e8, g8))

        Assertions.assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        Assertions.assertThat(chessGame.position.blackCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(chessGame.position.blackCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `white cannot castle queenside via threatened square`() {
        val chessGame = ChessGame(
            Position(
                mapOf(
                    e1 to WhiteKing,
                    a1 to WhiteRook,
                    h5 to BlackBishop
                )
            )
        ).movePiece(Move(e1, c1))

        Assertions.assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        Assertions.assertThat(chessGame.position.whiteCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(chessGame.position.whiteCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `black cannot castle queenside via threatened square`() {
        val chessGame = ChessGame(
            Position(
                board = mapOf(
                    e8 to BlackKing,
                    a8 to BlackRook,
                    h4 to WhiteBishop
                ),
                turn = Black
            )
        ).movePiece(Move(e8, c8))

        Assertions.assertThat(chessGame).isInstanceOf(PieceNotMoved::class.java)

        Assertions.assertThat(chessGame.position.blackCastlingOptions.queenSide).isTrue()
        Assertions.assertThat(chessGame.position.blackCastlingOptions.kingSide).isTrue()
    }


}