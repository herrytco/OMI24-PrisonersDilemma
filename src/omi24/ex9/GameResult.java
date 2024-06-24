package omi24.ex9;

import omi24.ex9.strategies.GameStrategy;

public record GameResult(int player1Score, int player2Score, GameStrategy winner, GameStrategy loser) {
}
