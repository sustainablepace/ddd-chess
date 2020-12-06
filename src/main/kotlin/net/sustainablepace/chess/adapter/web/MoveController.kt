package net.sustainablepace.chess.adapter.web

import net.sustainablepace.chess.adapter.database.ChessGameRepository
import net.sustainablepace.chess.domain.ChessGame
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MoveController(val chessGameRepository: ChessGameRepository) {
    @PostMapping("/move/{id}")
    fun move(@PathVariable id: String, @RequestBody move: String): ChessGame {
        chessGameRepository.get(id)?.let { chessGame ->
            val (departureSquare, arrivalSquare) = move.split("-")
            if (departureSquare == arrivalSquare) {
                throw IllegalArgumentException("invalid move")
            }
            chessGame.position.set(arrivalSquare, chessGame.position.getValue(departureSquare))
            chessGame.position.remove(departureSquare)
            if(!chessGame.position.values.map { it[0] }.containsAll(listOf('b', 'w'))) {
                chessGame.status = "checkmate"
            }
            if(chessGame.turn == "white") {
                chessGame.turn = "black"
            } else {
                chessGame.turn = "white"
            }
            println(chessGame.position)
            return chessGame
        }
        throw IllegalArgumentException("Unknown game {$id}")
    }
}
