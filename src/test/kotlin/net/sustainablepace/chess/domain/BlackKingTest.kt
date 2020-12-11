package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.BlackPieces
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackKing
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackRook
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BlackKingTest {
    @Test
    fun `castling queenside on empty board`() {
        val chessGame = ChessGame(BlackPieces, mapOf(
            "e8" to BlackKing(),
            "a8" to BlackRook()
        ))
        Assertions.assertThat(chessGame.blackCastlingOptions.kingSide).isTrue()
        Assertions.assertThat(chessGame.blackCastlingOptions.queenSide).isTrue()

        val updatedGame = chessGame.movePiece(ValidMove("e8-c8") as ValidMove)
        Assertions.assertThat(updatedGame.pieceOn("c8")).isEqualTo(BlackKing())
        Assertions.assertThat(updatedGame.pieceOn("d8")).isEqualTo(BlackRook())
        Assertions.assertThat(updatedGame.blackCastlingOptions.kingSide).isFalse()
        Assertions.assertThat(updatedGame.blackCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `castling kingside on empty board`() {
        val chessGame = ChessGame(BlackPieces, mapOf(
            "e8" to BlackKing(),
            "h8" to BlackRook()
        ))
        Assertions.assertThat(chessGame.blackCastlingOptions.kingSide).isTrue()
        Assertions.assertThat(chessGame.blackCastlingOptions.queenSide).isTrue()

        val updatedGame = chessGame.movePiece(ValidMove("e8-g8") as ValidMove)
        Assertions.assertThat(updatedGame.pieceOn("g8")).isEqualTo(BlackKing())
        Assertions.assertThat(updatedGame.pieceOn("f8")).isEqualTo(BlackRook())
        Assertions.assertThat(updatedGame.blackCastlingOptions.kingSide).isFalse()
        Assertions.assertThat(updatedGame.blackCastlingOptions.queenSide).isFalse()
    }

    @Test
    fun `moving king makes castling unavailable`() {
        val chessGame = ChessGame(BlackPieces)
            .movePiece(ValidMove("e7-e5") as ValidMove)
            .movePiece(ValidMove("e2-e3") as ValidMove)
            .movePiece(ValidMove("e8-e7") as ValidMove)

        Assertions.assertThat(chessGame.blackCastlingOptions.queenSide).isFalse()
        Assertions.assertThat(chessGame.blackCastlingOptions.kingSide).isFalse()
    }

}