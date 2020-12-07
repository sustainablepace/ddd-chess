package net.sustainablepace.chess.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChessGameTest {
    @Test
    fun `start a new game in default position`() {
        val game = ChessGame()

        assertThat(game.id).isNotEmpty()
        assertThat(game.status).isEqualTo("in progress")
        assertThat(game.turn).isEqualTo("white")
        assertThat(game.white).isEqualTo("human")
        assertThat(game.black).isEqualTo("computer")
        assertThat(game.position).isEqualTo(ChessGame.defaultPosition)
    }

    @Test
    fun `move e2-e4`() {
        val game = ChessGame()

        assertThat(game.position.get("e2")).isEqualTo("wP")
        assertThat(game.position.get("e4")).isNull()

        game.movePiece(Move("e2-e4").getOrThrow())

        assertThat(game.position.get("e2")).isNull()
        assertThat(game.position.get("e4")).isEqualTo("wP")
    }
}