package net.sustainablepace.chess.adapter.web

import net.sustainablepace.chess.application.ApplicationService
import net.sustainablepace.chess.application.port.`in`.command.CalculateMove
import net.sustainablepace.chess.application.port.`in`.command.MoveString
import net.sustainablepace.chess.domain.MoveCalculated
import net.sustainablepace.chess.domain.MoveCalculatedOrNot
import net.sustainablepace.chess.domain.NoMoveCalculated
import net.sustainablepace.chess.domain.aggregate.chessgame.ChessGameId
import net.sustainablepace.chess.logger
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CalculateMoveController(val calculateMoveService: ApplicationService<CalculateMove, MoveCalculatedOrNot>) {

    val log = logger<CalculateMoveController>()

    @PostMapping("/calculateMove/{id}")
    fun move(@PathVariable id: ChessGameId): ResponseEntity<MoveString> =
        calculateMoveService.process(CalculateMove(id)).let { event ->
            when(event) {
                is MoveCalculated -> event.move.run {
                    ok().body("$departureSquare-$arrivalSquare")
                }
                is NoMoveCalculated -> event.run {
                    log.warn(reason)
                    badRequest().build()
                }
            }
        }
}
