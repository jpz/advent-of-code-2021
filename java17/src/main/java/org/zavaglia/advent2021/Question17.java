package org.zavaglia.advent2021;

import java.util.regex.Pattern;

public class Question17 extends Question {

    Region parseRegion(String line) {
        // idiosyncratically, max y appears on right of y-span which is in reverse order (e.g.) -10 to -5
        var p = Pattern.compile("target area: x=(?<x0>-?\\d+)\\.\\.(?<x1>-?\\d+), y=(?<y1>-?\\d+)\\.\\.(?<y0>-?\\d+)");
        var result = p.matcher(line);
        result.matches();
        var x0 = result.group("x0");
        var x1 = result.group("x1");
        var y0 = result.group("y0");
        var y1 = result.group("y1");

        return new Region(
                new Coordinate(Integer.parseInt(x0), Integer.parseInt(y0)),
                new Coordinate(Integer.parseInt(x1), Integer.parseInt(y1))
        );
    }

    int getMaxHeight(int velocityX, int velocityY, Region region) {
        var x = 0;
        var y = 0;
        var maxHeight = Integer.MIN_VALUE;

        while (x <= region.bottomRight.x && y >= region.bottomRight.y) {
            maxHeight = Math.max(maxHeight, y);
            if (x >= region.topLeft.x && y <= region.topLeft.y) {
                return maxHeight;
            }
            x += velocityX;
            y += velocityY;
            velocityX = Math.max(velocityX - 1, 0);
            velocityY--;
        }
        return Integer.MIN_VALUE;
    }

    public long part1() {
        var region = parseRegion(getInputText().get(0));
        var maxHeight = Integer.MIN_VALUE;
        for (var x = 1; x <= region.bottomRight.x; x++) {
            for (var y = region.bottomRight.y; y < 300; y++) {
                maxHeight = Math.max(maxHeight, getMaxHeight(x, y, region));
            }
        }
        return maxHeight;
    }

    public long part2() {
        var region = parseRegion(getInputText().get(0));
        var count = 0;
        for (var x = 1; x <= region.bottomRight.x; x++) {
            for (var y = region.bottomRight.y; y < 300; y++) {
                if (getMaxHeight(x, y, region) != Integer.MIN_VALUE)
                    count++;
            }
        }
        return count;
    }

    record Coordinate(int x, int y) {
    }

    record Region(Coordinate topLeft, Coordinate bottomRight) {
    }
}
