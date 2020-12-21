package net.sustainablepace.chess.domain.move

import net.sustainablepace.chess.domain.aggregate.chessgame.*

sealed class MoveOrInvalidMove

class InvalidMove(
    val moveInput: String,
    val problem: String
) : MoveOrInvalidMove()

sealed class ValidMove : MoveOrInvalidMove() {
    abstract val departureSquare: Square
    abstract val arrivalSquare: Square
}

data class Move(
    override val departureSquare: Square,
    override val arrivalSquare: Square
) : ValidMove() {

    override fun toString(): String = "$departureSquare-$arrivalSquare"

    companion object {
        operator fun invoke(moveInput: String): MoveOrInvalidMove =
            if (!moveInput.matches(Regex("[a-h][1-8]-[a-h][1-8][BNRQ]{0,1}"))) {
                InvalidMove(moveInput, "Must be a string like e2-e4.")
            } else moveInput.split("-").let { (departureSquare, arrivalSquare) ->
                Pair(Square(departureSquare), Square(arrivalSquare.substring(0, 2)))
            }.let { (departureSquare, arrivalSquare) ->
                if (departureSquare is Square && arrivalSquare is Square) {
                    Pair(departureSquare, arrivalSquare)
                } else null
            }?.let { (departureSquare, arrivalSquare) ->
                if (departureSquare == arrivalSquare) {
                    InvalidMove(moveInput, "Departure and arrival square must be different.")
                } else if (moveInput.matches(Regex("[a-h][2-7]-[a-h][18][BNRQ]"))) {
                    when (val rank = arrivalSquare.rank) {
                        White.baseLine -> Black
                        Black.baseLine -> White
                        else -> throw IllegalArgumentException("Invalid promotion rank $rank")
                    }.let { side ->
                        when (val promotionPiece = PromotionPiece(moveInput.substring(5, 6), side)) {
                            is Piece -> promotionPiece
                            is NoPiece -> throw IllegalArgumentException("Unknown promotion piece $promotionPiece")
                        }
                    }.let { promotionPiece ->
                        PromotionMove(departureSquare, arrivalSquare, promotionPiece)
                    }
                } else {
                    Move(departureSquare, arrivalSquare)
                }
            } ?: InvalidMove(moveInput, "Invalid move, squares must be on board.")
    }
}

data class PromotionMove(
    override val departureSquare: Square,
    override val arrivalSquare: Square,
    val piece: Piece
) : ValidMove() {
    override fun toString(): String = "$departureSquare-$arrivalSquare ($piece)"
}
