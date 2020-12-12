package net.sustainablepace.chess.domain.aggregate.chessgame

import kotlin.random.Random.Default.nextInt

typealias ChessGameId = String

fun chessGameId(): ChessGameId =
    (('a'..'z').toSet() + ('1'..'9').toSet()).let { chars ->
        (1..7).map {
            chars.elementAt(nextInt(0, chars.size))
        }.joinToString("")
    }
