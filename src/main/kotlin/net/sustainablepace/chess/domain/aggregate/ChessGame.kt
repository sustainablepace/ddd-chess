package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import net.sustainablepace.chess.domain.move.rules.MoveRuleSet
import kotlin.math.abs

typealias EnPassantSquare = Square?

class ChessGame private constructor(
    val id: ChessGameId,
    val position: Position,
    val turn: Side,
    val white: Player,
    val black: Player,
    val status: String,
    val numberOfNextMove: Int = 1,
    val enPassantSquare: EnPassantSquare = null,
    val whiteCastlingOptions: CastlingOptions = CastlingOptions(),
    val blackCastlingOptions: CastlingOptions = CastlingOptions(),
) {
    constructor() : this(defaultPosition)
    constructor(position: Position) : this(White, position)
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
        get() = if (turn == White) white else black

    fun moveOptions(): Set<ValidMove> =
        position
            .filter { it.value.side == turn }.keys
            .flatMap { square -> moveOptions(square) }
            .toSet()

    fun moveOptions(departureSquare: Square): Set<ValidMove> =
        position[departureSquare].let { pieceToBeMoved ->
            if (pieceToBeMoved is Piece) {
                MoveRuleSet.getRulesForPiece(pieceToBeMoved).moveRules.flatMap { rule ->
                    rule.findMoves(this, departureSquare, pieceToBeMoved)
                }.toSet()
            } else emptySet()
        }

    private fun enpassantSquareOfMove(move: ValidMove): EnPassantSquare =
        if (
            position[move.departureSquare] is Pawn &&
            abs(move.departureSquare.rank() - move.arrivalSquare.rank()) == 2
        ) {
            move.arrivalSquare
        } else null

    private fun calculatePositionAfterMove(move: ValidMove): Position =
        mutableMapOf<Square, Piece>().run {
            putAll(position)
            this[move.arrivalSquare] = getValue(move.departureSquare)
            remove(move.departureSquare)

            // castling
            if (this[move.arrivalSquare] is WhiteKing && move == ValidMove("e1-c1")) {
                this["d1"] = WhiteRook
                remove("a1")
            }
            if (this[move.arrivalSquare] is WhiteKing && move == ValidMove("e1-g1")) {
                this["f1"] = WhiteRook
                remove("h1")
            }
            if (this[move.arrivalSquare] is BlackKing && move == ValidMove("e8-c8")) {
                this["d8"] = BlackRook
                remove("a8")
            }
            if (this[move.arrivalSquare] is BlackKing && move == ValidMove("e8-g8")) {
                this["f8"] = BlackRook
                remove("h8")
            }

            // Promotion
            if (this[move.arrivalSquare] is WhitePawn && move.arrivalSquare.rank() == '8') {
                this[move.arrivalSquare] = WhiteQueen
            }
            if (this[move.arrivalSquare] is BlackPawn && move.arrivalSquare.rank() == '1') {
                this[move.arrivalSquare] = BlackQueen
            }

            // En passant capturing
            if (enPassantSquare != null) {
                val lowerNeighbour = enPassantSquare.lowerNeighbour()
                val upperNeighbour = enPassantSquare.upperNeighbour()
                if (turn == White && upperNeighbour != null && this[upperNeighbour] is WhitePawn) {
                    remove(enPassantSquare)
                } else if (turn == Black && lowerNeighbour != null && this[lowerNeighbour] is BlackPawn) {
                    remove(enPassantSquare)
                }
            }
            this
        }

    fun movePiece(move: ValidMove): ChessGame =
        when (val piece = position[move.departureSquare]) {
            is Piece -> piece
            else -> null
        }?.let { piece ->
            if (moveOptions().contains(move)) {
                calculatePositionAfterMove(move).let { updatedPosition ->
                    val updatedTurn = if (turn == White) Black else White
                    val updatedNumberOfNextMove = if (updatedTurn != turn) (numberOfNextMove + 1) else numberOfNextMove
                    ChessGame(
                        id = id,
                        position = updatedPosition,
                        turn = updatedTurn,
                        numberOfNextMove = if (updatedNumberOfNextMove > numberOfNextMove) updatedNumberOfNextMove else numberOfNextMove,
                        enPassantSquare = if (updatedNumberOfNextMove > numberOfNextMove) enpassantSquareOfMove(move) else enPassantSquare,
                        white = white,
                        black = black,
                        status = if (!updatedPosition.containsBothWhiteAndBlackPieces()) "checkmate" else status,
                        whiteCastlingOptions = whiteCastlingOptions.updateAfterPieceMoved(move.departureSquare, piece),
                        blackCastlingOptions = blackCastlingOptions.updateAfterPieceMoved(move.departureSquare, piece)
                    )
                }
            } else this
        } ?: this

    fun pieceOn(arrivalSquare: Square): PieceOrNoPiece = position[arrivalSquare] ?: NoPiece

    companion object {
        val defaultPosition = mapOf(
            "a1" to WhiteRook,
            "b1" to WhiteKnight,
            "c1" to WhiteBishop,
            "d1" to WhiteQueen,
            "e1" to WhiteKing,
            "f1" to WhiteBishop,
            "g1" to WhiteKnight,
            "h1" to WhiteRook,
            "a2" to WhitePawn,
            "b2" to WhitePawn,
            "c2" to WhitePawn,
            "d2" to WhitePawn,
            "e2" to WhitePawn,
            "f2" to WhitePawn,
            "g2" to WhitePawn,
            "h2" to WhitePawn,
            "a8" to BlackRook,
            "b8" to BlackKnight,
            "c8" to BlackBishop,
            "d8" to BlackQueen,
            "e8" to BlackKing,
            "f8" to BlackBishop,
            "g8" to BlackKnight,
            "h8" to BlackRook,
            "a7" to BlackPawn,
            "b7" to BlackPawn,
            "c7" to BlackPawn,
            "d7" to BlackPawn,
            "e7" to BlackPawn,
            "f7" to BlackPawn,
            "g7" to BlackPawn,
            "h7" to BlackPawn
        )
    }
}