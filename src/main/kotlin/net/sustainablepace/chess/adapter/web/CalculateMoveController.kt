package net.sustainablepace.chess.adapter.web

import net.sustainablepace.chess.application.port.`in`.command.CalculateMove
import net.sustainablepace.chess.application.port.`in`.command.MoveString
import net.sustainablepace.chess.application.ApplicationService
import net.sustainablepace.chess.domain.aggregate.chessgame.ChessGameId
import net.sustainablepace.chess.domain.MoveCalculated
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CalculateMoveController(val calculateMoveService: ApplicationService<CalculateMove, MoveCalculated?>) {
    @PostMapping("/calculateMove/{id}")
    fun move(@PathVariable id: ChessGameId): ResponseEntity<MoveString> =
        calculateMoveService.process(CalculateMove(id))?.move?.run {
            ok().body("$departureSquare-$arrivalSquare")
        } ?: badRequest().build()
}
