package net.sustainablepace.chess.domain

object MoveCalculator {
    fun calculateMove(chessGame: ChessGame): Move? {
        val departureSquare = chessGame.position.filter { it.value[0] == chessGame.turn[0] }.keys.firstOrNull()
        val squares = ('a'..'h').flatMap { column ->
            ('1'..'8').map { column + "" + it }
        }
        val openSquares = squares - chessGame.position.keys
        val arrivalSquare = openSquares.firstOrNull()

        return if(departureSquare != null && arrivalSquare != null) {
            Move("$departureSquare-$arrivalSquare").getOrNull()
        } else null
    }
}