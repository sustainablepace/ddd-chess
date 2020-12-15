package net.sustainablepace.chess.application.service

import net.sustainablepace.chess.application.ApplicationService
import net.sustainablepace.chess.application.port.`in`.command.SetUpPieces
import net.sustainablepace.chess.application.port.out.ChessGameRepository
import net.sustainablepace.chess.domain.PiecesHaveBeenSetUp
import net.sustainablepace.chess.domain.aggregate.ChessGame
import org.springframework.stereotype.Component

@Component
class SetupPiecesService(val chessGameRepository: ChessGameRepository) :
    ApplicationService<SetUpPieces, PiecesHaveBeenSetUp> {

    override fun process(intent: SetUpPieces): PiecesHaveBeenSetUp =
        ChessGame().also {
            chessGameRepository.save(it.chessGame)
        }
}

