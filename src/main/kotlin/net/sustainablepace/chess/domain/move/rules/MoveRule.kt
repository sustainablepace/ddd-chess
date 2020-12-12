package net.sustainablepace.chess.domain.move.rules

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.Piece
import net.sustainablepace.chess.domain.aggregate.chessgame.Square
import net.sustainablepace.chess.domain.move.ValidMove
import net.sustainablepace.chess.domain.move.rules.CaptureType.DISALLOWED
import net.sustainablepace.chess.domain.move.rules.CaptureType.MANDATORY

class MoveRule private constructor(
    private val direction: Direction,
    private val captureType: CaptureType,
    private val pieceCanTakeMultipleSteps: Boolean = false,
    private val moveCondition: ((chessGame: ChessGame, departureSquare: Square, arrivalSquare: Square, piece: Piece) -> Boolean)? = null,
) {
    operator fun unaryMinus(): MoveRule = MoveRule(
        direction = !direction,
        captureType = captureType,
        pieceCanTakeMultipleSteps = pieceCanTakeMultipleSteps,
        moveCondition = moveCondition
    )

    fun findMoves(chessGame: ChessGame, departureSquare: Square, movingPiece: Piece): Set<ValidMove> =
        when (pieceCanTakeMultipleSteps) {
            true -> 7
            false -> 1
        }.let { steps ->
            var pieceIsBlocking = false
            (1..steps).flatMap { step ->
                when (pieceIsBlocking) {
                    true -> emptySet()
                    false -> if (moveCondition == null) {

                        when (val arrivalSquare = (direction * step).from(departureSquare)) {
                            is Square -> when (val blockingPiece = chessGame.pieceOn(arrivalSquare)) {
                                is Piece -> {
                                    pieceIsBlocking = true
                                    if (movingPiece.side != blockingPiece.side && captureType != DISALLOWED) {
                                        setOf(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
                                    } else emptySet()
                                }
                                else -> if (captureType != MANDATORY) {
                                    setOf(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
                                } else emptySet()
                            }
                            else -> emptySet()
                        }
                    } else {
                        val arrivalSquare = (direction * step).from(departureSquare)
                        if (arrivalSquare != null && moveCondition.invoke(chessGame, departureSquare, arrivalSquare, movingPiece)) {
                            setOf(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
                        } else emptySet()
                    }
                }
            }
        }.toSet()

    companion object {
        operator fun invoke(
            direction: Direction,
            captureType: CaptureType,
            pieceCanTakeMultipleSteps: Boolean = false,
            rotations: Boolean = false,
            moveCondition: ((chessGame: ChessGame, departureSquare: Square, arrivalSquare: Square, piece: Piece) -> Boolean)? = null
        ): Set<MoveRule> = when (rotations) {
            true -> 4
            false -> 1
        }.let { numRotations ->
            (1..numRotations).map { currentRotation ->
                MoveRule(
                    direction.rotate(currentRotation % numRotations),
                    captureType,
                    pieceCanTakeMultipleSteps,
                    moveCondition
                )
            }.toSet()
        }
    }
}

enum class CaptureType {
    DISALLOWED, ALLOWED, MANDATORY
}

