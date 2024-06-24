package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

import java.util.Random;

public class TheWinnerStrategy implements GameStrategy {
    private int roundCounter = 0;
    private final Random rng = new Random();

    @Override
    public String getName() {
        return "TheWinnerStrategie";
    }

    @Override
    public GameAction playRound(GameState state) {
        roundCounter++;
        int lastRoundIndex = state.getPlayer2Actions().size() - 1;
        GameAction lastOpponentAction = lastRoundIndex >= 0 ? state.getPlayer2Actions().get(lastRoundIndex) : GameAction.DEFECT;

        if (roundCounter == 1) {
            return GameAction.DEFECT;
        } else if (roundCounter == 2 || roundCounter == 3) {
            return lastOpponentAction;
        } else {
            roundCounter = 0; //wieder bei null anfangen für 3. runde
            return rng.nextBoolean() ? GameAction.COOPERATE : GameAction.DEFECT;
        }
    }

}