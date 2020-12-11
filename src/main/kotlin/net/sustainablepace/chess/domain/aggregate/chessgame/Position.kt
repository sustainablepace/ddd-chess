package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.BlackPieces
import net.sustainablepace.chess.domain.aggregate.chessgame.WhitePieces
import net.sustainablepace.chess.domain.aggregate.chessgame.position.Piece
import net.sustainablepace.chess.domain.aggregate.chessgame.position.Square

typealias Position = Map<Square, Piece>

fun Position.containsWhiteAndBlackPieces(): Boolean =
    values.map { it.colour }.containsAll(listOf(WhitePieces, BlackPieces))