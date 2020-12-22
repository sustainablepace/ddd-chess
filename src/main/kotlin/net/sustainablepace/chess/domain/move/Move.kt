package net.sustainablepace.chess.domain.move

import net.sustainablepace.chess.domain.aggregate.chessgame.*

sealed class MoveOrInvalidMove

typealias MoveInput = String

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
        operator fun invoke(moveInput: MoveInput): MoveOrInvalidMove =
            if (!moveInput.matches(Regex("[a-h][1-8]-[a-h][1-8][BNRQ]{0,1}"))) {
                InvalidMove(moveInput, "Must be a string like e2-e4.")
            } else {
                PromotionMove(moveInput) ?: moveInput
                    .split("-")
                    .let { (departureSquare, arrivalSquare) ->
                        Pair(Square(departureSquare), Square(arrivalSquare))
                    }.let { (departureSquare, arrivalSquare) ->
                        when {
                            departureSquare !is Square || arrivalSquare !is Square -> InvalidMove(
                                moveInput,
                                "Invalid move, squares must be on board."
                            )
                            departureSquare == arrivalSquare -> InvalidMove(
                                moveInput,
                                "Departure and arrival square must be different."
                            )
                            else -> Move(departureSquare, arrivalSquare)
                        }
                    }
            }
    }
}

data class PromotionMove(
    override val departureSquare: Square,
    override val arrivalSquare: Square,
    val piece: Piece
) : ValidMove() {
    override fun toString(): String = "$departureSquare-$arrivalSquare${piece.shortType}"

    companion object {
        operator fun invoke(moveInput: String): PromotionMove? =
            if (!moveInput.matches(Regex("[a-h][27]-[a-h][18][BNRQ]"))) {
                null
            } else moveInput.let {
                Pair(Square(moveInput.substring(0, 2)), Square(moveInput.substring(3, 5)))
            }.let { (departureSquare, arrivalSquare) ->
                if (departureSquare is Square && arrivalSquare is Square) {
                    when (arrivalSquare.rank) {
                        White.baseLine -> Black
                        Black.baseLine -> White
                        else -> null
                    }?.let { side ->
                        when (val promotionPiece = PromotionPiece(moveInput[5], side)) {
                            is Piece -> promotionPiece
                            is NoPiece -> throw IllegalArgumentException("Unknown promotion piece $promotionPiece")
                        }
                    }?.let { promotionPiece ->
                        PromotionMove(departureSquare, arrivalSquare, promotionPiece)
                    }
                } else null
            }
    }
}
