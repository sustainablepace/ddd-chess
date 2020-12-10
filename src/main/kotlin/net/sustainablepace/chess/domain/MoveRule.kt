package net.sustainablepace.chess.domain

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
                                return@flatMap if (pieceToBeMoved.colour != blockingPiece.colour && rule.canCapture) {
                                    setOf(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
                                } else emptySet()
                            } ?: setOf(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
                        } ?: emptySet()
                    }
                }
            }
        }.toSet()

    operator fun unaryMinus() : MoveRules = MoveRules(moveRules.map { -it }.toSet())
}


data class MoveRule(
    val direction: Direction,
    val canCapture: Boolean,
    val multiples: Boolean = false
) {
    companion object {
        operator fun invoke(
            direction: Direction,
            canCapture: Boolean,
            multiples: Boolean = false,
            rotations: Boolean = false
        ): Set<MoveRule> = when (rotations) {
            true -> 4
            false -> 1
        }.let { numRotations ->
            (1..numRotations).map { currentRotation ->
                MoveRule(
                    direction.rotate(currentRotation % numRotations),
                    canCapture,
                    multiples
                )
            }.toSet()
        }
    }
}

operator fun MoveRule.unaryMinus() : MoveRule = MoveRule(-direction, canCapture, multiples)

object PieceMoveRules {

    val rookMoveRules = MoveRules(
        MoveRule(
            direction = Direction.straightLine(),
            canCapture = true,
            multiples = true,
            rotations = true
        )
    )

    val knightMoveRules = MoveRules(
        MoveRule(
            direction = Direction.lShaped(),
            canCapture = true,
            rotations = true
        ).union(
            MoveRule(
                direction = -Direction.lShaped(),
                canCapture = true,
                rotations = true
            )
        )
    )

    val bishopMoveRules = MoveRules(
        MoveRule(
            direction = Direction.diagonal(),
            canCapture = true,
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
            canCapture = true,
            rotations = true
        ).union(
            MoveRule(
                direction = Direction.straightLine(),
                canCapture = true,
                rotations = true
            )
        )
    )

    val pawnMoveRules = MoveRules(
        MoveRule(
            direction = Direction.straightLine(),
            canCapture = true
        )
    )
}
