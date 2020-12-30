package net.sustainablepace.chess.domain.event

import net.sustainablepace.chess.domain.aggregate.ChessGame

data class PiecesHaveBeenSetUp(
    val chessGame: ChessGame
) : ChessGame by chessGame, Event