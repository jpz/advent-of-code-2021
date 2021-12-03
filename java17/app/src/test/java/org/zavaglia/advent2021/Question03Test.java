package org.zavaglia.advent2021;

import junit.framework.TestCase;

import java.io.IOException;

public class Question03Test extends TestCase {

    public void testA() throws IOException {
        var q = new Question03();
        assertEquals(q.Part1Expected(), q.Part1());
    }

    public void testB() throws IOException {
        var q = new Question03();
        assertEquals(q.Part2Expected(), q.Part2());
    }
}