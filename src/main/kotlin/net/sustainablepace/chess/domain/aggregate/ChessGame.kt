package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove

typealias EnPassantSquare = Square?

class ChessGame private constructor(
    val id: ChessGameId,
    val position: Position,
    val turn: Side,
    val white: Player,
    val black: Player,
    val status: Status,
    val numberOfNextMove: Int = 1
) {
    constructor() : this(Position())
    constructor(position: Position) : this(White, position)
    constructor(side: Side) : this(side, Position())
    constructor(side: Side, position: Position) : this(
        id = chessGameId(),
        position = position,
        turn = side,
        white = HumanPlayer, //StupidComputerPlayer,
        black = StupidComputerPlayer,
        status = InProgress
    )

    val activePlayer: Player
        get() = if (turn == White) white else black

    fun pieceOn(arrivalSquare: Square): PieceOrNoPiece = position.pieceOn(arrivalSquare)

    fun moveOptions(): Set<ValidMove> =
        position.moveOptions(turn)

    fun movePiece(move: ValidMove): ChessGame =
        if (moveOptions().contains(move)) {
            position.movePiece(move).let { updatedPosition ->
                val updatedTurn = if (turn == White) Black else White
                val updatedNumberOfNextMove = if (updatedTurn != turn) (numberOfNextMove + 1) else numberOfNextMove
                ChessGame(
                    id = id,
                    position = updatedPosition,
                    turn = updatedTurn,
                    numberOfNextMove = if (updatedNumberOfNextMove > numberOfNextMove) updatedNumberOfNextMove else numberOfNextMove,
                    white = white,
                    black = black,
                    status = when {
                        updatedPosition.isCheckMate(updatedTurn) -> Checkmate
                        updatedPosition.isStaleMate(updatedTurn) -> Stalemate
                        else -> status
                    }
                )
            }
        } else this



}