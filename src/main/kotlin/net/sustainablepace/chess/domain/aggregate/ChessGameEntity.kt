package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.event.*
import net.sustainablepace.chess.domain.move.ValidMove

interface ChessGame : Board {
    fun movePiece(move: ValidMove): PieceMovedOrNot

    val id: ChessGameId
    val position: Position
    val white: Player
    val black: Player
    val numberOfNextMove: Int
    val movesWithoutCaptureOrPawnMove: Int
    val moves: List<PositionChanged>
    val status: Status
    val activePlayer: Player
    val moveOptions: Set<ValidMove>
}

fun List<PositionChanged>.identicalPositions() =
    groupBy { it.position }.map { it.value.size }.maxOrNull() ?: 0

fun chessGame(): PiecesHaveBeenSetUp = chessGame(position())
fun chessGame(side: Side): PiecesHaveBeenSetUp = chessGame(position(turn = side))
fun chessGame(white: ComputerPlayer, black: ComputerPlayer): ChessGame =
    PiecesHaveBeenSetUp(
        ChessGameEntity(
            id = chessGameId(),
            position = position(),
            white = white,
            black = black
        )
    )

fun chessGame(position: Position): PiecesHaveBeenSetUp =
    PiecesHaveBeenSetUp(
        ChessGameEntity(
            position = position,
            white = AggressiveStupidComputerPlayer,
            black = MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer
        )
    )

class ChessGameEntity(
    override val id: ChessGameId = chessGameId(),
    override val position: Position,
    override val white: Player,
    override val black: Player,
    override val numberOfNextMove: Int = 1,
    override val movesWithoutCaptureOrPawnMove: Int = 0,
    override val moves: List<PositionChanged> = listOf(PositionSetUp(position))
) : ChessGame, MoveOptionsCalculator by position, Board by position {

    override val status: Status by lazy {
        when {
            position.isCheckMate -> Checkmate
            position.isStaleMate -> Stalemate
            position.isDeadPosition -> DeadPosition
            movesWithoutCaptureOrPawnMove >= 50 -> FiftyMoveRule
            moves.identicalPositions() >= 3 -> ThreefoldRepetition
            else -> InProgress
        }
    }

    override val activePlayer: Player by lazy {
        when (position.turn) {
            White -> white
            Black -> black
        }
    }

    override fun movePiece(move: ValidMove): PieceMovedOrNot =
        if (move !in moveOptions)
            PieceNotMoved(
                reason = "Move $move not possible in game $id.",
                chessGame = this
            )
        else {
            when (val event = position.movePiece(move)) {
                is PieceMovedOnBoard -> PieceMoved(
                    move = move,
                    chessGame = ChessGameEntity(
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
}
