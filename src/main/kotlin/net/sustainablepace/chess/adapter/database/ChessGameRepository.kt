package net.sustainablepace.chess.adapter.database

import net.sustainablepace.chess.domain.ChessGame
import org.springframework.stereotype.Component

@Component
class ChessGameRepository: MutableMap<String, ChessGame> {
    private val games = emptyMap<String, ChessGame>().toMutableMap()

    override val keys: MutableSet<String>
        get() = games.keys
    override val size: Int
        get() = games.size
    override val values: MutableCollection<ChessGame>
        get() = games.values

    override fun containsKey(key: String): Boolean = games.containsKey(key)

    override fun containsValue(value: ChessGame): Boolean = games.containsValue(value)

    override fun get(key: String): ChessGame? = games.get(key)

    override fun isEmpty(): Boolean = games.isEmpty()
    override val entries: MutableSet<MutableMap.MutableEntry<String, ChessGame>>
        get() = games.entries

    override fun clear() {
        games.clear()
    }

    override fun put(key: String, value: ChessGame): ChessGame? = games.put(key, value)

    override fun putAll(from: Map<out String, ChessGame>) = games.putAll(from)

    override fun remove(key: String): ChessGame? = games.remove(key)
}