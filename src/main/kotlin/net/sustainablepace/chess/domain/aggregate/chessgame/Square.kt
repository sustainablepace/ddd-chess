package net.sustainablepace.chess.domain.aggregate.chessgame

typealias Square = String
typealias File = Char
typealias Rank = Char

fun Square.file(): File = this[0]
fun Square.rank(): Rank = this[1]

fun Square.validate() = if (matches(Regex("[a-h][1-8]"))) { this } else null

fun Square.leftNeighbour() : Square? = ((file()-1) + "" + rank()).validate()
fun Square.rightNeighbour() : Square? = ((file()+1) + "" + rank()).validate()
fun Square.upperNeighbour() : Square? = (file() + "" + (rank()+1)).validate()
fun Square.lowerNeighbour() : Square? = (file() + "" + (rank()-1)).validate()