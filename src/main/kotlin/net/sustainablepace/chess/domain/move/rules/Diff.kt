package net.sustainablepace.chess.domain.move.rules

import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.Square
import kotlin.math.abs

infix fun Square.diff(other: Square): Direction = Direction(abs(file - other.file), abs(rank - other.rank))