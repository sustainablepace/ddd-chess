package net.sustainablepace.chess.application.port.`in`

import net.sustainablepace.chess.domain.ChessGameId

data class CalculateMove(val chessGameId: ChessGameId): Command