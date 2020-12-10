package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.CaptureType.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.White

class MoveRules(private val moveRules: Set<MoveRule>) {
    constructor(moveRule: MoveRule) : this(setOf(moveRule))
    constructor(vararg moveOptions: MoveRules) : this(moveOptions.flatMap { it.moveRules.map { it.copy() } }.toSet())

    fun findMoves(getPiece: (Square) -> Piece?, departureSquare: Square, movingPiece: Piece): Set<ValidMove> =
        moveRules.flatMap { rule ->
            when (rule.pieceCanTakeMultipleSteps) {
                true -> 7
                false -> 1
            }.let { steps ->
                var pieceIsBlocking = false
                (1..steps).flatMap { step ->
                    when (pieceIsBlocking) {
                        true -> emptySet()
                        false -> when (val arrivalSquare = departureSquare.add(rule.direction * step)) {
                            is Square -> when (val blockingPiece = getPiece(arrivalSquare)) {
                                is Piece -> {
                                    pieceIsBlocking = true
                                    if (rule.condition(departureSquare, movingPiece) && movingPiece.colour != blockingPiece.colour && rule.captureType != DISALLOWED) {
                                        setOf(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
                                    } else emptySet()
                                }
                                else -> if (rule.condition(departureSquare, movingPiece) && rule.captureType != MANDATORY) {
                                    setOf(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
                                } else emptySet()
                            }
                            else -> emptySet()
                        }
                    }
                }
            }
        }.toSet()

    operator fun unaryMinus(): MoveRules = MoveRules(moveRules.map { -it }.toSet())
}


data class MoveRule(
    val direction: Direction,
    val captureType: CaptureType,
    val pieceCanTakeMultipleSteps: Boolean = false,
    val condition: (square: Square, piece: Piece) -> Boolean = { _, _ -> true }
) {
    companion object {
        operator fun invoke(
            direction: Direction,
            captureType: CaptureType,
            multiples: Boolean = false,
            rotations: Boolean = false
        ): Set<MoveRule> = when (rotations) {
            true -> 4
            false -> 1
        }.let { numRotations ->
            (1..numRotations).map { currentRotation ->
                MoveRule(
                    direction.rotate(currentRotation % numRotations),
                    captureType,
                    multiples
                )
            }.toSet()
        }
    }
}

enum class CaptureType {
    DISALLOWED, ALLOWED, MANDATORY
}

operator fun MoveRule.unaryMinus(): MoveRule = copy(direction = !direction)

object PieceMoveRules {

    val rookMoveRules = MoveRules(
        MoveRule(
            direction = Direction.straightLine(),
            captureType = ALLOWED,
            multiples = true,
            rotations = true
        )
    )

    val knightMoveRules = MoveRules(
        MoveRule(
            direction = Direction.lShaped(),
            captureType = ALLOWED,
            rotations = true
        ).union(
            MoveRule(
                direction = -Direction.lShaped(),
                captureType = ALLOWED,
                rotations = true
            )
        )
    )

    val bishopMoveRules = MoveRules(
        MoveRule(
            direction = Direction.diagonal(),
            captureType = ALLOWED,
            multiples = true,
            rotations = true
        )
    )

    val queenMoveRules = MoveRules(
        bishopMoveRules,
        rookMoveRules
    )

    val kingMoveRules = MoveRules(
        MoveRule(
            direction = Direction.diagonal(),
            captureType = ALLOWED,
            rotations = true
        ).union(
            MoveRule(
                direction = Direction.straightLine(),
                captureType = ALLOWED,
                rotations = true
            )
        )
    )

    val pawnMoveRules = MoveRules(
        setOf(
            MoveRule(
                direction = Direction.straightLine(),
                captureType = DISALLOWED
            ),
            MoveRule(
                direction = Direction.initialPawnMove(),
                captureType = DISALLOWED,
                condition = { square, piece ->
                    square.rank() == '2' && piece is White ||
                    square.rank() == '7' && piece is Black
                }
            ),
            MoveRule(
                direction = Direction.diagonal(),
                captureType = MANDATORY
            ),
            MoveRule(
                direction = -Direction.diagonal(),
                captureType = MANDATORY
            )
        )
    )
}
