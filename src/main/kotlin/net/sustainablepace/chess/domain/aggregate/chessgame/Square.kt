package net.sustainablepace.chess.domain.aggregate.chessgame

import kotlin.math.abs

sealed class Square(val file: File, val rank: Rank) {
    init {
        check((file + "" + rank).matches(Regex("[a-h][1-8]")))
    }

    override fun toString() = file + "" + rank

    companion object {
        operator fun invoke(square: String): Square? {
            return if(square.matches(Regex("[a-h][1-8]"))) {
                invoke(square[0], square[1])
            } else null
        }

        operator fun invoke(file: File, rank: Rank): Square? {
            return when {
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
                E1.file == file && E1.rank == rank -> E1
                E2.file == file && E2.rank == rank -> E2
                E3.file == file && E3.rank == rank -> E3
                E4.file == file && E4.rank == rank -> E4
                E5.file == file && E5.rank == rank -> E5
                E6.file == file && E6.rank == rank -> E6
                E7.file == file && E7.rank == rank -> E7
                E8.file == file && E8.rank == rank -> E8
                F1.file == file && F1.rank == rank -> F1
                F2.file == file && F2.rank == rank -> F2
                F3.file == file && F3.rank == rank -> F3
                F4.file == file && F4.rank == rank -> F4
                F5.file == file && F5.rank == rank -> F5
                F6.file == file && F6.rank == rank -> F6
                F7.file == file && F7.rank == rank -> F7
                F8.file == file && F8.rank == rank -> F8
                G1.file == file && G1.rank == rank -> G1
                G2.file == file && G2.rank == rank -> G2
                G3.file == file && G3.rank == rank -> G3
                G4.file == file && G4.rank == rank -> G4
                G5.file == file && G5.rank == rank -> G5
                G6.file == file && G6.rank == rank -> G6
                G7.file == file && G7.rank == rank -> G7
                G8.file == file && G8.rank == rank -> G8
                H1.file == file && H1.rank == rank -> H1
                H2.file == file && H2.rank == rank -> H2
                H3.file == file && H3.rank == rank -> H3
                H4.file == file && H4.rank == rank -> H4
                H5.file == file && H5.rank == rank -> H5
                H6.file == file && H6.rank == rank -> H6
                H7.file == file && H7.rank == rank -> H7
                H8.file == file && H8.rank == rank -> H8
                else -> null
            }
        }
    }
}

object a1 : Square('a', '1')
object a2 : Square('a', '2')
object a3 : Square('a', '3')
object a4 : Square('a', '4')
object a5 : Square('a', '5')
object a6 : Square('a', '6')
object a7 : Square('a', '7')
object a8 : Square('a', '8')
object b1 : Square('b', '1')
object b2 : Square('b', '2')
object b3 : Square('b', '3')
object b4 : Square('b', '4')
object b5 : Square('b', '5')
object b6 : Square('b', '6')
object b7 : Square('b', '7')
object b8 : Square('b', '8')
object c1 : Square('c', '1')
object c2 : Square('c', '2')
object c3 : Square('c', '3')
object c4 : Square('c', '4')
object c5 : Square('c', '5')
object c6 : Square('c', '6')
object c7 : Square('c', '7')
object c8 : Square('c', '8')
object d1 : Square('d', '1')
object d2 : Square('d', '2')
object d3 : Square('d', '3')
object d4 : Square('d', '4')
object d5 : Square('d', '5')
object d6 : Square('d', '6')
object d7 : Square('d', '7')
object d8 : Square('d', '8')
object E1 : Square('e', '1')
object E2 : Square('e', '2')
object E3 : Square('e', '3')
object E4 : Square('e', '4')
object E5 : Square('e', '5')
object E6 : Square('e', '6')
object E7 : Square('e', '7')
object E8 : Square('e', '8')
object F1 : Square('f', '1')
object F2 : Square('f', '2')
object F3 : Square('f', '3')
object F4 : Square('f', '4')
object F5 : Square('f', '5')
object F6 : Square('f', '6')
object F7 : Square('f', '7')
object F8 : Square('f', '8')
object G1 : Square('g', '1')
object G2 : Square('g', '2')
object G3 : Square('g', '3')
object G4 : Square('g', '4')
object G5 : Square('g', '5')
object G6 : Square('g', '6')
object G7 : Square('g', '7')
object G8 : Square('g', '8')
object H1 : Square('h', '1')
object H2 : Square('h', '2')
object H3 : Square('h', '3')
object H4 : Square('h', '4')
object H5 : Square('h', '5')
object H6 : Square('h', '6')
object H7 : Square('h', '7')
object H8 : Square('h', '8')

typealias File = Char
typealias Rank = Char

infix fun Rank.diff(other: Rank): Int = abs(this - other)

fun Square.leftNeighbour(): Square? = Square(file - 1, rank)
fun Square.rightNeighbour(): Square? = Square(file + 1, rank)
fun Square.upperNeighbour(): Square? = Square(file, rank + 1)
fun Square.lowerNeighbour(): Square? = Square(file, rank - 1)