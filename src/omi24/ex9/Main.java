package omi24.ex9;

import omi24.ex9.strategies.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    var players = List.of(
        new Agent47(),
        new Agent48(),
        new AlwaysCooperateStrategy(),
        new AlwaysDefectStrategy(),
        new CloneStrat3000Strategy(),
        new CopyOtherPlayerStrategy(),
        new DarknessDestroyer2000Strategy(),
        new DolorStrategy(),
        new ForgivingButNotPeacefulStrategy(),
        new ForgivingTitForTatAlsoHasRabiesStrategy(),
        new IpsumStrategy(),
        new LoremStrategy(),
        new MaybeMeanStrategy(),
        new NiceDayStrategy(),
        new NuclearGhandiStrategy(),
        new PItifulAttemptToBeatMeStrategy(),
        new RandomStrategy(),
        new SneakyCopiesStrategy(),
        new SometimesRandomStrategy(),
        new TheArtfulWolveStrategy(),
        new TheForbiddenTransmutationStrategy(),
        new TheWinnerStrategy()
    );

    new TournamentRunner(new ArrayList<>(players)).run();
  }


}
