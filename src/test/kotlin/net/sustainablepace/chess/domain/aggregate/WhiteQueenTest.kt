package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhiteQueenTest {

    @Test
    fun `finds valid queen movements on empty board`() {
        val position = Position(mapOf(
                E4 to WhiteQueen
            )
        )
        val moves = position.moveOptions(E4)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(E4, D3),
            ValidMove(E4, C2),
            ValidMove(E4, B1),
            ValidMove(E4, D5),
            ValidMove(E4, C6),
            ValidMove(E4, B7),
            ValidMove(E4, A8),
            ValidMove(E4, F5),
            ValidMove(E4, G6),
            ValidMove(E4, H7),
            ValidMove(E4, F3),
            ValidMove(E4, G2),
            ValidMove(E4, H1),
            ValidMove(E4, E5),
            ValidMove(E4, E6),
            ValidMove(E4, E7),
            ValidMove(E4, E8),
            ValidMove(E4, E3),
            ValidMove(E4, E2),
            ValidMove(E4, E1),
            ValidMove(E4, D4),
            ValidMove(E4, C4),
            ValidMove(E4, B4),
            ValidMove(E4, A4),
            ValidMove(E4, F4),
            ValidMove(E4, G4),
            ValidMove(E4, H4)

        )
    }

    @Test
    fun `finds valid queen movements on crowded board`() {
        val position = Position(mapOf(
                E4 to WhiteQueen,
                D5 to BlackPawn,
                G2 to WhiteBishop,
                A4 to WhiteKing,
                G4 to BlackPawn,
                E1 to WhiteRook,
                E6 to BlackQueen
            )
        )
        val moves = position.moveOptions(E4)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(E4, D3),
            ValidMove(E4, C2),
            ValidMove(E4, B1),
            ValidMove(E4, D5),
            ValidMove(E4, F5),
            ValidMove(E4, G6),
            ValidMove(E4, H7),
            ValidMove(E4, F3),
            ValidMove(E4, D4),
            ValidMove(E4, C4),
            ValidMove(E4, B4),
            ValidMove(E4, F4),
            ValidMove(E4, G4),
            ValidMove(E4, E3),
            ValidMove(E4, E2),
            ValidMove(E4, E5),
            ValidMove(E4, E6)
        )
    }
}