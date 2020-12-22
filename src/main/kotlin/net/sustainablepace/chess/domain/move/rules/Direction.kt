package net.sustainablepace.chess.domain.move.rules

import net.sustainablepace.chess.domain.aggregate.chessgame.Square

class Memoize1<in T, in U, out R>(val f: (T, U) -> R) : (T, U) -> R {
    private val values = mutableMapOf<Pair<T, U>, R>()

    override fun invoke(p1: T, p2: U): R {
        return values.getOrPut(p1 to p2, { f(p1, p2) })
    }
}

fun <T, U, R> ((T, U) -> R).memoize(): (T, U) -> R = Memoize1(this)

data class Direction constructor(val x: Int, val y: Int) {

    fun rotate(n: Int): Direction = when {
        n == 1 -> Direction(y, -x)
        n > 1 -> Direction(y, -x).rotate(n - 1)
        else -> this
    }

    fun from(square: Square): Square? = Square(square.file + x,square.rank + y)

    operator fun times(scale: Int): Direction = Direction(x * scale, y * scale)
    operator fun unaryMinus() = Direction(-x, y)
    operator fun not() = Direction(x, -y)

    companion object {
        fun diagonal() = Direction(1, 1)
        fun straightLine() = Direction(0, 1)
        fun initialPawnMove() = Direction(0, 2)
        fun castlingMove() = Direction(2, 0)
        fun lShaped() = Direction(1, 2)


        private val from = { direction: Direction, square: Square ->
            Square(square.file + direction.x,square.rank + direction.y)
        }.memoize()

    }

}