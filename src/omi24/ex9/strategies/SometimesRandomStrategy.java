package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

import java.util.Random;

public class SometimesRandomStrategy implements GameStrategy {

  private boolean opponentDefect = false;
  public final Random rng = new Random();
  @Override
  public String getName() {
    return "SometimesRandomStrategy";
  }

  @Override
  public GameAction playRound(GameState state) {

    int low = 1;
    int high = 100;
    int result = rng.nextInt(high-low) + low;

    if (result%3 == 0){
      return GameAction.DEFECT;
    }

    if (!state.getPlayer2Actions().isEmpty()) {
      opponentDefect = state.getPlayer2Actions().get(state.getPlayer2Actions().size() - 1) == GameAction.DEFECT;
    }

    if (opponentDefect) {
      opponentDefect = false;
      return GameAction.DEFECT;
    }

    return GameAction.COOPERATE;
  }
}
