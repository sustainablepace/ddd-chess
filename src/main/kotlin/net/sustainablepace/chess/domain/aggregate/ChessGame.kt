package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.event.*
import net.sustainablepace.chess.domain.move.Move

fun List<PositionChanged>.identicalPositions() =
    groupBy { it.position }.map { it.value.size }.maxOrNull() ?: 0

class ChessGame private constructor(
    override val id: ChessGameId,
    override val position: Position,
    override val white: Player,
    override val black: Player,
    override val numberOfNextMove: Int = 1,
    override val movesWithoutCaptureOrPawnMove: Int = 0,
    override val moves: List<PositionChanged> = listOf(PositionSetUp(position))
) : ChessGameEvent, MoveOptionsCalculator by position {

    override val status: Status
        get() = when {
            position.isCheckMate() -> Checkmate
            position.isStaleMate() -> Stalemate
            position.isDeadPosition() -> DeadPosition
            movesWithoutCaptureOrPawnMove >= 50 -> FiftyMoveRule
            moves.identicalPositions() >= 3 -> ThreefoldRepetition
            else -> InProgress
        }

    override fun getActivePlayer(): Player = when (position.turn) {
        White -> white
        Black -> black
    }

    override fun pieceOn(arrivalSquare: Square): PieceOrNoPiece = position.pieceOn(arrivalSquare)

    override fun movePiece(move: Move): PieceMovedOrNot =
        if (move !in moveOptions())
            PieceNotMoved(
                reason = "Move $move not possible in game $id.",
                chessGame = this
            )
        else {
            when (val event = position.movePiece(move)) {
                is PieceMovedOnBoard -> PieceMoved(
                    move = move,
                    chessGame = ChessGame(
                        id = id,
                        position = event.position,
                        numberOfNextMove = numberOfNextMove + 1,
                        white = white,
                        black = black,
                        movesWithoutCaptureOrPawnMove = if (event.pieceCapturedOrPawnMoved) 0 else movesWithoutCaptureOrPawnMove + 1,
                        moves = moves.toMutableList().run {
                            add(event)
                            toList()
                        }
                    )
                )
                is PieceNotMovedOnBoard -> PieceNotMoved(
                    reason = "Move $move not executed in game $id.",
                    chessGame = this
                )
            }
        }


    companion object {
        operator fun invoke(): PiecesHaveBeenSetUp = ChessGame(Position())
        operator fun invoke(side: Side): PiecesHaveBeenSetUp = ChessGame(Position(turn = side))
        operator fun invoke(position: Position): PiecesHaveBeenSetUp =
            PiecesHaveBeenSetUp(
                ChessGame(
                    id = chessGameId(),
                    position = position,
                    white = HumanPlayer, //StupidComputerPlayer,
                    black = StupidComputerPlayer,
                    movesWithoutCaptureOrPawnMove = 0
                )
            )

    }
}