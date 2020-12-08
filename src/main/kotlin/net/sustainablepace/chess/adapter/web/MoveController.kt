package net.sustainablepace.chess.adapter.web

import net.sustainablepace.chess.adapter.database.ChessGameRepositoryAdapter
import net.sustainablepace.chess.application.port.`in`.MovePiece
import net.sustainablepace.chess.application.port.`in`.MoveString
import net.sustainablepace.chess.application.service.ApplicationService
import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.PieceMoved
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MoveController(val movePieceService: ApplicationService<MovePiece, PieceMoved>) {
    @PostMapping("/move/{id}")
    fun move(@PathVariable id: String, @RequestBody move: MoveString): ResponseEntity<ChessGame> =
        try {
            MovePiece(id, move).let { result ->
                result.fold({
                    movePieceService.process(it).chessGame
                }, {
                    throw IllegalArgumentException("Invalid move {$move}")
                })
            }.let {
                ok().body(it)
            }
        } catch (illegalArgumentException: IllegalArgumentException) {
            badRequest().build()
        } catch (ex: Exception) {
            status(INTERNAL_SERVER_ERROR).build()
        }

}
