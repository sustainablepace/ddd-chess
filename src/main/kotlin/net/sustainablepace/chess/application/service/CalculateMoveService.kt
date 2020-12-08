package net.sustainablepace.chess.application.service

import net.sustainablepace.chess.application.port.`in`.CalculateMove
import net.sustainablepace.chess.application.port.out.ChessGameRepository
import net.sustainablepace.chess.domain.ComputerPlayer
import net.sustainablepace.chess.domain.HumanPlayer
import net.sustainablepace.chess.domain.MoveCalculated
import net.sustainablepace.chess.domain.StupidComputerPlayer
import org.springframework.stereotype.Service

@Service
class CalculateMoveService(val chessGameRepository: ChessGameRepository) : ApplicationService<CalculateMove, MoveCalculated?> {
    override fun process(intent: CalculateMove) = with(intent) {
        chessGameRepository.findById(chessGameId)?.let { chessGame ->
            with(chessGame.activePlayer) {
                when(this) {
                    is ComputerPlayer -> calculateMove(chessGame)
                    is HumanPlayer -> null
                }?.let {
                    MoveCalculated(it, chessGame)
                }
            }
        }
    }
}