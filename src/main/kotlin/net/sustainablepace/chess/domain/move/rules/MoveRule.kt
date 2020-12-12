package net.sustainablepace.chess.domain.move.rules

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.NoPiece
import net.sustainablepace.chess.domain.aggregate.chessgame.Piece
import net.sustainablepace.chess.domain.aggregate.chessgame.Square
import net.sustainablepace.chess.domain.move.ValidMove
import net.sustainablepace.chess.domain.move.rules.CaptureType.*

sealed class MoveRule {
    abstract fun findMoves(chessGame: ChessGame, departureSquare: Square): Set<ValidMove>

    abstract operator fun unaryMinus(): MoveRule

    companion object {
        operator fun invoke(
            direction: Direction,
            captureType: CaptureType = ALLOWED,
            pieceCanTakeMultipleSteps: Boolean = false,
            rotations: Boolean = false,
            moveCondition: ((chessGame: ChessGame, departureSquare: Square, arrivalSquare: Square) -> Boolean)? = null
        ): Set<MoveRule> =
            when (moveCondition) {
                null -> when (rotations) {
                    true -> (1..4).map { direction.rotate(it % 4) }
                    false -> listOf(direction)
                }.map { currentDirection ->
                    ParameterizedRule(currentDirection, captureType, pieceCanTakeMultipleSteps)
                }.toSet()
                else -> setOf(CustomizedRule(direction, moveCondition))
            }
    }
}

enum class CaptureType {
    DISALLOWED, ALLOWED, MANDATORY
}

class ParameterizedRule(
    private val direction: Direction,
    private val captureType: CaptureType,
    private val pieceCanTakeMultipleSteps: Boolean = false
) : MoveRule() {
    override fun findMoves(chessGame: ChessGame, departureSquare: Square): Set<ValidMove> =
        when (pieceCanTakeMultipleSteps) {
            true -> mutableListOf<Square>().run {
                for (step in (1..7)) {
                    val arrivalSquare = (direction * step).from(departureSquare)
                    if (arrivalSquare is Square) add(arrivalSquare) else break
                    if (chessGame.pieceOn(arrivalSquare) is Piece) break
                }
                this
            }.toList()
            false ->
                when (val arrivalSquare = direction.from(departureSquare)) {
                    is Square -> listOf(arrivalSquare)
                    else -> emptyList()
                }
        }.mapNotNull { arrivalSquare ->
            val blockingPiece = chessGame.pieceOn(arrivalSquare)
            when {
                blockingPiece is NoPiece && captureType == MANDATORY -> null
                blockingPiece is Piece && (chessGame.turn == blockingPiece.side || captureType == DISALLOWED) -> null
                else -> ValidMove(departureSquare, arrivalSquare)
            }
        }.toSet()

    override operator fun unaryMinus(): MoveRule = ParameterizedRule(
        direction = !direction,
        captureType = captureType,
        pieceCanTakeMultipleSteps = pieceCanTakeMultipleSteps
    )
}

class CustomizedRule(
    private val direction: Direction,
    private val moveCondition: (chessGame: ChessGame, departureSquare: Square, arrivalSquare: Square) -> Boolean
) : MoveRule() {
    override fun findMoves(chessGame: ChessGame, departureSquare: Square): Set<ValidMove> =
        when (val arrivalSquare = direction.from(departureSquare)) {
            is Square -> if (moveCondition(chessGame, departureSquare, arrivalSquare)) {
                setOf(ValidMove(departureSquare, arrivalSquare))
            } else emptySet()
            else -> emptySet()
        }

    override operator fun unaryMinus(): MoveRule = CustomizedRule(
        direction = !direction,
        moveCondition = moveCondition
    )
}
