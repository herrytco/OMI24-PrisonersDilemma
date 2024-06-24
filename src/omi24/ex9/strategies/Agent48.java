package omi24.ex9.strategies;

import omi24.ex9.GameAction;
import omi24.ex9.GameState;

public class Agent48 implements GameStrategy {  // Tit-for-Tat

    @Override
    public String getName() {
        return "Agent48";
    }

    @Override
    public GameAction playRound(GameState state) {

        if (state.getPlayer1Actions().isEmpty()) { // Erste Runde? Start mit Cooperate
            return GameAction.COOPERATE;
        }

        GameAction opponentLastAction = state.getPlayer2Actions().getLast();   //Vergleich mit gegner Strategie

        if (Math.random() < 0.25) { // Letzter Gegner Befehl wird wiederholt mit 25% random defect chance.
            return GameAction.DEFECT;
        }

        return opponentLastAction;
    }
}