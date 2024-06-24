package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

public class Agent47 implements GameStrategy {   // Pavlov-Strategie = Win-Stay, Lose-Shift

  @Override
  public String getName() {
    return "Agent47";
  }

  @Override
  public GameAction playRound(GameState state) {

    if (state.player1Actions().isEmpty()) {  //erste Runde?? Dann Cooperate
      return GameAction.COOPERATE;
    }


    GameAction myLastAction = state.player1Actions().getLast();     // Beide ergebnisse werden angesehen
    GameAction opponentLastAction = state.player2Actions().getLast();


    if ((myLastAction == GameAction.COOPERATE && opponentLastAction == GameAction.COOPERATE) ||
        (myLastAction == GameAction.DEFECT && opponentLastAction == GameAction.DEFECT)) {
      return myLastAction;   //Bleibt bei derzeitiger Strategie
    } else {
      return (myLastAction == GameAction.COOPERATE) ? GameAction.DEFECT : GameAction.COOPERATE;  // Strategie wechel bei lose.
    }
  }
}