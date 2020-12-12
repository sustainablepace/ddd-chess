package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BlackKingTest {
    @Test
    fun `castling queenside on empty board`() {
        val chessGame = ChessGame(Black, mapOf(
            E8 to BlackKing,
            A8 to BlackRook
        ))
        Assertions.assertThat(chessGame.blackCastlingOptions.kingSide).isTrue()
        Assertions.assertThat(chessGame.blackCastlingOptions.queenSide).isTrue()

        val updatedGame = chessGame.movePiece(ValidMove(E8, C8))
        Assertions.assertThat(updatedGame.pieceOn(C8)).isEqualTo(BlackKing)
        Assertions.assertThat(updatedGame.pieceOn(D8)).isEqualTo(BlackRook)
        Assertions.assertThat(updatedGame.blackCastlingOptions.kingSide).isFalse()
        Assertions.assertThat(updatedGame.blackCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `castling kingside on empty board`() {
        val chessGame = ChessGame(Black, mapOf(
            E8 to BlackKing,
            H8 to BlackRook
        ))
        Assertions.assertThat(chessGame.blackCastlingOptions.kingSide).isTrue()
        Assertions.assertThat(chessGame.blackCastlingOptions.queenSide).isTrue()

        val updatedGame = chessGame.movePiece(ValidMove(E8, G8))
        Assertions.assertThat(updatedGame.pieceOn(G8)).isEqualTo(BlackKing)
        Assertions.assertThat(updatedGame.pieceOn(F8)).isEqualTo(BlackRook)
        Assertions.assertThat(updatedGame.blackCastlingOptions.kingSide).isFalse()
        Assertions.assertThat(updatedGame.blackCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `moving king makes castling unavailable`() {
        val chessGame = ChessGame(Black)
            .movePiece(ValidMove(E7, E5))
            .movePiece(ValidMove(E2, E3))
            .movePiece(ValidMove(E8, E7))

        Assertions.assertThat(chessGame.blackCastlingOptions.queenSide).isFalse()
        Assertions.assertThat(chessGame.blackCastlingOptions.kingSide).isFalse()
    }

}