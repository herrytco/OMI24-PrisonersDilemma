package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

import java.util.NoSuchElementException;

public class NiceDayStrategy implements GameStrategy {

  @Override
  public String getName() {
    return "Hab einen schönen Tag noch :)";
  }

  @Override
  public GameAction playRound(GameState state) {

    var otherPlayerActions = state.player1() == this ? state.player2Actions() : state.player1Actions();
    GameAction lastAction = null;
    GameAction secondLastAction = null;
    try {
      lastAction = otherPlayerActions.getLast() == GameAction.COOPERATE ? GameAction.COOPERATE :
          otherPlayerActions.getLast() == GameAction.DEFECT ? GameAction.DEFECT : null;

    } catch (NoSuchElementException e) {
      lastAction = null;

    }
    try {
      secondLastAction = otherPlayerActions.get(otherPlayerActions.size() - 2) == GameAction.COOPERATE ? GameAction.COOPERATE :
          otherPlayerActions.get(otherPlayerActions.size() - 2) == GameAction.DEFECT ? GameAction.DEFECT : null;

    } catch (NoSuchElementException e) {
      secondLastAction = null;
    } catch (IndexOutOfBoundsException i) {
      secondLastAction = null;
    }


    if (lastAction == null || secondLastAction == null) {

      return GameAction.DEFECT;

    }


    //var lastAction = otherPlayerActions.getLast() == GameAction.COOPERATE ? GameAction.COOPERATE : otherPlayerActions.getLast() == GameAction.DEFECT ? GameAction.DEFECT : null;
    //var secondLastAction = otherPlayerActions.get(otherPlayerActions.size()-2) == GameAction.COOPERATE ? GameAction.COOPERATE : otherPlayerActions.get(otherPlayerActions.size()-2)== GameAction.DEFECT ? GameAction.DEFECT : null;
    GameAction result = GameAction.DEFECT;
    switch (lastAction) {
      case COOPERATE:
        switch (secondLastAction) {
          case COOPERATE:
            result = GameAction.DEFECT;

            break;


          case DEFECT:
            result = GameAction.COOPERATE;

            break;


          default:
            result = GameAction.COOPERATE;

            break;
        }
        break;
      case DEFECT:
        switch (secondLastAction) {
          case COOPERATE:
            result = GameAction.DEFECT;

            break;
          case DEFECT:
            result = GameAction.DEFECT;

            break;


          default:
            result = GameAction.COOPERATE;

            break;
        }
        break;


      default:
        result = GameAction.COOPERATE;

        break;
    }

    return result;
  }

}
