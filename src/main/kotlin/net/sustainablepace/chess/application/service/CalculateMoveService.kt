package net.sustainablepace.chess.application.service

import net.sustainablepace.chess.application.port.`in`.CalculateMove
import net.sustainablepace.chess.application.port.out.ChessGameRepository
import net.sustainablepace.chess.domain.Move
import net.sustainablepace.chess.domain.MoveCalculated
import org.springframework.stereotype.Service

@Service
class CalculateMoveService(val chessGameRepository: ChessGameRepository): ApplicationService<CalculateMove, MoveCalculated?> {
    override fun process(intent: CalculateMove) = with(intent) {
        chessGameRepository.findById(chessGameId)?.let { chessGame ->
            val departureSquare = chessGame.position.filter { it.value[0] == chessGame.turn[0] }.keys.firstOrNull()
            val squares = ('a'..'h').flatMap { column ->
                ('1'..'8').map { column + "" + it }
            }
            val openSquares = squares - chessGame.position.keys
            val arrivalSquare = openSquares.firstOrNull()

            if(departureSquare != null && arrivalSquare != null) {
                Move("$departureSquare-$arrivalSquare").getOrNull()?.let {
                    MoveCalculated(it)
                }
            } else null
        }
    }
}