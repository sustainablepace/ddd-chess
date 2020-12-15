package net.sustainablepace.chess.application.service

import net.sustainablepace.chess.application.ApplicationService
import net.sustainablepace.chess.application.port.`in`.command.CalculateMove
import net.sustainablepace.chess.application.port.out.ChessGameRepository
import net.sustainablepace.chess.domain.MoveCalculated
import net.sustainablepace.chess.domain.MoveCalculatedOrNot
import net.sustainablepace.chess.domain.NoMoveCalculated
import net.sustainablepace.chess.domain.aggregate.chessgame.ComputerPlayer
import net.sustainablepace.chess.domain.aggregate.chessgame.HumanPlayer
import net.sustainablepace.chess.domain.move.ValidMove
import org.springframework.stereotype.Service

@Service
class CalculateMoveService(val chessGameRepository: ChessGameRepository) : ApplicationService<CalculateMove, MoveCalculatedOrNot> {
    override fun process(intent: CalculateMove) = with(intent) {
        chessGameRepository.findById(chessGameId)?.let { chessGame ->
            when(val player = chessGame.getActivePlayer()) {
                is ComputerPlayer -> when (val move = player.calculateMove(chessGame)) {
                    is ValidMove -> MoveCalculated(move, chessGame)
                    else -> NoMoveCalculated("No move possible in game $chessGameId.")
                }
                is HumanPlayer -> NoMoveCalculated("No move calculated, it's a human's turn!")
            }
        } ?: throw IllegalArgumentException("Could not find game $chessGameId.")
    }
}