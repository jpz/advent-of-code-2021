package org.zavaglia.advent2021;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Question21 extends Question {

    // map of number of ways from a draw of three three-sided dice can reach a sum (between 3-9)
    Map<Integer, Integer> sumDistribution = Map.of(
            3, 1,
            4, 3,
            5, 6,
            6, 7,
            7, 6,
            8, 3,
            9, 1
    );

    int player1StartingPosition() {
        var p = Pattern.compile("Player 1 starting position: (?<position>\\d+)");
        var result = p.matcher(getInputText().get(0));
        result.matches();
        return Integer.parseInt(result.group("position"));
    }

    int player2StartingPosition() {
        var p = Pattern.compile("Player 2 starting position: (?<position>\\d+)");
        var result = p.matcher(getInputText().get(1));
        result.matches();
        return Integer.parseInt(result.group("position"));
    }

    public long part1() {
        var player1Position = player1StartingPosition();
        var player2Position = player2StartingPosition();

        var die = new Die();

        int player1Sum = 0;
        int player2Sum = 0;

        while (true) {
            player1Position = (player1Position + die.draw() + die.draw() + die.draw() - 1) % 10 + 1;
            player1Sum += player1Position;
            if (player1Sum >= 1000)
                return die.getRolls() * player2Sum;
            player2Position = (player2Position + die.draw() + die.draw() + die.draw() - 1) % 10 + 1;
            player2Sum += player2Position;
            if (player2Sum >= 1000)
                return die.getRolls() * player1Sum;
        }


    }

    boolean hasPositionsBelow21(HashMap<GameState, Long> gameStates) {
        for (var gs : gameStates.keySet()) {
            if (gs.p1position < 21 && gs.p2position < 21)
                return true;
        }
        return false;
    }

    public long part2() {
        // a map of possible GameStates to the number of paths that are possibele to that given game state
        var gameStates = new HashMap<GameState, Long>();
        gameStates.put(new GameState(player1StartingPosition(), player2StartingPosition(), 0, 0), 1L);

        var winningP1Paths = 0L;
        var winningP2Paths = 0L;
        while (hasPositionsBelow21(gameStates)) {
            // do player 1 moves
            var newGameState1 = new HashMap<GameState, Long>();
            for (var gs : gameStates.keySet()) {
                for (var sd : sumDistribution.keySet()) {
                    long paths = sumDistribution.get(sd) * gameStates.get(gs);
                    int newPosition = (gs.p1position + sd - 1) % 10 + 1;
                    int newSum = gs.p1score + newPosition;
                    if (newSum >= 21) {
                        // don't add completed game state to the gamestate hashmap,
                        // but record it as completed
                        winningP1Paths += paths;
                    } else {
                        // update the game state, additively if prior paths to same state exist
                        var newKey = new GameState(newPosition, gs.p2position, newSum, gs.p2score);
                        var prevPaths = newGameState1.get(newKey);
                        newGameState1.put(newKey, paths + (prevPaths == null ? 0 : prevPaths));
                    }
                }
            }
            // do player 2 moves
            var newGameState2 = new HashMap<GameState, Long>();
            for (var gs : newGameState1.keySet()) {
                for (var sd : sumDistribution.keySet()) {
                    long paths = sumDistribution.get(sd) * newGameState1.get(gs);
                    int newPosition = (gs.p2position + sd - 1) % 10 + 1;
                    int newSum = gs.p2score + newPosition;
                    if (newSum >= 21) {
                        // don't add completed game state to the gamestate hashmap,
                        // but record it as completed
                        winningP2Paths += paths;
                    } else {
                        // update the game state, additively if prior paths to same state exist
                        var newKey = new GameState(gs.p1position, newPosition, gs.p1score, newSum);
                        var prevPaths = newGameState2.get(newKey);
                        newGameState2.put(newKey, paths + (prevPaths == null ? 0 : prevPaths));
                    }
                }
            }

            gameStates = newGameState2;

        }

        return Math.max(winningP1Paths, winningP2Paths);
    }

    // a given game state
    record GameState(int p1position, int p2position, int p1score, int p2score) {
    }

    class Die {
        private int face;
        private int rolls;

        public Die() {
            face = 0;
            rolls = 0;
        }

        int draw() {
            face = (face++) % 100 + 1;
            rolls++;
            return face;
        }

        int getRolls() {
            return rolls;
        }
    }

}
