package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;
import java.util.List;
import java.util.Random;

public class ForgivingTitForTatAlsoHasRabiesStrategy implements GameStrategy {
  private static final float probabilityOfForgiveness = 0.01f;

  private boolean preventForgiveness = true;
  private boolean firstRoundPlayed = false;
  private int numOfTurns = 0;
  private int currentTurn = 0;
  private boolean turnPlayed = false;

  @Override
  public String getName() {
    return "ForgivingTitForTat(AlsoHasRabies)";
  }

  @Override
  public GameAction playRound(GameState state) {
    GameStrategy currentOpponent = getOpponent(state);

    // If there are no player actions but there previously were, we know that a new round has started
    if (state.player1Actions().isEmpty() || state.player2Actions().isEmpty()) {
      currentTurn = 0;
    } else {
      turnPlayed = true;
    }

    // If no actions are in the game state even though we know there previously were, we know that the first round is over
    if ((state.player1Actions().isEmpty() || state.player2Actions().isEmpty()) && turnPlayed) {
      firstRoundPlayed = true;
    }

    currentTurn++;

    // While we are playing the first round, we count the number of turns
    // The number of turns per round stays the same during one tournament
    // This offers us some advantage by allowing us to defect on the last turn
    // We don't directly check if we're playing against the first opponent but rather use a boolean flag
    // This is because we can face the first opponent again later in the game
    if (!firstRoundPlayed) {
      numOfTurns = currentTurn;
    }

    // If we are on the last turn, we always defect, since the opponent can not retaliate
    // The "also has rabies" part of my strategy, we have to bite at the very end <3
    if (currentTurn == numOfTurns && firstRoundPlayed) {
      return GameAction.DEFECT;
    }

    // Obtain list of other player's actions
    List<GameAction> otherPlayerAction;
    if (this == state.player1()) {
      otherPlayerAction = state.player2Actions();
    } else {
      otherPlayerAction = state.player1Actions();
    }

    // Start by cooperating
    if (state.player1Actions().isEmpty()) {
      return GameAction.COOPERATE;
    }

    // The "forgiving" part
    // There's a very small chance that we cooperate even if the opponent has previously defected
    // This allows us to break a cycle of forever defecting,
    // if the opponent is a "rational" actor that was "testing the waters"
    // We may potentially score lower than the opponent by doing this
    // But we should accumulate more points than without this switch, as this allows us to get back to larger gains
    if (otherPlayerAction.getLast() == GameAction.DEFECT && !preventForgiveness) {

      Random r = new Random();
      if (r.nextFloat() < probabilityOfForgiveness) {
        // However, if we've forgiven the opponent and don't defect, and the player defects again regardless,
        // this is a strong indicator that the player wants to take advantage of our forgivingness
        // since they may assume, even if we continue defecting, that such a random fluke will occur again, giving them more advantage
        // Or they might be a modified grim-trigger-strategy or always-defect-strategy, that keeps defecting until the end.
        // To avoid furthering the advantage (since we also won't get any more points by cooperating if this is the case)
        // we prevent all future chances of forgiveness until the opponent cooperates again
        // Typically a player will not cooperate again when I also do not cooperate again
        // But this will fare better against random strategies
        // Every instance of forgiveness that isn't met with cooperation costs 10 points
        // but the strategy will play tit-for-tat until the end so can't lose more than this
        // Every instance of forgiveness that is met with cooperation costs 5 points
        // but will ideally gain more points than this in the long run, typically not beating its current opponent
        // but still having more points than other strategies in the same round
        preventForgiveness = true;
        return GameAction.COOPERATE;
      }
    }

    // We previously forgave the opponent and the opponent does cooperate again
    if (otherPlayerAction.getLast() == GameAction.COOPERATE && preventForgiveness) {
      preventForgiveness = false;
      // We allow forgiveness again
    }

    // The "tit-for-tat" part
    // We perform a classic tit-for-tat in most cases
    return otherPlayerAction.getLast();
  }

  private GameStrategy getOpponent(GameState state) {
    if (this == state.player1()) {
      return state.player2();
    } else {
      return state.player1();
    }
  }

}
