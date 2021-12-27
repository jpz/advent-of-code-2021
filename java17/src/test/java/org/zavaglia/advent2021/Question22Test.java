package org.zavaglia.advent2021;

import junit.framework.TestCase;

public class Question22Test extends TestCase {

    public void testVolume() {
        var box1 = new Question22.Box(0, 3, 0, 3, 0, 3);
        assertEquals(27, box1.volume());
    }

    public void testAddSingleDisjointBox() {
        var box1 = new Question22.Box(0, 1, 0, 1, 0, 1);
        var box2 = new Question22.Box(1, 2, 1, 2, 1, 2);
        var result = box1.add(box2);

        assertEquals(2, Question22.Box.boxSetVolume(result));
        assertTrue(result.contains(box1));
        assertTrue(result.contains(box2));
    }

    public void testAddSingleContainedBox() {
        var box1 = new Question22.Box(0, 3, 0, 3, 0, 3);
        var box2 = new Question22.Box(0, 1, 0, 1, 0, 1);
        var result1 = box1.add(box2);
        var result2 = box2.add(box1);

        assertEquals(27, box1.volume());
        assertEquals(27, Question22.Box.boxSetVolume(result1));
        assertEquals(27, Question22.Box.boxSetVolume(result2));
        assertEquals(1, result1.size());
        assertEquals(1, result2.size());
    }

    public void testAddOverlappedBox1() {
        var box1 = new Question22.Box(0, 3, 0, 3, 0, 3);
        var box2 = new Question22.Box(2, 4, 2, 4, 2, 4);
        var result1 = box1.add(box2);
        var result2 = box2.add(box1);

        assertEquals(27, box1.volume());
        assertEquals(8, box2.volume());
        assertEquals(34, Question22.Box.boxSetVolume(result1));
        assertEquals(34, Question22.Box.boxSetVolume(result2));
        assertEquals(4, result1.size());
        assertEquals(4, result2.size());
    }

    public void testAddOverlappedBox2() {
        var box1 = new Question22.Box(0, 2, 0, 2, 0, 2);
        var box2 = new Question22.Box(1, 3, 1, 3, 0, 2);
        var result1 = box1.add(box2);
        var result2 = box2.add(box1);

        assertEquals(8, box1.volume());
        assertEquals(8, box2.volume());
        assertEquals(14, Question22.Box.boxSetVolume(result1));
        assertEquals(14, Question22.Box.boxSetVolume(result2));
        assertEquals(3, result1.size());
        assertEquals(3, result2.size());
    }

    public void testAddOverlappedBox3() {
        var box1 = new Question22.Box(0, 2, 0, 2, 0, 2);
        var box2 = new Question22.Box(1, 3, 0, 2, 0, 2);
        var result1 = box1.add(box2);
        var result2 = box2.add(box1);

        assertEquals(8, box1.volume());
        assertEquals(8, box2.volume());
        assertEquals(12, Question22.Box.boxSetVolume(result1));
        assertEquals(12, Question22.Box.boxSetVolume(result2));
        assertEquals(2, result1.size());
        assertEquals(2, result2.size());
    }

}