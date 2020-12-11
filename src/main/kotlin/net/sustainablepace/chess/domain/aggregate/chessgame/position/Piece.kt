package net.sustainablepace.chess.domain.aggregate.chessgame.position

import net.sustainablepace.chess.domain.PieceMoveRules
import net.sustainablepace.chess.domain.aggregate.chessgame.*

sealed class ChessPiece
object NoPiece: ChessPiece()

sealed class Piece(): ChessPiece(), Colour {
    val colour: Side = when (this) {
        is White -> WhitePieces
        is Black -> BlackPieces
        else -> throw IllegalStateException("Piece is neither white nor black.")
    }

    init {
        check(colour in setOf(WhitePieces, BlackPieces))
    }

    val rules by lazy {
        when (this) {
            is Rook -> PieceMoveRules.rookMoveRules
            is Knight -> PieceMoveRules.knightMoveRules
            is Bishop -> PieceMoveRules.bishopMoveRules
            is Queen -> PieceMoveRules.queenMoveRules
            is King -> PieceMoveRules.kingMoveRules
            is Pawn -> PieceMoveRules.pawnMoveRules
        }.let { moveRules ->
            when(colour) {
                is WhitePieces -> moveRules
                is BlackPieces -> -moveRules
            }
        }
    }

    override fun toString() = when(colour) {
        is WhitePieces -> "White"
        is BlackPieces -> "Black"
    } + when (this) {
        is Pawn -> "Pawn"
        is Knight -> "Knight"
        is Rook -> "Rook"
        is Bishop -> "Bishop"
        is Queen -> "Queen"
        is King -> "King"
    }

    override fun equals(other: Any?): Boolean =
        other is Piece &&
            colour == other.colour &&
            when (this) {
                is Pawn -> other is Pawn
                is Knight -> other is Knight
                is Rook -> other is Rook
                is Bishop -> other is Bishop
                is Queen -> other is Queen
                is King -> other is King
            }

    override fun hashCode(): Int {
        return when(colour) {
            is WhitePieces -> 0
            is BlackPieces -> 7
        } + when (this) {
            is Pawn -> 1
            is Knight -> 2
            is Rook -> 3
            is Bishop -> 4
            is Queen -> 5
            is King -> 6
        }
    }
}

abstract class Pawn : Piece()
abstract class Knight : Piece()
abstract class Rook : Piece()
abstract class Bishop : Piece()
abstract class Queen : Piece()
abstract class King : Piece()

class WhitePawn : White, Pawn()
class WhiteKnight : White, Knight()
class WhiteRook : White, Rook()
class WhiteBishop : White, Bishop()
class WhiteQueen : White, Queen()
class WhiteKing : White, King()
class BlackPawn : Black, Pawn()
class BlackKnight : Black, Knight()
class BlackRook : Black, Rook()
class BlackBishop : Black, Bishop()
class BlackQueen : Black, Queen()
class BlackKing : Black, King()