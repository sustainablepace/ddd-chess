package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.Direction

typealias Square = String

fun Square.add(direction: Direction): Square? {
    return chunked(1).let { (file, rank) ->
        (file[0] + direction.x) + "" + (rank[0] + direction.y)
    }.let { newSquare ->
        if (newSquare.matches(Regex("[a-h][1-8]"))) {
            newSquare
        } else null
    }
}