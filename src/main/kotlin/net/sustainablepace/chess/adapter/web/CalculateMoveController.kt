package net.sustainablepace.chess.adapter.web

import net.sustainablepace.chess.application.port.`in`.CalculateMove
import net.sustainablepace.chess.application.service.ApplicationService
import net.sustainablepace.chess.domain.MoveCalculated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CalculateMoveController(val calculateMoveService: ApplicationService<CalculateMove, MoveCalculated?>) {
    @PostMapping("/calculateMove/{id}")
    fun move(@PathVariable id: String): String = calculateMoveService.process(CalculateMove(id))?.move.toString()
}
