package net.sustainablepace.chess.domain.move.rules

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove

class MoveRuleSet(val moveRules: Set<MoveRule>) {
    constructor(vararg moveOptions: Set<MoveRule>) : this(moveOptions.flatMap { it }.toSet())
    constructor(vararg moveOptions: MoveRuleSet) : this(moveOptions.flatMap { it.moveRules }.toSet())

    private operator fun unaryMinus(): MoveRuleSet = MoveRuleSet(moveRules.map { -it }.toSet())

    companion object {
        fun findMoves(
            pieceToBeMoved: Piece,
            departureSquare: Square,
            position: Position,
        ): Set<ValidMove> =
            (rules[pieceToBeMoved]?.moveRules ?: emptySet()).flatMap { rule ->
                rule.findMoves(pieceToBeMoved, departureSquare, position)
            }.toSet()

        fun isSquareThreatenedBy(threatenedSquare: Square, side: Side, position: Position): Boolean {
            when (side) {
                White -> position.whitePieces
                Black -> position.blackPieces
            }.forEach { (square, pieceToBeMoved) ->
                val diff = square diff threatenedSquare
                val isPotentiallyThreatened = when(pieceToBeMoved) { // TODO maybe move these to the individual rules?
                    is Pawn -> diff.x == 1 && diff.y == 1
                    is Knight -> diff.x == 1 && diff.y == 2 || diff.x == 2 && diff.y == 1
                    is Rook -> diff.x == 0 || diff.y == 0
                    is Bishop -> diff.x == diff.y
                    is Queen -> diff.x == 0 || diff.y == 0 || diff.x == diff.y
                    is King -> diff.x == 1 || diff.y == 1
                }
                if(isPotentiallyThreatened) {
                    capturingRules[pieceToBeMoved]
                        ?.moveRules
                        ?.any { it.isThreatened(pieceToBeMoved, threatenedSquare, position, square) }
                        ?.also { isThreatened ->
                            if (isThreatened) {
                                return true
                            }
                        }
                }

            }
            return false
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
            // TODO Unify both castling rules (?)
            MoveRule(
                direction = -Direction.castlingMove(), // queenside
                captureType = MoveRule.CaptureType.DISALLOWED,
                moveCondition = { pieceToBeMoved, departureSquare, arrivalSquare, position ->
                    with(position) {
                        when (pieceToBeMoved) {
                            is WhiteKing -> departureSquare == e1 &&
                                arrivalSquare == c1 &&
                                pieceOn(a1) is WhiteRook &&
                                whiteCastlingOptions.queenSide &&
                                pieceOn(b1) is NoPiece &&
                                pieceOn(c1) is NoPiece &&
                                pieceOn(d1) is NoPiece &&
                                !isSquareThreatenedBy(e1, Black, this) &&
                                !isSquareThreatenedBy(b1, Black, this) &&
                                !isSquareThreatenedBy(c1, Black, this) &&
                                !isSquareThreatenedBy(d1, Black, this)
                            is BlackKing ->
                                blackCastlingOptions.queenSide &&
                                    departureSquare == e8 && !isSquareThreatenedBy(e8, White, this) &&
                                    pieceOn(d8) is NoPiece && !isSquareThreatenedBy(d8, White, this) &&
                                    arrivalSquare == c8 &&
                                    pieceOn(c8) is NoPiece && !isSquareThreatenedBy(c8, White, this) &&
                                    pieceOn(b8) is NoPiece && !isSquareThreatenedBy(b8, White, this) &&
                                    pieceOn(a8) is BlackRook
                            else -> false
                        }
                    }

                }
            ),
            MoveRule(
                direction = Direction.castlingMove(), // kingside
                captureType = MoveRule.CaptureType.DISALLOWED,
                moveCondition = { pieceToBeMoved, departureSquare, arrivalSquare, position ->
                    when (pieceToBeMoved) {
                        is WhiteKing -> departureSquare == e1 &&
                            arrivalSquare == g1 &&
                            position.pieceOn(h1) is WhiteRook &&
                            position.whiteCastlingOptions.kingSide &&
                            position.pieceOn(f1) is NoPiece &&
                            position.pieceOn(g1) is NoPiece &&
                            !isSquareThreatenedBy(e1, Black, position) &&
                            !isSquareThreatenedBy(f1, Black, position) &&
                            !isSquareThreatenedBy(g1, Black, position)
                        is BlackKing -> departureSquare == e8 &&
                            arrivalSquare == g8 &&
                            position.pieceOn(h8) is BlackRook &&
                            position.blackCastlingOptions.kingSide &&
                            position.pieceOn(f8) is NoPiece &&
                            position.pieceOn(g8) is NoPiece &&
                            !isSquareThreatenedBy(e8, White, position) &&
                            !isSquareThreatenedBy(f8, White, position) &&
                            !isSquareThreatenedBy(g8, White, position)
                        else -> false
                    }
                }
            )
        )

        private val pawnMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.straightLine(),
                captureType = MoveRule.CaptureType.DISALLOWED
            ),
            MoveRule(
                direction = Direction.initialPawnMove(),
                captureType = MoveRule.CaptureType.DISALLOWED,
                moveCondition = { pieceToBeMoved, departureSquare, arrivalSquare, position ->
                    when (pieceToBeMoved.side) {
                        White -> departureSquare.rank == 2 &&
                            position.pieceOn(departureSquare.upperNeighbour()!!) is NoPiece &&
                            position.pieceOn(arrivalSquare) is NoPiece
                        Black -> departureSquare.rank == 7 &&
                            position.pieceOn(departureSquare.lowerNeighbour()!!) is NoPiece &&
                            position.pieceOn(arrivalSquare) is NoPiece

                    }
                }
            ),
            MoveRule(
                direction = Direction.diagonal(),
                captureType = MoveRule.CaptureType.MANDATORY
            ),
            MoveRule(
                direction = -Direction.diagonal(),
                captureType = MoveRule.CaptureType.MANDATORY
            ),
            MoveRule(
                direction = Direction.diagonal(),
                captureType = MoveRule.CaptureType.MANDATORY,
                moveCondition = { pieceToBeMoved, departureSquare, _, position ->
                    if (position.enPassantSquare == null) {
                        false
                    } else {
                        val neighbourSquare = departureSquare.rightNeighbour()
                        if (

                            position.enPassantSquare == neighbourSquare
                        ) {
                            val neighbourPiece = position.pieceOn(neighbourSquare)
                            neighbourPiece is Pawn && pieceToBeMoved is Pawn && pieceToBeMoved.side != neighbourPiece.side
                        } else false
                    }
                }
            ),
            MoveRule(
                direction = -Direction.diagonal(),
                captureType = MoveRule.CaptureType.MANDATORY,
                moveCondition = { pieceToBeMoved, departureSquare, _, position ->
                    if (position.enPassantSquare == null) {
                        false
                    } else {
                        val neighbourSquare = departureSquare.leftNeighbour()
                        if (
                            position.enPassantSquare == neighbourSquare
                        ) {
                            val neighbourPiece = position.pieceOn(neighbourSquare)
                            neighbourPiece is Pawn && pieceToBeMoved is Pawn && pieceToBeMoved.side != neighbourPiece.side
                        } else false
                    }
                }
            )
        )

        private val rules = mapOf(
            WhitePawn to pawnMoveRules,
            BlackPawn to -pawnMoveRules,
            WhiteRook to rookMoveRules,
            BlackRook to -rookMoveRules,
            WhiteKnight to knightMoveRules,
            BlackKnight to -knightMoveRules,
            WhiteBishop to bishopMoveRules,
            BlackBishop to -bishopMoveRules,
            WhiteQueen to queenMoveRules,
            BlackQueen to -queenMoveRules,
            WhiteKing to kingMoveRules,
            BlackKing to -kingMoveRules
        )

        private val capturingRules = rules.map { (piece, ruleSet) ->
            piece to MoveRuleSet(ruleSet.moveRules.filter { it.captureType != MoveRule.CaptureType.DISALLOWED }.toSet())
        }.toMap()
    }
}