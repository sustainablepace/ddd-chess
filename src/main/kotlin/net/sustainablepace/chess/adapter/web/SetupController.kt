package net.sustainablepace.chess.adapter

import net.sustainablepace.chess.application.port.`in`.SetupPieces
import net.sustainablepace.chess.application.service.ApplicationService
import net.sustainablepace.chess.application.service.SetupService
import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.PiecesSetUp
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SetupController(val setupService: ApplicationService<SetupPieces, PiecesSetUp>) {
    @PostMapping("/setup")
    fun setup() : ChessGame = setupService.process(SetupPieces).chessGame
}



