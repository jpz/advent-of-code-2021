package org.zavaglia.advent2021;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Question24 extends Question {

    List<List<Instruction>> parseInstructions(List<String> instructionText) {
        var programs = new ArrayList<List<Instruction>>();
        for (var line : instructionText) {
            var arr = line.split(" ");
            var instruction = new Instruction(
                    InstructionCode.valueOf(arr[0].toUpperCase()),
                    arr[1],
                    arr.length > 2 ? arr[2] : null);
            if (instruction.code == InstructionCode.INP) {
                programs.add(new ArrayList<>());
            }
            programs.get(programs.size() - 1).add(instruction);
        }
        return programs;
    }

    int divTruncate(int a, int b) {
        var result = ((double) a) / b;
        if (result < 0) {
            return (int) -Math.floor(-result);
        } else {
            return (int) Math.floor(result);
        }
    }

    int runProgram(Inputs input, List<Instruction> instructions) {
        var state = new State(0, 0, input.z, 0);
        for (var instruction : instructions) {
            state = switch (instruction.code) {
                case INP -> state.update(instruction.param1, input.input);
                case ADD -> state.update(instruction.param1,
                        state.getValue(instruction.param1) + state.getValue(instruction.param2));
                case MUL -> state.update(instruction.param1,
                        state.getValue(instruction.param1) * state.getValue(instruction.param2));
                case DIV -> state.update(instruction.param1,
                        divTruncate(state.getValue(instruction.param1), state.getValue(instruction.param2)));
                case MOD -> state.update(instruction.param1,
                        state.getValue(instruction.param1) % state.getValue(instruction.param2));
                case EQL -> state.update(instruction.param1,
                        state.getValue(instruction.param1) == state.getValue(instruction.param2)
                                ? 1 : 0);
            };
        }
        return state.z;
    }

    // the 5th instruction is either "div z 26" or "div z 1"
    // those which divide z by 26 potentially have a domain 26 time the range
    // of the output z, thus "shrinking" the domain of the next function.
    boolean isShrinker(List<Instruction> program) {
        return program.get(4).param2.equals("26");
    }

    /*
    function is either of the form:

    # grower
    def prog_div(w, z, a, b):
        if (z * 26 + a) != w:
            z = z + w + b
        else:
            return z / 26
        return z

    # shrinker
    def prog(w, z, a, b):
        test = (z * 26 + a) != w
        if test:
            z = z * 26 + w + b
        return z

    The following function finds solutions of the form (w, z) where (a,b) are embedded in "program",
    where the result of the function is any value in the set zTargetValue.

     */
    Map<Inputs, Integer> getResultsForZResults(List<Instruction> program, Set<Integer> zTargetValues) {
        var results = new HashMap<Inputs, Integer>();
        Set<Integer> zValues;

        if (isShrinker(program)) {
            zValues = zTargetValues.stream().map(i -> i * 26)
                    .flatMap(i -> IntStream.range(i, i + 26).boxed())
                    .filter(i -> i >= 0)
                    .collect(Collectors.toSet());
            zValues.addAll(zTargetValues);
        } else {
            zValues = zTargetValues.stream().map(i -> i / 26)
                    .flatMap(i -> IntStream.range(i, i + 9).boxed())
                    .filter(i -> i >= 0)
                    .collect(Collectors.toSet());
            zValues.addAll(zTargetValues);
        }

        for (var z : zValues) {
            for (var i = 1; i <= 9; i++) {
                var input = new Inputs(z, i);
                var zResult = runProgram(input, program);
                if (zTargetValues.contains(zResult))
                    results.put(input, zResult);
            }
        }
        return results;
    }

    public long part1() {
        var programs = parseInstructions(getInputText());
        var digitSolutions = getDigitSolutions(programs);
        return getLargestNumber(digitSolutions);
    }

    private ArrayList<Map<Inputs, Integer>> getDigitSolutions(List<List<Instruction>> programs) {
        var results = new ArrayList<Map<Inputs, Integer>>(Collections.nCopies(programs.size(), null));
        var zTargetValues = Set.of(0);

        for (var i = programs.size() - 1; i >= 0; i--) {
            var result = getResultsForZResults(programs.get(i), zTargetValues);
            results.set(i, result);
            zTargetValues = result.keySet().stream().map(input -> input.z).collect(Collectors.toSet());
        }
        return results;
    }

    public long part2() {
        var programs = parseInstructions(getInputText());
        var digitSolutions = getDigitSolutions(programs);
        return getSmallestNumber(digitSolutions);
    }


    long getLargestNumber(ArrayList<Map<Inputs, Integer>> graph) {
        var depth = 0;
        var zValues = Set.of(0);
        var digits = new ArrayList<Integer>();

        while (depth < 14) {
            final var zValuesFinal = zValues;
            var maxDigit = graph.get(depth).entrySet().stream()
                    .filter(es -> zValuesFinal.contains(es.getKey().z))
                    .max(Comparator.comparingInt(e -> e.getKey().input)).get().getKey().input;

            digits.add(maxDigit);

            zValues = graph.get(depth).entrySet().stream()
                    .filter(es -> es.getKey().input == maxDigit && zValuesFinal.contains(es.getKey().z))
                    .map(es -> es.getValue()).collect(Collectors.toSet());
            depth++;
        }

        var accum = 0L;
        for (var digit : digits)
            accum = digit + 10 * accum;

        return accum;
    }

    long getSmallestNumber(ArrayList<Map<Inputs, Integer>> graph) {
        var depth = 0;
        var zValues = Set.of(0);
        var digits = new ArrayList<Integer>();

        while (depth < 14) {
            final var zValuesFinal = zValues;
            var minDigit = graph.get(depth).entrySet().stream()
                    .filter(es -> zValuesFinal.contains(es.getKey().z))
                    .min(Comparator.comparingInt(e -> e.getKey().input)).get().getKey().input;

            digits.add(minDigit);

            zValues = graph.get(depth).entrySet().stream()
                    .filter(es -> es.getKey().input == minDigit && zValuesFinal.contains(es.getKey().z))
                    .map(es -> es.getValue()).collect(Collectors.toSet());
            depth++;
        }

        var accum = 0L;
        for (var digit : digits)
            accum = digit + 10 * accum;

        return accum;
    }

    enum InstructionCode {
        INP,
        ADD,
        MUL,
        DIV,
        MOD,
        EQL
    }

    record Instruction(InstructionCode code, String param1, String param2) {
    }

    record State(int x, int y, int z, int w) {
        static private final Pattern pattern = Pattern.compile("-?\\d+");

        State update(String register, int value) {
            var x = this.x;
            var y = this.y;
            var z = this.z;
            var w = this.w;
            switch (register) {
                case "x" -> x = value;
                case "y" -> y = value;
                case "z" -> z = value;
                case "w" -> w = value;
                default -> throw new RuntimeException("unrecognised register - " + register);
            }
            return new State(x, y, z, w);
        }

        int getValue(String registerOrNumber) {
            if (isNumeric(registerOrNumber)) {
                return Integer.parseInt(registerOrNumber);
            } else {
                return switch (registerOrNumber) {
                    case "x" -> x;
                    case "y" -> y;
                    case "z" -> z;
                    case "w" -> w;
                    default -> throw new RuntimeException("unrecognised register - " + registerOrNumber);
                };
            }
        }

        private boolean isNumeric(String strNum) {
            if (strNum == null) {
                return false;
            }
            return pattern.matcher(strNum).matches();
        }
    }

    record Inputs(int z, int input) {
        Inputs {
            assert input >= 1 && input <= 9;
        }
    }

}
/*
# script for analysis of input, which shows homogeneity of subprograms

lines = open("q24.txt").readlines()

program = []
for line in lines:
    words = line.split(' ')
    if words[0] == 'inp':
        program.append([])
    program[-1].append(words)

print("lengths:")
for subprogram in program:
    print(len(subprogram))

for i in range(18):
    print(f"instruction {i}")
    for subprogram in program:
        print(subprogram[i])
 */
