public class Piece {
    private char type, color;
    private boolean firstMove;

    public Piece(char pType, char pColor) {
        setType(pType);
        setColor(pColor);
        firstMove = true;
    }

    /*
        Getter-Setter of the variable firstMove
     */
    public void firstMoveDone() { firstMove = false; }
    public boolean getFirstMove() { return firstMove; }

    /*
        Getter-Setter of the variable type
     */
    private void setType(char pType) { this.type = pType; }
    public char getType() { return type; }

    /*
        Getter-Setter of the variable color
     */
    public void setColor(char pColor) {
        if (pColor == 'b' || pColor == 'w') {
            this.color = pColor;
        } else {
            System.out.println("ERROR on Piece + " + getType() + ": COLOR NOT RECOGNIZED: " + pColor);
            System.out.println(">>> Setting color to white <<<");
            this.color = 'w';
        }
    }
    public char getColor() { return color; }

    public String toString() {
        return "" + color + type;
    }
}
