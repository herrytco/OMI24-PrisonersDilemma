package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

import java.util.ArrayList;
import java.util.Random;

public class CloneStrat3000Strategy implements GameStrategy {

  public final Random rng = new Random();

  private GameStrategy gegner;

  @Override
  public String getName() {
    return "CloneStrat3000";
  }

  @Override
  public GameAction playRound(GameState state) {
    GameState gameState;
    if(gegner == null)
    {
      if(state.player1() == this)
      {
        try{
          gegner = state.player2().getClass().newInstance();
        } catch (Exception ignored) {

        }

      } else {
        try{
          gegner = state.player1().getClass().newInstance();
        } catch (Exception ignored) {

        }
      }

    }
    gameState = new GameState(
            state.player1() == this ? state.player1() : gegner,
            state.player2() == this ? state.player2() : gegner,
            new ArrayList<>(state.player1Actions()),
            new ArrayList<>(state.player2Actions())
    );

      assert gegner != null;
      GameAction test = gegner.playRound(gameState);


    if(test == GameAction.DEFECT)
    {
        return GameAction.DEFECT;
    } else {
      if(rng.nextBoolean())
      {
        return GameAction.DEFECT;
      } else {
        return GameAction.COOPERATE;
      }

    }
  }

}
