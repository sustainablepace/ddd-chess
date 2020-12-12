package net.sustainablepace.chess.domain.aggregate.chessgame

data class CastlingOptions(
    val kingSide: Boolean = true,
    val queenSide: Boolean = true
) {
    fun updateAfterPieceMoved(departureSquare: Square, piece: Piece): CastlingOptions =
        when (piece.side) {
            is White -> '1'
            is Black -> '8'
        }.let { rank ->
            if (piece is Rook && departureSquare == Square('a', rank)) {
                CastlingOptions(queenSide = false, kingSide = kingSide)
            } else if (piece is Rook && departureSquare == Square('h', rank)) {
                CastlingOptions(queenSide = queenSide, kingSide = false)
            } else if (piece is King && departureSquare == Square('e', rank)) {
                CastlingOptions(queenSide = false, kingSide = false)
            } else this
        }

}