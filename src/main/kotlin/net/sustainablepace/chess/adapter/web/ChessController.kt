package net.sustainablepace.chess.adapter.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ChessController {
    @GetMapping("/")
    fun chess(model: Model): String {
        model["title"] = "DDD Chess"
        return "chess"
    }
}