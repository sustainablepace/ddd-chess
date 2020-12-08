package net.sustainablepace.chess.domain

typealias Square = String

sealed class PossibleMove

object NoMove: PossibleMove()
sealed class Move: PossibleMove() {
    companion object {
        operator fun invoke(moveAsString: String): Move = ValidMove(moveAsString)
    }
}

class InvalidMove(val moveAsString: String, val problem: String) : Move()

class ValidMove private constructor(val departureSquare: Square, val arrivalSquare: Square) : Move() {
    companion object {
        operator fun invoke(moveAsString: String): Move {
            if (!moveAsString.matches(Regex("[a-h][1-8]-[a-h][1-8]"))) {
                return InvalidMove(moveAsString, "Must be a string like e2-e4.")
            }
            val (departureSquare, arrivalSquare) = moveAsString.split("-")
            if (departureSquare == arrivalSquare) {
                return InvalidMove(moveAsString, "Departure and arrival square must be different.")
            }
            return ValidMove(departureSquare, arrivalSquare)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is ValidMove && departureSquare == other.departureSquare && arrivalSquare == other.arrivalSquare
    }
}
