package org.zavaglia.advent2021;

import junit.framework.TestCase;

public class Question16Test extends TestCase {

    public void testParsePacketLiteralValue() {
        var q = new Question16();
        var bits = q.getAllBits("D2FE28");
        var packet = q.parsePacket(bits);
        assertEquals(6, packet.version());
        assertEquals(4, packet.typeId());
        assertEquals(2021, packet.literalValue());
    }

    public void testParsePacketOperator1() {
        var q = new Question16();
        var bits = q.getAllBits("38006F45291200");
        var packet = q.parsePacket(bits);
        assertEquals(1, packet.version());
        assertEquals(6, packet.typeId());
        assertEquals(27, packet.length());
        assertEquals(2, packet.subPackets().size());
        assertEquals(10, packet.subPackets().get(0).literalValue());
        assertEquals(20, packet.subPackets().get(1).literalValue());
    }

    public void testParsePacketOperator2() {
        var q = new Question16();
        var bits = q.getAllBits("EE00D40C823060");
        var packet = q.parsePacket(bits);
        assertEquals(7, packet.version());
        assertEquals(3, packet.typeId());
        assertEquals(3, packet.length());
        assertEquals(3, packet.subPackets().size());
        assertEquals(1, packet.subPackets().get(0).literalValue());
        assertEquals(2, packet.subPackets().get(1).literalValue());
        assertEquals(3, packet.subPackets().get(2).literalValue());
    }

    public void testValueSum() {
        var q = new Question16();
        var bits = q.getAllBits("C200B40A82");
        var packet = q.parsePacket(bits);
        assertEquals(3, packet.value());
    }

    public void testValueProduct() {
        var q = new Question16();
        var bits = q.getAllBits("04005AC33890");
        var packet = q.parsePacket(bits);
        assertEquals(54, packet.value());
    }

    public void testValueMin() {
        var q = new Question16();
        var bits = q.getAllBits("880086C3E88112");
        var packet = q.parsePacket(bits);
        assertEquals(7, packet.value());
    }

    public void testValueMax() {
        var q = new Question16();
        var bits = q.getAllBits("CE00C43D881120");
        var packet = q.parsePacket(bits);
        assertEquals(9, packet.value());
    }

    public void testValueLessThan() {
        var q = new Question16();
        var bits = q.getAllBits("D8005AC2A8F0");
        var packet = q.parsePacket(bits);
        assertEquals(1, packet.value());
    }

    public void testValueGreaterThan() {
        var q = new Question16();
        var bits = q.getAllBits("F600BC2D8F");
        var packet = q.parsePacket(bits);
        assertEquals(0, packet.value());
    }

    public void testValueEqualTo() {
        var q = new Question16();
        var bits = q.getAllBits("9C005AC2F8F0");
        var packet = q.parsePacket(bits);
        assertEquals(0, packet.value());
    }

    public void testValueComplex() {
        var q = new Question16();
        var bits = q.getAllBits("9C0141080250320F1802104A08");
        var packet = q.parsePacket(bits);
        assertEquals(1, packet.value());
    }


}