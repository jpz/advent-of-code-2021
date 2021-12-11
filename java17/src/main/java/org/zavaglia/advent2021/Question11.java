package org.zavaglia.advent2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Question11 extends Question {

    public long part1() {
        var arr = getRowColumnArray();

        var totalFlashes = 0;
        for (var i = 0; i < 100; i++) {
            incrementAll(arr);
            var prevFlashes = totalFlashes-1;

            while (prevFlashes != totalFlashes) {
                prevFlashes = totalFlashes;
                totalFlashes += flash(arr);
            }
        }

        return totalFlashes;
    }


    public long part2() {
        var arr = getRowColumnArray();

        var cycle = 0;
        var flashes = 0;
        while (flashes != arr.length * arr[0].length) {
            cycle++;
            incrementAll(arr);
            flashes = 0;
            var prevFlashes = flashes-1;

            while (prevFlashes != flashes) {
                prevFlashes = flashes;
                flashes += flash(arr);
            }
        }

        return cycle;
    }


    void incrementAll(int[][] arr) {
        for (var r = 0; r < arr.length; r++) {
            for (var c = 0; c < arr[0].length; c++) {
                arr[r][c]++;
            }
        }
    }

    // flashes all that are greater than 9, sets them to zero
    // increments neighbours that are non-zero.  A neighbour of value zero
    // has flashed and should not be incremented again.
    // returns count of items flashed.
    int flash(int[][] arr) {
        var flashCount = 0;
        for (var r = 0; r < arr.length; r++) {
            for (var c = 0; c < arr[0].length; c++) {
                if (arr[r][c] > 9) {
                    arr[r][c] = 0;
                    flashCount++;
                    var neighbours = getNeighbours(arr, r, c);
                    for (var coord: neighbours) {
                        if (arr[coord.row][coord.col] != 0) {
                            arr[coord.row][coord.col]++;
                        }
                    }
                }
            }
        }
        return flashCount;
    }

    List<Coordinates> getNeighbours(int[][] arr, int row, int col) {
        // produce a list of coordinates which are neighbours
        return IntStream
                .range(Math.max(0, row-1), Math.min(row+2, arr.length))
                .mapToObj(r ->
                        IntStream
                                .range(Math.max(0, col-1), Math.min(col+2, arr[0].length))
                                .mapToObj(c -> new Coordinates(r, c)))
                .flatMap(Function.identity())
                //.filter(coord -> coord.row != row || coord.col != col)
                .collect(Collectors.toList());
    }

    record Coordinates(int row, int col) {}

}
