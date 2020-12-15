package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChessGameTest {
    @Test
    fun `start a new game in default position`() {
        val game = ChessGame()

        assertThat(game.id).isNotEmpty()
        assertThat(game.getStatus()).isEqualTo(InProgress)
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

        val move = Move(E2, E3)

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
            .movePiece(Move(E2, E4))
            .movePiece(Move(F4, E3))

        assertThat(updatedChessGame.numberOfNextMove).isEqualTo(3)
        assertThat(updatedChessGame.pieceOn(E3)).isEqualTo(BlackPawn)
        assertThat(updatedChessGame.pieceOn(E4)).isEqualTo(NoPiece)
    }

    @Test
    fun `en passent capturing works for black (right)`() {
        val chessGame = ChessGame(
            Position(mapOf(
                d4 to BlackPawn,
                E2 to WhitePawn
            ))
        )
        val updatedChessGame = chessGame
            .movePiece(Move(E2, E4))
            .movePiece(Move(d4, E3))

        assertThat(updatedChessGame.numberOfNextMove).isEqualTo(3)
        assertThat(updatedChessGame.pieceOn(E3)).isEqualTo(BlackPawn)
        assertThat(updatedChessGame.pieceOn(E4)).isEqualTo(NoPiece)
    }

    @Test
    fun `en passent capturing works for white (left)`() {
        val chessGame = ChessGame(
            Black, Position(mapOf(
                E7 to BlackPawn,
                d5 to WhitePawn
            ))
        )
        val updatedChessGame = chessGame
            .movePiece(Move(E7, E5))
            .movePiece(Move(d5, E6))

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
            .movePiece(Move(E7, E5))
            .movePiece(Move(F5, E6))

        assertThat(updatedChessGame.numberOfNextMove).isEqualTo(3)
        assertThat(updatedChessGame.pieceOn(E6)).isEqualTo(WhitePawn)
        assertThat(updatedChessGame.pieceOn(E5)).isEqualTo(NoPiece)
    }

    @Test
    fun `castling sets the game stats accordingly`() {
        val chessGame = ChessGame(
            Position(mapOf(
                E1 to WhiteKing,
                a1 to WhiteRook,
                E8 to BlackKing,
                a8 to BlackRook
            ))
        )
        assertThat(chessGame.position.whiteCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.position.whiteCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.position.blackCastlingOptions.kingSide).isTrue()
        assertThat(chessGame.position.blackCastlingOptions.queenSide).isTrue()

        val gameAfterWhiteCastling = chessGame
            .movePiece(Move(E1, c1))

        assertThat(gameAfterWhiteCastling.numberOfNextMove).isEqualTo(2)
        assertThat(gameAfterWhiteCastling.pieceOn(c1)).isEqualTo(WhiteKing)
        assertThat(gameAfterWhiteCastling.pieceOn(d1)).isEqualTo(WhiteRook)
        assertThat(gameAfterWhiteCastling.position.whiteCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterWhiteCastling.position.whiteCastlingOptions.queenSide).isFalse()
        assertThat(gameAfterWhiteCastling.position.blackCastlingOptions.kingSide).isTrue()
        assertThat(gameAfterWhiteCastling.position.blackCastlingOptions.queenSide).isTrue()

        val gameAfterBlackCastling = gameAfterWhiteCastling
            .movePiece(Move(E8, c8))
        assertThat(gameAfterBlackCastling.numberOfNextMove).isEqualTo(3)
        assertThat(gameAfterBlackCastling.pieceOn(c8)).isEqualTo(BlackKing)
        assertThat(gameAfterBlackCastling.pieceOn(d8)).isEqualTo(BlackRook)
        assertThat(gameAfterBlackCastling.position.whiteCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterBlackCastling.position.whiteCastlingOptions.queenSide).isFalse()
        assertThat(gameAfterBlackCastling.position.blackCastlingOptions.kingSide).isFalse()
        assertThat(gameAfterBlackCastling.position.blackCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `disallow moves that result in a checked position`() {
        val chessGame = ChessGame()
            .movePiece(Move(E2, E4))
            .movePiece(Move(E7, E6))
            .movePiece(Move(F1, b5))

        assertThat(chessGame.moveOptions()).doesNotContain(Move(d7, d6), Move(d7, d5))
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
                    a3 to BlackPawn
                ))
        )
        val chessGameAfterMove3 = chessGame
            .movePiece(Move(F1, E3))
            .movePiece(Move(E8, E7))
            .movePiece(Move(E3, d1))

        assertThat(chessGameAfterMove3.numberOfNextMove).isEqualTo(4)
        assertThat(chessGameAfterMove3.movesWithoutCaptureOrPawnMove).isEqualTo(3)

        val chessGameAfterMove4 = chessGameAfterMove3.movePiece(Move(a3, a2))

        assertThat(chessGameAfterMove4.movesWithoutCaptureOrPawnMove).isEqualTo(0)
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
                    a3 to BlackPawn
                ))
        )
        val chessGameAfterMove4 = chessGame
            .movePiece(Move(F1, d2))
            .movePiece(Move(E8, E7))
            .movePiece(Move(d2, b1))
            .movePiece(Move(E7, E8))

        assertThat(chessGameAfterMove4.numberOfNextMove).isEqualTo(5)
        assertThat(chessGameAfterMove4.movesWithoutCaptureOrPawnMove).isEqualTo(4)

        val chessGameAfterMove5 = chessGameAfterMove4.movePiece(Move(b1, a3))

        assertThat(chessGameAfterMove5.numberOfNextMove).isEqualTo(6)
        assertThat(chessGameAfterMove5.movesWithoutCaptureOrPawnMove).isEqualTo(0)
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
            .movePiece(Move(E1, F1))
            .movePiece(Move(E8, F8))
            .movePiece(Move(F1, G1))
            .movePiece(Move(F8, G8))
            .movePiece(Move(G1, H1))
            .movePiece(Move(G8, H8))
            .movePiece(Move(H1, H2))
            .movePiece(Move(H8, H7))
            .movePiece(Move(H2, G2))
            .movePiece(Move(H7, G7))
            .movePiece(Move(G2, F2))
            .movePiece(Move(G7, F7))
            .movePiece(Move(F2, E2))
            .movePiece(Move(F7, E7))
            .movePiece(Move(E2, d2))
            .movePiece(Move(E7, d7))
            .movePiece(Move(d2, c2))
            .movePiece(Move(d7, c7))
            .movePiece(Move(c2, b2))
            .movePiece(Move(c7, b7))
            .movePiece(Move(b2, a2))
            .movePiece(Move(b7, a7))
            .movePiece(Move(a2, a1))
            .movePiece(Move(a7, a8))
            .movePiece(Move(a1, b1))
            .movePiece(Move(a8, b8))
            .movePiece(Move(b1, c1))
            .movePiece(Move(b8, c8))
            .movePiece(Move(c1, d1))
            .movePiece(Move(c8, d8))
            .movePiece(Move(d1, E1))
            .movePiece(Move(d8, E8))
            .movePiece(Move(E1, F1))
            .movePiece(Move(E8, F8))
            .movePiece(Move(F1, G1))
            .movePiece(Move(F8, G8))
            .movePiece(Move(G1, H1))
            .movePiece(Move(G8, H8))
            .movePiece(Move(H1, H2))
            .movePiece(Move(H8, H7))
            .movePiece(Move(H2, G2))
            .movePiece(Move(H7, G7))
            .movePiece(Move(G2, F2))
            .movePiece(Move(G7, F7))
            .movePiece(Move(F2, E2))
            .movePiece(Move(F7, E7))
            .movePiece(Move(E2, d2))
            .movePiece(Move(E7, d7))
            .movePiece(Move(d2, c2))
            .movePiece(Move(d7, c7))

        assertThat(chessGameAfterMove50.numberOfNextMove).isEqualTo(51)
        assertThat(chessGameAfterMove50.movesWithoutCaptureOrPawnMove).isEqualTo(50)
        assertThat(chessGameAfterMove50.getStatus()).isEqualTo(FiftyMoveRule)
    }

}