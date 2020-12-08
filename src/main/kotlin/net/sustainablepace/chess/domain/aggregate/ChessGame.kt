package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Colour
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.White


class ChessGame private constructor(
    val id: ChessGameId,
    val position: Position,
    val turn: Colour,
    val white: Player,
    val black: Player,
    val status: String
) {

    constructor() : this(Position.default)
    private constructor(position: Position) : this(
        id = chessGameId(),
        position = position,
        turn = White,
        white = HumanPlayer,
        black = StupidComputerPlayer,
        status = "in progress"
    )

    val activePlayer: Player
        get() = if (turn == White) {
            white
        } else black

    fun movePiece(move: ValidMove): ChessGame =
        position.movePiece(move).let { newPosition ->
            ChessGame(
                id = id,
                position = newPosition,
                turn = if (turn == White) {
                    Black
                } else {
                    White
                },
                white = white,
                black = black,
                status = if (!newPosition.containsWhiteAndBlackPieces()) {
                    "checkmate"
                } else status
            )
        }
}