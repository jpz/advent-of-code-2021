package org.zavaglia.advent2021;

import junit.framework.TestCase;

import java.io.IOException;

public class Question02Test extends TestCase {

    public void testA() throws IOException {
        var q = new Question02();
        assertEquals(q.A(), q.ExpectedA());
    }

    public void testB() throws IOException {
        var q = new Question02();
        assertEquals(q.B(), q.ExpectedB());
    }
}