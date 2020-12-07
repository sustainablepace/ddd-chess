package net.sustainablepace.chess.application.service

import net.sustainablepace.chess.application.port.`in`.CalculateMove
import net.sustainablepace.chess.application.port.out.ChessGameRepository
import net.sustainablepace.chess.domain.Move
import net.sustainablepace.chess.domain.MoveCalculated
import net.sustainablepace.chess.domain.MoveCalculator
import org.springframework.stereotype.Service

@Service
class CalculateMoveService(val chessGameRepository: ChessGameRepository) : ApplicationService<CalculateMove, MoveCalculated?> {
    override fun process(intent: CalculateMove) = with(intent) {
        chessGameRepository.findById(chessGameId)?.let { chessGame ->
            MoveCalculator.calculateMove(chessGame)?.let { move ->
                MoveCalculated(move, chessGame)
            }
        }
    }
}