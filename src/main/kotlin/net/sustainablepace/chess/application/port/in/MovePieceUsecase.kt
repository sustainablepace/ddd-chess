package net.sustainablepace.chess.application.port.`in`

import net.sustainablepace.chess.domain.Position

interface MovePieceUsecase {
    fun move(command: MoveCommand): Position
}
