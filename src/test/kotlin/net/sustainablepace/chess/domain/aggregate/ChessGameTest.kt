package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.ChessGame.Companion.defaultPosition
import net.sustainablepace.chess.domain.Move
import net.sustainablepace.chess.domain.ValidMove
import net.sustainablepace.chess.domain.aggregate.chessgame.BlackPieces
import net.sustainablepace.chess.domain.aggregate.chessgame.ComputerPlayer
import net.sustainablepace.chess.domain.aggregate.chessgame.HumanPlayer
import net.sustainablepace.chess.domain.aggregate.chessgame.WhitePieces
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.NoPiece
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhitePawn
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
        assertThat(game.getPosition()).isEqualTo(defaultPosition.toMap())
        assertThat(game.numberOfNextMove).isEqualTo(1)
        assertThat(game.enPassantSquare).isEqualTo(null)
    }

    @Test
    fun `move e2-e4`() {
        val game = ChessGame()

        assertThat(game.pieceOn("e2")).isEqualTo(WhitePawn())
        assertThat(game.pieceOn("e3")).isEqualTo(NoPiece)

        val move = Move("e2-e3") as ValidMove

        val updatedGame = game.movePiece(move)

        assertThat(updatedGame.turn).isEqualTo(BlackPieces)
        assertThat(updatedGame.numberOfNextMove).isEqualTo(2)
        assertThat(updatedGame.pieceOn("e2")).isEqualTo(NoPiece)
        assertThat(updatedGame.pieceOn("e3")).isEqualTo(WhitePawn())
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
        assertThat(chessGame.enPassantSquare).isNull()

        chessGame.movePiece(ValidMove("e2-e4") as ValidMove).let {
            assertThat(it.enPassantSquare).isEqualTo("e4")
        }

        chessGame.movePiece(ValidMove("e2-e3") as ValidMove).let {
            assertThat(it.enPassantSquare).isNull()
        }
    }

    @Test
    fun `en passant (black)`() {
        val chessGame = ChessGame(BlackPieces)
        assertThat(chessGame.enPassantSquare).isNull()

        chessGame.movePiece(ValidMove("e7-e6") as ValidMove).let {
            assertThat(it.enPassantSquare).isNull()
        }

        chessGame.movePiece(ValidMove("e7-e5") as ValidMove).let {
            assertThat(it.enPassantSquare).isEqualTo("e5")
        }

    }

    @Test
    fun `en passent capturing works for black (left)`() {
        val chessGame = ChessGame(mapOf(
            "f4" to BlackPawn(),
            "e2" to WhitePawn()
        ))
        val updatedChessGame = chessGame
            .movePiece(ValidMove("e2-e4") as ValidMove)
            .movePiece(ValidMove("f4-e3") as ValidMove)

        assertThat(updatedChessGame.pieceOn("e3")).isEqualTo(BlackPawn())
        assertThat(updatedChessGame.pieceOn("e4")).isEqualTo(NoPiece)
    }

    @Test
    fun `en passent capturing works for black (right)`() {
        val chessGame = ChessGame(mapOf(
            "d4" to BlackPawn(),
            "e2" to WhitePawn()
        ))
        val updatedChessGame = chessGame
            .movePiece(ValidMove("e2-e4") as ValidMove)
            .movePiece(ValidMove("d4-e3") as ValidMove)

        assertThat(updatedChessGame.pieceOn("e3")).isEqualTo(BlackPawn())
        assertThat(updatedChessGame.pieceOn("e4")).isEqualTo(NoPiece)
    }

    @Test
    fun `en passent capturing works for white (left)`() {
        val chessGame = ChessGame(BlackPieces, mapOf(
            "e7" to BlackPawn(),
            "d5" to WhitePawn()
        ))
        val updatedChessGame = chessGame
            .movePiece(ValidMove("e7-e5") as ValidMove)
            .movePiece(ValidMove("d5-e6") as ValidMove)

        assertThat(updatedChessGame.pieceOn("e6")).isEqualTo(WhitePawn())
        assertThat(updatedChessGame.pieceOn("e5")).isEqualTo(NoPiece)
    }

    @Test
    fun `en passent capturing works for white (right)`() {
        val chessGame = ChessGame(BlackPieces, mapOf(
            "e7" to BlackPawn(),
            "f5" to WhitePawn()
        ))
        val updatedChessGame = chessGame
            .movePiece(ValidMove("e7-e5") as ValidMove)
            .movePiece(ValidMove("f5-e6") as ValidMove)

        assertThat(updatedChessGame.pieceOn("e6")).isEqualTo(WhitePawn())
        assertThat(updatedChessGame.pieceOn("e5")).isEqualTo(NoPiece)
    }
}