package net.sustainablepace.chess.domain

data class CastlingOptions<Side>(
    val kingSide: Boolean = true,
    val queenSide: Boolean = true
) {
    fun removeKingSideOption() = copy(
        kingSide = false,
        queenSide = queenSide
    )
    fun removeQueenSideOption() = copy(
        kingSide = kingSide,
        queenSide = false
    )
}
