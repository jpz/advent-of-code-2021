package org.zavaglia.advent2021;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Question22 extends Question {

    public long part1() {
        var instructions = getInputText().stream().map(Instruction::parseLine).toList();
        var points = new HashSet<Point>();
        for (var instruction : instructions) {
            if (instruction.on) {
                points.addAll(instruction.box.restrict().getPoints());
            } else {
                points.removeAll(instruction.box.restrict().getPoints());
            }
        }

        return points.size();

    }

    public long part2() {
        var instructions = getInputText().stream().map(Instruction::parseLine).toList();
        Set<Box> boxes = Set.of();
        for (var instruction : instructions) {
            var result = new HashSet<Box>();
            if (instruction.on) {
                if (boxes.size() == 0) {
                    result.add(instruction.box);
                } else {
                    for (var box : boxes) {
                        result.addAll(instruction.box.add(box));
                    }
                }
            } else {
                for (var box : boxes) {
                    result.addAll(box.subtract(instruction.box));
                }
            }
            boxes = result;
        }

        return Box.boxSetVolume(boxes);

    }

    record Instruction(boolean on, Box box) {
        static Instruction parseLine(String line) {
            var p = Pattern.compile("(?<onoff>on|off) x=(?<x0>-?\\d+)\\.\\.(?<x1>-?\\d+),y=(?<y0>-?\\d+)\\.\\.(?<y1>-?\\d+),z=(?<z0>-?\\d+)\\.\\.(?<z1>-?\\d+)");
            var result = p.matcher(line);
            result.matches();
            var onoff = result.group("onoff");
            var x0 = result.group("x0");
            var x1 = result.group("x1");
            var y0 = result.group("y0");
            var y1 = result.group("y1");
            var z0 = result.group("z0");
            var z1 = result.group("z1");

            return new Instruction(
                    onoff.equals("on"),
                    new Box(
                            Integer.parseInt(x0), Integer.parseInt(x1) + 1,
                            Integer.parseInt(y0), Integer.parseInt(y1) + 1,
                            Integer.parseInt(z0), Integer.parseInt(z1) + 1)
            );
        }

    }

    record Point(int x, int y, int z) {
    }

    record Box(int x0, int x1, int y0, int y1, int z0, int z1) {

        static long boxSetVolume(Set<Box> boxes) {
            return boxes.stream().map(box -> (long) box.xDist() * box.yDist() * box.zDist()).reduce(0L, Long::sum);
        }

        Set<Point> getPoints() {
            return IntStream.range(x0, x1).boxed()
                    .flatMap(x -> IntStream.range(y0, y1).boxed()
                            .flatMap(y -> IntStream.range(z0, z1).boxed()
                                    .map(z -> new Point(x, y, z)))).collect(Collectors.toSet());
        }

        int restrictLowerBound(int i) {
            return Math.min(Math.max(i, -50), 51);
        }

        int restrictUpperBound(int i) {
            return Math.min(Math.max(i, -50), 51);
        }

        Box restrict() {
            return new Box(
                    restrictLowerBound(x0), restrictUpperBound(x1),
                    restrictLowerBound(y0), restrictUpperBound(y1),
                    restrictLowerBound(z0), restrictUpperBound(z1));
        }

        long volume() {
            return xDist() * yDist() * zDist();
        }

        int xDist() {
            return x1 - x0;
        }

        int yDist() {
            return y1 - y0;
        }

        int zDist() {
            return z1 - z0;
        }

        boolean intersects(Box other) {
            var x0 = Math.max(this.x0, other.x0);
            var x1 = Math.min(this.x1, other.x1);
            var y0 = Math.max(this.y0, other.y0);
            var y1 = Math.min(this.y1, other.y1);
            var z0 = Math.max(this.z0, other.z0);
            var z1 = Math.min(this.z1, other.z1);

            return (x0 < x1 && y0 < y1 && z0 < z1);
        }

        boolean contains(Box other) {
            return this.x0 <= other.x0 && this.x1 >= other.x1 &&
                    this.y0 <= other.y0 && this.y1 >= other.y1 &&
                    this.z0 <= other.z0 && this.z1 >= other.z1;
        }

        Stream<Box> divideX(int x) {
            if (this.x0 < x && x < this.x1) {
                return Stream.of(
                        new Box(
                                this.x0, x,
                                this.y0, this.y1,
                                this.z0, this.z1
                        ),
                        new Box(
                                x, this.x1,
                                this.y0, this.y1,
                                this.z0, this.z1
                        ));
            } else return Stream.of(this);
        }

        Stream<Box> divideY(int y) {
            if (this.y0 < y && y < this.y1) {
                return Stream.of(
                        new Box(
                                this.x0, this.x1,
                                this.y0, y,
                                this.z0, this.z1
                        ),
                        new Box(
                                this.x0, this.x1,
                                y, this.y1,
                                this.z0, this.z1
                        ));
            } else return Stream.of(this);
        }

        Stream<Box> divideZ(int z) {
            if (this.z0 < z && z < this.z1) {
                return Stream.of(
                        new Box(
                                this.x0, this.x1,
                                this.y0, this.y1,
                                this.z0, z
                        ),
                        new Box(
                                this.x0, this.x1,
                                this.y0, this.y1,
                                z, this.z1
                        ));
            } else return Stream.of(this);
        }

        Stream<Box> divide(Box other) {
            return divideX(other.x0)
                    .flatMap(box -> box.divideX(other.x1))
                    .flatMap(box -> box.divideY(other.y0))
                    .flatMap(box -> box.divideY(other.y1))
                    .flatMap(box -> box.divideZ(other.z0))
                    .flatMap(box -> box.divideZ(other.z1));

        }

        Set<Box> subtract(Box other) {
            // handle trivial cases first
            if (!this.intersects(other)) {// no
                return Set.of(this);
            }

            // divide the box across all face planes of the other box, with these sub-boxes, keep
            // those that do not intersect the subtraction box.
            return divide(other).filter(box -> !other.contains(box)).collect(Collectors.toSet());
        }

        Set<Box> add(Box other) {
            // handle trivial cases first
            if (this.contains(other)) {
                return Set.of(this);
            }
            if (other.contains(this)) {
                return Set.of(other);
            }
            if (!this.intersects(other)) {
                return Set.of(this, other);
            }

            // basic strategy for box addition
            // add the additional space as zero, one or two cubes on LHS and RHS of a given axis.
            // next, add the additional space with zero, one or two cubes on LHS of the next axis,
            // noting that we truncate the width of these cubes are confined by the first axis to
            // the added two cube.
            // lastly add the additional space with zero, one or two cubes on the third axis, noting
            // mow that we can truncate the width/height in the two axes already treated.

            int x0 = other.x0;
            int x1 = other.x1;
            int y0 = other.y0;
            int y1 = other.y1;
            int z0 = other.z0;
            int z1 = other.z1;

            var result = new HashSet<Box>();
            result.add(this);

            // create rectangle for all space exceeding this.x1 on RHS
            if (other.x1 > this.x1) {
                result.add(
                        new Box(this.x1, other.x1, y0, y1, z0, z1)
                );
                x1 = this.x1;
            }
            // create rectangle for all space exceeding this.x0 on LHS
            if (other.x0 < this.x0) {
                result.add(
                        new Box(other.x0, this.x0, y0, y1, z0, z1)
                );
                x0 = this.x0;
            }

            // do likewise box additions for y-axis, noting we restrict x-axes to
            // a maximum dimensions of the box being added to
            if (other.y1 > this.y1) {
                result.add(
                        new Box(x0, x1, this.y1, other.y1, z0, z1)
                );
                y1 = this.y1;
            }
            if (other.y0 < this.y0) {
                result.add(
                        new Box(x0, x1, other.y0, this.y0, z0, z1)
                );
                y0 = this.y0;
            }

            // do likewise box additions for z-axis, noting we restrict x-axes and y-axes to
            // a maximum dimensions of the box being added to
            if (other.z1 > this.z1) {
                result.add(
                        new Box(x0, x1, y0, y1, this.z1, other.z1)
                );
            }
            if (other.z0 < this.z0) {
                result.add(
                        new Box(x0, x1, y0, y1, other.z0, this.z0)
                );
            }

            return result;
        }

    }

}
