package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PositionTest {
    @Test
    fun `initial position is not in check`() {
        val chessGame = ChessGame()

        assertThat(chessGame.position.isInCheck(chessGame.turn)).isFalse()
    }

    @Test
    fun `initial position is not in check (black)`() {
        val chessGame = ChessGame(Black)

        assertThat(chessGame.position.isInCheck(chessGame.turn)).isFalse()
    }


    @Test
    fun `black is in check by bishop`() {
        val position = Position()
            .movePiece(ValidMove(E2, E4))
            .movePiece(ValidMove(D7, D6))
            .movePiece(ValidMove(F1, B5))

        assertThat(position.moveOptions(B5)).contains(ValidMove(B5, E8))
        assertThat(position.isInCheck(Black)).isTrue()
    }
}