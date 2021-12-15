package org.zavaglia.advent2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Question15 extends Question {

    public long part1() {
        final var riskLevelMap = getRiskLevelMap();
        final var maxX = riskLevelMap[0].length;
        final var maxY = riskLevelMap.length;

        final var queue = new PriorityQueue<Path>(100, (path1, path2) -> path1.riskLevel - path2.riskLevel);
        final var start = new Path(new Coordinate(0, 0), 0, List.of(new Coordinate(0, 0)));

        final var cheapestPath = new HashMap<Coordinate, Path>();

        queue.add(start);

        while (queue.size() > 0) {
            var p = queue.remove();
            var location = p.last;
            if (location.x == maxX - 1 && location.y == maxY - 1) {
                return p.riskLevel;
            }

            var cheapest = cheapestPath.get(location);
            if (cheapest == null) {
                cheapestPath.put(location, p);
            } else {
                if (cheapest.riskLevel <= p.riskLevel) {
                    // don't bother processing as we found a cheaper path to this location
                    continue;
                }
            }

            if (location.x > 0) {
                var left = new Coordinate(location.x - 1, location.y);
                if (!p.visited.contains(left)) {
                    queue.add(p.createNewPath(left, riskLevelMap[left.y][left.x]));
                }
            }
            if (location.x < maxX - 1) {
                var right = new Coordinate(location.x + 1, location.y);
                if (!p.visited.contains(right)) {
                    queue.add(p.createNewPath(right, riskLevelMap[right.y][right.x]));
                }
            }
            if (location.y > 0) {
                var up = new Coordinate(location.x, location.y - 1);
                if (!p.visited.contains(up)) {
                    queue.add(p.createNewPath(up, riskLevelMap[up.y][up.x]));
                }
            }
            if (location.y < maxY - 1) {
                var down = new Coordinate(location.x, location.y + 1);
                if (!p.visited.contains(down)) {
                    queue.add(p.createNewPath(down, riskLevelMap[down.y][down.x]));
                }
            }
        }

        return -1;
    }

    int getRiskLevelX5(int[][] riskLevelMap, int x, int y) {
        final var maxX = riskLevelMap[0].length;
        final var maxY = riskLevelMap.length;
        var xBlock = x / maxX;
        var yBlock = y / maxY;

        return (riskLevelMap[y % maxY][x % maxX] + xBlock + yBlock - 1) % 9 + 1;
    }

    public long part2() {
        final var riskLevelMap = getRiskLevelMap();
        final var maxX = riskLevelMap[0].length;
        final var maxY = riskLevelMap.length;

        final var queue = new PriorityQueue<Path>(100, (path1, path2) -> path1.riskLevel - path2.riskLevel);
        final var start = new Path(new Coordinate(0, 0), 0, List.of(new Coordinate(0, 0)));

        final var cheapestPath = new HashMap<Coordinate, Integer>();

        queue.add(start);

        while (queue.size() > 0) {
            var p = queue.remove();
            var location = p.last;
            if (location.x == maxX * 5 - 1 && location.y == maxY * 5 - 1) {
                return p.riskLevel;
            }

            var cheapest = cheapestPath.get(location);
            if (cheapest == null) {
                cheapestPath.put(location, p.riskLevel);
            } else {
                if (cheapest <= p.riskLevel) {
                    // don't bother processing as we found a cheaper path to this location
                    continue;
                }
            }

            if (location.x > 0) {
                var left = new Coordinate(location.x - 1, location.y);
                if (!p.visited.contains(left)) {
                    queue.add(p.createNewPath(left, getRiskLevelX5(riskLevelMap, left.x, left.y)));
                }
            }
            if (location.x < maxX * 5 - 1) {
                var right = new Coordinate(location.x + 1, location.y);
                if (!p.visited.contains(right)) {
                    queue.add(p.createNewPath(right, getRiskLevelX5(riskLevelMap, right.x, right.y)));
                }
            }
            if (location.y > 0) {
                var up = new Coordinate(location.x, location.y - 1);
                if (!p.visited.contains(up)) {
                    queue.add(p.createNewPath(up, getRiskLevelX5(riskLevelMap, up.x, up.y)));
                }
            }
            if (location.y < maxY * 5 - 1) {
                var down = new Coordinate(location.x, location.y + 1);
                if (!p.visited.contains(down)) {
                    queue.add(p.createNewPath(down, getRiskLevelX5(riskLevelMap, down.x, down.y)));
                }
            }
        }

        return -1;

    }

    private int[][] getRiskLevelMap() {
        return getRowColumnArray();
    }

    record Coordinate(int x, int y) {
    }

    // implementing this with ArrayList instead of HashSet ends up being
    // 10x faster.  It appears that for this problem, checking whether a value
    // is in a contiguous array ends up being much more performant.
    record Path(Coordinate last, int riskLevel, List<Coordinate> visited) {
        Path createNewPath(Coordinate last, int riskLevel) {
            var visited = new ArrayList<>(this.visited);
            visited.add(last);
            return new Path(last, this.riskLevel + riskLevel, visited);
        }
    }


}
