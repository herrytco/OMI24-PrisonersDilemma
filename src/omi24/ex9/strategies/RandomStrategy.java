package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;
import java.util.Random;

public class RandomStrategy implements GameStrategy {

  public final Random rng = new Random();

  @Override
  public String getName() {
    return "SuperRandomStrat";
  }

  @Override
  public GameAction playRound(GameState state) {
    return rng.nextBoolean() ? GameAction.COOPERATE : GameAction.DEFECT;
  }

}
