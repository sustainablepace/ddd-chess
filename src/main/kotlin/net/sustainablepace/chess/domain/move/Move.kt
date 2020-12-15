package net.sustainablepace.chess.domain.move

import net.sustainablepace.chess.domain.aggregate.chessgame.Square

sealed class MoveOrInvalidMove

class InvalidMove(val moveInput: String, val problem: String) : MoveOrInvalidMove()

data class Move(val departureSquare: Square, val arrivalSquare: Square) : MoveOrInvalidMove() {

    companion object {

        operator fun invoke(moveInput: String) =
            if (!moveInput.matches(Regex("[a-h][1-8]-[a-h][1-8]"))) {
                InvalidMove(moveInput, "Must be a string like e2-e4.")
            } else moveInput.split("-").let { (departureSquare, arrivalSquare) ->
                Pair(Square(departureSquare), Square(arrivalSquare))
            }.let { (departureSquare, arrivalSquare) ->
                if (departureSquare is Square && arrivalSquare is Square) {
                    Pair(departureSquare, arrivalSquare)
                } else null
            }?.let { (departureSquare, arrivalSquare) ->
                if (departureSquare == arrivalSquare) {
                    InvalidMove(moveInput, "Departure and arrival square must be different.")
                } else
                    Move(departureSquare, arrivalSquare)
            } ?: InvalidMove(moveInput, "Invalid move, squares must be on board.")
    }

    override fun toString(): String = "$departureSquare-$arrivalSquare"
}
