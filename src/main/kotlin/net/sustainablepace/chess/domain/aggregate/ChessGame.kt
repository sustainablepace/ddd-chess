package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.*
import java.lang.Math.abs

typealias EnPassentSquare = Square?

typealias Position = MutableMap<Square, Piece>
fun Position.containsWhiteAndBlackPieces(): Boolean =
    values.map { it.colour }.containsAll(listOf(WhitePieces, BlackPieces))


class ChessGame private constructor(
    val id: ChessGameId,
    val position: Position,
    val turn: Side,
    val white: Player,
    val black: Player,
    val status: String,
    val numberOfNextMove: Int = 1,
    val enPassant: EnPassentSquare = null,
    val whiteCastlingOptions: CastlingOptions<WhitePieces> = CastlingOptions(),
    val blackCastlingOptions: CastlingOptions<BlackPieces> = CastlingOptions(),
) {

    constructor() : this(default)
    constructor(position: Position) : this(WhitePieces, position)
    constructor(side: Side) : this(side, default)
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
        val squares = position.filter { it.value.colour == turn }.keys
        return squares.flatMap { findMoves(it) }.toSet()
    }

    fun findMoves(departureSquare: Square): Set<ValidMove> {
        val pieceToBeMoved = position.get(departureSquare)
        if (pieceToBeMoved !is Piece) {
            return emptySet()
        }
        return pieceToBeMoved.moveRules.findMoves(this, departureSquare, pieceToBeMoved)
    }

    fun enpassantSquareOfMove(move: ValidMove): EnPassentSquare {
        val piece = position.get(move.departureSquare)
        if (piece is Pawn) {
            val diff = abs(move.departureSquare.rank() - move.arrivalSquare.rank())
            if( diff == 2) {
                return move.arrivalSquare
            }
        }
        return null
    }

    fun movePiece(move: ValidMove): ChessGame =
        if (findMoves().contains(move)) {
            val turnBeforeMove = turn
            var whiteCastling = false
            var blackCastling = false
            mutableMapOf<Square, Piece>().run<MutableMap<Square, Piece>, MutableMap<Square, Piece>> {
                putAll(position)
                this[move.arrivalSquare] = getValue(move.departureSquare)
                remove(move.departureSquare)

                // castling
                if (this[move.arrivalSquare] is WhiteKing && move == ValidMove("e1-c1")) {
                    this["d1"] = WhiteRook()
                    remove("a1")
                    whiteCastling = true
                }
                if (this[move.arrivalSquare] is WhiteKing && move == ValidMove("e1-g1")) {
                    this["f1"] = WhiteRook()
                    remove("h1")
                    whiteCastling = true
                }
                if (this[move.arrivalSquare] is BlackKing && move == ValidMove("e8-c8")) {
                    this["d8"] = BlackRook()
                    remove("a8")
                    blackCastling = true
                }
                if (this[move.arrivalSquare] is BlackKing && move == ValidMove("e8-g8")) {
                    this["f8"] = BlackRook()
                    remove("h8")
                    blackCastling = true
                }

                // Promotion
                if (this[move.arrivalSquare] is WhitePawn && move.arrivalSquare.rank() == '8') {
                    this[move.arrivalSquare] = WhiteQueen()
                }
                if (this[move.arrivalSquare] is BlackPawn && move.arrivalSquare.rank() == '1') {
                    this[move.arrivalSquare] = BlackQueen()
                }

                // En passant capturing
                if (enPassant != null) {
                    val lowerNeighbour = enPassant.lowerNeighbour()
                    val upperNeighbour = enPassant.upperNeighbour()
                    if (turn == WhitePieces && upperNeighbour != null && this[upperNeighbour] is WhitePawn) {
                        remove(enPassant)
                    } else if (turn == BlackPieces && lowerNeighbour != null && this[lowerNeighbour] is BlackPawn) {
                        remove(enPassant)
                    }
                }
                this
            }.let { newPosition ->

                val updatedTurn = if (turn == WhitePieces) {
                    BlackPieces
                } else {
                    WhitePieces
                }
                val updatedGame = ChessGame(
                    id = id,
                    position = newPosition,
                    turn = updatedTurn,
                    numberOfNextMove = if (updatedTurn != turnBeforeMove) {
                        numberOfNextMove + 1
                    } else {
                        numberOfNextMove
                    },
                    enPassant = if (updatedTurn != turnBeforeMove) {
                        enpassantSquareOfMove(move)
                    } else {
                        enPassant
                    },
                    white = white,
                    black = black,
                    status = if (!newPosition.containsWhiteAndBlackPieces()) {
                        "checkmate"
                    } else status,
                    whiteCastlingOptions = if(whiteCastling) {
                        CastlingOptions<WhitePieces>(false, false)
                    } else whiteCastlingOptions,
                    blackCastlingOptions = if(blackCastling) {
                        CastlingOptions<BlackPieces>(false, false)
                    } else blackCastlingOptions,
                )
                updatedGame
            }
        } else this

    val allSquares = ('a'..'h').flatMap { column ->
        ('1'..'8').map { column + "" + it }
    }

    companion object {
        val default = mutableMapOf(
            "a1" to WhiteRook(),
            "b1" to WhiteKnight(),
            "c1" to WhiteBishop(),
            "d1" to WhiteQueen(),
            "e1" to WhiteKing(),
            "f1" to WhiteBishop(),
            "g1" to WhiteKnight(),
            "h1" to WhiteRook(),
            "a2" to WhitePawn(),
            "b2" to WhitePawn(),
            "c2" to WhitePawn(),
            "d2" to WhitePawn(),
            "e2" to WhitePawn(),
            "f2" to WhitePawn(),
            "g2" to WhitePawn(),
            "h2" to WhitePawn(),
            "a8" to BlackRook(),
            "b8" to BlackKnight(),
            "c8" to BlackBishop(),
            "d8" to BlackQueen(),
            "e8" to BlackKing(),
            "f8" to BlackBishop(),
            "g8" to BlackKnight(),
            "h8" to BlackRook(),
            "a7" to BlackPawn(),
            "b7" to BlackPawn(),
            "c7" to BlackPawn(),
            "d7" to BlackPawn(),
            "e7" to BlackPawn(),
            "f7" to BlackPawn(),
            "g7" to BlackPawn(),
            "h7" to BlackPawn()
        ) as Position
    }
}