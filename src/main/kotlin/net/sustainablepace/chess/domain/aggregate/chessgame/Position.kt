package net.sustainablepace.chess.domain.aggregate.chessgame

typealias Position = Map<Square, Piece>

fun Position.containsBothWhiteAndBlackPieces(): Boolean =
    values.map { it.side }.containsAll(listOf(White, Black))