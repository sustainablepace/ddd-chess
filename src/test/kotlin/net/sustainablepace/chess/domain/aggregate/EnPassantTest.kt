package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class EnPassantTest {

    @Test
    fun `en passent capturing works for black (left)`() {
        val chessGame = chessGame(
            position(
                mapOf(
                    f4 to BlackPawn,
                    e2 to WhitePawn
                )
            )
        )
        val updatedChessGame = chessGame
            .movePiece(Move(e2, e4))
            .movePiece(Move(f4, e3))

        Assertions.assertThat(updatedChessGame.numberOfNextMove).isEqualTo(3)
        Assertions.assertThat(updatedChessGame.pieceOn(e3)).isEqualTo(BlackPawn)
        Assertions.assertThat(updatedChessGame.pieceOn(e4)).isEqualTo(NoPiece)
    }

    @Test
    fun `en passent capturing works for black (right)`() {
        val chessGame = chessGame(
            position(
                mapOf(
                    d4 to BlackPawn,
                    e2 to WhitePawn
                )
            )
        )
        val updatedChessGame = chessGame
            .movePiece(Move(e2, e4))
            .movePiece(Move(d4, e3))

        Assertions.assertThat(updatedChessGame.numberOfNextMove).isEqualTo(3)
        Assertions.assertThat(updatedChessGame.pieceOn(e3)).isEqualTo(BlackPawn)
        Assertions.assertThat(updatedChessGame.pieceOn(e4)).isEqualTo(NoPiece)
    }

    @Test
    fun `en passent capturing works for white (left)`() {
        val chessGame = chessGame(
            position(
                board = mapOf(
                    e7 to BlackPawn,
                    d5 to WhitePawn
                ),
                turn = Black
            )
        )
        val updatedChessGame = chessGame
            .movePiece(Move(e7, e5))
            .movePiece(Move(d5, e6))

        Assertions.assertThat(updatedChessGame.numberOfNextMove).isEqualTo(3)
        Assertions.assertThat(updatedChessGame.pieceOn(e6)).isEqualTo(WhitePawn)
        Assertions.assertThat(updatedChessGame.pieceOn(e5)).isEqualTo(NoPiece)
    }

    @Test
    fun `en passent capturing works for white (right)`() {
        val chessGame = chessGame(
            position(
                board = mapOf(
                    e7 to BlackPawn,
                    f5 to WhitePawn
                ),
                turn = Black
            )
        )
        val updatedChessGame = chessGame
            .movePiece(Move(e7, e5))
            .movePiece(Move(f5, e6))

        Assertions.assertThat(updatedChessGame.numberOfNextMove).isEqualTo(3)
        Assertions.assertThat(updatedChessGame.pieceOn(e6)).isEqualTo(WhitePawn)
        Assertions.assertThat(updatedChessGame.pieceOn(e5)).isEqualTo(NoPiece)
    }

}