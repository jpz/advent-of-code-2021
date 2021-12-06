package org.zavaglia.advent2021;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Question06 extends Question {

    public long part1() {
        var fish = new long[9];
        for (var number : getNumbers()) {
            fish[number]++;
        }

        for (var days = 0; days < 80; days++) {
            var zeros = fish[0];
            for (var i = 0; i < 8; i++) {
                fish[i] = fish[i + 1];
            }
            fish[6] += zeros;
            fish[8] = zeros;
        }
        return Arrays.stream(fish).reduce(0L, Long::sum);
    }

    public long part2() {
        var fish = new long[9];
        for (var number : getNumbers()) {
            fish[number]++;
        }

        for (var days = 0; days < 256; days++) {
            var zeros = fish[0];
            for (var i = 0; i < 8; i++) {
                fish[i] = fish[i + 1];
            }
            fish[6] += zeros;
            fish[8] = zeros;
        }
        return Arrays.stream(fish).reduce(0L, Long::sum);
    }

    List<Integer> getNumbers() {
        return Arrays.stream(getInputText().get(0).split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
