package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

public interface GameStrategy {

  String getName();

  GameAction playRound(GameState state);

}
