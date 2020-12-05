package net.sustainablepace.chess.adapter.web

import net.sustainablepace.chess.adapter.database.ChessGameRepository
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MoveController(val chessGameRepository: ChessGameRepository) {
    @PostMapping("/move/{id}")
    fun move(@PathVariable id: String, @RequestBody move: String): String {
        chessGameRepository.get(id)?.let { chessGame ->
            val (from, to) = move.split("-")
            chessGame.position.set(to, chessGame.position.getValue(from))
            chessGame.position.remove(from)
            println(chessGame.position)
            return ""
            //return "invalid move"
            //return "checkmate"
            //return "draw"
        }
        throw IllegalArgumentException("Unknown game {$id}")
    }
}
