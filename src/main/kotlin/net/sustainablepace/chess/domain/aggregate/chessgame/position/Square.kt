package net.sustainablepace.chess.domain.aggregate.chessgame.position

import net.sustainablepace.chess.domain.Direction

typealias Square = String
typealias File = Char
typealias Rank = Char

fun Square.add(direction: Direction): Square? =
    ((file() + direction.x) + "" + (rank() + direction.y)).let { newSquare ->
        if (newSquare.matches(Regex("[a-h][1-8]"))) {
            newSquare
        } else null
    }

fun Square.file(): File = this[0]
fun Square.rank(): Rank = this[1]