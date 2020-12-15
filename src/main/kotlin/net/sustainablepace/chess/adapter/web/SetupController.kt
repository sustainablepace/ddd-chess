package net.sustainablepace.chess.adapter.web

import net.sustainablepace.chess.application.ApplicationService
import net.sustainablepace.chess.application.port.`in`.command.SetUpPieces
import net.sustainablepace.chess.domain.event.PiecesHaveBeenSetUp
import net.sustainablepace.chess.domain.readmodel.ChessGameReadModel
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SetupController(val setupService: ApplicationService<SetUpPieces, PiecesHaveBeenSetUp>) {
    @PostMapping("/setup")
    fun setup(): ResponseEntity<ChessGameReadModel> =
        setupService.process(SetUpPieces).let { piecesHaveBeenSetUp ->
            ok().body(ChessGameReadModel(piecesHaveBeenSetUp.chessGame))
        }
}



