package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

public class PItifulAttemptToBeatMeStrategy implements GameStrategy {

  static double pi = Math.PI;

  @Override
  public String getName() {
    return "PItifulAttemptToBeatMe";
  }

  @Override
  public GameAction playRound(GameState state) {
    int pinumber = calculateNumber();
    return switch (pinumber) {
      case 0 -> GameAction.COOPERATE;
      case 1 -> GameAction.DEFECT;
      case 2 -> GameAction.COOPERATE;
      case 3 -> GameAction.COOPERATE;
      case 4 -> GameAction.DEFECT;
      case 5 -> GameAction.DEFECT;
      case 6 -> GameAction.COOPERATE;
      case 7 -> GameAction.DEFECT;
      case 8 -> GameAction.DEFECT;
      case 9 -> GameAction.DEFECT;
      default -> GameAction.DEFECT;
    };
  }

  public static int calculateNumber() {
    if (pi == 0.0) {
      pi = Math.PI;
    }
    int rnd = (int) pi;
    pi = pi - rnd;
    pi = pi * 10;
    return rnd;
  }

}