package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

import java.util.List;
import java.util.Random;

public class LoremStrategy implements GameStrategy {
  private static final double RANDOM_DEFECT_PROBABILITY = 0.3;
  private static final int DEFECT_THRESHOLD = 10;
  private int defectCount = 0;
  //public static int wins = 0;

  @Override
  public String getName() {
    return "LoremStrategy";
  }

  @Override
  public GameAction playRound(GameState state) {
    List<GameAction> opponentActions = state.getPlayer2Actions();

    // Erste Runde defect, um vorsprung eventuell zu erschaffen
    if (opponentActions.isEmpty()) {
      return GameAction.DEFECT;
    }

    GameAction lastOpponentAction = opponentActions.getLast();

    // Defects des Gegners zählen
    if (lastOpponentAction == GameAction.DEFECT) {
      defectCount++;
    }

    // Wenn der gegner zu oft defectet --> defect only strategie (um cooperate ausnutzer entgegen zu wirken)
    if (defectCount > DEFECT_THRESHOLD) {
      return GameAction.DEFECT;
    }

    Random random = new Random();
    // Implementiert Tit-for-Tat mit zufälligen defect
    if (random.nextDouble() < RANDOM_DEFECT_PROBABILITY) {
      return GameAction.DEFECT;
    } else {
      return lastOpponentAction;
    }
  }

}
