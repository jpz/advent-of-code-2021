package org.zavaglia.advent2021;

import java.util.*;

public class Question03 extends Question {

    public Question03() {
        super();
    }

    public long part1() {
        final var numbers = getInputText();
        final var bitcounts = getBitCounts(numbers);
        final var bits = numbers.get(0).length();

        var gamma = 0L;
        var epsilon = 0L;
        for (var i = 0; i < bits; i++) {
            gamma *= 2;
            epsilon *= 2;
            if (bitcounts.get(i) * 2 > numbers.size()) {
                gamma += 1;
            } else {
                epsilon += 1;
            }
        }

        return gamma * epsilon;
    }

    public long part2() {
        final var numbers = getInputText();
        final var bits = numbers.get(0).length();

        final var o2GeneratorCandidates = new HashSet<>(numbers);
        final var co2ScrubberCandidates = new HashSet<>(numbers);

        for (var i = 0; i < bits; i++) {
            if (o2GeneratorCandidates.size() > 1) {
                final var bitcount = getBitCount(o2GeneratorCandidates, i);
                var eliminate = '0';
                if (bitcount * 2 < o2GeneratorCandidates.size()) {
                    eliminate = '1';
                }
                for (var candidate : new ArrayList<>(o2GeneratorCandidates)) {
                    if (candidate.charAt(i) == eliminate) {
                        o2GeneratorCandidates.remove(candidate);
                    }
                }
            }
            if (co2ScrubberCandidates.size() > 1) {
                final var bitcount = getBitCount(co2ScrubberCandidates, i);
                var eliminate = '1';
                if (bitcount * 2 < co2ScrubberCandidates.size()) {
                    eliminate = '0';
                }
                for (var candidate : new ArrayList<>(co2ScrubberCandidates)) {
                    if (candidate.charAt(i) == eliminate) {
                        co2ScrubberCandidates.remove(candidate);
                    }
                }
            }
        }
        assert o2GeneratorCandidates.size() == 1;
        assert co2ScrubberCandidates.size() == 1;
        final var o2GeneratorRating = Long.parseLong(o2GeneratorCandidates.stream().findFirst().get(), 2);
        final var co2ScrubberRating = Long.parseLong(co2ScrubberCandidates.stream().findFirst().get(), 2);

        return o2GeneratorRating * co2ScrubberRating;
    }

    private HashMap<Integer, Integer> getBitCounts(List<String> numbers) {
        final var bits = numbers.get(0).length();
        final var bitcounts = new HashMap<Integer, Integer>();
        for (var i = 0; i < bits; i++) {
            bitcounts.put(i, getBitCount(numbers, i));
        }
        return bitcounts;
    }

    private int getBitCount(Collection<String> numbers, int idx) {
        int bitcount = 0;
        for (var number : numbers) {
            if (number.charAt(idx) == '1') {
                bitcount++;
            }
        }
        return bitcount;
    }

}
