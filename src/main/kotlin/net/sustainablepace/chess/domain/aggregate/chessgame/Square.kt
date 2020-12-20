@file:Suppress("ClassName")

package net.sustainablepace.chess.domain.aggregate.chessgame

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
            if (square.matches(Regex("[a-h][1-8]"))) {
                invoke(square[0], square[1].toString().toInt())
            } else null

        operator fun invoke(file: File, rank: Rank): Square? =
            when {
                a1.file == file && a1.rank == rank -> a1
                a2.file == file && a2.rank == rank -> a2
                a3.file == file && a3.rank == rank -> a3
                a4.file == file && a4.rank == rank -> a4
                a5.file == file && a5.rank == rank -> a5
                a6.file == file && a6.rank == rank -> a6
                a7.file == file && a7.rank == rank -> a7
                a8.file == file && a8.rank == rank -> a8
                b1.file == file && b1.rank == rank -> b1
                b2.file == file && b2.rank == rank -> b2
                b3.file == file && b3.rank == rank -> b3
                b4.file == file && b4.rank == rank -> b4
                b5.file == file && b5.rank == rank -> b5
                b6.file == file && b6.rank == rank -> b6
                b7.file == file && b7.rank == rank -> b7
                b8.file == file && b8.rank == rank -> b8
                c1.file == file && c1.rank == rank -> c1
                c2.file == file && c2.rank == rank -> c2
                c3.file == file && c3.rank == rank -> c3
                c4.file == file && c4.rank == rank -> c4
                c5.file == file && c5.rank == rank -> c5
                c6.file == file && c6.rank == rank -> c6
                c7.file == file && c7.rank == rank -> c7
                c8.file == file && c8.rank == rank -> c8
                d1.file == file && d1.rank == rank -> d1
                d2.file == file && d2.rank == rank -> d2
                d3.file == file && d3.rank == rank -> d3
                d4.file == file && d4.rank == rank -> d4
                d5.file == file && d5.rank == rank -> d5
                d6.file == file && d6.rank == rank -> d6
                d7.file == file && d7.rank == rank -> d7
                d8.file == file && d8.rank == rank -> d8
                e1.file == file && e1.rank == rank -> e1
                e2.file == file && e2.rank == rank -> e2
                e3.file == file && e3.rank == rank -> e3
                e4.file == file && e4.rank == rank -> e4
                e5.file == file && e5.rank == rank -> e5
                e6.file == file && e6.rank == rank -> e6
                e7.file == file && e7.rank == rank -> e7
                e8.file == file && e8.rank == rank -> e8
                f1.file == file && f1.rank == rank -> f1
                f2.file == file && f2.rank == rank -> f2
                f3.file == file && f3.rank == rank -> f3
                f4.file == file && f4.rank == rank -> f4
                f5.file == file && f5.rank == rank -> f5
                f6.file == file && f6.rank == rank -> f6
                f7.file == file && f7.rank == rank -> f7
                f8.file == file && f8.rank == rank -> f8
                g1.file == file && g1.rank == rank -> g1
                g2.file == file && g2.rank == rank -> g2
                g3.file == file && g3.rank == rank -> g3
                g4.file == file && g4.rank == rank -> g4
                g5.file == file && g5.rank == rank -> g5
                g6.file == file && g6.rank == rank -> g6
                g7.file == file && g7.rank == rank -> g7
                g8.file == file && g8.rank == rank -> g8
                h1.file == file && h1.rank == rank -> h1
                h2.file == file && h2.rank == rank -> h2
                h3.file == file && h3.rank == rank -> h3
                h4.file == file && h4.rank == rank -> h4
                h5.file == file && h5.rank == rank -> h5
                h6.file == file && h6.rank == rank -> h6
                h7.file == file && h7.rank == rank -> h7
                h8.file == file && h8.rank == rank -> h8
                else -> null
            }
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

typealias File = Char
typealias Rank = Int

infix fun Rank.diff(other: Rank): Int = abs(this - other)

fun Square.leftNeighbour(): Square? = Square(file - 1, rank)
fun Square.rightNeighbour(): Square? = Square(file + 1, rank)
fun Square.upperNeighbour(): Square? = Square(file, rank + 1)
fun Square.lowerNeighbour(): Square? = Square(file, rank - 1)