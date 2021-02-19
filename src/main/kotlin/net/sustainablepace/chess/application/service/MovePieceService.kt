package net.sustainablepace.chess.application.service

import net.sustainablepace.chess.adapter.database.ChessGameRepository
import net.sustainablepace.chess.adapter.database.GameRepository
import net.sustainablepace.chess.domain.ChessGameId
import net.sustainablepace.chess.domain.Move
import net.sustainablepace.chess.domain.Position
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class MovePieceService(
    val gameRepository: GameRepository,
    val moveValidator: MoveValidator
): ApplicationService {

    fun move(move: Move, id: ChessGameId): Position {


        gameRepository.get(id)?.let { chessGame ->
            if (moveValidator.isValid(move, chessGame.currentPosition)) {
                chessGame.applyMove(move)
                return chessGame.currentPosition
            } else {
                return chessGame.currentPosition
            }

        }
        throw IllegalArgumentException("Unknown game {$id}")

    }
}

@Component
class MoveValidator {

    fun isValid(move: Move, currentPosition: Position): Boolean {
        return true
    }
}
