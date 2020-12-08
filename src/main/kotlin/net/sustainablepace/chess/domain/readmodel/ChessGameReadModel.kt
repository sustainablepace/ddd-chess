package net.sustainablepace.chess.domain.readmodel

import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.White


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
            when (this) {
                is WhitePawn -> "wP"
                is WhiteKnight -> "wN"
                is WhiteRook -> "wR"
                is WhiteBishop -> "wB"
                is WhiteQueen -> "wQ"
                is WhiteKing -> "wK"
                is BlackPawn -> "bP"
                is BlackKnight -> "bN"
                is BlackRook -> "bR"
                is BlackBishop -> "bB"
                is BlackQueen -> "bQ"
                is BlackKing -> "bK"
            }

        operator fun invoke(chessGame: ChessGame): ChessGameReadModel {
            return with(chessGame) {
                ChessGameReadModel(
                    id = id,
                    position = position.squaresWithPieces.mapValues { it.value.stringify() },
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
                    status = status,
                    computerTurn = when(turn) {
                        White -> white is ComputerPlayer
                        Black -> black is ComputerPlayer
                    }
                )
            }
        }
    }
}