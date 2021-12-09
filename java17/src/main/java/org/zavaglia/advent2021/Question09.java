package org.zavaglia.advent2021;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;


public class Question09 extends Question {

    public long part1() {
        int[][] heightMap = getHeightMap();

        int countLowPoints = 0;
        int sumLowPointHeights = 0;

        for (var point : computeLowPoints(heightMap)) {
            countLowPoints++;
            sumLowPointHeights += heightMap[point.y][point.x];
        }

        return countLowPoints + sumLowPointHeights;
    }

    public long part2() {
        int[][] heightMap = getHeightMap();

        final var basinSizes = new ArrayList<Integer>();

        for (var point : computeLowPoints(heightMap)) {
            var size = floodFill(heightMap, point);
            basinSizes.add(size);
        }
        basinSizes.sort(Comparator.comparingInt(a -> -a));

        return basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2);
    }

    int floodFill(int[][] heightMap, Point point) {
        final var floodPoints = new HashSet<Point>();
        final var queue = new HashSet<>(neighbours(heightMap, point));
        floodPoints.add(point);

        while (queue.size() > 0) {
            for (var currentPoint : queue.stream().toList()) {
                if (!floodPoints.contains(currentPoint)) {
                    floodPoints.add(currentPoint);
                    var n = neighbours(heightMap, currentPoint);
                    n.removeAll(floodPoints);
                    queue.addAll(n);
                }
                queue.remove(currentPoint);
            }
        }

        return floodPoints.size();
    }

    List<Point> neighbours(int[][] heightMap, Point point) {
        var retval = new ArrayList<Point>();
        if (point.x > 0 && heightMap[point.y][point.x - 1] < 9) {
            retval.add(new Point(point.x - 1, point.y));
        }
        if (point.x < heightMap[0].length - 1 && heightMap[point.y][point.x + 1] < 9) {
            retval.add(new Point(point.x + 1, point.y));
        }
        if (point.y > 0 && heightMap[point.y - 1][point.x] < 9) {
            retval.add(new Point(point.x, point.y - 1));
        }
        if (point.y < heightMap.length - 1 && heightMap[point.y + 1][point.x] < 9) {
            retval.add(new Point(point.x, point.y + 1));
        }
        return retval;
    }


    private int[][] getHeightMap() {
        var tmpHeightMap = getInputText().stream().map(
                line -> line.chars().map(ch -> ch - '0').toArray()
        ).toList();
        var heightMap = tmpHeightMap.toArray(new int[tmpHeightMap.size()][]);
        return heightMap;
    }

    List<Point> computeLowPoints(int[][] heightMap) {
        var lowPoints = new ArrayList<Point>();
        for (var x = 0; x < heightMap[0].length; x++) {
            for (var y = 0; y < heightMap.length; y++) {
                if (x > 0 && heightMap[y][x - 1] <= heightMap[y][x])
                    continue;
                if (x < heightMap[0].length - 1 && heightMap[y][x] >= heightMap[y][x + 1])
                    continue;
                if (y > 0 && heightMap[y - 1][x] <= heightMap[y][x])
                    continue;
                if (y < heightMap.length - 1 && heightMap[y][x] >= heightMap[y + 1][x])
                    continue;
                lowPoints.add(new Point(x, y));
            }
        }
        return lowPoints;
    }

    record Point(int x, int y) {
    }

}
