package net.sustainablepace.chess.application.service

import net.sustainablepace.chess.application.ApplicationService
import net.sustainablepace.chess.application.port.`in`.command.CalculateMove
import net.sustainablepace.chess.application.port.out.ChessGameRepository
import net.sustainablepace.chess.domain.*
import net.sustainablepace.chess.domain.aggregate.chessgame.ComputerPlayer
import net.sustainablepace.chess.domain.aggregate.chessgame.HumanPlayer
import org.springframework.stereotype.Service

@Service
class CalculateMoveService(val chessGameRepository: ChessGameRepository) : ApplicationService<CalculateMove, MoveCalculatedOrNot> {
    override fun process(intent: CalculateMove) = with(intent) {
        chessGameRepository.findById(chessGameId)?.let { chessGame ->
            with(chessGame.activePlayer) {
                when (this) {
                    is ComputerPlayer -> calculateMove(chessGame).let { move ->
                        when (move) {
                            is ValidMove -> MoveCalculated(move, chessGame)
                            else -> NoMoveCalculated("No move possible in game $chessGameId.")
                        }
                    }
                    is HumanPlayer -> NoMoveCalculated("No move calculated, it's a human's turn!")
                }
            }
        } ?: NoMoveCalculated("Could not find game $chessGameId.")
    }
}