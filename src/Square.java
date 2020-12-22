public class Square {
    private Piece piece;

    /*
        Konstruktoren
     */
    public Square() {
        setPiece(null);
    }
    public Square(Piece pPiece) {
        setPiece(pPiece);
    }

    /*
        Getter-Setter of the Object piece
     */
    public Piece getPiece() { return piece; }
    public void setPiece(Piece piece) { this.piece = piece; }

    public String toString() {
        if (piece != null) {
            return "[" + piece.toString() + "]";
        } else {
            return "[  ]";
        }
    }
}
