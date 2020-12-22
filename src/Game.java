import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Board b = new Board();
        while (sc.hasNextLine()) {
            String input = sc.next();
            String start = "" + input.charAt(0) + "" + input.charAt(1);
            String target = "" + input.charAt(2) + "" + input.charAt(3);
            b.movePieceTranslated(start,target);
        }
    }
}