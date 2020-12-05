package net.sustainablepace.chess.adapter

import net.sustainablepace.chess.adapter.database.ChessGameRepository
import net.sustainablepace.chess.domain.ChessGame
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SetupController(val chessGameRepository: ChessGameRepository) {
    @PostMapping("/setup")
    fun setup() : ChessGame {
        val chessGame = ChessGame()
        chessGameRepository.set(chessGame.id, chessGame)
        return chessGame
    }
}

