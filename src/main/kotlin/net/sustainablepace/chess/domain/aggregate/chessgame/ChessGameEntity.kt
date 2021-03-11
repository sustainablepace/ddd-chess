package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.position.Board
import net.sustainablepace.chess.domain.aggregate.chessgame.position.MoveOptionsCalculator
import net.sustainablepace.chess.domain.event.*
import net.sustainablepace.chess.domain.move.ValidMove
import kotlin.random.Random

class ChessGameEntity(
    override val id: ChessGameId = chessGameId(),
    override val position: Position,
    override val white: Player,
    override val black: Player,
    override val numberOfNextMove: Int = 1,
    override val movesWithoutCaptureOrPawnMove: Int = 0,
    override val moves: List<Int> = listOf(position.hashCode()),
    override val identicalPositions: Int = 0
) : ChessGame, MoveOptionsCalculator by position, Board by position {

    override val status: Status by lazy {
        when {
            position.isCheckMate -> Checkmate
            position.isStaleMate -> Stalemate
            position.isDeadPosition -> DeadPosition
            movesWithoutCaptureOrPawnMove >= 50 -> FiftyMoveRule
            identicalPositions >= 3 -> ThreefoldRepetition
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
                is PieceMovedOnBoard -> {
                    val eventStore = moves.toMutableList().run {
                        add(event.position.hashCode())
                        toList()
                    }
                    PieceMoved(
                        move = move,
                        chessGame = ChessGameEntity(
                            id = id,
                            position = event.position,
                            numberOfNextMove = numberOfNextMove + 1,
                            white = white,
                            black = black,
                            movesWithoutCaptureOrPawnMove = if (event.pieceCapturedOrPawnMoved) 0 else movesWithoutCaptureOrPawnMove + 1,
                            moves = eventStore,
                            identicalPositions = eventStore.count { it == eventStore.last() }
                        ))
                }

                is PieceNotMovedOnBoard -> PieceNotMoved(
                    reason = "Move $move not executed in game $id.",
                    chessGame = this
                )
            }
        }
}
typealias ChessGameId = String

fun chessGameId(): ChessGameId =
    (('a'..'z').toSet() + ('1'..'9').toSet()).let { chars ->
        (1..7).map {
            chars.elementAt(Random.nextInt(0, chars.size))
        }.joinToString("")
    }