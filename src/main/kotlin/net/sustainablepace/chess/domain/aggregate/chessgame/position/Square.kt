package net.sustainablepace.chess.domain.aggregate.chessgame.position

import net.sustainablepace.chess.domain.Direction

typealias Square = String
typealias File = Char
typealias Rank = Char

fun Square.add(direction: Direction): Square? =
    ((file() + direction.x) + "" + (rank() + direction.y)).validate()

fun Square.file(): File = this[0]
fun Square.rank(): Rank = this[1]

fun Square.validate() = if (matches(Regex("[a-h][1-8]"))) { this } else null

fun Square.leftNeighbour() : Square? = ((file()-1) + "" + rank()).validate()
fun Square.rightNeighbour() : Square? = ((file()+1) + "" + rank()).validate()
fun Square.upperNeighbour() : Square? = (file() + "" + (rank()+1)).validate()
fun Square.lowerNeighbour() : Square? = (file() + "" + (rank()-1)).validate()