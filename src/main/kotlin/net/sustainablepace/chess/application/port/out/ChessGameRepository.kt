package net.sustainablepace.chess.application.port.out

import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.ChessGameId

interface ChessGameRepository {
    fun save(chessGame: ChessGame)
    fun findById(chessGameId: ChessGameId): ChessGame?
}