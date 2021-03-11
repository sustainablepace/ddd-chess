package net.sustainablepace.chess.adapter.web

import net.sustainablepace.chess.application.ApplicationService
import net.sustainablepace.chess.application.port.`in`.command.MoveInput
import net.sustainablepace.chess.application.port.`in`.command.MovePiece
import net.sustainablepace.chess.domain.event.PieceMoved
import net.sustainablepace.chess.domain.event.PieceMovedOrNot
import net.sustainablepace.chess.domain.event.PieceNotMoved
import net.sustainablepace.chess.domain.readmodel.ChessGameReadModel
import net.sustainablepace.chess.logger
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MoveController(
    val movePieceService: ApplicationService<MovePiece, PieceMovedOrNot>
) {

    val log = logger<MoveController>()

    @PostMapping("/move/{id}")
    fun move(@PathVariable id: String, @RequestBody move: MoveInput): ResponseEntity<ChessGameReadModel> =
        try {
            MovePiece(id, move).let { result ->
                result.fold({
                    movePieceService.process(it)
                }, {
                    throw IllegalArgumentException("Invalid move {$move}. ${it.message}")
                })
            }.let { event ->
                when (event) {
                    is PieceMoved -> ok().body(ChessGameReadModel(event.chessGame))
                    is PieceNotMoved -> {
                        log.info(event.reason)
                        badRequest().build()
                    }
                }
            }
        } catch (illegalArgumentException: IllegalArgumentException) {
            badRequest().build()
        } catch (ex: Exception) {
            status(INTERNAL_SERVER_ERROR).build() // TODO: this isn't necessary
        }
}
