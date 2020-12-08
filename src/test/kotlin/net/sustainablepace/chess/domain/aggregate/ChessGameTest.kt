package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.*
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.White
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhitePawn
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
        assertThat(game.position).isEqualTo(Position.default)
    }

    @Test
    fun `move e2-e4`() {
        val game = ChessGame()

        assertThat(game.position.get("e2")).isEqualTo(WhitePawn())
        assertThat(game.position.get("e4")).isNull()

        val move = Move("e2-e4") as ValidMove

        val updatedGame = game.movePiece(move)

        assertThat(updatedGame.turn).isEqualTo(Black)
        assertThat(updatedGame.position.get("e2")).isNull()
        assertThat(updatedGame.position.get("e4")).isEqualTo(WhitePawn())
    }
}