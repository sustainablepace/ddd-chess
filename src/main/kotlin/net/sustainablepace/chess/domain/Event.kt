package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.move.ValidMove

interface Event

data class PiecesHaveBeenSetUp(val chessGame: ChessGame): Event

sealed class PieceMovedOrNot: Event
class PieceMoved(val move: ValidMove, val chessGame: ChessGame): PieceMovedOrNot()
class PieceNotMoved(val reason: String): PieceMovedOrNot()

sealed class MoveCalculatedOrNot: Event
class MoveCalculated(val move: ValidMove, val chessGame: ChessGame): MoveCalculatedOrNot()
class NoMoveCalculated(val reason: String): MoveCalculatedOrNot()