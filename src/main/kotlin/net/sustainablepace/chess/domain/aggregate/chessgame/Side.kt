package net.sustainablepace.chess.domain.aggregate.chessgame

sealed class Side
operator fun Side.not() = when(this) {
    is White -> Black
    is Black -> White
}
object White: Side()
object Black: Side()
