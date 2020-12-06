package net.sustainablepace.chess.adapter.database

import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.ChessGameId
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
class ChessGameRepository: MutableMap<ChessGameId, ChessGame> {
    private val games = emptyMap<ChessGameId, ChessGame>().toMutableMap()

    override val keys: MutableSet<ChessGameId>
        get() = games.keys
    override val size: Int
        get() = games.size
    override val values: MutableCollection<ChessGame>
        get() = games.values

    override fun containsKey(key: ChessGameId): Boolean = games.containsKey(key)

    override fun containsValue(value: ChessGame): Boolean = games.containsValue(value)

    override fun get(key: ChessGameId): ChessGame? = games.get(key)

    override fun isEmpty(): Boolean = games.isEmpty()
    override val entries: MutableSet<MutableMap.MutableEntry<ChessGameId, ChessGame>>
        get() = games.entries

    override fun clear() {
        games.clear()
    }

    override fun put(key: ChessGameId, value: ChessGame): ChessGame? = games.put(key, value)

    override fun putAll(from: Map<out ChessGameId, ChessGame>) = games.putAll(from)

    override fun remove(key: ChessGameId): ChessGame? = games.remove(key)
}