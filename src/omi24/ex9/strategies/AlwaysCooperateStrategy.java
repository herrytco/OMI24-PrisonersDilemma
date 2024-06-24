package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

public class AlwaysCooperateStrategy implements GameStrategy {

  @Override
  public String getName() {
    return "AlwaysCooperate";
  }

  @Override
  public GameAction playRound(GameState state) {
    return GameAction.COOPERATE;
  }
}
