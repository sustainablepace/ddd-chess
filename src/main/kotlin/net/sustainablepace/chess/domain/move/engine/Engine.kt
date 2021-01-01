package net.sustainablepace.chess.domain.move.engine

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.move.ValidMove

abstract class Engine {
    abstract fun bestMove(chessGame: ChessGame): ValidMove?
}

