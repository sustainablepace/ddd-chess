package net.sustainablepace.chess.application.port.`in`

import net.sustainablepace.chess.domain.ChessGameId
import net.sustainablepace.chess.domain.Move

sealed class Command {
}

data class MoveCommand(
        val gameId: ChessGameId,
        val move: Move
): Command()
