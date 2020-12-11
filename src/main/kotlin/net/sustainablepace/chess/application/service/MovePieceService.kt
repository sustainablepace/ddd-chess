package net.sustainablepace.chess.application.service

import net.sustainablepace.chess.application.ApplicationService
import net.sustainablepace.chess.application.port.`in`.command.MovePiece
import net.sustainablepace.chess.application.port.out.ChessGameRepository
import net.sustainablepace.chess.domain.PieceMoved
import net.sustainablepace.chess.domain.PieceMovedOrNot
import net.sustainablepace.chess.domain.PieceNotMoved
import org.springframework.stereotype.Service

@Service
class MovePieceService(val chessGameRepository: ChessGameRepository) : ApplicationService<MovePiece, PieceMovedOrNot> {
    override fun process(intent: MovePiece): PieceMovedOrNot = with(intent) {
        chessGameRepository.findById(chessGameId)?.let { chessGame ->
            chessGame.movePiece(move).let { updatedChessGame ->
                if(updatedChessGame.numberOfNextMove > chessGame.numberOfNextMove) {
                    chessGameRepository.save(updatedChessGame)
                    PieceMoved(move, updatedChessGame)
                } else {
                    PieceNotMoved("Move ${move.departureSquare}-${move.arrivalSquare} not possible in game $chessGameId.")
                }
            }
        } ?: PieceNotMoved("Could not find game $chessGameId.")
    }
}
