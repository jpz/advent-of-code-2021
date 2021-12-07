package org.zavaglia.advent2021;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Question07 extends Question {

    public long part1() {
        final var minVal = getNumbers().stream().min(Integer::compare).get();
        final var maxVal = getNumbers().stream().max(Integer::compare).get();
        var minCost = Integer.MAX_VALUE;
        for (var i = minVal; i <= maxVal; i++) {
            final var ii = i;
            final var cost = getNumbers().stream().map(n -> Math.abs(n - ii)).reduce(0, Integer::sum);
            minCost = Math.min(cost, minCost);
        }

        return minCost;
    }

    long sumArithmeticSeries(long start, long step, long count) {
        final var end = start + step * (count - 1);
        return count * (start + end) / 2;
    }

    public long part2() {
        final var minVal = getNumbers().stream().min(Integer::compare).get();
        final var maxVal = getNumbers().stream().max(Integer::compare).get();
        var minCost = Long.MAX_VALUE;
        for (var i = minVal; i <= maxVal; i++) {
            final var ii = i;
            final var cost = getNumbers().stream().map(
                    n -> sumArithmeticSeries(1, 1, Math.abs(n - ii))
            ).reduce(0L, Long::sum);
            minCost = Math.min(cost, minCost);
        }

        return minCost;
    }

    List<Integer> getNumbers() {
        return Arrays.stream(getInputText().get(0).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
