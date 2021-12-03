package org.zavaglia.advent2021;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Question01 extends Question {
    public Question01() {
        super(1);
    }

    // return count where a successor element is larger than predecessor, i.e. intList[i] < intList[i+1]
    int CountLargerSuccessors(List<Integer> intList) {
        return IntStream.range(0, intList.size() - 1)
                .map(i -> intList.get(i) < intList.get(i + 1) ? 1 : 0)
                .reduce(0, Integer::sum);
    }

    public long Part1() throws IOException {
        var ints = GetIntegers();
        return CountLargerSuccessors(ints);
    }

    public long Part1Expected() {
        return 1393L;
    }

    public long Part2() throws IOException {
        var ints = GetIntegers();
        // compute the aggregates x[a] + x[a+1] + x[a+2]
        var aggregates = IntStream.range(0, ints.size() - 2)
                .mapToObj(i -> ints.get(i) + ints.get(i + 1) + ints.get(i + 2))
                .collect(Collectors.toList());
        return CountLargerSuccessors(aggregates);
    }

    public long Part2Expected() {
        return 1359L;
    }

}
