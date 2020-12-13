package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.aggregate.EnPassantSquare
import net.sustainablepace.chess.domain.move.ValidMove
import net.sustainablepace.chess.domain.move.rules.MoveRuleSet

typealias Position = Map<Square, Piece>

fun Position.containsBothWhiteAndBlackPieces(): Boolean =
    values.map { it.side }.containsAll(listOf(White, Black))

fun Position.pieceOn(square: Square): PieceOrNoPiece = this[square] ?: NoPiece

fun Position.moveOptions(
    square: Square,
    enPassantSquare: EnPassantSquare,
    whiteCastlingOptions: CastlingOptions,
    blackCastlingOptions: CastlingOptions
) : Set<ValidMove> {
    return get(square).let { pieceToBeMoved ->
        if (pieceToBeMoved is Piece) {
            MoveRuleSet.getRulesForPiece(pieceToBeMoved).moveRules.flatMap { rule ->
                rule.findMoves(square, pieceToBeMoved, this, enPassantSquare, whiteCastlingOptions, blackCastlingOptions)
            }.filter { move ->
                true // TODO find moves that result in a check position
            }.toSet()
        } else emptySet()
    }
}

fun Position.isInCheck(side: Side): Boolean {
    val threatenedKingSquare = filter {
        it.value is King && it.value.side == side
    }.map {
        it.key
    }.firstOrNull() ?: return false

    filter {
        it.value.side != side
    }.keys.map { square ->
        moveOptions(
            square,
            null,
            CastlingOptions(White, false, false),
            CastlingOptions(White, false, false)
        ).find { it.arrivalSquare == threatenedKingSquare }?.let {
            return true
        }
    }
    return false
}

fun Position.movePiece(move: ValidMove, enPassantSquare: EnPassantSquare): Position {
    return mutableMapOf<Square, Piece>().let { newPosition ->
        newPosition.putAll(this)
        newPosition[move.arrivalSquare] = getValue(move.departureSquare)
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
    }
}