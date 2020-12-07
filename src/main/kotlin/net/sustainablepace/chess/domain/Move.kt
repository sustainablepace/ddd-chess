package net.sustainablepace.chess.domain

typealias Square = String

data class Move private constructor(val departureSquare: Square, val arrivalSquare: Square) {

    override fun toString() = "$departureSquare-$arrivalSquare"
    companion object {
        operator fun invoke(moveAsString: String): Result<Move> {
            if(!moveAsString.matches(Regex("[a-h][1-8]-[a-h][1-8]"))) {
                return Result.failure(IllegalArgumentException("Must be a string like e2-e4."))
            }
            val (departureSquare, arrivalSquare) = moveAsString.split("-")
            if (departureSquare == arrivalSquare) {
                return Result.failure(IllegalArgumentException("Departure and arrival square must be different."))
            }
            return Result.success(Move(departureSquare, arrivalSquare))
        }
    }
}
