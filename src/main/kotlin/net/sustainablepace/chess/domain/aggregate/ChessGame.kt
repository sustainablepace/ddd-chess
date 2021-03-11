package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.Board
import net.sustainablepace.chess.domain.event.PieceMovedOrNot
import net.sustainablepace.chess.domain.event.PiecesHaveBeenSetUp
import net.sustainablepace.chess.domain.move.ValidMove

interface ChessGame : Board {
    fun movePiece(move: ValidMove): PieceMovedOrNot

    val id: ChessGameId
    val position: Position
    val white: Player
    val black: Player
    val numberOfNextMove: Int
    val movesWithoutCaptureOrPawnMove: Int
    val moves: List<Int>
    val status: Status
    val activePlayer: Player
    val moveOptions: Set<ValidMove>
    val identicalPositions: Int
}

fun chessGame(): PiecesHaveBeenSetUp = chessGame(position())
fun chessGame(side: Side): PiecesHaveBeenSetUp = chessGame(position(turn = side))
fun chessGame(white: ComputerPlayer, black: ComputerPlayer) =
    PiecesHaveBeenSetUp(
        ChessGameEntity(
            id = chessGameId(),
            position = position(),
            white = white,
            black = black
        )
    )

fun chessGame(position: Position) =
    PiecesHaveBeenSetUp(
        ChessGameEntity(
            position = position,
            white = HumanPlayer,
            black = MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer
        )
    )