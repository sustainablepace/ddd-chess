package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.Position
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhiteKing
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhiteKnight
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhiteKnightTest {

    @Test
    fun `finds valid knight movements`() {
        val chessGame = ChessGame(Position(mapOf(
            "c2" to WhiteKnight(),
            "b4" to BlackPawn(),
            "e1" to WhiteKing()
        )))
        val moves = chessGame.findMoves("c2")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("c2-d4") as ValidMove,
            ValidMove("c2-b4") as ValidMove,
            ValidMove("c2-e3") as ValidMove,
            ValidMove("c2-a1") as ValidMove,
            ValidMove("c2-a3") as ValidMove
        )
    }

}