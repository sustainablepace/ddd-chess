package net.sustainablepace.chess.domain

data class CastlingOptions(
    val kingSide: Boolean = true,
    val queenSide: Boolean = true
)