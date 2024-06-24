package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Meine Strategie ist eine Adaptive Tit for Tat Strategie die sich auch die Stochastik Strategie zu nutzen macht.
 * Eine sogenannte Hybrid Strategie. Beim ersten mal gibt es eine vor eingestellte warscheinlichkeit das der Bot
 * Kooperiert. In jeder Runde gibt es eine Warscheinlichkeit das der Bot das gleiche macht wie der Gegner letzte Runde oder
 * genau das gegenteil. Zusätzlich wird die warscheinlichkeit angepasst in dem der Bot in die Vergangenheit schaut und
 * nachschaut was der Gegner in den Letzten runden gemacht hat. Hat er Kooperiert oder Defektet und daran passt er dann die
 * Strategie an.
 */
public class TheForbiddenTransmutationStrategy implements GameStrategy {

  private final List<GameAction> myHistory = new ArrayList<>();
  private double p = 0.7;
  private List<GameAction> enemyHistory = new ArrayList<>();
  private boolean isPlayer1 = false;

  @Override
  public String getName() {
    return "The_Forbidden_Transmutation";
  }

  @Override
  public GameAction playRound(GameState state) {
    witchPlayeramI(state);
    getOpponentHistory(state);
    look_into_history();
    Random rand = new Random();
    double randNumber = rand.nextDouble();

    if (randNumber < p) {
      if (enemyHistory.isEmpty()) {
        myHistory.add(GameAction.COOPERATE);
      } else {
        myHistory.add(enemyHistory.getLast());
      }
    } else {
      if (enemyHistory.isEmpty()) {
        myHistory.add(GameAction.DEFECT);
      } else {
        myHistory.add(flip(enemyHistory.getLast()));
      }
    }
    p = 1 - p;
    return myHistory.getLast();
  }


  private void look_into_history() {
    int streakCount = 0;
    int lookBackInTime = 4;
    if (enemyHistory.size() > lookBackInTime) {
      GameAction lastAction = enemyHistory.getLast();
      for (int i = enemyHistory.size() - 1; i > enemyHistory.size() - lookBackInTime; i--) {
        if (lastAction == enemyHistory.get(i)) {
          streakCount++;
        }
      }
      if (streakCount == lookBackInTime) {
        adjust();
      }
    }
  }

  private void adjust() {
    if (enemyHistory.getLast() == GameAction.COOPERATE) {
      p -= 0.1;
    } else {
      p += 0.1;
    }
  }

  private void witchPlayeramI(GameState state) {
    isPlayer1 = state.player1().equals(this);
  }

  private GameAction flip(GameAction action) {
    return action == GameAction.COOPERATE ? GameAction.DEFECT : GameAction.COOPERATE;
  }

  private void getOpponentHistory(GameState state) {
    enemyHistory = isPlayer1 ? state.player2Actions() : state.player1Actions();
  }


}
