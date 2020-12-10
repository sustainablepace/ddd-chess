package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.Position
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhitePawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhiteRook
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhiteRookTest {

    @Test
    fun `finds valid rook movements`() {
        val chessGame = ChessGame(Position(mapOf(
            "c3" to WhiteRook(),
            "c7" to BlackPawn(),
            "e3" to WhitePawn()
        )))

        val moves = chessGame.findMoves("c3")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("c3-c4") as ValidMove,
            ValidMove("c3-c5") as ValidMove,
            ValidMove("c3-c6") as ValidMove,
            ValidMove("c3-c7") as ValidMove,
            ValidMove("c3-b3") as ValidMove,
            ValidMove("c3-a3") as ValidMove,
            ValidMove("c3-c2") as ValidMove,
            ValidMove("c3-c1") as ValidMove,
            ValidMove("c3-d3") as ValidMove
        )
    }
}