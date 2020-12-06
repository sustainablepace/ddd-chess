package net.sustainablepace.chess.domain

typealias ChessGameId = String

fun chessGameId(): ChessGameId {
    val charPool = ('a'..'z').toList() + ('1'..'9').toList()
    return (1..7)
        .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("");
}