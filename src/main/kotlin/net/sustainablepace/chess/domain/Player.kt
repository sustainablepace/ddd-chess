package net.sustainablepace.chess.domain

sealed class Player {
    val computer by lazy { this is ComputerPlayer }
}

abstract class ComputerPlayer: Player() {
    open fun calculateMove(chessGame: ChessGame): PossibleMove = NoMove
}

object StupidComputerPlayer: ComputerPlayer() {
    override fun calculateMove(chessGame: ChessGame): PossibleMove {
        val departureSquare = chessGame.position.filter { it.value[0] == chessGame.turn[0] }.keys.firstOrNull()
        val squares = ('a'..'h').flatMap { column ->
            ('1'..'8').map { column + "" + it }
        }
        val openSquares = squares - chessGame.position.keys
        val arrivalSquare = openSquares.firstOrNull()

        return if(departureSquare != null && arrivalSquare != null) {
            Move("$departureSquare-$arrivalSquare")
        } else NoMove
    }
}

object HumanPlayer: Player()