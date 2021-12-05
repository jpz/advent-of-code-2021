package org.zavaglia.advent2021;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Question05Test extends TestCase {

    public void testCaseA() throws IOException {
        var inputString = """
                0,9 -> 5,9
                8,0 -> 0,8
                9,4 -> 3,4
                2,2 -> 2,1
                7,0 -> 7,4
                6,4 -> 2,0
                0,9 -> 2,9
                3,4 -> 1,4
                0,0 -> 8,8
                5,5 -> 8,2""";
        var input = Arrays.stream(inputString.split("\n")).collect(Collectors.toList());
        var q = new Question05(input);
        assertEquals(5, q.Part1());
    }


    public void testA() throws IOException {
        var q = new Question05();
        assertEquals(q.Part1Expected(), q.Part1());
    }

    public void testB() throws IOException {
        var q = new Question05();
        assertEquals(q.Part2Expected(), q.Part2());
    }
}