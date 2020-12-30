package net.sustainablepace.chess.domain.move.rules

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import net.sustainablepace.chess.domain.move.PromotionMove
import net.sustainablepace.chess.domain.move.ValidMove
import net.sustainablepace.chess.domain.move.rules.MoveRule.CaptureType.DISALLOWED
import net.sustainablepace.chess.domain.move.rules.MoveRule.CaptureType.MANDATORY

typealias MoveCondition = (
    departureSquare: Square,
    arrivalSquare: Square,
    position: Position
) -> Boolean

sealed class MoveRule {

    abstract val direction: Direction
    abstract val captureType: CaptureType

    abstract fun findMoves(
        departureSquare: Square,
        position: Position,
    ): Set<ValidMove>

    abstract fun isThreatened(threatenedSquare: Square, position: Position, departureSquare: Square): Boolean

    abstract operator fun unaryMinus(): MoveRule

    companion object {
        operator fun invoke(
            direction: Direction,
            captureType: CaptureType,
            pieceCanTakeMultipleSteps: Boolean = false,
            rotations: Boolean = false,
            moveCondition: MoveCondition? = null
        ): Set<MoveRule> =
            when (moveCondition) {
                is MoveCondition -> setOf(CustomizedRule(direction, captureType, moveCondition))
                else -> when (rotations) {
                    true -> (1..4).map { direction.rotate(it % 4) }
                    false -> listOf(direction)
                }.map { currentDirection ->
                    ParameterizedRule(currentDirection, captureType, pieceCanTakeMultipleSteps)
                }.toSet()
            }
    }

    enum class CaptureType {
        DISALLOWED, ALLOWED, MANDATORY
    }

    class ParameterizedRule(
        override val direction: Direction,
        override val captureType: CaptureType,
        private val pieceCanTakeMultipleSteps: Boolean = false
    ) : MoveRule() {
        override fun findMoves(
            departureSquare: Square,
            position: Position
        ): Set<ValidMove> =
            when (pieceCanTakeMultipleSteps) {
                true -> mutableSetOf<ValidMove>().run {
                    for (step in (White.baseLine until Black.baseLine)) { // Move must not exceed board dimensions
                        val arrivalSquare = (direction * step).from(departureSquare)
                        if (arrivalSquare is Square) {
                            addAll(checkMove(position, departureSquare, arrivalSquare))
                        } else break
                        if (position.pieceOn(arrivalSquare) is Piece) break
                    }
                    this
                }
                false ->
                    when (val arrivalSquare = direction.from(departureSquare)) {
                        is Square -> checkMove(position, departureSquare, arrivalSquare)
                        else -> emptySet()
                    }
            }

        override fun isThreatened(threatenedSquare: Square, position: Position, departureSquare: Square): Boolean {
            when (pieceCanTakeMultipleSteps) {
                true -> mutableSetOf<ValidMove>().run {
                    for (step in (White.baseLine until Black.baseLine)) { // Move must not exceed board dimensions
                        val arrivalSquare = (direction * step).from(departureSquare)
                        if (arrivalSquare is Square) {
                            val moves = checkMove(position, departureSquare, arrivalSquare)
                            if(moves.find { it.arrivalSquare == threatenedSquare } != null) {
                                return true
                            }
                        } else break
                        if (position.pieceOn(arrivalSquare) is Piece) break
                    }
                    return false
                }
                false ->
                    return when (val arrivalSquare = direction.from(departureSquare)) {
                        is Square -> checkMove(position, departureSquare, arrivalSquare).find { it.arrivalSquare == threatenedSquare } != null
                        else -> false
                    }
            }
        }

        private fun checkMove(position: Position, departureSquare: Square, arrivalSquare: Square): Set<ValidMove> {
            val movingPiece = position.pieceOn(departureSquare)
            val blockingPiece = position.pieceOn(arrivalSquare)
            return when {
                blockingPiece is NoPiece && captureType == MANDATORY -> emptySet()
                movingPiece is Piece && blockingPiece is Piece &&
                    (movingPiece.side == blockingPiece.side || captureType == DISALLOWED) -> emptySet()
                movingPiece is WhitePawn && arrivalSquare.rank == 8 -> setOf(
                    PromotionMove(departureSquare, arrivalSquare, WhiteQueen),
                    PromotionMove(departureSquare, arrivalSquare, WhiteRook),
                    PromotionMove(departureSquare, arrivalSquare, WhiteBishop),
                    PromotionMove(departureSquare, arrivalSquare, WhiteKnight)
                )
                movingPiece is BlackPawn && arrivalSquare.rank == 1 -> setOf(
                    PromotionMove(departureSquare, arrivalSquare, BlackQueen),
                    PromotionMove(departureSquare, arrivalSquare, BlackRook),
                    PromotionMove(departureSquare, arrivalSquare, BlackBishop),
                    PromotionMove(departureSquare, arrivalSquare, BlackKnight)
                )
                else -> setOf(Move(departureSquare, arrivalSquare))
            }
        }

        override operator fun unaryMinus(): MoveRule = ParameterizedRule(
            direction = !direction,
            captureType = captureType,
            pieceCanTakeMultipleSteps = pieceCanTakeMultipleSteps
        )
    }

    class CustomizedRule(
        override val direction: Direction,
        override val captureType: CaptureType,
        private val moveCondition: MoveCondition
    ) : MoveRule() {
        override fun findMoves(
            departureSquare: Square,
            position: Position
        ): Set<ValidMove> =
            when (val arrivalSquare = direction.from(departureSquare)) {
                is Square -> if (moveCondition(
                        departureSquare,
                        arrivalSquare,
                        position
                    )
                ) {
                    setOf(Move(departureSquare, arrivalSquare))
                } else emptySet()
                else -> emptySet()
            }

        override fun isThreatened(threatenedSquare: Square, position: Position, departureSquare: Square): Boolean {
            findMoves(departureSquare, position).forEach { if(it.arrivalSquare == threatenedSquare) {
                return true
            } }
            return false
        }

        override operator fun unaryMinus(): MoveRule = CustomizedRule(
            direction = !direction,
            captureType = captureType,
            moveCondition = moveCondition
        )
    }
}
