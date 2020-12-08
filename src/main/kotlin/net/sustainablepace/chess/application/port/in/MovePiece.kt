package net.sustainablepace.chess.application.port.`in`

import net.sustainablepace.chess.domain.ChessGameId
import net.sustainablepace.chess.domain.InvalidMove
import net.sustainablepace.chess.domain.ValidMove

typealias MoveString = String

class MovePiece private constructor(val chessGameId: ChessGameId, val move: ValidMove) : Command {
    companion object {
        operator fun invoke(chessGameId: ChessGameId, moveString: MoveString): Result<MovePiece> =
            ValidMove(moveString).let { move ->
                when (move) {
                    is ValidMove -> Result.success(MovePiece(chessGameId, move))
                    is InvalidMove -> Result.failure(IllegalArgumentException(move.problem))
                }
            }
    }
}