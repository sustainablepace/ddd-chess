package net.sustainablepace.chess.adapter.web

import net.sustainablepace.chess.adapter.database.ChessGameRepository
import net.sustainablepace.chess.domain.ChessGame
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CalculateMoveController(val chessGameRepository: ChessGameRepository) {
    @PostMapping("/calculateMove/{id}")
    fun move(@PathVariable id: String): String {
        chessGameRepository.get(id)?.let { chessGame ->
            val departureSquare = chessGame.position.filter { it.value[0] == chessGame.turn[0] }.keys.firstOrNull()
            val squares = ('a'..'h').flatMap { column ->
                ('1'..'8').map { column + "" + it }
            }
            val openSquares = squares - chessGame.position.keys
            val arrivalSquare = openSquares.firstOrNull()

            if(departureSquare != null && arrivalSquare != null) {
                return "$departureSquare-$arrivalSquare"
            }
        }
        throw IllegalStateException("Cannot make a move.")
    }
}
