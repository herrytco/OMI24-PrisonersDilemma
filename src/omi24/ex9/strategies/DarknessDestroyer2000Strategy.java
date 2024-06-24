package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

import java.util.ArrayList;
import java.util.List;

public class DarknessDestroyer2000Strategy implements GameStrategy {
  private List<GameAction> myMoves;
  int x;

  @Override
  public String getName() {
    // TODO implement

    return "DarknessDestroyer2000";
  }

  private void myAction(GameState state) {
    var p1move = state.player1Actions();
    var p2move = state.player2Actions();
    if (myMoves.isEmpty()) {
      myMoves.add(GameAction.DEFECT);
    } else {
      if (myMoves.equals(p1move)) {
        x += p2move.getLast().equals(GameAction.DEFECT) ? 2 : 0;
      } else {
        x += p1move.getLast().equals(GameAction.DEFECT) ? 2 : 0;
      }
      if (x > 0) {
        myMoves.add(GameAction.DEFECT);
        x--;
      } else {
        myMoves.add(GameAction.COOPERATE);
      }
    }
  }

  @Override
  public GameAction playRound(GameState state) {
    if (state.player1Actions().isEmpty()) {
      myMoves = new ArrayList<>();
      x = 0;
    }

    myAction(state);
    //System.out.println(myMoves.getLast());
    return myMoves.getLast();

  }


}
