package org.zavaglia.advent2021;

import junit.framework.TestCase;

public class SnailNumberTest extends TestCase {

    public void testParseSimple() {
        var sn = new SnailNumber(null);
        var idx = sn.parseNumber("[1,2]", 0);
        assertEquals(5, idx);
        assertNotNull(sn.left);
        assertNull(sn.left.left);
        assertNull(sn.left.right);
        assertEquals(sn.left.number, 1);
        assertNotNull(sn.right);
        assertEquals(sn.right.number, 2);
    }

    public void testExplode1() {
        var sn = new SnailNumber(null);
        sn.parseNumber("[[[[[9,8],1],2],3],4]", 0);
        assertTrue(sn.explode());
        assertEquals("[[[[0,9],2],3],4]", sn.toString());
    }

    public void testExplode2() {
        var sn = new SnailNumber(null);
        sn.parseNumber("[7,[6,[5,[4,[3,2]]]]]", 0);
        assertTrue(sn.explode());
        assertEquals("[7,[6,[5,[7,0]]]]", sn.toString());
    }

    public void testExplode3() {
        var sn = new SnailNumber(null);
        sn.parseNumber("[[6,[5,[4,[3,2]]]],1]", 0);
        assertTrue(sn.explode());
        assertEquals("[[6,[5,[7,0]]],3]", sn.toString());
    }

    public void testExplode4() {
        var sn = new SnailNumber(null);
        sn.parseNumber("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", 0);
        assertTrue(sn.explode());
        assertEquals("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", sn.toString());
    }

    public void testExplode5() {
        var sn = new SnailNumber(null);
        sn.parseNumber("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", 0);
        assertTrue(sn.explode());
        assertEquals("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", sn.toString());
    }

    public void testAddition() {
        var sn1 = new SnailNumber(null);
        sn1.parseNumber("[[[[4,3],4],4],[7,[[8,4],9]]]", 0);
        var sn2 = new SnailNumber(null);
        sn2.parseNumber("[1,1]", 0);
        var sum = SnailNumber.add(sn1, sn2);
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", sum.toString());
    }


}