package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

public class IpsumStrategy implements GameStrategy {

  private GameAction lastAction = GameAction.COOPERATE;

  @Override
  public String getName() {

    return "Ipsum";
  }

  @Override
  public GameAction playRound(GameState state) {
    var otherPlayerActions = state.player1() == this ? state.player2Actions() : state.player1Actions();
    var myActions = state.player1() == this ? state.player1Actions() : state.player2Actions();

    if (!otherPlayerActions.isEmpty() && !myActions.isEmpty()) {
      var myLastAction = myActions.get(myActions.size() - 1);
      var otherLastAction = otherPlayerActions.get(otherPlayerActions.size() - 1);

      if ((myLastAction == GameAction.COOPERATE && otherLastAction == GameAction.COOPERATE) ||
          (myLastAction == GameAction.DEFECT && otherLastAction == GameAction.COOPERATE)) {
        // Win-Stay
        lastAction = myLastAction;
      } else {
        // Lose-Shift
        lastAction = myLastAction == GameAction.COOPERATE ? GameAction.DEFECT : GameAction.COOPERATE;
      }
    }

    return lastAction;
  }
}
