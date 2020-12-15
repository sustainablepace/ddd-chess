package net.sustainablepace.chess.domain.readmodel

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.*

data class ChessGameReadModel(
    val id: ChessGameId,
    val position: Map<String, String>,
    val turn: String,
    val white: String,
    val black: String,
    val status: String,
    val computerTurn: Boolean
) {
    companion object {

        private fun Piece.stringify(): String =
            when (side) {
                is White -> "w"
                is Black -> "b"
            } + when (this) {
                is Pawn -> "P"
                is Knight -> "N"
                is Rook -> "R"
                is Bishop -> "B"
                is Queen -> "Q"
                is King -> "K"
            }

        operator fun invoke(chessGame: ChessGame): ChessGameReadModel {
            return with(chessGame) {
                ChessGameReadModel(
                    id = id,
                    position = position.board.map { it.key.toString() to it.value.stringify() }.toMap(),
                    turn = when (turn) {
                        White -> "white"
                        Black -> "black"
                    },
                    white = when (white) {
                        is HumanPlayer -> "human"
                        is ComputerPlayer -> "computer"
                    },
                    black = when (black) {
                        is HumanPlayer -> "human"
                        is ComputerPlayer -> "computer"
                    },
                    status = getStatus().javaClass.simpleName,
                    computerTurn = when (turn) {
                        White -> white is ComputerPlayer
                        Black -> black is ComputerPlayer
                    }
                )
            }
        }
    }
}