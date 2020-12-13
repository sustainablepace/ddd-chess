package net.sustainablepace.chess.domain.move.rules

import net.sustainablepace.chess.domain.aggregate.chessgame.*

class MoveRuleSet(val moveRules: Set<MoveRule>) {
    constructor(vararg moveOptions: Set<MoveRule>) : this(moveOptions.flatMap { it }.toSet())
    constructor(vararg moveOptions: MoveRuleSet) : this(moveOptions.flatMap { it.moveRules }.toSet())

    operator fun unaryMinus(): MoveRuleSet = MoveRuleSet(moveRules.map { -it }.toSet())

    companion object {
        fun getRulesForPiece(piece: Piece): MoveRuleSet =
            when (piece) {
                is Rook -> rookMoveRules
                is Knight -> knightMoveRules
                is Bishop -> bishopMoveRules
                is Queen -> queenMoveRules
                is King -> kingMoveRules
                is Pawn -> pawnMoveRules
            }.let { moveRules ->
                when (piece.side) {
                    is White -> moveRules
                    is Black -> -moveRules
                }
            }

        private val rookMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.straightLine(),
                pieceCanTakeMultipleSteps = true,
                rotations = true
            )
        )

        private val knightMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.lShaped(),
                rotations = true
            ),
            MoveRule(
                direction = -Direction.lShaped(),
                rotations = true
            )
        )

        private val bishopMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.diagonal(),
                pieceCanTakeMultipleSteps = true,
                rotations = true
            )
        )

        private val queenMoveRules = MoveRuleSet(
            bishopMoveRules,
            rookMoveRules
        )

        private val kingMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.diagonal(),
                rotations = true
            ),
            MoveRule(
                direction = Direction.straightLine(),
                rotations = true
            ),
            MoveRule(
                direction = -Direction.castlingMove(), // queenside
                moveCondition = { departureSquare, arrivalSquare, position, _, whiteCastlingOptions, blackCastlingOptions ->
                    when (val movingPiece = position.pieceOn(departureSquare)) {
                        is WhiteKing -> departureSquare == E1 &&
                            arrivalSquare == C1 &&
                            position.pieceOn(A1) is WhiteRook &&
                            whiteCastlingOptions.queenSide &&
                            position.pieceOn(B1) is NoPiece &&
                            position.pieceOn(C1) is NoPiece &&
                            position.pieceOn(D1) is NoPiece
                        is BlackKing -> departureSquare == E8 &&
                            arrivalSquare == C8 &&
                            position.pieceOn(A8) is BlackRook &&
                            blackCastlingOptions.queenSide &&
                            position.pieceOn(B8) is NoPiece &&
                            position.pieceOn(C8) is NoPiece &&
                            position.pieceOn(D8) is NoPiece
                        else -> false
                    }
                }
            ),
            MoveRule(
                direction = Direction.castlingMove(), // kingside
                moveCondition = { departureSquare, arrivalSquare, position, enPassantSquare, whiteCastlingOptions, blackCastlingOptions ->
                    when (val movingPiece = position.pieceOn(departureSquare)) {
                        is WhiteKing -> departureSquare == E1 &&
                            arrivalSquare == G1 &&
                            position.pieceOn(H1) is WhiteRook &&
                            whiteCastlingOptions.kingSide &&
                            position.pieceOn(F1) is NoPiece &&
                            position.pieceOn(G1) is NoPiece
                        is BlackKing -> departureSquare == E8 &&
                            arrivalSquare == G8 &&
                            position.pieceOn(H8) is BlackRook &&
                            blackCastlingOptions.queenSide &&
                            position.pieceOn(F8) is NoPiece &&
                            position.pieceOn(G8) is NoPiece
                        else -> false
                    }
                }
            )
        )

        private val pawnMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.straightLine(),
                captureType = CaptureType.DISALLOWED
            ),
            MoveRule(
                direction = Direction.initialPawnMove(),
                moveCondition = { departureSquare, arrivalSquare, position, enPassantSquare, whiteCastlingOptions, blackCastlingOptions ->
                    when (val movingPiece = position.pieceOn(departureSquare)) {
                        is NoPiece -> false
                        is Piece -> when (movingPiece.side) {
                            White -> departureSquare.rank == '2' &&
                                position.pieceOn(departureSquare.upperNeighbour()!!) is NoPiece &&
                                position.pieceOn(arrivalSquare) is NoPiece
                            Black -> departureSquare.rank == '7' &&
                                position.pieceOn(departureSquare.lowerNeighbour()!!) is NoPiece &&
                                position.pieceOn(arrivalSquare) is NoPiece
                        }
                    }
                }
            ),
            MoveRule(
                direction = Direction.diagonal(),
                captureType = CaptureType.MANDATORY
            ),
            MoveRule(
                direction = -Direction.diagonal(),
                captureType = CaptureType.MANDATORY
            ),
            MoveRule(
                direction = Direction.diagonal(),
                captureType = CaptureType.MANDATORY,
                moveCondition = { departureSquare, arrivalSquare, position, enPassantSquare, whiteCastlingOptions, blackCastlingOptions ->
                    val neighbourSquare = departureSquare.rightNeighbour()
                    val movingPiece = position.pieceOn(departureSquare)
                    if (
                        enPassantSquare is Square &&
                        enPassantSquare == neighbourSquare
                    ) {
                        val neighbourPiece = position.pieceOn(neighbourSquare)
                        neighbourPiece is Pawn && movingPiece is Piece && movingPiece.side != neighbourPiece.side
                    } else false
                }
            ),
            MoveRule(
                direction = -Direction.diagonal(),
                captureType = CaptureType.MANDATORY,
                moveCondition = { departureSquare, arrivalSquare, position, enPassantSquare, whiteCastlingOptions, blackCastlingOptions ->
                    val neighbourSquare = departureSquare.leftNeighbour()
                    val movingPiece = position.pieceOn(departureSquare)
                    if (
                        enPassantSquare is Square &&
                        enPassantSquare == neighbourSquare
                    ) {
                        val neighbourPiece = position.pieceOn(neighbourSquare)
                        neighbourPiece is Pawn && movingPiece is Piece && movingPiece.side != neighbourPiece.side
                    } else false
                }
            )
        )
    }
}