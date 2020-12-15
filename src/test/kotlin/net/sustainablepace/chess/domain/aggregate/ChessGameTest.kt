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

        assertThat(updatedChessGame.numberOfNextMove).isEqualTo(3)
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

        assertThat(updatedChessGame.numberOfNextMove).isEqualTo(3)
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

        assertThat(updatedChessGame.numberOfNextMove).isEqualTo(3)
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

        assertThat(updatedChessGame.numberOfNextMove).isEqualTo(3)
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

        assertThat(gameAfterWhiteCastling.numberOfNextMove).isEqualTo(2)
        assertThat(gameAfterWhiteCastling.pieceOn(C1)).isEqualTo(WhiteKing)
        assertThat(gameAfterWhiteCastling.pieceOn(D1)).isEqualTo(WhiteRook)
        assertThat(gameAfterWhiteCastling.position.whiteCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterWhiteCastling.position.whiteCastlingOptions.queenSide).isFalse()
        assertThat(gameAfterWhiteCastling.position.blackCastlingOptions.kingSide).isTrue()
        assertThat(gameAfterWhiteCastling.position.blackCastlingOptions.queenSide).isTrue()

        val gameAfterBlackCastling = gameAfterWhiteCastling
            .movePiece(ValidMove(E8, C8))
        assertThat(gameAfterBlackCastling.numberOfNextMove).isEqualTo(3)
        assertThat(gameAfterBlackCastling.pieceOn(C8)).isEqualTo(BlackKing)
        assertThat(gameAfterBlackCastling.pieceOn(D8)).isEqualTo(BlackRook)
        assertThat(gameAfterBlackCastling.position.whiteCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterBlackCastling.position.whiteCastlingOptions.queenSide).isFalse()
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

    @Test
    fun `Fifty move rule reset because pawn moved`() {
        val chessGame = ChessGame(
            Position(
                mapOf(
                    E1 to WhiteKing,
                    E8 to BlackKing,
                    F1 to WhiteKnight,
                    G3 to WhiteKnight,
                    A3 to BlackPawn
                ))
        )
        val chessGameAfterMove3 = chessGame
            .movePiece(ValidMove(F1, E3))
            .movePiece(ValidMove(E8, E7))
            .movePiece(ValidMove(E3, D1))

        assertThat(chessGameAfterMove3.numberOfNextMove).isEqualTo(4)
        assertThat(chessGameAfterMove3.fiftyMoveRule).isEqualTo(3)

        val chessGameAfterMove4 = chessGameAfterMove3.movePiece(ValidMove(A3, A2))

        assertThat(chessGameAfterMove4.fiftyMoveRule).isEqualTo(0)
    }


    @Test
    fun `Fifty move rule reset because piece was captured`() {
        val chessGame = ChessGame(
            Position(
                mapOf(
                    E1 to WhiteKing,
                    E8 to BlackKing,
                    F1 to WhiteKnight,
                    G3 to WhiteKnight,
                    A3 to BlackPawn
                ))
        )
        val chessGameAfterMove4 = chessGame
            .movePiece(ValidMove(F1, D2))
            .movePiece(ValidMove(E8, E7))
            .movePiece(ValidMove(D2, B1))
            .movePiece(ValidMove(E7, E8))

        assertThat(chessGameAfterMove4.numberOfNextMove).isEqualTo(5)
        assertThat(chessGameAfterMove4.fiftyMoveRule).isEqualTo(4)

        val chessGameAfterMove5 = chessGameAfterMove4.movePiece(ValidMove(B1, A3))

        assertThat(chessGameAfterMove5.numberOfNextMove).isEqualTo(6)
        assertThat(chessGameAfterMove5.fiftyMoveRule).isEqualTo(0)
    }

    @Test
     fun `Fifty move rule because no pawn moved and no piece captured`() {
        val chessGame = ChessGame(
            Position(
                mapOf(
                    E1 to WhiteKing,
                    E8 to BlackKing,
                    F3 to WhiteKnight,
                    G3 to WhiteKnight
                ))
        )
        val chessGameAfterMove50 = chessGame
            .movePiece(ValidMove(E1, F1))
            .movePiece(ValidMove(E8, F8))
            .movePiece(ValidMove(F1, G1))
            .movePiece(ValidMove(F8, G8))
            .movePiece(ValidMove(G1, H1))
            .movePiece(ValidMove(G8, H8))
            .movePiece(ValidMove(H1, H2))
            .movePiece(ValidMove(H8, H7))
            .movePiece(ValidMove(H2, G2))
            .movePiece(ValidMove(H7, G7))
            .movePiece(ValidMove(G2, F2))
            .movePiece(ValidMove(G7, F7))
            .movePiece(ValidMove(F2, E2))
            .movePiece(ValidMove(F7, E7))
            .movePiece(ValidMove(E2, D2))
            .movePiece(ValidMove(E7, D7))
            .movePiece(ValidMove(D2, C2))
            .movePiece(ValidMove(D7, C7))
            .movePiece(ValidMove(C2, B2))
            .movePiece(ValidMove(C7, B7))
            .movePiece(ValidMove(B2, A2))
            .movePiece(ValidMove(B7, A7))
            .movePiece(ValidMove(A2, A1))
            .movePiece(ValidMove(A7, A8))
            .movePiece(ValidMove(A1, B1))
            .movePiece(ValidMove(A8, B8))
            .movePiece(ValidMove(B1, C1))
            .movePiece(ValidMove(B8, C8))
            .movePiece(ValidMove(C1, D1))
            .movePiece(ValidMove(C8, D8))
            .movePiece(ValidMove(D1, E1))
            .movePiece(ValidMove(D8, E8))
            .movePiece(ValidMove(E1, F1))
            .movePiece(ValidMove(E8, F8))
            .movePiece(ValidMove(F1, G1))
            .movePiece(ValidMove(F8, G8))
            .movePiece(ValidMove(G1, H1))
            .movePiece(ValidMove(G8, H8))
            .movePiece(ValidMove(H1, H2))
            .movePiece(ValidMove(H8, H7))
            .movePiece(ValidMove(H2, G2))
            .movePiece(ValidMove(H7, G7))
            .movePiece(ValidMove(G2, F2))
            .movePiece(ValidMove(G7, F7))
            .movePiece(ValidMove(F2, E2))
            .movePiece(ValidMove(F7, E7))
            .movePiece(ValidMove(E2, D2))
            .movePiece(ValidMove(E7, D7))
            .movePiece(ValidMove(D2, C2))
            .movePiece(ValidMove(D7, C7))

        assertThat(chessGameAfterMove50.numberOfNextMove).isEqualTo(51)
        assertThat(chessGameAfterMove50.fiftyMoveRule).isEqualTo(50)
        assertThat(chessGameAfterMove50.status).isEqualTo(FiftyMoveRule)
    }

}