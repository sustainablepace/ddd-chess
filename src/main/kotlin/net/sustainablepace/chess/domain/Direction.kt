package net.sustainablepace.chess.domain

class Direction private constructor(val x: Int, val y: Int) {
    fun rotate(n: Int): Direction = when {
        n == 1 -> Direction(y, -x)
        n > 1 -> Direction(y, -x).rotate(n - 1)
        else -> this
    }

    operator fun times(scale: Int): Direction = Direction(x * scale, y * scale)

    operator fun unaryMinus() = Direction(-x, y)
    operator fun not() = Direction(x, -y)

    companion object {
        fun diagonal() = Direction(1, 1)
        fun straightLine() = Direction(0, 1)
        fun initialPawnMove() = Direction(0, 2)
        fun lShaped() = Direction(1, 2)
    }

}