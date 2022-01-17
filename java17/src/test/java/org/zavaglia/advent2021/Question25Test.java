package org.zavaglia.advent2021;

import junit.framework.TestCase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question25Test extends TestCase {


    public void testSimpleEastMove() {
        var east = List.of(BigInteger.valueOf(0b1000));
        var south = List.of(BigInteger.ZERO);
        var c = new Question25.CucumberMap(east, south, 1, 8);

        assertEquals("....>...\n", c.toString());
        assertEquals(".....>..\n", c.moveEast().toString());
        assertEquals("......>.\n", c.moveEast().moveEast().toString());
        assertEquals(".......>\n", c.moveEast().moveEast().moveEast().toString());
        assertEquals(">.......\n", c.moveEast().moveEast().moveEast().moveEast().toString());

        for (var i = 0; i < 16; i++) {
            System.out.println(c);
            c = c.moveEast();
        }
    }

    public void testBlockedEastMove() {
        var east = List.of(BigInteger.valueOf(0b1000));
        var south = List.of(BigInteger.valueOf(0b0100));
        var c = new Question25.CucumberMap(east, south, 1, 8);

        assertEquals("....>v..\n", c.toString());
        assertEquals("....>v..\n", c.moveEast().toString());

        for (var i = 0; i < 16; i++) {
            System.out.println(c);
            c = c.moveEast();
        }
    }

    public void testBlockedEastWrappedMove() {
        var east = List.of(BigInteger.valueOf(0b0001));
        var south = List.of(BigInteger.valueOf(0b1000));
        var c = new Question25.CucumberMap(east, south, 1, 4);

        assertEquals("v..>\n", c.toString());
        assertEquals("v..>\n", c.moveEast().toString());

    }

    public void testQueuedEastMove() {
        var east = List.of(BigInteger.valueOf(0b1100));
        var south = List.of(BigInteger.ZERO);
        var c = new Question25.CucumberMap(east, south, 1, 8);

        assertEquals("....>>..\n", c.toString());
        assertEquals("....>.>.\n", c.moveEast().toString());
        assertEquals(".....>.>\n", c.moveEast().moveEast().toString());
        assertEquals(">.....>.\n", c.moveEast().moveEast().moveEast().toString());

        for (var i = 0; i < 16; i++) {
            System.out.println(c);
            c = c.moveEast();
        }
    }

    public void testSimpleSouthMove() {
        var east = List.of(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
        var south = List.of(BigInteger.valueOf(1), BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
        var c = new Question25.CucumberMap(east, south, 4, 1);

        assertEquals("v\n.\n.\n.\n", c.toString());
        assertEquals(".\nv\n.\n.\n", c.moveSouth().toString());
        assertEquals(".\n.\nv\n.\n", c.moveSouth().moveSouth().toString());
        assertEquals(".\n.\n.\nv\n", c.moveSouth().moveSouth().moveSouth().toString());
        assertEquals("v\n.\n.\n.\n", c.moveSouth().moveSouth().moveSouth().moveSouth().toString());

        for (var i = 0; i < 16; i++) {
            System.out.println(c);
            c = c.moveEast();
        }
    }

    public void testBlockedSouthMove() {
        var east = List.of(BigInteger.ZERO, BigInteger.valueOf(1), BigInteger.ZERO, BigInteger.ZERO);
        var south = List.of(BigInteger.valueOf(1), BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
        var c = new Question25.CucumberMap(east, south, 4, 1);

        assertEquals("v\n>\n.\n.\n", c.toString());
        assertEquals("v\n>\n.\n.\n", c.moveSouth().toString());
//        assertEquals(".\n.\nv\n.\n", c.moveSouth().moveSouth().toString());
//        assertEquals(".\n.\n.\nv\n", c.moveSouth().moveSouth().moveSouth().toString());
//        assertEquals("v\n.\n.\n.\n", c.moveSouth().moveSouth().moveSouth().moveSouth().toString());

        for (var i = 0; i < 16; i++) {
            System.out.println(c);
            c = c.moveEast();
        }
    }

    public void testQueuedSouthMove() {
        var east = List.of(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
        var south = List.of(BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.ZERO, BigInteger.ZERO);
        var c = new Question25.CucumberMap(east, south, 4, 1);

        assertEquals("v\nv\n.\n.\n", c.toString());
        assertEquals("v\n.\nv\n.\n", c.moveSouth().toString());
        assertEquals(".\nv\n.\nv\n", c.moveSouth().moveSouth().toString());
        assertEquals("v\n.\nv\n.\n", c.moveSouth().moveSouth().moveSouth().toString());
        assertEquals(".\nv\n.\nv\n", c.moveSouth().moveSouth().moveSouth().moveSouth().toString());

        for (var i = 0; i < 16; i++) {
            System.out.println(c);
            c = c.moveEast();
        }
    }

    public void testSimpleStep1() {
        var input = """
                ...>...
                .......
                ......>
                v.....>
                ......>
                .......
                ..vvv..
                """;
        var c = Question25.CucumberMap.parseInput(Arrays.stream(input.split("\n")).toList());
        c = c.moveEast();
        assertEquals("""
                ....>..
                .......
                >......
                v.....>
                >......
                .......
                ..vvv..
                """, c.toString());

        c = c.moveSouth();
        assertEquals("""
                ..vv>..
                .......
                >......
                v.....>
                >......
                .......
                ....v..
                """, c.toString());
    }

    /*
    Initial state:
...>...
.......
......>
v.....>
......>
.......
..vvv..

After 1 step:
..vv>..
.......
>......
v.....>
>......
.......
....v..

After 2 steps:
....v>.
..vv...
.>.....
......>
v>.....
.......
.......

After 3 steps:
......>
..v.v..
..>v...
>......
..>....
v......
.......

After 4 steps:
>......
..v....
..>.v..
.>.v...
...>...
.......
v......
     */
}