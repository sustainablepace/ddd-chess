package net.sustainablepace.chess.domain.aggregate.chessgame

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
                A1.file == file && A1.rank == rank -> A1
                A2.file == file && A2.rank == rank -> A2
                A3.file == file && A3.rank == rank -> A3
                A4.file == file && A4.rank == rank -> A4
                A5.file == file && A5.rank == rank -> A5
                A6.file == file && A6.rank == rank -> A6
                A7.file == file && A7.rank == rank -> A7
                A8.file == file && A8.rank == rank -> A8
                B1.file == file && B1.rank == rank -> B1
                B2.file == file && B2.rank == rank -> B2
                B3.file == file && B3.rank == rank -> B3
                B4.file == file && B4.rank == rank -> B4
                B5.file == file && B5.rank == rank -> B5
                B6.file == file && B6.rank == rank -> B6
                B7.file == file && B7.rank == rank -> B7
                B8.file == file && B8.rank == rank -> B8
                C1.file == file && C1.rank == rank -> C1
                C2.file == file && C2.rank == rank -> C2
                C3.file == file && C3.rank == rank -> C3
                C4.file == file && C4.rank == rank -> C4
                C5.file == file && C5.rank == rank -> C5
                C6.file == file && C6.rank == rank -> C6
                C7.file == file && C7.rank == rank -> C7
                C8.file == file && C8.rank == rank -> C8
                D1.file == file && D1.rank == rank -> D1
                D2.file == file && D2.rank == rank -> D2
                D3.file == file && D3.rank == rank -> D3
                D4.file == file && D4.rank == rank -> D4
                D5.file == file && D5.rank == rank -> D5
                D6.file == file && D6.rank == rank -> D6
                D7.file == file && D7.rank == rank -> D7
                D8.file == file && D8.rank == rank -> D8
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

object A1 : Square('a', '1')
object A2 : Square('a', '2')
object A3 : Square('a', '3')
object A4 : Square('a', '4')
object A5 : Square('a', '5')
object A6 : Square('a', '6')
object A7 : Square('a', '7')
object A8 : Square('a', '8')
object B1 : Square('b', '1')
object B2 : Square('b', '2')
object B3 : Square('b', '3')
object B4 : Square('b', '4')
object B5 : Square('b', '5')
object B6 : Square('b', '6')
object B7 : Square('b', '7')
object B8 : Square('b', '8')
object C1 : Square('c', '1')
object C2 : Square('c', '2')
object C3 : Square('c', '3')
object C4 : Square('c', '4')
object C5 : Square('c', '5')
object C6 : Square('c', '6')
object C7 : Square('c', '7')
object C8 : Square('c', '8')
object D1 : Square('d', '1')
object D2 : Square('d', '2')
object D3 : Square('d', '3')
object D4 : Square('d', '4')
object D5 : Square('d', '5')
object D6 : Square('d', '6')
object D7 : Square('d', '7')
object D8 : Square('d', '8')
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

fun Square.leftNeighbour(): Square? = Square(file - 1, rank)
fun Square.rightNeighbour(): Square? = Square(file + 1, rank)
fun Square.upperNeighbour(): Square? = Square(file, rank + 1)
fun Square.lowerNeighbour(): Square? = Square(file, rank - 1)