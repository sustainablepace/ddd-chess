package net.sustainablepace.chess.domain.move.rules

import net.sustainablepace.chess.domain.aggregate.EnPassantSquare
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import net.sustainablepace.chess.domain.move.rules.CaptureType.*

sealed class MoveRule {
    abstract fun findMoves(
        departureSquare: Square,
        movingPiece: Piece,
        position: Position,
        enPassantSquare: EnPassantSquare,
        whiteCastlingOptions: CastlingOptions,
        blackCastlingOptions: CastlingOptions
    ): Set<ValidMove>

    abstract operator fun unaryMinus(): MoveRule

    companion object {
        operator fun invoke(
            direction: Direction,
            captureType: CaptureType = ALLOWED,
            pieceCanTakeMultipleSteps: Boolean = false,
            rotations: Boolean = false,
            moveCondition: ((
                departureSquare: Square,
                arrivalSquare: Square,
                position: Position,
                enPassantSquare: EnPassantSquare,
                whiteCastlingOptions: CastlingOptions,
                blackCastlingOptions: CastlingOptions
            ) -> Boolean)? = null
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
    override fun findMoves(
        departureSquare: Square,
        movingPiece: Piece,
        position: Position,
        enPassantSquare: EnPassantSquare,
        whiteCastlingOptions: CastlingOptions,
        blackCastlingOptions: CastlingOptions
    ): Set<ValidMove> =
        when (pieceCanTakeMultipleSteps) {
            true -> mutableListOf<Square>().run {
                for (step in (1..7)) {
                    val arrivalSquare = (direction * step).from(departureSquare)
                    if (arrivalSquare is Square) add(arrivalSquare) else break
                    if (position.pieceOn(arrivalSquare) is Piece) break
                }
                this
            }.toList()
            false ->
                when (val arrivalSquare = direction.from(departureSquare)) {
                    is Square -> listOf(arrivalSquare)
                    else -> emptyList()
                }
        }.mapNotNull { arrivalSquare ->
            val blockingPiece = position.pieceOn(arrivalSquare)
            when {
                blockingPiece is NoPiece && captureType == MANDATORY -> null
                blockingPiece is Piece && (movingPiece.side == blockingPiece.side || captureType == DISALLOWED) -> null
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
    private val moveCondition: (
        departureSquare: Square,
        arrivalSquare: Square,
        position: Position,
        enPassantSquare: EnPassantSquare,
        whiteCastlingOptions: CastlingOptions,
        blackCastlingOptions: CastlingOptions
    ) -> Boolean
) : MoveRule() {
    override fun findMoves(
        departureSquare: Square,
        movingPiece: Piece,
        position: Position,
        enPassantSquare: EnPassantSquare,
        whiteCastlingOptions: CastlingOptions,
        blackCastlingOptions: CastlingOptions
    ): Set<ValidMove> =
        when (val arrivalSquare = direction.from(departureSquare)) {
            is Square -> if (moveCondition(
                    departureSquare,
                    arrivalSquare,
                    position,
                    enPassantSquare,
                    whiteCastlingOptions,
                    blackCastlingOptions
                )
            ) {
                setOf(ValidMove(departureSquare, arrivalSquare))
            } else emptySet()
            else -> emptySet()
        }

    override operator fun unaryMinus(): MoveRule = CustomizedRule(
        direction = !direction,
        moveCondition = moveCondition
    )
}
