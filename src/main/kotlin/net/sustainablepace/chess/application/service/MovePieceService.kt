package net.sustainablepace.chess.application.service

import net.sustainablepace.chess.application.port.`in`.MovePiece
import net.sustainablepace.chess.application.port.out.ChessGameRepository
import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.PieceMoved
import org.springframework.stereotype.Service

@Service
class MovePieceService(val chessGameRepository: ChessGameRepository): ApplicationService<MovePiece, PieceMoved?> {
    override fun process(intent: MovePiece): PieceMoved? = with(intent) {
        chessGameRepository.findById(chessGameId)?.let { chessGame ->
            with(move) {
                chessGame.movePiece(move)
                println(chessGame.position)
                PieceMoved(move, chessGame)
            }
        }
    }
}