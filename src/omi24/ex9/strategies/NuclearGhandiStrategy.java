package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

import java.util.List;
import java.util.Random;

public class NuclearGhandiStrategy implements GameStrategy {

  /*** Alternative Idee: Strategie "Rage-Quit": Wenn gegner mehr Punkte hat als man selbst Gegner
   * möglichst viel sabotieren oder einfach System.exit() */

  // Eine Urban-Legend in der Videospielszene erzählt von einem Bug in dem Videospiel Sid Meier's Civilization.
  // der NPC Gandhi, auf maximaler peacefulness eingestellt; erreicht schnell das Atomzeitalter im Tech-Tree
  // Doch geht man eine allianz mit ihm ein, so entsteht wegen der zusätzlichen erhöhung des Freundschaft-Werts ein integer underflow.
  // Und Gandhi attackiert sofort mit maximaler Feindseligkeit, seine technologie benützend.

  // Meine Strategie defected automatisch nach 125 Runden Friedensstiftung.
  // Features include: Retaliation-Queue, Friendsangebot-Annahme, Forgiveness und coolen getName()

  private final Random random = new Random();

  @Override
  public String getName() {
    return hasgonenuclear ? nuclearName : normalName;
  }

  private final String normalName = "\033[33;1m" + "[Q1] Nuclear Gandhi" + "\033[0m" + " ";
  private final String nuclearName = "\033[33;1m" + "[Q1] " + "\033[0m" + "\033[31;4;1m" + "Nuclear" + "\033[0m" + "\033[33;1m" + " Gandhi" + "\033[0m" + " ";

  private List<GameAction> me;
  private List<GameAction> enemy;

  private int enemyDefectsTotal;
  private int enemyDefectsRecent;
  private int retaliationQueue;
  private int round;
  private boolean newRound;
  private boolean retaliating;
  private boolean holdRetaliation;
  private boolean hasgonenuclear;

  private int roundcounter;

  private void checkNewRound() {
    if (me.isEmpty()) {
      newRound = true;
    }
  }

  private void newRoundTrackerReset() {
    enemyDefectsTotal = 0;
    enemyDefectsRecent = 0;
    retaliationQueue = 0;
    roundcounter = 0;
    retaliating = false;
    holdRetaliation = false;
    newRound = false;
    hasgonenuclear = false;
  }

  // 1 in 8 chance bei dauerhaftem defecting mal wieder cooperate
  private GameAction friendlySprinkle() {
    if (random.nextInt(8) == 0) {
      return GameAction.COOPERATE;
    }
    return GameAction.DEFECT;
  }

  private void updateTrackers() {
    if (enemy.getLast() == GameAction.DEFECT) {
      enemyDefectsTotal++;
      enemyDefectsRecent++;
    } else {
      enemyDefectsRecent = 0;
    }

  }

  // Sehr ineffizientes pattern matching, aber findet isolierte defects ("ausrutscher") und verzeiht diese
  private void forgive() {
    int margin = 5;
    if (margin * 2 < round) {
      return;
    }
    for (int i = margin; i < round - margin; i++) {
      boolean hasOtherDefects = false;
      for (int j = 0; j <= margin * 2; j++) {
        if (j == margin) {
          continue;
        }
        if (enemy.get(i + j) == GameAction.DEFECT) {
          hasOtherDefects = true;
          break;
        }
      }
      if (!hasOtherDefects) {
        enemyDefectsTotal--;
      }
    }
  }

  // Anti-cheat
  public boolean playingAgainstTobiasWurzer() {
    return roundcounter != round;
  }


  @Override
  public GameAction playRound(GameState state) {

    me = state.player1() == this ? state.player1Actions() : state.player2Actions();
    enemy = state.player1() != this ? state.player1Actions() : state.player2Actions();

    round = me.size();
    checkNewRound();

    if (newRound) {
      newRoundTrackerReset();
      roundcounter++;
      return GameAction.COOPERATE;
    }

    updateTrackers();

    // Necessary antimeasures. (Nothing personal)
    if (playingAgainstTobiasWurzer()) {
      return GameAction.DEFECT;
    }
    roundcounter++;

    // Nach x Runden ist Gandhi böse.
    // (Mit 1-2 sprinkles of friendliness dazwischen um generous tit-for-tats abzuziehen)

    if (round >= 125) {
      hasgonenuclear = true;
      return friendlySprinkle();
    }

    // Hat Gegner gerade defected? Wenn ja merk ich mir das!
    updateTrackers();

    // Gandhi vergibt seinem Gegner kleinere ausrutscher (Wenn links und rechts davor mehr als 5x cooperated wurde)
    forgive();

    // Wenn der Gegner 3x hintereinander defected dann gibt es einen verhältnismäßig größeren Gegenschlag
    // Wenn der Gegner insgesamt mehr als 15x defected hat dann gibt es einen Gegenschlag.
    if (enemyDefectsRecent >= 3) {
      retaliationQueue = 5;
    } else if (enemyDefectsTotal >= 15) {
      retaliationQueue = 8;
    }

    // Friedensangebot annehmen wenn während meiner retaliation er cooperated.
    if (retaliating && enemy.getLast() == GameAction.COOPERATE) {
      holdRetaliation = true;
    }

    // Wenn dann aber der frieden auf probation period missbraucht wird... noch mehr retaliation.
    if (holdRetaliation && enemy.getLast() == GameAction.DEFECT) {
      holdRetaliation = false;
      retaliationQueue += 5;
    }

    // Perform Retaliations
    if (retaliationQueue > 0 && !holdRetaliation) {
      retaliationQueue--;
      enemyDefectsTotal -= 2; //doppelt gezählt
      retaliating = true;
      return GameAction.DEFECT;
    } else {
      retaliating = false;
    }

    // Gandhi ist grundsätzlich freundlich
    return GameAction.COOPERATE;
  }
}
