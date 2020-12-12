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
            if (pieceToBeMoved is Piece && pieceToBeMoved.side == turn) {
                MoveRuleSet.getRulesForPiece(pieceToBeMoved).moveRules.flatMap { rule ->
                    rule.findMoves(this, departureSquare)
                }.toSet()
            } else emptySet()
        }

    private fun enpassantSquareOfMove(move: ValidMove): EnPassantSquare =
        if (
            position[move.departureSquare] is Pawn &&
            abs(move.departureSquare.rank - move.arrivalSquare.rank) == 2
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
                this[D1] = WhiteRook
                remove(A1)
            }
            if (this[move.arrivalSquare] is WhiteKing && move == ValidMove("e1-g1")) {
                this[F1] = WhiteRook
                remove(H1)
            }
            if (this[move.arrivalSquare] is BlackKing && move == ValidMove("e8-c8")) {
                this[D8] = BlackRook
                remove(A8)
            }
            if (this[move.arrivalSquare] is BlackKing && move == ValidMove("e8-g8")) {
                this[F8] = BlackRook
                remove(H8)
            }

            // Promotion
            if (this[move.arrivalSquare] is WhitePawn && move.arrivalSquare.rank == '8') {
                this[move.arrivalSquare] = WhiteQueen
            }
            if (this[move.arrivalSquare] is BlackPawn && move.arrivalSquare.rank == '1') {
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
            A1 to WhiteRook,
            B1 to WhiteKnight,
            C1 to WhiteBishop,
            D1 to WhiteQueen,
            E1 to WhiteKing,
            F1 to WhiteBishop,
            G1 to WhiteKnight,
            H1 to WhiteRook,
            A2 to WhitePawn,
            B2 to WhitePawn,
            C2 to WhitePawn,
            D2 to WhitePawn,
            E2 to WhitePawn,
            F2 to WhitePawn,
            G2 to WhitePawn,
            H2 to WhitePawn,
            A8 to BlackRook,
            B8 to BlackKnight,
            C8 to BlackBishop,
            D8 to BlackQueen,
            E8 to BlackKing,
            F8 to BlackBishop,
            G8 to BlackKnight,
            H8 to BlackRook,
            A7 to BlackPawn,
            B7 to BlackPawn,
            C7 to BlackPawn,
            D7 to BlackPawn,
            E7 to BlackPawn,
            F7 to BlackPawn,
            G7 to BlackPawn,
            H7 to BlackPawn
        )
    }
}