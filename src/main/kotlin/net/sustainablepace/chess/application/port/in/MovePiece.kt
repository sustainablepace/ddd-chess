package net.sustainablepace.chess.application.port.`in`

import net.sustainablepace.chess.domain.ChessGameId
import net.sustainablepace.chess.domain.Move

class MovePiece private constructor(val chessGameId: ChessGameId, val move: Move): Command {
    companion object {
        operator fun invoke(chessGameId: ChessGameId, moveAsString: String): Result<MovePiece> {
            Move(moveAsString).let { moveResult ->
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