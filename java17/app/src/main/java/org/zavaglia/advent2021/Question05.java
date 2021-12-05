package org.zavaglia.advent2021;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Question05 extends Question {

    public Question05() {
        super(5);
    }

    public Question05(List<String> input) {
        super(5, input);
    }

    List<Line> GetLines() throws IOException {
        return GetInputText().stream()
                .map(Line::fromString)
                .collect(Collectors.toList());
    }

    public long Part1() throws IOException {
        final var lines = GetLines();
        int xMax = 0, yMax = 0;
        for (var line : lines) {
            if (line.start.x > xMax) xMax = line.start.x;
            if (line.end.x > xMax) xMax = line.end.x;
            if (line.start.y > yMax) yMax = line.start.y;
            if (line.end.y > yMax) yMax = line.end.y;
        }
        final var occupiedPoints = new int[xMax + 1][yMax + 1];

        for (var line : GetLines()) {
            if (line.isHorizontal()) {
                for (var x = Math.min(line.start.x, line.end.x); x <= Math.max(line.start.x, line.end.x); x++) {
                    occupiedPoints[x][line.start.y]++;
                }
            }
            if (line.isVertical()) {
                for (var y = Math.min(line.start.y, line.end.y); y <= Math.max(line.start.y, line.end.y); y++) {
                    occupiedPoints[line.start.x][y]++;
                }
            }
        }

        var count = 0;
        for (var y = 0; y <= yMax; y++) {
            for (var x = 0; x <= xMax; x++) {
                if (occupiedPoints[x][y] > 1) {
                    count++;
                }
            }
        }

        return count;
    }

    public long Part1Expected() {
        return 5373;
    }

    public long Part2() throws IOException {
        final var lines = GetLines();
        int xMax = 0, yMax = 0;
        for (var line : lines) {
            if (line.start.x > xMax) xMax = line.start.x;
            if (line.end.x > xMax) xMax = line.end.x;
            if (line.start.y > yMax) yMax = line.start.y;
            if (line.end.y > yMax) yMax = line.end.y;
        }
        final var occupiedPoints = new int[xMax + 1][yMax + 1];

        for (var line : GetLines()) {
            if (line.isHorizontal()) {
                for (var x = Math.min(line.start.x, line.end.x); x <= Math.max(line.start.x, line.end.x); x++) {
                    occupiedPoints[x][line.start.y]++;
                }
            }
            if (line.isVertical()) {
                for (var y = Math.min(line.start.y, line.end.y); y <= Math.max(line.start.y, line.end.y); y++) {
                    occupiedPoints[line.start.x][y]++;
                }
            }
            if (line.isDiagonal()) {
                var gradient = line.gradient();
                assert gradient == 1 || gradient == -1;
                // point the point with the smallest x on the LHS
                var leftPoint = line.start.x < line.end.x ? line.start : line.end;
                var rightPoint = leftPoint == line.start ? line.end : line.start;
                var y = leftPoint.y;
                for (var x = leftPoint.x; x <= rightPoint.x; x++) {
                    occupiedPoints[x][y]++;
                    y += gradient;
                }
            }

        }
        var count = 0;
        for (var y = 0; y <= yMax; y++) {
            for (var x = 0; x <= xMax; x++) {
                if (occupiedPoints[x][y] > 1) {
                    count++;
                }
            }
        }
        return count;
    }

    public long Part2Expected() {
        return 21514;
    }

    record Point(int x, int y) {
    }

    record Line(Point start, Point end) {
        static Line fromString(String s) {
            var points = Arrays.stream(s.replace(" -> ", ",").split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            return new Line(new Point(points.get(0), points.get(1)), new Point(points.get(2), points.get(3)));
        }

        boolean isHorizontal() {
            return start.y == end.y;
        }

        boolean isVertical() {
            return start.x == end.x;
        }

        boolean isDiagonal() {
            return !isHorizontal() && !isVertical() && Math.abs(start.x - end.x) == Math.abs(start.y - end.y);
        }

        int gradient() {
            assert isDiagonal();
            // will return either +1 / -1, and indicates the rate of change of y relative to x, when stepping from
            // min x to max x.
            return (end.y - start.y) / (end.x - start.x);
        }

    }
}
