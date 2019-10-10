import java.util.*;

public class Poker {

  private int[][] pokerHand = new int[4][13];
  private int[] currHandSuit = new int[5];
  private int[] currHandValues = new int[5];

  public void pushCard(char type, int value) {
    // we do value - 1 because the Ace card starts at 0 rather than 1
    switch (type) {
      case 'D':
        this.pokerHand[0][value-1] = 1;
        break;
      case 'H':
        this.pokerHand[1][value-1] = 1;
        break;
      case 'C':
        this.pokerHand[2][value-1] = 1;
        break;
      case 'S':
        this.pokerHand[3][value-1] = 1;
        break;
    }
  }

  public void requestCards() {
    Scanner input = new Scanner(System.in);
    int i = 1;

    while(i <= 5) {
      System.out.printf("Card #%d: ", i);
      int cardNumber = input.nextInt();

      System.out.printf("Suit %d: ", i);
      char suit = input.next().charAt(0);
      suit = Character.toUpperCase(suit);

      this.pushCard(suit, cardNumber);
      i++;
    }
  }

  public void determineHand() {
    int pos = 0;
    for(int i=0; i<4; i++) {
      for(int j=0; j<13; j++) {
        if(pokerHand[i][j] == 1) {
          currHandSuit[pos] = i;
          currHandValues[pos] = j;
          pos++;
        }
      }
    }
    Arrays.sort(currHandValues);

    // straight flush
    if(this.straight() && this.flush())
      System.out.println("The hand is a straight flush.");

    // four-of-a-kind
    else if(this.ofAKind(4,"-1")[0] == "true")
      System.out.println("The hand is a four-of-a-kind.");

    // full house:3 cards of one rank, two cards of another rank
    else if(this.fullHouse())
      System.out.println("The hand is a full house.");

    // flush: all cards have the same suit
    else if(this.flush())
      System.out.println("The hand is a flush.");

    // straight: cards have consecutive ranks
    else if(this.straight())
      System.out.println("The hand is a straight.");

    // three-of-a-kind
    else if(this.ofAKind(3,"-1")[0] == "true")
      System.out.println("The hand is a three-of-a-kind.");

    // two pair
    else if(this.pair(2))
      System.out.println("The hand is a two pair.");

    // one pair
    else if(this.pair(1))
      System.out.println("The hand is a one pair.");

    // none of the above
    else
      System.out.println("None of the above.");
  }

  public boolean flush() {
    boolean allSame = false;
    int i=0;
    while(i<4) {
      if(currHandSuit[i] == currHandSuit[i+1])
        allSame = true;
      else {
        allSame = false;
        break;
      }
      i++;
    }
    return allSame;
  }

  public boolean straight() {
    int prev = 0;
    int count = 0;
    int i=0;
    boolean consecutive = false;

    while(i<5) {
      if(count == 0) {
        prev = currHandValues[i];
        count++;
      }
      else {
        if(currHandValues[i] == prev+1) {
          // System.out.printf("%d: %d,%d\n",i,currHandValues[i],prev);
          prev = currHandValues[i];
          count++;
          consecutive = true;
        }
        else {
          // System.out.printf("WRONG: %d,%d\n",currHandValues[i],prev);
          consecutive = false;
          break;
        }
      }
      i++;
    }
    return consecutive;
  }

  public String[] ofAKind(int numCards, String kind) {
    String result[] = {"false",""};
    int count = 0;
    int val;
    int thisKind = Integer.parseInt(kind);

    for(int i=0;i<currHandValues.length;i++) {
      val = currHandValues[i];
      count = 0;

      for(int j=i+1;j<currHandValues.length;j++) {
        if(currHandValues[j] == val) {
          count++;
          if(count == (numCards-1) && val != thisKind) {
            result[0] = "true";
            result[1] = Integer.toString(val);
            return result;
          }
        }
      }
    }
    return result;
  }

  public boolean fullHouse() {
    if(this.ofAKind(3,"-1")[0] == "true") {
      String val = this.ofAKind(3,"-1")[1];
      if(this.ofAKind(2,val)[0] == "true")
        return true;
    }
    return false;
  }

  public boolean pair(int numPairs) {
    boolean isPair = false;

    if(numPairs == 1) {
      if(this.ofAKind(2,"-1")[0] == "true")
        isPair = true;
    }
    else {
      if(this.ofAKind(2,"-1")[0] == "true") {
        String val = this.ofAKind(2,"-1")[1];
        if(this.ofAKind(2,val)[0] == "true")
          isPair = true;
      }
    }
    return isPair;
  }
}
