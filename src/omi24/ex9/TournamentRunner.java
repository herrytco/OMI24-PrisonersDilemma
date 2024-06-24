package omi24.ex9;

import omi24.ex9.strategies.GameStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TournamentRunner {
  private final List<GameStrategy> players;
  private final Map<GameStrategy, Integer> playerNumbers = new HashMap<>();

  private static final Random rng = new Random();

  public TournamentRunner(List<GameStrategy> players) {
    this.players = players;
    for (var player : players) {
      playerNumbers.put(player, players.indexOf(player));
    }
  }

  public void run() {
    int currentRound = 1;

    int nRounds = Math.abs(rng.nextInt(200));

    while (players.size() > 1) {
      System.out.printf("====== ROUND %d ======%n", currentRound++);
      Collections.shuffle(players);

      List<StrategyResult> results = new ArrayList<>();

      for (int i = 0; i < players.size() - 1; i += 2) {
        playRound(i, results, nRounds);
      }

      results.sort(Comparator.comparingInt(StrategyResult::points));

      for (int i = 0; i < results.size() / 2; i++) {
        players.remove(results.get(i).strategy());
      }
    }

    var winner = players.getFirst();

    System.out.println("====== ULTIMATE WINNER ======");
    System.out.printf("%s#%d%n", winner.getName(), playerNumbers.get(winner));
  }

  private void playRound(int i, List<StrategyResult> results, int nRounds) {
    GameStrategy player1 = players.get(i);
    GameStrategy player2 = players.get(i + 1);

    var game = new GameRunner(
        nRounds,
        player1,
        player2
    );

    var result = game.play();

    results.add(new StrategyResult(player1, result.player1Score()));
    results.add(new StrategyResult(player2, result.player2Score()));

    System.out.printf("%s#%d vs %s#%d: %d:%d%n",
        player1.getName(),
        playerNumbers.get(player1),
        player2.getName(),
        playerNumbers.get(player2),
        result.player1Score(),
        result.player2Score()
    );
  }
}
