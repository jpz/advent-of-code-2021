package org.zavaglia.advent2021;

import junit.framework.TestCase;

import java.io.IOException;

public class Question01Test extends TestCase {

    public void testA() throws IOException {
        var q = new Question01();
        assertEquals(q.Part1(), q.Part1Expected());
    }

    public void testB() throws IOException {
        var q = new Question01();
        assertEquals(q.Part2(), q.Part2Expected());
    }
}