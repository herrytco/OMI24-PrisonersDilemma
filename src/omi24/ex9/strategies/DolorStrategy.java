package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

import java.util.List;

public class DolorStrategy implements GameStrategy {

  @Override
  public String getName() {
    return "DolorStrategy";
  }

  @Override
  public GameAction playRound(GameState state) {
    List<GameAction> myActions = state.player1Actions();
    List<GameAction> opponentActions = state.player2Actions();

    int round = myActions.size();

    // If this is the first round, cooperate
    if (round == 0) {
      return GameAction.COOPERATE;
    }

    // If the opponent defected in the last two rounds, defect
    if (round > 1 && opponentActions.get(round - 1) == GameAction.DEFECT && opponentActions.get(round - 2) == GameAction.DEFECT) {
      return GameAction.DEFECT;
    }

    // Otherwise, cooperate
    return GameAction.COOPERATE;
  }
}


