package org.zavaglia.advent2021;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Question04 extends Question {

    public Question04() {
        super(4);
    }

    record Board(int[][] value, boolean[][] marked) {

        public Board(int[][] value)  {
            this(value, new boolean[5][5]);
        }

        public boolean mark(int number) {
            var hasMarked = false;
            for (var x = 0; x < 5; x++) {
                for (var y = 0; y < 5; y++) {
                    if (value[x][y] == number) {
                        hasMarked = true;
                        marked[x][y] = true;
                    }
                }
            }
            return hasMarked;
        }
        public boolean hasSolution() {
            for (var i = 0; i < 5; i++) {
                var solvedAcross = true;
                var solvedDown = true;
                for (var j = 0; j < 5; j++) {
                    solvedAcross = solvedAcross && marked[i][j];
                    solvedDown = solvedDown && marked[j][i];
                }
                if (solvedAcross || solvedDown) {
                    return true;
                }
            }
            return false;
        }

        public int score() {
            int sum = 0;
            for (var i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (!marked[i][j]) {
                        sum += value[i][j];
                    }
                }
            }
            return sum;
        }

    }

    List<Integer> GetNumberDraw() throws IOException {
        return Arrays.stream(GetLines().get(0).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    List<Board> GetBoards() throws IOException {
        var lines = GetLines();

        var result = new ArrayList<Board>();
        for (var baseIndex = 2; baseIndex < lines.size(); baseIndex += 6) {
            var values = new int[5][5];
            for (var row = 0; row < 5; row++) {
                var numbers = Arrays.stream(lines.get(baseIndex + row).trim().split(" +"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for (var column = 0; column < 5; column++) {
                    values[row][column] = numbers.get(column);
                }
            }
            result.add(new Board(values));
        }
        return result;
    }

    public long Part1() throws IOException {
        final var boards = GetBoards();

        for (var number: GetNumberDraw()) {
            for (var board: boards) {
                board.mark(number);
                if (board.hasSolution()) {
                    return board.score() * number;
                }
            }
        }
        return -1;
    }

    public long Part1Expected() {
        return 58374;
    }

    public long Part2() throws IOException {
        final var boards = GetBoards();
        final var unsolvedBoards = new HashSet<>(boards);

        for (var number: GetNumberDraw()) {
            for (var board: boards) {
                if (board.mark(number) && board.hasSolution()) {
                    unsolvedBoards.remove(board);
                    if (unsolvedBoards.size() == 0) {
                        return board.score() * number;
                    }
                }
            }
        }
        return -1;
    }

    public long Part2Expected() {
        return 11377;
    }
}
