package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.Square

sealed class CalculatedMove

object NoMove : CalculatedMove()
sealed class Move : CalculatedMove() {
    companion object {
        operator fun invoke(moveInput: String) = ValidMove(moveInput)
    }
}

class InvalidMove(val moveInput: String, val problem: String) : Move()

class ValidMove private constructor(val departureSquare: Square, val arrivalSquare: Square) : Move() {
    companion object {
        private val validMovePattern = Regex("[a-h][1-8]-[a-h][1-8]")

        operator fun invoke(moveInput: String) =
            if (!moveInput.matches(validMovePattern)) {
                InvalidMove(moveInput, "Must be a string like e2-e4.")
            } else moveInput.split("-").let { (departureSquare, arrivalSquare) ->
                if (departureSquare == arrivalSquare) {
                    InvalidMove(moveInput, "Departure and arrival square must be different.")
                } else
                    ValidMove(departureSquare, arrivalSquare)
            }
    }

    override fun equals(other: Any?): Boolean =
        other is ValidMove &&
            departureSquare == other.departureSquare &&
            arrivalSquare == other.arrivalSquare

    override fun hashCode(): Int {
        var result = departureSquare.hashCode()
        result = 31 * result + arrivalSquare.hashCode()
        return result
    }
}
