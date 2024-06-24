package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;
import java.util.Random;

public class CopyOtherPlayerStrategy implements GameStrategy {

  public final Random rng = new Random();

  @Override
  public String getName() {
    return "Copycat";
  }

  @Override
  public GameAction playRound(GameState state) {
    var otherPlayerActions = state.player1() == this ? state.player2Actions() : state.player1Actions();

    if (otherPlayerActions.isEmpty()) {
      return GameAction.COOPERATE;
    }

    return otherPlayerActions.getLast() == GameAction.COOPERATE ? GameAction.COOPERATE : GameAction.DEFECT;
  }

}
