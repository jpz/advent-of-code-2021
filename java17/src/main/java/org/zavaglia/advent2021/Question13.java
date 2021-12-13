package org.zavaglia.advent2021;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Question13 extends Question {

    public long part1() {
        var coordinates = getCoordinates();
        var folds = getFolds();
        for (var fold : folds) {
            for (var coord : List.copyOf(coordinates)) {
                if (fold.axis == 'x') {
                    if (coord.x >= fold.number) {
                        coordinates.remove(coord);
                        if (coord.x > fold.number) {
                            coordinates.add(new Coordinate(fold.number - (coord.x - fold.number), coord.y));
                        }
                    }
                } else {
                    if (coord.y >= fold.number) {
                        coordinates.remove(coord);
                        if (coord.y > fold.number) {
                            coordinates.add(new Coordinate(coord.x, fold.number - (coord.y - fold.number)));
                        }
                    }
                }
            }
            break; // only one fold
        }
        return coordinates.size();
    }

    void print(Set<Coordinate> coords) {
        var minX = Integer.MAX_VALUE;
        var maxX = Integer.MIN_VALUE;
        var minY = Integer.MAX_VALUE;
        var maxY = Integer.MIN_VALUE;
        for (var coord : coords) {
            minX = Math.min(minX, coord.x);
            minY = Math.min(minY, coord.y);
            maxX = Math.max(maxX, coord.x);
            maxY = Math.max(maxY, coord.y);
        }
        System.out.printf("(%d,%d) to (%d, %d)\n", minX, minY, maxX, maxY);
        for (var y = minY; y <= maxY; y++) {
            for (var x = minX; x <= maxX; x++) {
                if (coords.contains(new Coordinate(x, y))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.print("\n");
        }
    }

    public long part2() {
        var coordinates = getCoordinates();
        var folds = getFolds();
        for (var fold : folds) {
            for (var coord : List.copyOf(coordinates)) {
                if (fold.axis == 'x') {
                    if (coord.x >= fold.number) {
                        coordinates.remove(coord);
                        if (coord.x > fold.number) {
                            coordinates.add(new Coordinate(fold.number - (coord.x - fold.number), coord.y));
                        }
                    }
                } else {
                    if (coord.y >= fold.number) {
                        coordinates.remove(coord);
                        if (coord.y > fold.number) {
                            coordinates.add(new Coordinate(coord.x, fold.number - (coord.y - fold.number)));
                        }
                    }
                }
            }
        }
        print(coordinates);
        return coordinates.size();
    }

    HashSet<Coordinate> getCoordinates() {
        var coordinates = new HashSet<Coordinate>();

        for (var line : getInputText()) {
            if (line.equals("")) {
                break;
            }

            var arr = line.split(",");
            coordinates.add(new Coordinate(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])));
        }
        return coordinates;
    }

    List<Fold> getFolds() {
        var folds = new ArrayList<Fold>();

        var skippingCoords = true;
        for (var line : getInputText()) {
            if (skippingCoords) {
                if (line.equals("")) {
                    skippingCoords = false;
                }
                continue;
            }
            final var prefix = "fold along ";
            assert line.startsWith(prefix);
            final var arr = line.substring(prefix.length()).split("=");

            folds.add(new Fold(arr[0].charAt(0), Integer.parseInt(arr[1])));
        }
        return folds;
    }

    record Coordinate(int x, int y) {
    }

    record Fold(char axis, int number) {
    }
}
