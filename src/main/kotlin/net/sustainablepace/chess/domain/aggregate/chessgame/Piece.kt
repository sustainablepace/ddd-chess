package net.sustainablepace.chess.domain.aggregate.chessgame

sealed class PieceOrNoPiece
object NoPiece : PieceOrNoPiece()

sealed class Piece(val side: Side) : PieceOrNoPiece() {
    val isPromotionOption: Boolean
        get() = when (this) {
            is Pawn, is King -> false
            is Knight, is Bishop, is Rook, is Queen -> true
        }

    val shortType: Char
        get() = when (this) {
            is Pawn -> 'P'
            is Knight -> 'N'
            is Rook -> 'R'
            is Bishop -> 'B'
            is Queen -> 'Q'
            is King -> 'K'
        }

    override fun toString() = when (side) {
        is White -> "White"
        is Black -> "Black"
    } + when (this) {
        is Pawn -> "Pawn"
        is Knight -> "Knight"
        is Rook -> "Rook"
        is Bishop -> "Bishop"
        is Queen -> "Queen"
        is King -> "King"
    }
}

fun PromotionPiece(identifier: Char, side: Side): PieceOrNoPiece =
    when (side) {
        is White -> when (identifier) {
            WhiteBishop.shortType -> WhiteBishop
            WhiteKnight.shortType -> WhiteKnight
            WhiteRook.shortType -> WhiteRook
            WhiteQueen.shortType -> WhiteQueen
            else -> NoPiece
        }
        is Black -> when (identifier) {
            BlackBishop.shortType -> BlackBishop
            BlackKnight.shortType -> BlackKnight
            BlackRook.shortType -> BlackRook
            BlackQueen.shortType -> BlackQueen
            else -> NoPiece
        }
    }

abstract class Pawn(colour: Side) : Piece(colour)
abstract class Knight(colour: Side) : Piece(colour)
abstract class Rook(colour: Side) : Piece(colour)
abstract class Bishop(colour: Side) : Piece(colour)
abstract class Queen(colour: Side) : Piece(colour)
abstract class King(colour: Side) : Piece(colour)

object WhitePawn : Pawn(White)
object WhiteKnight : Knight(White)
object WhiteRook : Rook(White)
object WhiteBishop : Bishop(White)
object WhiteQueen : Queen(White)
object WhiteKing : King(White)
object BlackPawn : Pawn(Black)
object BlackKnight : Knight(Black)
object BlackRook : Rook(Black)
object BlackBishop : Bishop(Black)
object BlackQueen : Queen(Black)
object BlackKing : King(Black)