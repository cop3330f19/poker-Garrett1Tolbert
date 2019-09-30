import java.util.Scanner;

public class PokerTest {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    Poker thisGame = new Poker();

    thisGame.requestCards();
    thisGame.determineHand();
  }
}
