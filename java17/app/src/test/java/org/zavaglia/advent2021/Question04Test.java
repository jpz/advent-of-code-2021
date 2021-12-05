package org.zavaglia.advent2021;

import junit.framework.TestCase;

import java.io.IOException;

public class Question04Test extends TestCase {

    public void testA() throws IOException {
        var q = new Question04();
        assertEquals(q.Part1Expected(), q.Part1());
    }

    public void testB() throws IOException {
        var q = new Question04();
        assertEquals(q.Part2Expected(), q.Part2());
    }
}