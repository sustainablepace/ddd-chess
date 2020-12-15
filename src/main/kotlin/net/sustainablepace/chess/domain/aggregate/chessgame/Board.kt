package net.sustainablepace.chess.domain.aggregate.chessgame

typealias Board = Map<Square, Piece>

fun Board.findKing(side: Side): Square? = filter {
    it.value is King && it.value.side == side
}.map {
    it.key
}.firstOrNull()

fun Board.findSquares(side: Side): Set<Square> = filter {
    it.value.side == side
}.keys