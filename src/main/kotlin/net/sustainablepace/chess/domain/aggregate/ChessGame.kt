package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.Position
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.*
import net.sustainablepace.chess.domain.aggregate.containsWhiteAndBlackPieces
import java.lang.Math.abs

typealias EnPassentSquare = Square?


class ChessGame private constructor(
    val id: ChessGameId,
    private val position: Position,
    val turn: Side,
    val white: Player,
    val black: Player,
    val status: String,
    val numberOfNextMove: Int = 1,
    val enPassantSquare: EnPassentSquare = null,
    val whiteCastlingOptions: CastlingOptions = CastlingOptions(),
    val blackCastlingOptions: CastlingOptions = CastlingOptions(),
) {

    constructor() : this(defaultPosition)
    constructor(position: Position) : this(WhitePieces, position)
    constructor(side: Side) : this(side, defaultPosition)
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

    fun findMoves(departureSquare: Square): Set<ValidMove> =
        position.get(departureSquare).let { pieceToBeMoved ->
            if (pieceToBeMoved is Piece) {
                pieceToBeMoved.rules.findMoves(this, departureSquare, pieceToBeMoved)
            }
            else emptySet()
        }

    fun enpassantSquareOfMove(move: ValidMove): EnPassentSquare =
        if (
            position.get(move.departureSquare) is Pawn &&
            abs(move.departureSquare.rank() - move.arrivalSquare.rank()) == 2
        ) {
            move.arrivalSquare
        } else null

    fun movePiece(move: ValidMove): ChessGame =
        if (findMoves().contains(move)) {
            val turnBeforeMove = turn
            val piece = position.get(move.departureSquare)
            mutableMapOf<Square, Piece>().run {
                putAll(position)
                this[move.arrivalSquare] = getValue(move.departureSquare)
                remove(move.departureSquare)

                // castling
                if (this[move.arrivalSquare] is WhiteKing && move == ValidMove("e1-c1")) {
                    this["d1"] = WhiteRook()
                    remove("a1")
                }
                if (this[move.arrivalSquare] is WhiteKing && move == ValidMove("e1-g1")) {
                    this["f1"] = WhiteRook()
                    remove("h1")
                }
                if (this[move.arrivalSquare] is BlackKing && move == ValidMove("e8-c8")) {
                    this["d8"] = BlackRook()
                    remove("a8")
                }
                if (this[move.arrivalSquare] is BlackKing && move == ValidMove("e8-g8")) {
                    this["f8"] = BlackRook()
                    remove("h8")
                }

                // Promotion
                if (this[move.arrivalSquare] is WhitePawn && move.arrivalSquare.rank() == '8') {
                    this[move.arrivalSquare] = WhiteQueen()
                }
                if (this[move.arrivalSquare] is BlackPawn && move.arrivalSquare.rank() == '1') {
                    this[move.arrivalSquare] = BlackQueen()
                }

                // En passant capturing
                if (enPassantSquare != null) {
                    val lowerNeighbour = enPassantSquare.lowerNeighbour()
                    val upperNeighbour = enPassantSquare.upperNeighbour()
                    if (turn == WhitePieces && upperNeighbour != null && this[upperNeighbour] is WhitePawn) {
                        remove(enPassantSquare)
                    } else if (turn == BlackPieces && lowerNeighbour != null && this[lowerNeighbour] is BlackPawn) {
                        remove(enPassantSquare)
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
                    enPassantSquare = if (updatedTurn != turnBeforeMove) {
                        enpassantSquareOfMove(move)
                    } else {
                        enPassantSquare
                    },
                    white = white,
                    black = black,
                    status = if (!newPosition.containsWhiteAndBlackPieces()) {
                        "checkmate"
                    } else status,
                    whiteCastlingOptions = if(piece is WhiteRook && move.departureSquare == "a1") {
                        CastlingOptions(queenSide = false, kingSide = whiteCastlingOptions.kingSide)
                    } else if(piece is WhiteRook && move.departureSquare == "h1") {
                        CastlingOptions(queenSide = whiteCastlingOptions.queenSide, kingSide = false)
                    } else if(piece is WhiteKing && move.departureSquare == "e1") {
                        CastlingOptions(queenSide = false, kingSide = false)
                    } else whiteCastlingOptions,
                    blackCastlingOptions = if(piece is BlackRook && move.departureSquare == "a8") {
                        CastlingOptions(queenSide = false, kingSide = blackCastlingOptions.kingSide)
                    } else if(piece is BlackRook && move.departureSquare == "h8") {
                        CastlingOptions(queenSide = blackCastlingOptions.queenSide, kingSide = false)
                    } else if(piece is BlackKing && move.departureSquare == "e8") {
                        CastlingOptions(queenSide = false, kingSide = false)
                    } else blackCastlingOptions,
                )
                updatedGame
            }
        } else this

    fun pieceOn(arrivalSquare: Square): ChessPiece = position.get(arrivalSquare) ?: NoPiece

    fun getPosition() = position.toMap()

    val allSquares = ('a'..'h').flatMap { column ->
        ('1'..'8').map { column + "" + it }
    }

    companion object {
        val defaultPosition = mapOf(
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
        )
    }
}