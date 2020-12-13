package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.ChessGame.Companion.defaultPosition
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChessGameTest {
    @Test
    fun `start a new game in default position`() {
        val game = ChessGame()

        assertThat(game.id).isNotEmpty()
        assertThat(game.status).isEqualTo("in progress")
        assertThat(game.turn).isEqualTo(White)
        assertThat(game.white).isInstanceOf(HumanPlayer::class.java)
        assertThat(game.black).isInstanceOf(ComputerPlayer::class.java)
        assertThat(game.position).isEqualTo(defaultPosition)
        assertThat(game.numberOfNextMove).isEqualTo(1)
        assertThat(game.enPassantSquare).isEqualTo(null)
    }

    @Test
    fun `move e2-e4`() {
        val game = ChessGame()

        assertThat(game.pieceOn(E2)).isEqualTo(WhitePawn)
        assertThat(game.pieceOn(E3)).isEqualTo(NoPiece)

        val move = ValidMove(E2, E3)

        val updatedGame = game.movePiece(move)

        assertThat(updatedGame.turn).isEqualTo(Black)
        assertThat(updatedGame.numberOfNextMove).isEqualTo(2)
        assertThat(updatedGame.pieceOn(E2)).isEqualTo(NoPiece)
        assertThat(updatedGame.pieceOn(E3)).isEqualTo(WhitePawn)
    }

    @Test
    fun `find moves for white in default position`() {
        val game = ChessGame()

        val moves = game.moveOptions()

        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(A2, A3),
            ValidMove(B2, B3),
            ValidMove(C2, C3),
            ValidMove(D2, D3),
            ValidMove(E2, E3),
            ValidMove(F2, F3),
            ValidMove(G2, G3),
            ValidMove(H2, H3),
            ValidMove(A2, A4),
            ValidMove(B2, B4),
            ValidMove(C2, C4),
            ValidMove(D2, D4),
            ValidMove(E2, E4),
            ValidMove(F2, F4),
            ValidMove(G2, G4),
            ValidMove(H2, H4),
            ValidMove(B1, A3),
            ValidMove(B1, C3),
            ValidMove(G1, F3),
            ValidMove(G1, H3)
        )
    }

    @Test
    fun `find moves for black in default position`() {
        val game = ChessGame(Black)

        val moves = game.moveOptions()

        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(A7, A6),
            ValidMove(B7, B6),
            ValidMove(C7, C6),
            ValidMove(D7, D6),
            ValidMove(E7, E6),
            ValidMove(F7, F6),
            ValidMove(G7, G6),
            ValidMove(H7, H6),
            ValidMove(A7, A5),
            ValidMove(B7, B5),
            ValidMove(C7, C5),
            ValidMove(D7, D5),
            ValidMove(E7, E5),
            ValidMove(F7, F5),
            ValidMove(G7, G5),
            ValidMove(H7, H5),
            ValidMove(B8, A6),
            ValidMove(B8, C6),
            ValidMove(G8, F6),
            ValidMove(G8, H6)
        )
    }

    @Test
    fun `en passant (white)`() {
        val chessGame = ChessGame()
        assertThat(chessGame.enPassantSquare).isNull()

        chessGame.movePiece(ValidMove(E2, E4)).let {
            assertThat(it.enPassantSquare).isEqualTo(E4)
        }

        chessGame.movePiece(ValidMove(E2, E3)).let {
            assertThat(it.enPassantSquare).isNull()
        }
    }

    @Test
    fun `en passant (black)`() {
        val chessGame = ChessGame(Black)
        assertThat(chessGame.enPassantSquare).isNull()

        chessGame.movePiece(ValidMove(E7, E6)).let {
            assertThat(it.enPassantSquare).isNull()
        }

        chessGame.movePiece(ValidMove(E7, E5)).let {
            assertThat(it.enPassantSquare).isEqualTo(E5)
        }

    }

    @Test
    fun `en passent capturing works for black (left)`() {
        val chessGame = ChessGame(
            mapOf(
                F4 to BlackPawn,
                E2 to WhitePawn
            )
        )
        val updatedChessGame = chessGame
            .movePiece(ValidMove(E2, E4))
            .movePiece(ValidMove(F4, E3))

        assertThat(updatedChessGame.pieceOn(E3)).isEqualTo(BlackPawn)
        assertThat(updatedChessGame.pieceOn(E4)).isEqualTo(NoPiece)
    }

    @Test
    fun `en passent capturing works for black (right)`() {
        val chessGame = ChessGame(
            mapOf(
                D4 to BlackPawn,
                E2 to WhitePawn
            )
        )
        val updatedChessGame = chessGame
            .movePiece(ValidMove(E2, E4))
            .movePiece(ValidMove(D4, E3))

        assertThat(updatedChessGame.pieceOn(E3)).isEqualTo(BlackPawn)
        assertThat(updatedChessGame.pieceOn(E4)).isEqualTo(NoPiece)
    }

    @Test
    fun `en passent capturing works for white (left)`() {
        val chessGame = ChessGame(
            Black, mapOf(
                E7 to BlackPawn,
                D5 to WhitePawn
            )
        )
        val updatedChessGame = chessGame
            .movePiece(ValidMove(E7, E5))
            .movePiece(ValidMove(D5, E6))

        assertThat(updatedChessGame.pieceOn(E6)).isEqualTo(WhitePawn)
        assertThat(updatedChessGame.pieceOn(E5)).isEqualTo(NoPiece)
    }

    @Test
    fun `en passent capturing works for white (right)`() {
        val chessGame = ChessGame(
            Black, mapOf(
                E7 to BlackPawn,
                F5 to WhitePawn
            )
        )
        val updatedChessGame = chessGame
            .movePiece(ValidMove(E7, E5))
            .movePiece(ValidMove(F5, E6))

        assertThat(updatedChessGame.pieceOn(E6)).isEqualTo(WhitePawn)
        assertThat(updatedChessGame.pieceOn(E5)).isEqualTo(NoPiece)
    }

    @Test
    fun `castling sets the game stats accordingly`() {
        val chessGame = ChessGame(
            mapOf(
                E1 to WhiteKing,
                A1 to WhiteRook,
                E8 to BlackKing,
                A8 to BlackRook
            )
        )
        assertThat(chessGame.whiteCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.whiteCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.blackCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.blackCastlingOptions.queenSide).isTrue()

        val gameAfterWhiteCastling = chessGame
            .movePiece(ValidMove(E1, C1))
        assertThat(gameAfterWhiteCastling.pieceOn(C1)).isEqualTo(WhiteKing)
        assertThat(gameAfterWhiteCastling.pieceOn(D1)).isEqualTo(WhiteRook)
        assertThat(gameAfterWhiteCastling.whiteCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterWhiteCastling.whiteCastlingOptions.queenSide).isFalse()
        assertThat(gameAfterWhiteCastling.blackCastlingOptions.kingSide).isTrue()
        assertThat(gameAfterWhiteCastling.blackCastlingOptions.queenSide).isTrue()

        val gameAfterBlackCastling = gameAfterWhiteCastling
            .movePiece(ValidMove(E8, C8))
        assertThat(gameAfterBlackCastling.pieceOn(C8)).isEqualTo(BlackKing)
        assertThat(gameAfterBlackCastling.pieceOn(D8)).isEqualTo(BlackRook)
        assertThat(gameAfterWhiteCastling.whiteCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterWhiteCastling.whiteCastlingOptions.queenSide).isFalse()
        assertThat(gameAfterBlackCastling.blackCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterBlackCastling.blackCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `disallow moves that result in a checked position`() {
        val chessGame = ChessGame()
            .movePiece(ValidMove(E2, E4))
            .movePiece(ValidMove(E7, E6))
            .movePiece(ValidMove(F1, B5))

        assertThat(chessGame.moveOptions()).doesNotContain(ValidMove(D7, D6), ValidMove(D7, D5))
    }
}