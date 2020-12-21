package net.sustainablepace.chess.application.port.`in`.command

import net.sustainablepace.chess.application.port.`in`.Command
import net.sustainablepace.chess.domain.aggregate.chessgame.ChessGameId
import net.sustainablepace.chess.domain.move.InvalidMove
import net.sustainablepace.chess.domain.move.Move
import net.sustainablepace.chess.domain.move.PromotionMove
import net.sustainablepace.chess.domain.move.ValidMove

typealias MoveString = String

class MovePiece private constructor(val chessGameId: ChessGameId, val move: ValidMove) : Command {
    companion object {
        operator fun invoke(chessGameId: ChessGameId, moveString: MoveString): Result<MovePiece> =
            Move(moveString).let { move ->
                when (move) {
                    is Move -> Result.success(MovePiece(chessGameId, move))
                    is PromotionMove -> Result.success(MovePiece(chessGameId, move))
                    is InvalidMove -> Result.failure(IllegalArgumentException("Invalid move " + move.moveInput + "." + move.problem))
                }
            }
    }
}