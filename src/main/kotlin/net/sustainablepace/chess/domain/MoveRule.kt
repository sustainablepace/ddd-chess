package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.CaptureType.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.*

class MoveRules(private val moveRules: Set<MoveRule>) {
    constructor(moveRule: MoveRule) : this(setOf(moveRule))
    constructor(vararg moveOptions: MoveRules) : this(moveOptions.flatMap { it.moveRules.map { it.copy() } }.toSet())

    fun findMoves(getPiece: (Square) -> Piece?, departureSquare: Square, pieceToBeMoved: Piece): Set<ValidMove> =
        moveRules.flatMap { rule ->
            when (rule.multiples) {
                true -> (1..7)
                false -> (1..1)
            }.let { range ->
                var abort = false
                range.flatMap { scale ->
                    if (abort) {
                        emptySet()
                    } else {
                        departureSquare.add(rule.direction * scale)?.let { arrivalSquare ->
                            getPiece(arrivalSquare)?.let { blockingPiece ->
                                abort = true
                                return@flatMap if (pieceToBeMoved.colour != blockingPiece.colour && rule.captureType != NO) {
                                    setOf(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
                                } else emptySet()
                            } ?: if(rule.captureType != MANDATORY) setOf(ValidMove("$departureSquare-$arrivalSquare") as ValidMove) else emptySet()
                        } ?: emptySet()
                    }
                }
            }
        }.toSet()

    operator fun unaryMinus(): MoveRules = MoveRules(moveRules.map { -it }.toSet())
}


data class MoveRule(
    val direction: Direction,
    val captureType: CaptureType,
    val multiples: Boolean = false
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
    NO, OPTIONAL, MANDATORY
}

operator fun MoveRule.unaryMinus(): MoveRule = MoveRule(!direction, captureType, multiples)

object PieceMoveRules {

    val rookMoveRules = MoveRules(
        MoveRule(
            direction = Direction.straightLine(),
            captureType = OPTIONAL,
            multiples = true,
            rotations = true
        )
    )

    val knightMoveRules = MoveRules(
        MoveRule(
            direction = Direction.lShaped(),
            captureType = OPTIONAL,
            rotations = true
        ).union(
            MoveRule(
                direction = -Direction.lShaped(),
                captureType = OPTIONAL,
                rotations = true
            )
        )
    )

    val bishopMoveRules = MoveRules(
        MoveRule(
            direction = Direction.diagonal(),
            captureType = OPTIONAL,
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
            captureType = OPTIONAL,
            rotations = true
        ).union(
            MoveRule(
                direction = Direction.straightLine(),
                captureType = OPTIONAL,
                rotations = true
            )
        )
    )

    val pawnMoveRules = MoveRules(
        setOf(
            MoveRule(
                direction = Direction.straightLine(),
                captureType = NO
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
