package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.move.Move

typealias Board = Map<Square, Piece>

fun Board.findKing(side: Side): Square? = filter {
    it.value is King && it.value.side == side
}.map {
    it.key
}.firstOrNull()

fun Board.findSquares(side: Side): Set<Square> = filter {
    it.value.side == side
}.keys

fun Board.isDeadPosition(): Boolean =
    containsOnlyKings() ||
        containsOnlyKingsAndOneBishop() ||
        containsOnlyKingsAndOneKnight() ||
        containsOnlyKingsAndTwoBishopsOnSameSquareColour()

private fun Board.containsOnlyKings() = values.toSet() == setOf(WhiteKing, BlackKing)
private fun Board.containsOnlyKingsAndOneBishop() = values.toSet().let {
    it == setOf(WhiteKing, BlackKing, WhiteBishop) || it == setOf(WhiteKing, BlackKing, BlackBishop)
}
private fun Board.containsOnlyKingsAndOneKnight() = values.toSet().let {
    it == setOf(WhiteKing, BlackKing, WhiteKnight) || it == setOf(WhiteKing, BlackKing, BlackKnight)
}
private fun Board.containsOnlyKingsAndTwoBishopsOnSameSquareColour() =
    values.toSet() == setOf(WhiteKing, BlackKing, WhiteBishop, BlackBishop) &&
        filter { it.value is Bishop }.map { it.key.colour() }.distinct().size == 1

fun Board.movePiece(move: Move, movingPiece: Piece, enPassantSquare: EnPassantSquare): Board {
    val updatedBoard = mutableMapOf<Square, Piece>()
    updatedBoard.putAll(this)

    updatedBoard[move.arrivalSquare] = movingPiece
    updatedBoard.remove(move.departureSquare)

    // castling
    if (updatedBoard[move.arrivalSquare] is WhiteKing && move == Move(e1, c1)) {
        updatedBoard[d1] = WhiteRook
        updatedBoard.remove(a1)
    } else if (updatedBoard[move.arrivalSquare] is WhiteKing && move == Move(e1, g1)) {
        updatedBoard[f1] = WhiteRook
        updatedBoard.remove(h1)
    } else if (updatedBoard[move.arrivalSquare] is BlackKing && move == Move(e8, c8)) {
        updatedBoard[d8] = BlackRook
        updatedBoard.remove(a8)
    } else if (updatedBoard[move.arrivalSquare] is BlackKing && move == Move(e8, g8)) {
        updatedBoard[f8] = BlackRook
        updatedBoard.remove(h8)
    }

    // Promotion
    if (updatedBoard[move.arrivalSquare] is WhitePawn && move.arrivalSquare.rank == 8) {
        updatedBoard[move.arrivalSquare] = WhiteQueen
    } else if (updatedBoard[move.arrivalSquare] is BlackPawn && move.arrivalSquare.rank == 1) {
        updatedBoard[move.arrivalSquare] = BlackQueen
    }

    // En passant capturing
    if (enPassantSquare != null) {
        val lowerNeighbour = enPassantSquare.lowerNeighbour()
        val upperNeighbour = enPassantSquare.upperNeighbour()
        if (movingPiece.side == White && upperNeighbour != null && updatedBoard[upperNeighbour] is WhitePawn) {
            updatedBoard.remove(enPassantSquare)
        } else if (movingPiece.side == Black && lowerNeighbour != null && updatedBoard[lowerNeighbour] is BlackPawn) {
            updatedBoard.remove(enPassantSquare)
        }
    }
    return updatedBoard
}