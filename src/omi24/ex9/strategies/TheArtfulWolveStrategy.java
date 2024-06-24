package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the <b>Adaptive Pavlov</b> strategy,
 * which monitors the enemies behaviour in the first few rounds,
 * tries to categorize and find the best strategy against it.
 * <br><br>
 * After a certain amount of rounds, it calculates the average payoff
 * and adapts itself to the new circumstances by deciding if another
 * detection cycle is needed or not.
 *
 * @author Philipp Wolfger
 */
public class TheArtfulWolveStrategy implements GameStrategy {

  private static final int DETECTION_SAMPLE_SIZE = 6;
  private int roundsPlayed;
  private int roundsSinceLastDetection;
  private final List<GameAction> playerActions = new ArrayList<>();
  private final StrategyContext strategyContext;

  public TheArtfulWolveStrategy() {
    roundsPlayed = 0;
    roundsSinceLastDetection = 0;
    strategyContext = new StrategyContext();
  }

  @Override
  public String getName() {
    return "[Q1] TheArtfulWolve";
  }

  @Override
  public GameAction playRound(GameState state) {
    if (isNewRound(state)) {
      //New round, so reset values
      roundsPlayed = 0;
      roundsSinceLastDetection = 0;
      playerActions.clear();
    }

    //Check if the player is player 1 or 2, depending on the given game state
    List<GameAction> enemyActions = state.player1() == this ? state.player2Actions() : state.player1Actions();

    //Determine player behaviour and set strategy in the first few rounds
    if (isDetectionNecessary(enemyActions)) {
      //Play TFT for the first six rounds to determine enemy behaviour
      strategyContext.setCurrentStrategy(new TFT());
      roundsSinceLastDetection = 0;
    } else if (roundsSinceLastDetection == 0) {
      //Determine strategy according to the enemys last 6 actions
      var sampleEnemyActions = enemyActions.subList(Math.max(enemyActions.size() - 6, 0), enemyActions.size());
      strategyContext.setCurrentStrategy(findBestStrategyForSample(sampleEnemyActions));
      roundsSinceLastDetection++;
    } else {
      roundsSinceLastDetection++;
    }

    //Execute current set strategy
    GameAction action = strategyContext.executeStrategy(playerActions, enemyActions);
    roundsPlayed++;
    playerActions.add(action);
    return action;
  }

  private boolean isNewRound(GameState state) {
    return state.player1Actions().isEmpty() && state.player2Actions().isEmpty();
  }

  private boolean isDetectionNecessary(List<GameAction> enemyActions) {
    if (roundsPlayed <= DETECTION_SAMPLE_SIZE || enemyActions.size() < 6) {
      return true;
    }

    if (roundsSinceLastDetection <= DETECTION_SAMPLE_SIZE) {
      return false;
    }
         /*
            Check the average payoff every six rounds and determine if the
            opponent changed strategy and a new detection cycle is necessary
         */
    int payoff = 0;
    int startIndex = enemyActions.size() - DETECTION_SAMPLE_SIZE;
    for (int i = startIndex; i < enemyActions.size(); i++) {
      payoff += Util.getRoundResult(playerActions.get(i), enemyActions.get(i));
    }

    int averagePayoff = payoff / DETECTION_SAMPLE_SIZE;
    return averagePayoff < 3;
  }

  private Strategy findBestStrategyForSample(List<GameAction> sampleActions) {

    if (sampleActions.stream().allMatch(ac -> ac.equals(GameAction.COOPERATE))) {
      //Enemy always cooperated in sample size, so play TFT so still be able to fast
      //detect changes in behaviour
      return new TFT();
    }

    int defectionCount = sampleActions.stream().filter(ac -> ac.equals(GameAction.DEFECT)).toList().size();
    int maxDefections = (DETECTION_SAMPLE_SIZE / 2);

    if (defectionCount == maxDefections) {
      //Opponent defected in half the sample rounds so play TFTT strategy,
      //so cooperation can be re-established
      return new TFTT();
    }

    //Enemy defected in more than half the rounds in the sample size,
    //so either the enemy is playing All-D or random strat, so always defect
    return new AlwaysDefect();
  }

  //region Strategy Pattern

  interface Strategy {
    GameAction execute(List<GameAction> playerActions, List<GameAction> enemyActions);
  }

  static class StrategyContext {

    private Strategy currentStrategy;

    public void setCurrentStrategy(Strategy currentStrategy) {
      this.currentStrategy = currentStrategy;
    }

    public GameAction executeStrategy(List<GameAction> playerActions, List<GameAction> enemyActions) {
      return currentStrategy.execute(playerActions, enemyActions);
    }
  }

  //endregion
  //region PD Strategy Implementations

  /* ----------- IMPLEMENTATION OF DIFFERENT STRATEGIES -----------  */

  /**
   * Tit for tat strategy
   */
  static class TFT implements Strategy {
    @Override
    public GameAction execute(List<GameAction> playerActions, List<GameAction> enemyActions) {
      if (enemyActions.isEmpty()) {
        //First round, so start with COOPERATE.
        return GameAction.COOPERATE;
      }

      //Imitate the enemys actions
      return GameAction.COOPERATE.equals(enemyActions.getLast())
          ? GameAction.COOPERATE
          : GameAction.DEFECT;
    }
  }

  /**
   * Always defect strategy
   */
  static class AlwaysDefect implements Strategy {
    @Override
    public GameAction execute(List<GameAction> playerActions, List<GameAction> enemyActions) {
      return GameAction.DEFECT;
    }
  }

  /**
   * Tit for two tats strategy
   */
  static class TFTT implements Strategy {

    @Override
    public GameAction execute(List<GameAction> playerActions, List<GameAction> enemyActions) {
      //Defects if the enemy defected twice in a row, else cooperates

      if (enemyActions.size() < 2) {
        return GameAction.COOPERATE;
      }
      var lastEnemyAction = enemyActions.getLast();
      var penultimateEnemyAction = enemyActions.get(enemyActions.size() - 2);

      boolean defectedTwice = GameAction.DEFECT.equals(lastEnemyAction)
          && GameAction.DEFECT.equals(penultimateEnemyAction);
      return defectedTwice ? GameAction.DEFECT : GameAction.COOPERATE;
    }
  }

  //endregion
  //region Utility
  static class Util {
    public static int getRoundResult(GameAction playerAction, GameAction enemyAction) {
      return switch (playerAction) {
        case COOPERATE -> enemyAction.equals(GameAction.COOPERATE)
            ? 3 //Both cooperated -> Reward R
            : 0; //Enemy defected -> Suckers Payoff S
        case DEFECT -> enemyAction.equals(GameAction.COOPERATE)
            ? 5 //Player defected -> Temptation T
            : 1; //Both defected -> Punishment P
      };
    }
  }
  //endregion
}