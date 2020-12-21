package net.sustainablepace.chess.domain.aggregate.chessgame

sealed class Side {
    abstract val baseLine: Rank
}
operator fun Side.not() = when(this) {
    is White -> Black
    is Black -> White
}
object White: Side() {
    override val baseLine: Rank = 1
}

object Black: Side() {
    override val baseLine: Rank = 8
}
