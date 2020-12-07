package net.sustainablepace.chess.adapter.web

import net.sustainablepace.chess.adapter.database.ChessGameRepositoryAdapter
import net.sustainablepace.chess.application.port.`in`.MovePiece
import net.sustainablepace.chess.application.service.ApplicationService
import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.PieceMoved
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MoveController(val movePieceService: ApplicationService<MovePiece, PieceMoved>) {
    @PostMapping("/move/{id}")
    fun move(@PathVariable id: String, @RequestBody move: String): ChessGame {
        MovePiece(id, move).let { result ->
            result
                .onSuccess { return movePieceService.process(it).chessGame }
                .onFailure { throw IllegalArgumentException("") }
        }
        throw IllegalArgumentException("")
    }
}
