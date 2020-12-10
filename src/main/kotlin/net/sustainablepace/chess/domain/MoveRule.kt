package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.position.*

class MoveRules(val moveRules: Set<MoveRule>) {
    constructor(moveRule: MoveRule) : this(setOf(moveRule))
    constructor(vararg moveOptions: MoveRules) : this(moveOptions.flatMap { it.moveRules.map { it.copy() } }.toSet())
}

data class MoveRule(
    val direction: Direction,
    val canCapture: Boolean,
    val multiples: Boolean = false,
    val rotations: Boolean = false
)

object PieceMoveRules {

    fun getRulesForPiece(piece: Piece) = when (piece) {
        is Rook -> rookMoveRules
        is Knight -> knightMoveRules
        is Bishop -> bishopMoveRules
        is Queen -> queenMoveRules
        is King -> kingMoveRules
        else -> TODO()
    }

    private val rookMoveRules = MoveRules(
        MoveRule(
            direction = Direction.straightLine(),
            canCapture = true,
            multiples = true,
            rotations = true
        )
    )

    private val knightMoveRules = MoveRules(
        setOf(
            MoveRule(
                direction = Direction.lShaped(),
                canCapture = true,
                rotations = true
            ),
            MoveRule(
                direction = -Direction.lShaped(),
                canCapture = true,
                rotations = true
            )
        )
    )

    private val bishopMoveRules = MoveRules(
        MoveRule(
            direction = Direction.diagonal(),
            canCapture = true,
            multiples = true,
            rotations = true
        )
    )

    private val queenMoveRules = MoveRules(
        bishopMoveRules,
        rookMoveRules
    )

    private val kingMoveRules = MoveRules(
        setOf(
            MoveRule(
                direction = Direction.diagonal(),
                canCapture = true,
                rotations = true
            ),
            MoveRule(
                direction = Direction.straightLine(),
                canCapture = true,
                rotations = true
            )
        )
    )
}
