package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.CaptureType.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.White

class MoveRules(private val moveRules: Set<MoveRule>) {
    constructor(moveRule: MoveRule) : this(setOf(moveRule))
    constructor(vararg moveOptions: MoveRule) : this(moveOptions.toSet())
    constructor(vararg moveOptions: Set<MoveRule>) : this(moveOptions.flatMap { it }.toSet())
    constructor(vararg moveOptions: MoveRules) : this(moveOptions.flatMap { it.moveRules.map { it.copy() } }.toSet())

    fun findMoves(chessGame: ChessGame, departureSquare: Square, movingPiece: Piece): Set<ValidMove> =
        moveRules.flatMap { rule ->
            when (rule.pieceCanTakeMultipleSteps) {
                true -> 7
                false -> 1
            }.let { steps ->
                var pieceIsBlocking = false
                (1..steps).flatMap { step ->
                    when (pieceIsBlocking) {
                        true -> emptySet()
                        false -> if (rule.moveCondition == null) {

                            when (val arrivalSquare = departureSquare.add(rule.direction * step)) {
                                is Square -> when (val blockingPiece = chessGame.get(arrivalSquare)) {
                                    is Piece -> {
                                        pieceIsBlocking = true
                                        if (movingPiece.colour != blockingPiece.colour && rule.captureType != DISALLOWED) {
                                            setOf(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
                                        } else emptySet()
                                    }
                                    else -> if (rule.captureType != MANDATORY) {
                                        setOf(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
                                    } else emptySet()
                                }
                                else -> emptySet()
                            }
                        } else {
                            val arrivalSquare = departureSquare.add(rule.direction * step)
                            if (arrivalSquare != null && rule.moveCondition.invoke(chessGame, departureSquare, arrivalSquare, movingPiece)) {
                                setOf(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
                            } else emptySet()
                        }
                    }
                }
            }
        }.toSet()

    operator fun unaryMinus(): MoveRules = MoveRules(moveRules.map { -it }.toSet())
}


data class MoveRule private constructor(
    val direction: Direction,
    val captureType: CaptureType,
    val pieceCanTakeMultipleSteps: Boolean = false,
    val moveCondition: ((chessGame: ChessGame, departureSquare: Square, arrivalSquare: Square, piece: Piece) -> Boolean)? = null,
) {
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

operator fun MoveRule.unaryMinus(): MoveRule = copy(direction = !direction)

object PieceMoveRules {

    val rookMoveRules = MoveRules(
        MoveRule(
            direction = Direction.straightLine(),
            captureType = ALLOWED,
            pieceCanTakeMultipleSteps = true,
            rotations = true
        )
    )

    val knightMoveRules = MoveRules(
        MoveRule(
            direction = Direction.lShaped(),
            captureType = ALLOWED,
            rotations = true
        ),
        MoveRule(
            direction = -Direction.lShaped(),
            captureType = ALLOWED,
            rotations = true
        )
    )

    val bishopMoveRules = MoveRules(
        MoveRule(
            direction = Direction.diagonal(),
            captureType = ALLOWED,
            pieceCanTakeMultipleSteps = true,
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
        ),
        MoveRule(
            direction = Direction.straightLine(),
            captureType = ALLOWED,
            rotations = true
        ),
        MoveRule(
            direction = -Direction.castlingMove(), // queenside
            captureType = DISALLOWED,
            moveCondition = { chessGame, departureSquare, arrivalSquare, piece ->
                if (piece is WhiteKing) {
                    departureSquare == "e1" &&
                        arrivalSquare == "c1" &&
                            chessGame.get("a1") is WhiteRook &&
                            chessGame.whiteCastlingOptions.queenSide &&
                            chessGame.get("b1") == null &&
                            chessGame.get("c1") == null &&
                            chessGame.get("d1") == null
                } else if(piece is BlackKing) {
                    departureSquare == "e8" &&
                        arrivalSquare == "c8" &&
                        chessGame.get("a8") is BlackRook &&
                        chessGame.whiteCastlingOptions.queenSide &&
                        chessGame.get("b8") == null &&
                        chessGame.get("c8") == null &&
                        chessGame.get("d8") == null
                } else false
            }
        ),
        MoveRule(
            direction = Direction.castlingMove(),
            captureType = DISALLOWED,
            moveCondition = { chessGame, departureSquare, arrivalSquare, piece ->
                if (piece is WhiteKing) {
                    departureSquare == "e1" &&
                        arrivalSquare == "g1" &&
                        chessGame.get("h1") is WhiteRook &&
                        chessGame.whiteCastlingOptions.kingSide &&
                        chessGame.get("f1") == null &&
                        chessGame.get("g1") == null
                } else if(piece is BlackKing) {
                    departureSquare == "e8" &&
                        arrivalSquare == "g8" &&
                        chessGame.get("h8") is BlackRook &&
                        chessGame.whiteCastlingOptions.queenSide &&
                        chessGame.get("f8") == null &&
                        chessGame.get("g8") == null
                } else false
            }
        )
    )

    val pawnMoveRules = MoveRules(
        MoveRule(
            direction = Direction.straightLine(),
            captureType = DISALLOWED
        ),
        MoveRule(
            direction = Direction.initialPawnMove(),
            captureType = DISALLOWED,
            moveCondition = { chessGame, departureSquare, arrivalSquare, piece ->
                departureSquare.rank() == '2' && piece is White &&
                    chessGame.get(departureSquare.upperNeighbour()!!) == null &&
                    chessGame.get(arrivalSquare) == null ||
                    departureSquare.rank() == '7' && piece is Black &&
                    chessGame.get(departureSquare.lowerNeighbour()!!) == null &&
                    chessGame.get(arrivalSquare) == null
            }
        ),
        MoveRule(
            direction = Direction.diagonal(),
            captureType = MANDATORY
        ),
        MoveRule(
            direction = -Direction.diagonal(),
            captureType = MANDATORY
        ),
        MoveRule(
            direction = Direction.diagonal(),
            captureType = MANDATORY,
            moveCondition = { chessGame, departureSquare, arrivalSquare, piece ->
                val neighbourSquare = departureSquare.rightNeighbour()
                if (
                    chessGame.enPassant is Square &&
                    chessGame.enPassant == neighbourSquare
                ) {
                    val neighbourPiece = chessGame.get(neighbourSquare)
                    neighbourPiece is Pawn && piece.colour != neighbourPiece.colour
                } else false
            }
        ),
        MoveRule(
            direction = -Direction.diagonal(),
            captureType = MANDATORY,
            moveCondition = { chessGame, departureSquare, arrivalSquare, piece ->
                val neighbourSquare = departureSquare.leftNeighbour()
                if (
                    chessGame.enPassant is Square &&
                    chessGame.enPassant == neighbourSquare
                ) {
                    val neighbourPiece = chessGame.get(neighbourSquare)
                    neighbourPiece is Pawn && piece.colour != neighbourPiece.colour
                } else false
            }
        )
    )
}
