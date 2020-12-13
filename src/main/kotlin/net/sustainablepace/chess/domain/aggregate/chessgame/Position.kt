package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.aggregate.EnPassantSquare
import net.sustainablepace.chess.domain.move.ValidMove
import net.sustainablepace.chess.domain.move.rules.MoveRuleSet
import kotlin.math.abs

typealias Board = Map<Square, Piece>

data class Position(
    val board: Board,
    val enPassantSquare: EnPassantSquare = null,
    val whiteCastlingOptions: CastlingOptions = CastlingOptions(White),
    val blackCastlingOptions: CastlingOptions = CastlingOptions(Black)
) {

    constructor() : this(defaultBoard)

    fun pieceOn(square: Square): PieceOrNoPiece = board[square] ?: NoPiece

    fun moveOptions(
        square: Square
    ): Set<ValidMove> {
        return board[square].let { pieceToBeMoved ->
            if (pieceToBeMoved is Piece) {
                MoveRuleSet.getRulesForPiece(pieceToBeMoved).moveRules.flatMap { rule ->
                    rule.findMoves(square, pieceToBeMoved, this)
                }.filter { move ->
                    true // TODO find moves that result in a check position
                }.toSet()
            } else emptySet()
        }
    }


    fun moveOptions(side: Side): Set<ValidMove> =
        board
            .filter { it.value.side == side }.keys
            .flatMap { square -> moveOptions(square) }
            .filter { move ->
                !movePiece(move).isInCheck(side)
            }
            .toSet()

    fun isCheckMate(side: Side): Boolean = moveOptions(side).isEmpty() && isInCheck(side)
    fun isStaleMate(side: Side): Boolean = moveOptions(side).isEmpty() && !isInCheck(side)

    fun isInCheck(side: Side): Boolean {
        val threatenedKingSquare = board.filter {
            it.value is King && it.value.side == side
        }.map {
            it.key
        }.firstOrNull() ?: return false

        board.filter {
            it.value.side != side
        }.keys.map { square ->
            moveOptions(square).find { it.arrivalSquare == threatenedKingSquare }?.let {
                return true
            }
        }
        return false
    }

    fun movePiece(move: ValidMove): Position =
        mutableMapOf<Square, Piece>().let { newPosition ->
            newPosition.putAll(board)
            newPosition[move.arrivalSquare] = board.getValue(move.departureSquare)
            newPosition.remove(move.departureSquare)

            // castling
            if (newPosition[move.arrivalSquare] is WhiteKing && move == ValidMove("e1-c1")) {
                newPosition[D1] = WhiteRook
                newPosition.remove(A1)
            }
            if (newPosition[move.arrivalSquare] is WhiteKing && move == ValidMove("e1-g1")) {
                newPosition[F1] = WhiteRook
                newPosition.remove(H1)
            }
            if (newPosition[move.arrivalSquare] is BlackKing && move == ValidMove("e8-c8")) {
                newPosition[D8] = BlackRook
                newPosition.remove(A8)
            }
            if (newPosition[move.arrivalSquare] is BlackKing && move == ValidMove("e8-g8")) {
                newPosition[F8] = BlackRook
                newPosition.remove(H8)
            }

            // Promotion
            if (newPosition[move.arrivalSquare] is WhitePawn && move.arrivalSquare.rank == '8') {
                newPosition[move.arrivalSquare] = WhiteQueen
            }
            if (newPosition[move.arrivalSquare] is BlackPawn && move.arrivalSquare.rank == '1') {
                newPosition[move.arrivalSquare] = BlackQueen
            }

            // En passant capturing
            val movingPiece = pieceOn(move.departureSquare) as Piece
            if (enPassantSquare != null) {
                val lowerNeighbour = enPassantSquare.lowerNeighbour()
                val upperNeighbour = enPassantSquare.upperNeighbour()
                if (movingPiece.side == White && upperNeighbour != null && newPosition[upperNeighbour] is WhitePawn) {
                    newPosition.remove(enPassantSquare)
                } else if (movingPiece.side == Black && lowerNeighbour != null && newPosition[lowerNeighbour] is BlackPawn) {
                    newPosition.remove(enPassantSquare)
                }
            }
            newPosition
        }.let { board ->
            val movingPiece = pieceOn(move.departureSquare) as Piece

            Position(
                board,
                enpassantSquareOfMove(move),
                whiteCastlingOptions.updateAfterPieceMoved(move.departureSquare, movingPiece),
                blackCastlingOptions.updateAfterPieceMoved(move.departureSquare, movingPiece)
            )
        }

    private fun enpassantSquareOfMove(move: ValidMove): EnPassantSquare =
        if (
            board[move.departureSquare] is Pawn &&
            abs(move.departureSquare.rank - move.arrivalSquare.rank) == 2
        ) {
            move.arrivalSquare
        } else null

    companion object {
        private val defaultBoard = mapOf(
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