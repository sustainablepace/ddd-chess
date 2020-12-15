package net.sustainablepace.chess.application.service

import net.sustainablepace.chess.application.ApplicationService
import net.sustainablepace.chess.application.port.`in`.command.CalculateMove
import net.sustainablepace.chess.application.port.out.ChessGameRepository
import net.sustainablepace.chess.domain.aggregate.chessgame.ComputerPlayer
import net.sustainablepace.chess.domain.aggregate.chessgame.HumanPlayer
import net.sustainablepace.chess.domain.event.MoveCalculatedOrNot
import net.sustainablepace.chess.domain.event.NoMoveCalculated
import org.springframework.stereotype.Service

@Service
class CalculateMoveService(
    private val chessGameRepository: ChessGameRepository
) : ApplicationService<CalculateMove, MoveCalculatedOrNot> {

    override fun process(intent: CalculateMove) =
        chessGameRepository.findById(intent.chessGameId)?.let { chessGame ->
            when (val player = chessGame.getActivePlayer()) {
                is ComputerPlayer -> player.calculateMove(chessGame)
                is HumanPlayer -> NoMoveCalculated(
                    reason = "Human players must calculate their own moves.",
                    chessGame = chessGame
                )
            }
        } ?: throw IllegalArgumentException("Could not find game ${intent.chessGameId}.")
}
