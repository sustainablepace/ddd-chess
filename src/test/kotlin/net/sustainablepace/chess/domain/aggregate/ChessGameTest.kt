package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.*
import net.sustainablepace.chess.domain.ChessGame.Companion.default
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.BlackPieces
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.White
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.WhitePieces
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChessGameTest {
    @Test
    fun `start a new game in default position`() {
        val game = ChessGame()

        assertThat(game.id).isNotEmpty()
        assertThat(game.status).isEqualTo("in progress")
        assertThat(game.turn).isEqualTo(WhitePieces)
        assertThat(game.white).isInstanceOf(HumanPlayer::class.java)
        assertThat(game.black).isInstanceOf(ComputerPlayer::class.java)
        assertThat(game.getPosition()).isEqualTo(default.toMap())
        assertThat(game.numberOfNextMove).isEqualTo(1)
        assertThat(game.enPassant).isEqualTo(null)
    }

    @Test
    fun `move e2-e4`() {
        val game = ChessGame()

        assertThat(game.get("e2")).isEqualTo(WhitePawn())
        assertThat(game.get("e3")).isNull()

        val move = Move("e2-e3") as ValidMove

        val updatedGame = game.movePiece(move)

        assertThat(updatedGame.turn).isEqualTo(BlackPieces)
        assertThat(updatedGame.numberOfNextMove).isEqualTo(2)
        assertThat(updatedGame.get("e2")).isNull()
        assertThat(updatedGame.get("e3")).isEqualTo(WhitePawn())
    }

    @Test
    fun `find moves for white in default position`() {
        val game = ChessGame()

        val moves = game.findMoves()

        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("a2-a3") as ValidMove,
            ValidMove("b2-b3") as ValidMove,
            ValidMove("c2-c3") as ValidMove,
            ValidMove("d2-d3") as ValidMove,
            ValidMove("e2-e3") as ValidMove,
            ValidMove("f2-f3") as ValidMove,
            ValidMove("g2-g3") as ValidMove,
            ValidMove("h2-h3") as ValidMove,
            ValidMove("a2-a4") as ValidMove,
            ValidMove("b2-b4") as ValidMove,
            ValidMove("c2-c4") as ValidMove,
            ValidMove("d2-d4") as ValidMove,
            ValidMove("e2-e4") as ValidMove,
            ValidMove("f2-f4") as ValidMove,
            ValidMove("g2-g4") as ValidMove,
            ValidMove("h2-h4") as ValidMove,
            ValidMove("b1-a3") as ValidMove,
            ValidMove("b1-c3") as ValidMove,
            ValidMove("g1-f3") as ValidMove,
            ValidMove("g1-h3") as ValidMove
        )
    }

    @Test
    fun `find moves for black in default position`() {
        val game = ChessGame(BlackPieces)

        val moves = game.findMoves()

        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("a7-a6") as ValidMove,
            ValidMove("b7-b6") as ValidMove,
            ValidMove("c7-c6") as ValidMove,
            ValidMove("d7-d6") as ValidMove,
            ValidMove("e7-e6") as ValidMove,
            ValidMove("f7-f6") as ValidMove,
            ValidMove("g7-g6") as ValidMove,
            ValidMove("h7-h6") as ValidMove,
            ValidMove("a7-a5") as ValidMove,
            ValidMove("b7-b5") as ValidMove,
            ValidMove("c7-c5") as ValidMove,
            ValidMove("d7-d5") as ValidMove,
            ValidMove("e7-e5") as ValidMove,
            ValidMove("f7-f5") as ValidMove,
            ValidMove("g7-g5") as ValidMove,
            ValidMove("h7-h5") as ValidMove,
            ValidMove("b8-a6") as ValidMove,
            ValidMove("b8-c6") as ValidMove,
            ValidMove("g8-f6") as ValidMove,
            ValidMove("g8-h6") as ValidMove
        )
    }

    @Test
    fun `en passant (white)`() {
        val chessGame = ChessGame()
        assertThat(chessGame.enPassant).isNull()

        chessGame.movePiece(ValidMove("e2-e4") as ValidMove).let {
            assertThat(it.enPassant).isEqualTo("e4")
        }

        chessGame.movePiece(ValidMove("e2-e3") as ValidMove).let {
            assertThat(it.enPassant).isNull()
        }
    }

    @Test
    fun `en passant (black)`() {
        val chessGame = ChessGame(BlackPieces)
        assertThat(chessGame.enPassant).isNull()

        chessGame.movePiece(ValidMove("e7-e6") as ValidMove).let {
            assertThat(it.enPassant).isNull()
        }

        chessGame.movePiece(ValidMove("e7-e5") as ValidMove).let {
            assertThat(it.enPassant).isEqualTo("e5")
        }

    }

    @Test
    fun `en passent capturing works for black (left)`() {
        val chessGame = ChessGame(mutableMapOf(
            "f4" to BlackPawn(),
            "e2" to WhitePawn()
        ))
        val updatedChessGame = chessGame
            .movePiece(ValidMove("e2-e4") as ValidMove)
            .movePiece(ValidMove("f4-e3") as ValidMove)

        assertThat(updatedChessGame.get("e3")).isEqualTo(BlackPawn())
        assertThat(updatedChessGame.get("e4")).isNull()
    }

    @Test
    fun `en passent capturing works for black (right)`() {
        val chessGame = ChessGame(mutableMapOf(
            "d4" to BlackPawn(),
            "e2" to WhitePawn()
        ))
        val updatedChessGame = chessGame
            .movePiece(ValidMove("e2-e4") as ValidMove)
            .movePiece(ValidMove("d4-e3") as ValidMove)

        assertThat(updatedChessGame.get("e3")).isEqualTo(BlackPawn())
        assertThat(updatedChessGame.get("e4")).isNull()
    }

    @Test
    fun `en passent capturing works for white (left)`() {
        val chessGame = ChessGame(BlackPieces, mutableMapOf(
            "e7" to BlackPawn(),
            "d5" to WhitePawn()
        ))
        val updatedChessGame = chessGame
            .movePiece(ValidMove("e7-e5") as ValidMove)
            .movePiece(ValidMove("d5-e6") as ValidMove)

        assertThat(updatedChessGame.get("e6")).isEqualTo(WhitePawn())
        assertThat(updatedChessGame.get("e5")).isNull()
    }

    @Test
    fun `en passent capturing works for white (right)`() {
        val chessGame = ChessGame(BlackPieces, mutableMapOf(
            "e7" to BlackPawn(),
            "f5" to WhitePawn()
        ))
        val updatedChessGame = chessGame
            .movePiece(ValidMove("e7-e5") as ValidMove)
            .movePiece(ValidMove("f5-e6") as ValidMove)

        assertThat(updatedChessGame.get("e6")).isEqualTo(WhitePawn())
        assertThat(updatedChessGame.get("e5")).isNull()
    }
}