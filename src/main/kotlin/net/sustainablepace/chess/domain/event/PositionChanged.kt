package net.sustainablepace.chess.domain.event

import net.sustainablepace.chess.domain.aggregate.chessgame.Position
import net.sustainablepace.chess.domain.move.ValidMove

sealed class PositionChanged: Position, Event {
    abstract val position: Position
}

sealed class PieceMovedOnBoardOrNot : PositionChanged() {
    abstract val move: ValidMove
    abstract val pieceCapturedOrPawnMoved: Boolean
}

class PieceMovedOnBoard(
    override val position: Position,
    override val pieceCapturedOrPawnMoved: Boolean,
    override val move: ValidMove
) : PieceMovedOnBoardOrNot(), Position by position

class PieceNotMovedOnBoard(
    override val position: Position,
    override val pieceCapturedOrPawnMoved: Boolean,
    override val move: ValidMove
) : PieceMovedOnBoardOrNot(), Position by position

class PositionSetUp(
    override val position: Position
) : PositionChanged(), Position by position