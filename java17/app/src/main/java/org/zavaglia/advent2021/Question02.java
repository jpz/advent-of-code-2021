package org.zavaglia.advent2021;

import java.util.List;
import java.util.stream.Collectors;

public class Question02 extends Question {

    public Question02() {
        super();
    }

    List<Command> getCommands() {
        return getInputText().stream()
                .map(line -> line.split(" "))
                .map(arr -> new Command(arr[0], Integer.parseInt(arr[1])))
                .collect(Collectors.toList());
    }

    public long part1() {
        long depth = 0;
        long lateral = 0;

        for (var command : getCommands()) {
            switch (command.direction) {
                case "up" -> depth -= command.magnitude;
                case "down" -> depth += command.magnitude;
                case "forward" -> lateral += command.magnitude;
            }
        }
        return depth * lateral;
    }

    public long part2() {
        long depth = 0;
        long lateral = 0;
        long aim = 0;

        for (var command : getCommands()) {
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

    record Command(String direction, int magnitude) {
    }
}
