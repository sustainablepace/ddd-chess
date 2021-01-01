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
            } + this.shortType


        operator fun invoke(chessGame: ChessGame): ChessGameReadModel {
            return with(chessGame) {
                ChessGameReadModel(
                    id = id,
                    position = position.board.pieces.map { it.first.toString() to it.second.stringify() }.toMap(),
                    turn = when (position.turn) {
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
                    status = status.javaClass.simpleName,
                    computerTurn = when (position.turn) {
                        White -> white is ComputerPlayer
                        Black -> black is ComputerPlayer
                    }
                )
            }
        }
    }
}