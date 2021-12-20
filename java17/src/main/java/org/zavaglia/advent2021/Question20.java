package org.zavaglia.advent2021;

import java.util.HashSet;
import java.util.Set;

public class Question20 extends Question {

    int countSurroundingPixels(Set<RowColumn> image, RowColumn rc) {
        var count = 0;
        var pixels = 0;
        for (var r = rc.row - 1; r <= rc.row + 1; r++) {
            for (var c = rc.col - 1; c <= rc.col + 1; c++) {
                count *= 2;
                pixels++;
                if (image.contains(new RowColumn(r, c))) {
                    count += 1;
                }
            }
        }
        assert pixels == 9;
        return count;
    }

    Set<RowColumn> enhance(Set<RowColumn> image, String algorithm) {
        var d = Dimensions.create(image);
        var newImage = new HashSet<RowColumn>();

        for (var r = d.rMin - 10; r <= d.rMax + 10; r++) {
            for (var c = d.cMin - 10; c <= d.cMax + 10; c++) {
                var idx = countSurroundingPixels(image, new RowColumn(r, c));
                if (algorithm.charAt(idx) == '#') {
                    newImage.add(new RowColumn(r, c));
                }
            }
        }
        return newImage;
    }

    void printImage(Set<RowColumn> image) {
        var d = Dimensions.create(image);
        for (var r = d.rMin - 1; r <= d.rMax + 1; r++) {
            for (var c = d.cMin - 1; c <= d.cMax + 1; c++) {
                if (image.contains(new RowColumn(r, c)))
                    System.out.printf("#");
                else
                    System.out.printf(".");
            }
            System.out.println();
        }
    }

    public long part1() {
        var algorithm = getInputText().get(0);
        assert algorithm.length() == 512;

        // sparse map, which identifies bits which are switched on
        Set<RowColumn> image = new HashSet<>();

        for (var r = 0; r < getInputText().size() - 2; r++) {
            for (var c = 0; c < getInputText().get(2).length(); c++) {
                if (getInputText().get(r + 2).charAt(c) == '#') {
                    image.add(new RowColumn(r, c));
                }
            }
        }

        image = Set.copyOf(image);

        printImage(image);
        var d = Dimensions.create(image);
        System.out.println("size = " + image.size());
        System.out.println("-------------------");
        image = enhance(image, algorithm);
        printImage(image);
        System.out.println("size = " + image.size());
        System.out.println("-------------------");
        image = enhance(image, algorithm);
        scrubBorders(image, d);
        System.out.println("size = " + image.size());
        printImage(image);

        return image.size();
    }

    private void scrubBorders(Set<RowColumn> image, Dimensions d) {
        for (var r = d.rMin - 20; r <= d.rMax + 20; r++)
            for (var c = d.cMin - 20; c <= d.cMax + 20; c++)
                if (r < d.rMin - 5 || r > d.rMax + 5 || c < d.cMin - 5 || c > d.cMax + 5)
                    image.remove(new RowColumn(r, c));
    }

    public long part2() {
        var algorithm = getInputText().get(0);
        assert algorithm.length() == 512;

        // sparse map, which identifies bits which are switched on
        Set<RowColumn> image = new HashSet<>();

        for (var r = 0; r < getInputText().size() - 2; r++) {
            for (var c = 0; c < getInputText().get(2).length(); c++) {
                if (getInputText().get(r + 2).charAt(c) == '#') {
                    image.add(new RowColumn(r, c));
                }
            }
        }

        image = Set.copyOf(image);

        for (var i = 0; i < 25; i++) {
            var d = Dimensions.create(image);
            image = enhance(image, algorithm);
            image = enhance(image, algorithm);
            scrubBorders(image, d);
        }

        return image.size();
    }

    record RowColumn(int row, int col) {
    }

    record Dimensions(int rMin, int rMax, int cMin, int cMax) {
        static Dimensions create(Set<RowColumn> image) {
            var rMin = Integer.MAX_VALUE;
            var rMax = Integer.MIN_VALUE;
            var cMin = Integer.MAX_VALUE;
            var cMax = Integer.MIN_VALUE;

            for (var rc : image) {
                rMin = Math.min(rMin, rc.row);
                rMax = Math.max(rMax, rc.row);
                cMin = Math.min(cMin, rc.col);
                cMax = Math.max(cMax, rc.col);
            }

            return new Dimensions(rMin, rMax, cMin, cMax);
        }
    }
}
