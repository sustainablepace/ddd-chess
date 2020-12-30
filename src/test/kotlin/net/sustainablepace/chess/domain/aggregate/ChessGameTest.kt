package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChessGameTest {
    @Test
    fun `start a new game in default position`() {
        val game = chessGame()

        assertThat(game.id).isNotEmpty()
        assertThat(game.status).isEqualTo(InProgress)
//        assertThat(game.white).isInstanceOf(HumanPlayer::class.java)
//        assertThat(game.black).isInstanceOf(ComputerPlayer::class.java)
        assertThat(game.position).isEqualTo(position())
        assertThat(game.numberOfNextMove).isEqualTo(1)
        assertThat(game.position.enPassantSquare).isEqualTo(null)
    }

    @Test
    fun `move e2-e4`() {
        val game = chessGame()

        assertThat(game.pieceOn(e2)).isEqualTo(WhitePawn)
        assertThat(game.pieceOn(e3)).isEqualTo(NoPiece)

        val move = Move(e2, e3)

        val updatedGame = game.movePiece(move)

        assertThat(updatedGame.numberOfNextMove).isEqualTo(2)
        assertThat(updatedGame.pieceOn(e2)).isEqualTo(NoPiece)
        assertThat(updatedGame.pieceOn(e3)).isEqualTo(WhitePawn)
    }

    @Test
    fun `disallow moves that result in a checked position`() {
        val chessGame = chessGame()
            .movePiece(Move(e2, e4))
            .movePiece(Move(e7, e6))
            .movePiece(Move(f1, b5))

        assertThat(chessGame.moveOptions()).doesNotContain(Move(d7, d6), Move(d7, d5))
    }

    @Test
    fun `dead position`() {
        val chessGame = chessGame(
            position(
                mapOf(
                    e1 to WhiteKing,
                    e8 to BlackKing
                )
            )
        )
        assertThat(chessGame.status).isEqualTo(DeadPosition)
    }


    @Test
    fun `two white bishops and kings are not dead position`() {
        val chessGame = chessGame(
            position(
                mapOf(
                    e1 to WhiteKing,
                    e8 to BlackKing,
                    c1 to WhiteBishop,
                    f1 to WhiteBishop,
                )
            )
        )
        assertThat(chessGame.status).isEqualTo(InProgress)
    }

}