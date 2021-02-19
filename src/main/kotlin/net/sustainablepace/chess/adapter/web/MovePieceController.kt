package net.sustainablepace.chess.adapter.web

import net.sustainablepace.chess.application.port.`in`.MovePieceUsecase
import net.sustainablepace.chess.domain.Game
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MovePieceController(val movePieceUsecase: MovePieceUsecase) {
    @PostMapping("/move/{id}")
    fun move(@PathVariable id: String, @RequestBody move: String): Game {
        val (departureSquare, arrivalSquare) = move.split("-")

        throw IllegalArgumentException("Unknown game {$id}")
    }
}
