package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;
import java.util.List;

public class ForgivingButNotPeacefulStrategy implements GameStrategy {

  @Override
  public String getName() { //wird nicht gewinnen, aber bevor ich 1:1 die beste strategie kopiere, ist dies hoffentlich besser...
    return "Forgiving_but_not_Peaceful"; //ist fast wie "tit for tat" aber am anfang wird angegriffen, und es wird 1 mal vergeben
  }

  @Override
  public GameAction playRound(GameState state) {
    List<GameAction> opponentMove; //liste mit gegner zügen
    if (state.player1() == this) { // this - instanz des spielers - wenn dies zutrifft, ist man selbst player 1
      opponentMove = state.player2Actions(); //also player2 wird opponentMove zugeordnet
    } else {
      opponentMove = state.player1Actions(); //ansonsten ist player1 der gegner
    }

    if (opponentMove.isEmpty()) { // am anfang wird angegriffen - dann gewinnt man wenigstens gegen AlwaysCooperate ^^
      return GameAction.DEFECT;
    }

    GameAction lastOpponentAction = opponentMove.getLast(); //letzer zug vom gegner

    // wenn der Gegner kooperiert, bleibt man auch friedlich
    if (lastOpponentAction == GameAction.COOPERATE) {
      return GameAction.COOPERATE; //es wird kooperiert
    } else {
      // sollte der gegner angreifen, wird ihm einmal vegeben - dannach wird offensiv gespielt
      if (opponentMove.size() > 1) { //gegner mind 2 züge durchgeführt? ansonsten kooperieren
        GameAction secondLastOpponentAction = opponentMove.get(opponentMove.size() - 2); //vorletzten zug
        if (secondLastOpponentAction == GameAction.DEFECT) { //wenn dieser vorletzte zu defect war:
          return GameAction.DEFECT; // hier beginnt die Offensive
        } else {
          return GameAction.COOPERATE; //wieder kooperieren
        }
      } else {
        return GameAction.COOPERATE; // /\
      }
    }
  }
}
