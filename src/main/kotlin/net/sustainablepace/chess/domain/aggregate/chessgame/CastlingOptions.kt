package net.sustainablepace.chess.domain.aggregate.chessgame

data class CastlingOptions(
    val side: Side,
    val kingSide: Boolean = true,
    val queenSide: Boolean = true
) {
    fun updateAfterPieceMoved(departureSquare: Square, piece: Piece): CastlingOptions =
        if (side != piece.side) {
            this
        } else
            if (piece is Rook && departureSquare == Square('a', side.baseLine)) {
                CastlingOptions(side = side, kingSide = kingSide, queenSide = false)
            } else if (piece is Rook && departureSquare == Square('h', side.baseLine)) {
                CastlingOptions(side = side, kingSide = false, queenSide = queenSide)
            } else if (piece is King && departureSquare == Square('e', side.baseLine)) {
                CastlingOptions(side = side, kingSide = false, queenSide = false)
            } else this
        

}