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
                moveCondition = { departureSquare, arrivalSquare, position ->
                    when (position.pieceOn(departureSquare)) {
                        is WhiteKing -> departureSquare == e1 &&
                            arrivalSquare == c1 &&
                            position.pieceOn(a1) is WhiteRook &&
                            position.whiteCastlingOptions.queenSide &&
                            position.pieceOn(b1) is NoPiece &&
                            position.pieceOn(c1) is NoPiece &&
                            position.pieceOn(d1) is NoPiece &&
                            !position.isInCheck(White) &&
                            !position.isSquareThreatenedBy(b1, Black) &&
                            !position.isSquareThreatenedBy(c1, Black) &&
                            !position.isSquareThreatenedBy(d1, Black)
                        is BlackKing -> departureSquare == e8 &&
                            arrivalSquare == c8 &&
                            position.pieceOn(a8) is BlackRook &&
                            position.blackCastlingOptions.queenSide &&
                            position.pieceOn(b8) is NoPiece &&
                            position.pieceOn(c8) is NoPiece &&
                            position.pieceOn(d8) is NoPiece &&
                            !position.isInCheck(Black) &&
                            !position.isSquareThreatenedBy(b8, White) &&
                            !position.isSquareThreatenedBy(c8, White) &&
                            !position.isSquareThreatenedBy(d8, White)
                        else -> false
                    }
                }
            ),
            MoveRule(
                direction = Direction.castlingMove(), // kingside
                moveCondition = { departureSquare, arrivalSquare, position ->
                    when (position.pieceOn(departureSquare)) {
                        is WhiteKing -> departureSquare == e1 &&
                            arrivalSquare == g1 &&
                            position.pieceOn(h1) is WhiteRook &&
                            position.whiteCastlingOptions.kingSide &&
                            position.pieceOn(f1) is NoPiece &&
                            position.pieceOn(g1) is NoPiece &&
                            !position.isInCheck(White) &&
                            !position.isSquareThreatenedBy(f1, Black) &&
                            !position.isSquareThreatenedBy(g1, Black)
                        is BlackKing -> departureSquare == e8 &&
                            arrivalSquare == g8 &&
                            position.pieceOn(h8) is BlackRook &&
                            position.blackCastlingOptions.kingSide &&
                            position.pieceOn(f8) is NoPiece &&
                            position.pieceOn(g8) is NoPiece &&
                            !position.isInCheck(Black)&&
                            !position.isSquareThreatenedBy(f8, White) &&
                            !position.isSquareThreatenedBy(g8, White)
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
                moveCondition = { departureSquare, arrivalSquare, position ->
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
                moveCondition = { departureSquare, _, position ->
                    val neighbourSquare = departureSquare.rightNeighbour()
                    val movingPiece = position.pieceOn(departureSquare)
                    if (
                        position.enPassantSquare is Square &&
                        position.enPassantSquare == neighbourSquare
                    ) {
                        val neighbourPiece = position.pieceOn(neighbourSquare)
                        neighbourPiece is Pawn && movingPiece is Pawn && movingPiece.side != neighbourPiece.side
                    } else false
                }
            ),
            MoveRule(
                direction = -Direction.diagonal(),
                captureType = CaptureType.MANDATORY,
                moveCondition = { departureSquare, _, position ->
                    val neighbourSquare = departureSquare.leftNeighbour()
                    val movingPiece = position.pieceOn(departureSquare)
                    if (
                        position.enPassantSquare is Square &&
                        position.enPassantSquare == neighbourSquare
                    ) {
                        val neighbourPiece = position.pieceOn(neighbourSquare)
                        neighbourPiece is Pawn && movingPiece is Pawn && movingPiece.side != neighbourPiece.side
                    } else false
                }
            )
        )
    }
}