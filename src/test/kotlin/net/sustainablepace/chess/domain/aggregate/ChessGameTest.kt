package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChessGameTest {
    @Test
    fun `start a new game in default position`() {
        val game = ChessGame()

        assertThat(game.id).isNotEmpty()
        assertThat(game.status).isEqualTo(InProgress)
        assertThat(game.turn).isEqualTo(White)
        assertThat(game.white).isInstanceOf(HumanPlayer::class.java)
        assertThat(game.black).isInstanceOf(ComputerPlayer::class.java)
        assertThat(game.position).isEqualTo(Position())
        assertThat(game.numberOfNextMove).isEqualTo(1)
        assertThat(game.position.enPassantSquare).isEqualTo(null)
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
    fun `en passent capturing works for black (left)`() {
        val chessGame = ChessGame(
            Position(mapOf(
                F4 to BlackPawn,
                E2 to WhitePawn
            ))
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
            Position(mapOf(
                D4 to BlackPawn,
                E2 to WhitePawn
            ))
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
            Black, Position(mapOf(
                E7 to BlackPawn,
                D5 to WhitePawn
            ))
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
            Black, Position(mapOf(
                E7 to BlackPawn,
                F5 to WhitePawn
            ))
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
            Position(mapOf(
                E1 to WhiteKing,
                A1 to WhiteRook,
                E8 to BlackKing,
                A8 to BlackRook
            ))
        )
        assertThat(chessGame.position.whiteCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.position.whiteCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.position.blackCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.position.blackCastlingOptions.queenSide).isTrue()

        val gameAfterWhiteCastling = chessGame
            .movePiece(ValidMove(E1, C1))
        assertThat(gameAfterWhiteCastling.pieceOn(C1)).isEqualTo(WhiteKing)
        assertThat(gameAfterWhiteCastling.pieceOn(D1)).isEqualTo(WhiteRook)
        assertThat(gameAfterWhiteCastling.position.whiteCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterWhiteCastling.position.whiteCastlingOptions.queenSide).isFalse()
        assertThat(gameAfterWhiteCastling.position.blackCastlingOptions.kingSide).isTrue()
        assertThat(gameAfterWhiteCastling.position.blackCastlingOptions.queenSide).isTrue()

        val gameAfterBlackCastling = gameAfterWhiteCastling
            .movePiece(ValidMove(E8, C8))
        assertThat(gameAfterBlackCastling.pieceOn(C8)).isEqualTo(BlackKing)
        assertThat(gameAfterBlackCastling.pieceOn(D8)).isEqualTo(BlackRook)
        assertThat(gameAfterWhiteCastling.position.whiteCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterWhiteCastling.position.whiteCastlingOptions.queenSide).isFalse()
        assertThat(gameAfterBlackCastling.position.blackCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterBlackCastling.position.blackCastlingOptions.queenSide).isFalse()
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