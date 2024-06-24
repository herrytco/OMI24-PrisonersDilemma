package omi24.ex9;

import omi24.ex9.strategies.GameStrategy;
import java.util.ArrayList;
import java.util.List;

public record GameState(
    GameStrategy player1,
    GameStrategy player2,
    List<GameAction> player1Actions,
    List<GameAction> player2Actions
) {

  @Override
  public List<GameAction> player1Actions() {
    return new ArrayList<>(player1Actions);
  }

  @Override
  public List<GameAction> player2Actions() {
    return new ArrayList<>(player2Actions);
  }

  public List<GameAction> getPlayer1Actions() {
    return new ArrayList<>(player1Actions);
  }

  public List<GameAction> getPlayer2Actions() {
    return new ArrayList<>(player2Actions);
  }
}
