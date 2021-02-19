package net.sustainablepace.chess.adapter.database

import net.sustainablepace.chess.domain.ChessGameId
import net.sustainablepace.chess.domain.Game
import org.springframework.stereotype.Repository

@Repository
class GameRepository(val store: MutableMap<ChessGameId, Game> = mutableMapOf()): MutableMap<ChessGameId, Game> by store