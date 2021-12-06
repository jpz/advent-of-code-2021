package org.zavaglia.advent2021;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Question01 extends Question {

    public long part1() {
        var ints = getIntegers();
        return countLargerSuccessors(ints);
    }

    public long part2() {
        var ints = getIntegers();
        // compute the aggregates x[a] + x[a+1] + x[a+2]
        var aggregates = IntStream.range(0, ints.size() - 2)
                .mapToObj(i -> ints.get(i) + ints.get(i + 1) + ints.get(i + 2))
                .collect(Collectors.toList());
        return countLargerSuccessors(aggregates);
    }

    // return count where a successor element is larger than predecessor, i.e. intList[i] < intList[i+1]
    int countLargerSuccessors(List<Integer> intList) {
        return IntStream.range(0, intList.size() - 1)
                .map(i -> intList.get(i) < intList.get(i + 1) ? 1 : 0)
                .reduce(0, Integer::sum);
    }
}
