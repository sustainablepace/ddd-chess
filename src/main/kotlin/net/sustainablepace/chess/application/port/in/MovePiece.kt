package net.sustainablepace.chess.application.port.`in`

import net.sustainablepace.chess.domain.ChessGameId
import net.sustainablepace.chess.domain.Move

typealias MoveString = String

class MovePiece private constructor(val chessGameId: ChessGameId, val move: Move): Command {
    companion object {
        operator fun invoke(chessGameId: ChessGameId, moveString: MoveString): Result<MovePiece> {// TODO: is Result really the best solution?
            Move(moveString).let { moveResult ->
                moveResult.onSuccess {
                    return Result.success(MovePiece(chessGameId, it))
                }.onFailure {
                    return Result.failure(it)
                }
            }
            return Result.failure(IllegalStateException(""))
        }
    }
}