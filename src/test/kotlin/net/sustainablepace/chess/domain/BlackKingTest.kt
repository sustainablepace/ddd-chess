package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackKing
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackRook
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.BlackPieces
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BlackKingTest {
    @Test
    fun `castling queenside on empty board`() {
        val chessGame = ChessGame(BlackPieces, mutableMapOf(
            "e8" to BlackKing(),
            "a8" to BlackRook()
        ))
        Assertions.assertThat(chessGame.blackCastlingOptions.kingSide).isTrue()
        Assertions.assertThat(chessGame.blackCastlingOptions.queenSide).isTrue()

        val updatedGame = chessGame.movePiece(ValidMove("e8-c8") as ValidMove)
        Assertions.assertThat(updatedGame.get("c8")).isEqualTo(BlackKing())
        Assertions.assertThat(updatedGame.get("d8")).isEqualTo(BlackRook())
        Assertions.assertThat(updatedGame.blackCastlingOptions.kingSide).isFalse()
        Assertions.assertThat(updatedGame.blackCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `castling kingside on empty board`() {
        val chessGame = ChessGame(BlackPieces, mutableMapOf(
            "e8" to BlackKing(),
            "h8" to BlackRook()
        ))
        Assertions.assertThat(chessGame.blackCastlingOptions.kingSide).isTrue()
        Assertions.assertThat(chessGame.blackCastlingOptions.queenSide).isTrue()

        val updatedGame = chessGame.movePiece(ValidMove("e8-g8") as ValidMove)
        Assertions.assertThat(updatedGame.get("g8")).isEqualTo(BlackKing())
        Assertions.assertThat(updatedGame.get("f8")).isEqualTo(BlackRook())
        Assertions.assertThat(updatedGame.blackCastlingOptions.kingSide).isFalse()
        Assertions.assertThat(updatedGame.blackCastlingOptions.queenSide).isFalse()
    }

}