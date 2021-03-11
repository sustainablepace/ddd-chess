@file:Suppress("ClassName")

package net.sustainablepace.chess.domain.aggregate.chessgame.position.board

import net.sustainablepace.chess.domain.aggregate.chessgame.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.Side
import net.sustainablepace.chess.domain.aggregate.chessgame.White
import kotlin.math.abs

sealed class Square(val file: File, val rank: Rank) {
    init {
        check((file + "" + rank).matches(Regex("[a-h][1-8]")))
    }

    fun colour(): Side = if (
        file in setOf('b', 'd', 'f', 'h') && rank % 2 == 0 ||
        file in setOf('a', 'c', 'e', 'g') && rank % 2 == 1
    ) Black else White

    override fun toString() = file + "" + rank

    companion object {
        operator fun invoke(square: String): Square? =
            when(square) {
                "a1" -> a1
                "a2" -> a2
                "a3" -> a3
                "a4" -> a4
                "a5" -> a5
                "a6" -> a6
                "a7" -> a7
                "a8" -> a8
                "b1" -> b1
                "b2" -> b2
                "b3" -> b3
                "b4" -> b4
                "b5" -> b5
                "b6" -> b6
                "b7" -> b7
                "b8" -> b8
                "c1" -> c1
                "c2" -> c2
                "c3" -> c3
                "c4" -> c4
                "c5" -> c5
                "c6" -> c6
                "c7" -> c7
                "c8" -> c8
                "d1" -> d1
                "d2" -> d2
                "d3" -> d3
                "d4" -> d4
                "d5" -> d5
                "d6" -> d6
                "d7" -> d7
                "d8" -> d8
                "e1" -> e1
                "e2" -> e2
                "e3" -> e3
                "e4" -> e4
                "e5" -> e5
                "e6" -> e6
                "e7" -> e7
                "e8" -> e8
                "f1" -> f1
                "f2" -> f2
                "f3" -> f3
                "f4" -> f4
                "f5" -> f5
                "f6" -> f6
                "f7" -> f7
                "f8" -> f8
                "g1" -> g1
                "g2" -> g2
                "g3" -> g3
                "g4" -> g4
                "g5" -> g5
                "g6" -> g6
                "g7" -> g7
                "g8" -> g8
                "h1" -> h1
                "h2" -> h2
                "h3" -> h3
                "h4" -> h4
                "h5" -> h5
                "h6" -> h6
                "h7" -> h7
                "h8" -> h8
                else -> null
            }

        operator fun invoke(file: File, rank: Rank): Square? = if(file - 'a' in 0..7 && rank-1 in 0..7 ) {
            charMap[file-'a'][rank-1]
        } else null
    }
}

object a1 : Square('a', 1)
object a2 : Square('a', 2)
object a3 : Square('a', 3)
object a4 : Square('a', 4)
object a5 : Square('a', 5)
object a6 : Square('a', 6)
object a7 : Square('a', 7)
object a8 : Square('a', 8)
object b1 : Square('b', 1)
object b2 : Square('b', 2)
object b3 : Square('b', 3)
object b4 : Square('b', 4)
object b5 : Square('b', 5)
object b6 : Square('b', 6)
object b7 : Square('b', 7)
object b8 : Square('b', 8)
object c1 : Square('c', 1)
object c2 : Square('c', 2)
object c3 : Square('c', 3)
object c4 : Square('c', 4)
object c5 : Square('c', 5)
object c6 : Square('c', 6)
object c7 : Square('c', 7)
object c8 : Square('c', 8)
object d1 : Square('d', 1)
object d2 : Square('d', 2)
object d3 : Square('d', 3)
object d4 : Square('d', 4)
object d5 : Square('d', 5)
object d6 : Square('d', 6)
object d7 : Square('d', 7)
object d8 : Square('d', 8)
object e1 : Square('e', 1)
object e2 : Square('e', 2)
object e3 : Square('e', 3)
object e4 : Square('e', 4)
object e5 : Square('e', 5)
object e6 : Square('e', 6)
object e7 : Square('e', 7)
object e8 : Square('e', 8)
object f1 : Square('f', 1)
object f2 : Square('f', 2)
object f3 : Square('f', 3)
object f4 : Square('f', 4)
object f5 : Square('f', 5)
object f6 : Square('f', 6)
object f7 : Square('f', 7)
object f8 : Square('f', 8)
object g1 : Square('g', 1)
object g2 : Square('g', 2)
object g3 : Square('g', 3)
object g4 : Square('g', 4)
object g5 : Square('g', 5)
object g6 : Square('g', 6)
object g7 : Square('g', 7)
object g8 : Square('g', 8)
object h1 : Square('h', 1)
object h2 : Square('h', 2)
object h3 : Square('h', 3)
object h4 : Square('h', 4)
object h5 : Square('h', 5)
object h6 : Square('h', 6)
object h7 : Square('h', 7)
object h8 : Square('h', 8)

val charMap = arrayOf(
    arrayOf(a1, a2, a3, a4, a5, a6, a7, a8),
    arrayOf(b1, b2, b3, b4, b5, b6, b7, b8),
    arrayOf(c1, c2, c3, c4, c5, c6, c7, c8),
    arrayOf(d1, d2, d3, d4, d5, d6, d7, d8),
    arrayOf(e1, e2, e3, e4, e5, e6, e7, e8),
    arrayOf(f1, f2, f3, f4, f5, f6, f7, f8),
    arrayOf(g1, g2, g3, g4, g5, g6, g7, g8),
    arrayOf(h1, h2, h3, h4, h5, h6, h7, h8)
)

typealias File = Char
typealias Rank = Int

infix fun Rank.diff(other: Rank): Int = abs(this - other)

fun Square.leftNeighbour(): Square? = Square(file - 1, rank)
fun Square.rightNeighbour(): Square? = Square(file + 1, rank)
fun Square.upperNeighbour(): Square? = Square(file, rank + 1)
fun Square.lowerNeighbour(): Square? = Square(file, rank - 1)