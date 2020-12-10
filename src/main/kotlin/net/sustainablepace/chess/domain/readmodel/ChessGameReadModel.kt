package net.sustainablepace.chess.domain.readmodel

import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.BlackPieces
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.White
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.WhitePieces


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
            when (colour) {
                is WhitePieces -> "w"
                is BlackPieces -> "b"
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
                    position = position.squaresWithPieces.mapValues { it.value.stringify() },
                    turn = when (turn) {
                        WhitePieces -> "white"
                        BlackPieces -> "black"
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
                    computerTurn = when (turn) {
                        WhitePieces -> white is ComputerPlayer
                        BlackPieces -> black is ComputerPlayer
                    }
                )
            }
        }
    }
}