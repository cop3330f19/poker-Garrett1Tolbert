/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker2;

import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

/**
 *
 * @author garretttolbert
 */
public class PokerController {
    @FXML private TextField card1_input;
    @FXML private TextField card2_input;
    @FXML private TextField card3_input;
    @FXML private TextField card4_input;
    @FXML private TextField card5_input;
    
    @FXML private TextField card1_suit;
    @FXML private TextField card2_suit;
    @FXML private TextField card3_suit;
    @FXML private TextField card4_suit;
    @FXML private TextField card5_suit;
    @FXML private Button submit;

    @FXML private Label result;

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
        result.setText("The hand is a straight flush.");

      // four-of-a-kind
      else if("true".equals(this.ofAKind(4,"-1")[0]))
        result.setText("The hand is a four-of-a-kind.");

      // full house:3 cards of one rank, two cards of another rank
      else if(this.fullHouse())
        result.setText("The hand is a full house.");

      // flush: all cards have the same suit
      else if(this.flush())
        result.setText("The hand is a flush.");

      // straight: cards have consecutive ranks
      else if(this.straight())
        result.setText("The hand is a straight.");

      // three-of-a-kind
      else if("true".equals(this.ofAKind(3,"-1")[0]))
        result.setText("The hand is a three-of-a-kind.");

      // two pair
      else if(this.pair(2))
        result.setText("The hand is a two pair.");

      // one pair
      else if(this.pair(1))
        result.setText("The hand is a one pair.");

      // none of the above
      else
        result.setText("None of the above.");
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
    if("true".equals(this.ofAKind(3,"-1")[0])) {
      String val = this.ofAKind(3,"-1")[1];
      if("true".equals(this.ofAKind(2,val)[0]))
        return true;
    }
    return false;
  }

  public boolean pair(int numPairs) {
    boolean isPair = false;

    if(numPairs == 1) {
      if("true".equals(this.ofAKind(2,"-1")[0]))
        isPair = true;
    }
    else {
      if("true".equals(this.ofAKind(2,"-1")[0])) {
        String val = this.ofAKind(2,"-1")[1];
        if("true".equals(this.ofAKind(2,val)[0]))
          isPair = true;
      }
    }
    return isPair;
  }
  
  public void reset() {
    for(int i=0; i<4; i++) {
      for(int j=0; j<13; j++)
          pokerHand[i][j] = 0;
    }      
  }
  
  public void displayResult(ActionEvent e) {
    int card1 = Integer.parseInt(card1_input.getText());
    int card2 = Integer.parseInt(card2_input.getText());
    int card3 = Integer.parseInt(card3_input.getText());
    int card4 = Integer.parseInt(card4_input.getText());
    int card5 = Integer.parseInt(card5_input.getText());

    String card_suit1 = card1_suit.getText().toUpperCase();
    String card_suit2 = card2_suit.getText().toUpperCase();
    String card_suit3 = card3_suit.getText().toUpperCase();
    String card_suit4 = card4_suit.getText().toUpperCase();
    String card_suit5 = card5_suit.getText().toUpperCase();      
    
    this.reset();
    this.pushCard(card_suit1.charAt(0), card1);
    this.pushCard(card_suit2.charAt(0), card2);
    this.pushCard(card_suit3.charAt(0), card3);
    this.pushCard(card_suit4.charAt(0), card4);
    this.pushCard(card_suit5.charAt(0), card5);
    
    this.determineHand();
  }
    
}
