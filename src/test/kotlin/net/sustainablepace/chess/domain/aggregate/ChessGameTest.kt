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

        assertThat(game.pieceOn(e2)).isEqualTo(WhitePawn)
        assertThat(game.pieceOn(e3)).isEqualTo(NoPiece)

        val move = Move(e2, e3)

        val updatedGame = game.movePiece(move)

        assertThat(updatedGame.turn).isEqualTo(Black)
        assertThat(updatedGame.numberOfNextMove).isEqualTo(2)
        assertThat(updatedGame.pieceOn(e2)).isEqualTo(NoPiece)
        assertThat(updatedGame.pieceOn(e3)).isEqualTo(WhitePawn)
    }
    @Test
    fun `disallow moves that result in a checked position`() {
        val chessGame = ChessGame()
            .movePiece(Move(e2, e4))
            .movePiece(Move(e7, e6))
            .movePiece(Move(f1, b5))

        assertThat(chessGame.moveOptions()).doesNotContain(Move(d7, d6), Move(d7, d5))
    }

    @Test
    fun `Fifty move rule reset because pawn moved`() {
        val chessGame = ChessGame(
            Position(
                mapOf(
                    e1 to WhiteKing,
                    e8 to BlackKing,
                    f1 to WhiteKnight,
                    g3 to WhiteKnight,
                    a3 to BlackPawn
                )
            )
        )
        val chessGameAfterMove3 = chessGame
            .movePiece(Move(f1, e3))
            .movePiece(Move(e8, e7))
            .movePiece(Move(e3, d1))

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
                    e1 to WhiteKing,
                    e8 to BlackKing,
                    f1 to WhiteKnight,
                    g3 to WhiteKnight,
                    a3 to BlackPawn
                )
            )
        )
        val chessGameAfterMove4 = chessGame
            .movePiece(Move(f1, d2))
            .movePiece(Move(e8, e7))
            .movePiece(Move(d2, b1))
            .movePiece(Move(e7, e8))

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
                    e1 to WhiteKing,
                    e8 to BlackKing,
                    f3 to WhiteKnight,
                    g3 to WhiteKnight
                )
            )
        )
        val chessGameAfterMove50 = chessGame
            .movePiece(Move(e1, f1))
            .movePiece(Move(e8, f8))
            .movePiece(Move(f1, g1))
            .movePiece(Move(f8, g8))
            .movePiece(Move(g1, h1))
            .movePiece(Move(g8, h8))
            .movePiece(Move(h1, h2))
            .movePiece(Move(h8, h7))
            .movePiece(Move(h2, g2))
            .movePiece(Move(h7, g7))
            .movePiece(Move(g2, f2))
            .movePiece(Move(g7, f7))
            .movePiece(Move(f2, e2))
            .movePiece(Move(f7, e7))
            .movePiece(Move(e2, d2))
            .movePiece(Move(e7, d7))
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
            .movePiece(Move(d1, e1))
            .movePiece(Move(d8, e8))
            .movePiece(Move(e1, f1))
            .movePiece(Move(e8, f8))
            .movePiece(Move(f1, g1))
            .movePiece(Move(f8, g8))
            .movePiece(Move(g1, h1))
            .movePiece(Move(g8, h8))
            .movePiece(Move(h1, h2))
            .movePiece(Move(h8, h7))
            .movePiece(Move(h2, g2))
            .movePiece(Move(h7, g7))
            .movePiece(Move(g2, f2))
            .movePiece(Move(g7, f7))
            .movePiece(Move(f2, e2))
            .movePiece(Move(f7, e7))
            .movePiece(Move(e2, d2))
            .movePiece(Move(e7, d7))
            .movePiece(Move(d2, c2))
            .movePiece(Move(d7, c7))

        assertThat(chessGameAfterMove50.numberOfNextMove).isEqualTo(51)
        assertThat(chessGameAfterMove50.movesWithoutCaptureOrPawnMove).isEqualTo(50)
        assertThat(chessGameAfterMove50.getStatus()).isEqualTo(FiftyMoveRule)
    }

}