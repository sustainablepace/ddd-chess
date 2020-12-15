package net.sustainablepace.chess.application.service

import net.sustainablepace.chess.application.ApplicationService
import net.sustainablepace.chess.application.port.`in`.command.MovePiece
import net.sustainablepace.chess.application.port.out.ChessGameRepository
import net.sustainablepace.chess.domain.event.PieceMoved
import net.sustainablepace.chess.domain.event.PieceMovedOrNot
import net.sustainablepace.chess.domain.event.PieceNotMoved
import org.springframework.stereotype.Service

@Service
class MovePieceService(
    private val chessGameRepository: ChessGameRepository
) : ApplicationService<MovePiece, PieceMovedOrNot> {

    override fun process(intent: MovePiece): PieceMovedOrNot =
        chessGameRepository.findById(intent.chessGameId)?.let { chessGame ->
            chessGame.movePiece(intent.move).let { event ->
                when (event) {
                    is PieceMoved -> event.also { chessGameRepository.save(it.chessGame) }
                    is PieceNotMoved -> event
                }
            }
        } ?: throw IllegalArgumentException("Could not find game ${intent.chessGameId}.")
}
