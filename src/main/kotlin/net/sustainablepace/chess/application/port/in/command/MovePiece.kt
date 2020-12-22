package net.sustainablepace.chess.application.port.`in`.command

import net.sustainablepace.chess.application.port.`in`.Command
import net.sustainablepace.chess.domain.aggregate.chessgame.ChessGameId
import net.sustainablepace.chess.domain.move.InvalidMove
import net.sustainablepace.chess.domain.move.Move
import net.sustainablepace.chess.domain.move.MoveInput
import net.sustainablepace.chess.domain.move.ValidMove

class MovePiece private constructor(
    val chessGameId: ChessGameId,
    val move: ValidMove
) : Command {

    companion object {
        operator fun invoke(chessGameId: ChessGameId, moveInput: MoveInput): Result<MovePiece> =
            when (val move = Move(moveInput)) {
                is ValidMove -> Result.success(
                    MovePiece(chessGameId, move)
                )
                is InvalidMove -> Result.failure(
                    IllegalArgumentException("Invalid move " + move.moveInput + "." + move.problem)
                )
            }
    }

}