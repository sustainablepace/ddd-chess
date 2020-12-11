package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhitePawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhiteRook
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WhiteRookTest {

    @Test
    fun `finds valid rook movements`() {
        val chessGame = ChessGame(mapOf(
            "c3" to WhiteRook(),
            "c7" to BlackPawn(),
            "e3" to WhitePawn()
        ))

        val moves = chessGame.findMoves("c3")
        assertThat(moves).containsExactlyInAnyOrder(
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

    @Test
    fun `moving left rook makes castling unavailable`() {
        val chessGame = ChessGame()
            .movePiece(ValidMove("a2-a4") as ValidMove)
            .movePiece(ValidMove("a7-a6") as ValidMove)
            .movePiece(ValidMove("a1-a3") as ValidMove)

        assertThat(chessGame.whiteCastlingOptions.queenSide).isFalse()
        assertThat(chessGame.whiteCastlingOptions.kingSide).isTrue()
    }

    @Test
    fun `moving right rook makes castling unavailable`() {
        val chessGame = ChessGame()
            .movePiece(ValidMove("h2-h4") as ValidMove)
            .movePiece(ValidMove("h7-h6") as ValidMove)
            .movePiece(ValidMove("h1-h3") as ValidMove)

        assertThat(chessGame.whiteCastlingOptions.queenSide).isTrue()
        assertThat(chessGame.whiteCastlingOptions.kingSide).isFalse()
    }
}