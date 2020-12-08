package net.sustainablepace.chess.adapter

import net.sustainablepace.chess.application.port.`in`.command.SetupPieces
import net.sustainablepace.chess.application.ApplicationService
import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.PiecesSetUp
import net.sustainablepace.chess.domain.readmodel.ChessGameReadModel
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SetupController(val setupService: ApplicationService<SetupPieces, PiecesSetUp>) {
    @PostMapping("/setup")
    fun setup(): ResponseEntity<ChessGameReadModel> =
        setupService.process(SetupPieces).chessGame.let { chessGame ->
            ok().body(ChessGameReadModel(chessGame))
        }
}



