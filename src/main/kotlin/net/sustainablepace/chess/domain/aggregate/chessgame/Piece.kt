package net.sustainablepace.chess.domain.aggregate.chessgame

sealed class PieceOrNoPiece
object NoPiece: PieceOrNoPiece()

sealed class Piece(val side: Side): PieceOrNoPiece() {
    override fun toString() = when(side) {
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