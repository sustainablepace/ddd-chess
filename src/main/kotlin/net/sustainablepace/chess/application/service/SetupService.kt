package net.sustainablepace.chess.application.service

import net.sustainablepace.chess.application.port.`in`.SetupPieces
import net.sustainablepace.chess.application.port.out.ChessGameRepository
import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.PiecesSetUp
import org.springframework.stereotype.Component

@Component
class SetupService(val chessGameRepository: ChessGameRepository): ApplicationService<SetupPieces, PiecesSetUp> {
    override fun process(intent: SetupPieces): PiecesSetUp = ChessGame().let {
        chessGameRepository.save(it)
        return PiecesSetUp(it)
    }
}

