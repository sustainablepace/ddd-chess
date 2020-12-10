package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.Piece
import net.sustainablepace.chess.domain.aggregate.chessgame.position.Square
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.*


class ChessGame private constructor(
    val id: ChessGameId,
    val position: Position,
    val turn: Side,
    val white: Player,
    val black: Player,
    val status: String
) {

    constructor() : this(Position.default)
    constructor(position: Position) : this(WhitePieces, position)
    constructor(side: Side) : this(side, Position.default)
    constructor(side: Side, position: Position) : this(
        id = chessGameId(),
        position = position,
        turn = side,
        white = HumanPlayer, //StupidComputerPlayer,
        black = StupidComputerPlayer,
        status = "in progress"
    )

    val activePlayer: Player
        get() = if (turn == WhitePieces) {
            white
        } else black

    fun findMoves(): Set<ValidMove> {
        val squares = position.squaresWithPieces.filter { it.value.colour == turn }.keys
        return squares.flatMap { findMoves( it ) }.toSet()
    }

    fun findMoves(departureSquare: Square): Set<ValidMove> {
        val pieceToBeMoved = position.get(departureSquare)
        if (pieceToBeMoved !is Piece) {
            return emptySet()
        }
        return pieceToBeMoved.moveRules.findMoves(position::get, departureSquare, pieceToBeMoved)
    }

    fun movePiece(move: ValidMove): ChessGame =
        if(findMoves().contains(move)) {
            position.movePiece(move).let { newPosition ->
                ChessGame(
                    id = id,
                    position = newPosition,
                    turn = if (turn == WhitePieces) {
                        BlackPieces
                    } else {
                        WhitePieces
                    },
                    white = white,
                    black = black,
                    status = if (!newPosition.containsWhiteAndBlackPieces()) {
                        "checkmate"
                    } else status
                )
            }
        } else this
}