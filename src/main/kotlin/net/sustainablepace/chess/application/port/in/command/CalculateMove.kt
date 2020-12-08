package net.sustainablepace.chess.application.port.`in`.command

import net.sustainablepace.chess.application.port.`in`.Command
import net.sustainablepace.chess.domain.aggregate.chessgame.ChessGameId

class CalculateMove(val chessGameId: ChessGameId): Command