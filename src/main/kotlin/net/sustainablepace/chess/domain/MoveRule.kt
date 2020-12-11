package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.CaptureType.*
import net.sustainablepace.chess.domain.aggregate.chessgame.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.White
import net.sustainablepace.chess.domain.aggregate.chessgame.position.*

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
                                is Square -> when (val blockingPiece = chessGame.pieceOn(arrivalSquare)) {
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
    operator fun unaryMinus(): MoveRule = copy(direction = !direction)

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
                        chessGame.pieceOn("a1") is WhiteRook &&
                        chessGame.whiteCastlingOptions.queenSide &&
                        chessGame.pieceOn("b1") is NoPiece &&
                        chessGame.pieceOn("c1") is NoPiece &&
                        chessGame.pieceOn("d1") is NoPiece
                } else if (piece is BlackKing) {
                    departureSquare == "e8" &&
                        arrivalSquare == "c8" &&
                        chessGame.pieceOn("a8") is BlackRook &&
                        chessGame.whiteCastlingOptions.queenSide &&
                        chessGame.pieceOn("b8") is NoPiece &&
                        chessGame.pieceOn("c8") is NoPiece &&
                        chessGame.pieceOn("d8") is NoPiece
                } else false
            }
        ),
        MoveRule(
            direction = Direction.castlingMove(), // kingside
            captureType = DISALLOWED,
            moveCondition = { chessGame, departureSquare, arrivalSquare, piece ->
                if (piece is WhiteKing) {
                    departureSquare == "e1" &&
                        arrivalSquare == "g1" &&
                        chessGame.pieceOn("h1") is WhiteRook &&
                        chessGame.whiteCastlingOptions.kingSide &&
                        chessGame.pieceOn("f1") is NoPiece &&
                        chessGame.pieceOn("g1") is NoPiece
                } else if (piece is BlackKing) {
                    departureSquare == "e8" &&
                        arrivalSquare == "g8" &&
                        chessGame.pieceOn("h8") is BlackRook &&
                        chessGame.whiteCastlingOptions.queenSide &&
                        chessGame.pieceOn("f8") is NoPiece &&
                        chessGame.pieceOn("g8") is NoPiece
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
                piece is White && departureSquare.rank() == '2' &&
                    chessGame.pieceOn(departureSquare.upperNeighbour()!!) is NoPiece &&
                    chessGame.pieceOn(arrivalSquare) is NoPiece
                    ||
                    piece is Black && departureSquare.rank() == '7' &&
                    chessGame.pieceOn(departureSquare.lowerNeighbour()!!) is NoPiece &&
                    chessGame.pieceOn(arrivalSquare) is NoPiece
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
            moveCondition = { chessGame, departureSquare, _, piece ->
                val neighbourSquare = departureSquare.rightNeighbour()
                if (
                    chessGame.enPassantSquare is Square &&
                    chessGame.enPassantSquare == neighbourSquare
                ) {
                    val neighbourPiece = chessGame.pieceOn(neighbourSquare)
                    neighbourPiece is Pawn && piece.colour != neighbourPiece.colour
                } else false
            }
        ),
        MoveRule(
            direction = -Direction.diagonal(),
            captureType = MANDATORY,
            moveCondition = { chessGame, departureSquare, _, piece ->
                val neighbourSquare = departureSquare.leftNeighbour()
                if (
                    chessGame.enPassantSquare is Square &&
                    chessGame.enPassantSquare == neighbourSquare
                ) {
                    val neighbourPiece = chessGame.pieceOn(neighbourSquare)
                    neighbourPiece is Pawn && piece.colour != neighbourPiece.colour
                } else false
            }
        )
    )
}
