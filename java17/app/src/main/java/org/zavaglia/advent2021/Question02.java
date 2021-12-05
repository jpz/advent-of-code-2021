package org.zavaglia.advent2021;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Question02 extends Question {

    public Question02() {
        super(2);
    }

    List<Command> GetCommands() throws IOException {
        return GetInputText().stream()
                .map(line -> line.split(" "))
                .map(arr -> new Command(arr[0], Integer.parseInt(arr[1])))
                .collect(Collectors.toList());
    }

    public long Part1() throws IOException {
        long depth = 0;
        long lateral = 0;

        for (var command : GetCommands()) {
            switch (command.direction) {
                case "up" -> depth -= command.magnitude;
                case "down" -> depth += command.magnitude;
                case "forward" -> lateral += command.magnitude;
            }
        }
        return depth * lateral;
    }

    public long Part1Expected() {
        return 1882980;
    }

    public long Part2() throws IOException {
        long depth = 0;
        long lateral = 0;
        long aim = 0;

        for (var command : GetCommands()) {
            switch (command.direction) {
                case "up" -> aim -= command.magnitude;
                case "down" -> aim += command.magnitude;
                case "forward" -> {
                    lateral += command.magnitude;
                    depth += aim * command.magnitude;
                }
            }
        }
        return depth * lateral;
    }

    public long Part2Expected() {
        return 1971232560;
    }

    record Command(String direction, int magnitude) {
    }
}
