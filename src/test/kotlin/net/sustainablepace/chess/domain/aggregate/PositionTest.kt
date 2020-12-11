package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhitePawn
import net.sustainablepace.chess.domain.containsWhiteAndBlackPieces
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PositionTest {
    @Test
    fun `insufficient material`() {
        assertThat(ChessGame(mutableMapOf()).getPosition().containsWhiteAndBlackPieces()).isFalse()
        assertThat(ChessGame(mutableMapOf("e1" to WhitePawn())).getPosition().containsWhiteAndBlackPieces()).isFalse()
        assertThat(ChessGame(mutableMapOf("e1" to BlackPawn())).getPosition().containsWhiteAndBlackPieces()).isFalse()
        assertThat(ChessGame(mutableMapOf("e1" to WhitePawn(), "e2" to BlackPawn())).getPosition().containsWhiteAndBlackPieces()).isTrue()
    }

}