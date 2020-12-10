package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.*
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.White
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChessGameTest {
    @Test
    fun `start a new game in default position`() {
        val game = ChessGame()

        assertThat(game.id).isNotEmpty()
        assertThat(game.status).isEqualTo("in progress")
        assertThat(game.turn).isEqualTo(White)
        assertThat(game.white).isInstanceOf(HumanPlayer::class.java)
        assertThat(game.black).isInstanceOf(ComputerPlayer::class.java)
        assertThat(game.position).isEqualTo(Position.default)
    }

    @Test
    fun `move e2-e4`() {
        val game = ChessGame()

        assertThat(game.position.get("e2")).isEqualTo(WhitePawn())
        assertThat(game.position.get("e4")).isNull()

        val move = Move("e2-e4") as ValidMove

        val updatedGame = game.movePiece(move)

        assertThat(updatedGame.turn).isEqualTo(Black)
        assertThat(updatedGame.position.get("e2")).isNull()
        assertThat(updatedGame.position.get("e4")).isEqualTo(WhitePawn())
    }


    @Test
    fun `finds valid rook movements`() {
        val chessGame = ChessGame(Position(mapOf(
            "c3" to WhiteRook(),
            "c7" to BlackPawn(),
            "e3" to WhitePawn()
        )))

        val moves = findMoves(chessGame, "c3")
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
    fun `finds valid knight movements`() {
        val chessGame = ChessGame(Position(mapOf(
            "c2" to WhiteKnight(),
            "b4" to BlackPawn(),
            "e1" to WhiteKing()
        )))
        val moves = findMoves(chessGame, "c2")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("c2-d4") as ValidMove,
            ValidMove("c2-b4") as ValidMove,
            ValidMove("c2-e3") as ValidMove,
            ValidMove("c2-a1") as ValidMove,
            ValidMove("c2-a3") as ValidMove
        )
    }

    @Test
    fun `finds valid bishop movements on empty board`() {
        val chessGame = ChessGame(Position(mapOf(
            "e4" to WhiteBishop()
        )))
        val moves = findMoves(chessGame, "e4")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-c2") as ValidMove,
            ValidMove("e4-b1") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-c6") as ValidMove,
            ValidMove("e4-b7") as ValidMove,
            ValidMove("e4-a8") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-g6") as ValidMove,
            ValidMove("e4-h7") as ValidMove,
            ValidMove("e4-f3") as ValidMove,
            ValidMove("e4-g2") as ValidMove,
            ValidMove("e4-h1") as ValidMove
        )
    }

    @Test
    fun `finds valid bishop movements on crowded board`() {
        val chessGame = ChessGame(Position(mapOf(
            "e4" to WhiteBishop(),
            "d5" to BlackPawn(),
            "g2" to WhiteQueen()
        )))
        val moves = findMoves(chessGame, "e4")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-c2") as ValidMove,
            ValidMove("e4-b1") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-g6") as ValidMove,
            ValidMove("e4-h7") as ValidMove,
            ValidMove("e4-f3") as ValidMove
        )
    }

    @Test
    fun `finds valid queen movements on empty board`() {
        val chessGame = ChessGame(Position(mapOf(
            "e4" to WhiteQueen()
        )))
        val moves = findMoves(chessGame, "e4")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-c2") as ValidMove,
            ValidMove("e4-b1") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-c6") as ValidMove,
            ValidMove("e4-b7") as ValidMove,
            ValidMove("e4-a8") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-g6") as ValidMove,
            ValidMove("e4-h7") as ValidMove,
            ValidMove("e4-f3") as ValidMove,
            ValidMove("e4-g2") as ValidMove,
            ValidMove("e4-h1") as ValidMove,
            ValidMove("e4-e5") as ValidMove,
            ValidMove("e4-e6") as ValidMove,
            ValidMove("e4-e7") as ValidMove,
            ValidMove("e4-e8") as ValidMove,
            ValidMove("e4-e3") as ValidMove,
            ValidMove("e4-e2") as ValidMove,
            ValidMove("e4-e1") as ValidMove,
            ValidMove("e4-d4") as ValidMove,
            ValidMove("e4-c4") as ValidMove,
            ValidMove("e4-b4") as ValidMove,
            ValidMove("e4-a4") as ValidMove,
            ValidMove("e4-f4") as ValidMove,
            ValidMove("e4-g4") as ValidMove,
            ValidMove("e4-h4") as ValidMove

        )
    }

    @Test
    fun `finds valid queen movements on crowded board`() {
        val chessGame = ChessGame(Position(mapOf(
            "e4" to WhiteQueen(),
            "d5" to BlackPawn(),
            "g2" to WhiteBishop(),
            "a4" to WhiteKing(),
            "g4" to BlackPawn(),
            "e1" to WhiteRook(),
            "e6" to BlackQueen()
        )))
        val moves = findMoves(chessGame, "e4")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-c2") as ValidMove,
            ValidMove("e4-b1") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-g6") as ValidMove,
            ValidMove("e4-h7") as ValidMove,
            ValidMove("e4-f3") as ValidMove,
            ValidMove("e4-d4") as ValidMove,
            ValidMove("e4-c4") as ValidMove,
            ValidMove("e4-b4") as ValidMove,
            ValidMove("e4-f4") as ValidMove,
            ValidMove("e4-g4") as ValidMove,
            ValidMove("e4-e3") as ValidMove,
            ValidMove("e4-e2") as ValidMove,
            ValidMove("e4-e5") as ValidMove,
            ValidMove("e4-e6") as ValidMove
        )
    }

    @Test
    fun `finds valid king movements on empty board`() {
        val chessGame = ChessGame(Position(mapOf(
            "e4" to WhiteKing()
        )))
        val moves = findMoves(chessGame, "e4")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-e3") as ValidMove,
            ValidMove("e4-f3") as ValidMove,
            ValidMove("e4-f4") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-e5") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-d4") as ValidMove
        )
    }

    @Test
    fun `finds valid king movements on crowded board`() {
        val chessGame = ChessGame(Position(mapOf(
            "e4" to WhiteKing(),
            "d5" to BlackPawn(),
            "e5" to WhiteQueen()
        )))
        val moves = findMoves(chessGame, "e4")
        assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-e3") as ValidMove,
            ValidMove("e4-f3") as ValidMove,
            ValidMove("e4-f4") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-d4") as ValidMove
        )
    }
}