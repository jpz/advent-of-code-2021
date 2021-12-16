package org.zavaglia.advent2021;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Question16 extends Question {

    List<Boolean> getAllBits(String line) {
        List<Boolean> bits = new LinkedList<Boolean>();
        var s = line;
        for (var i = 0; i < s.length(); i++) {
            var value = Integer.parseInt(s.substring(i, i + 1), 16);

            bits.add((value & 8) > 0);
            bits.add((value & 4) > 0);
            bits.add((value & 2) > 0);
            bits.add((value & 1) > 0);
        }
        return bits;
    }

    long getNBits(int n, List<Boolean> bits) {
        var accum = 0;
        while (n-- > 0) {
            accum = accum * 2 + (bits.remove(0) ? 1 : 0);
        }
        return accum;
    }

    Packet parsePacket(List<Boolean> bits) {
        var packetVersion = (int) getNBits(3, bits);
        var typeId = (int) getNBits(3, bits);

        if (typeId == 4) {
            var bit = getNBits(1, bits);
            var accum = getNBits(4, bits);
            while (bit == 1) {
                bit = getNBits(1, bits);
                accum = accum * 16 + getNBits(4, bits);
            }

            return new Packet(packetVersion, typeId, accum, 0, 0, new ArrayList<>());
        } else {
            var lengthTypeId = (int) getNBits(1, bits);
            var length = getNBits(lengthTypeId == 0 ? 15 : 11, bits);
            var subPackets = new ArrayList<Packet>();
            if (lengthTypeId == 0) {
                var startBits = bits.size();
                while (startBits - bits.size() < length) {
                    subPackets.add(parsePacket(bits));
                }
            } else {
                var packetCount = 0;
                while (packetCount < length) {
                    subPackets.add(parsePacket(bits));
                    packetCount++;
                }
            }
            return new Packet(packetVersion, typeId, 0, lengthTypeId, length, subPackets);
        }
    }

    int sumVersionNumbers(Packet packet) {
        var sum = packet.version();
        for (var p : packet.subPackets()) {
            sum += sumVersionNumbers(p);
        }
        return sum;
    }

    public long part1() {
        var bits = getAllBits(getInputText().get(0));
        var packet = parsePacket(bits);
        return sumVersionNumbers(packet);
    }

    public long part2() {
        var bits = getAllBits(getInputText().get(0));
        var packet = parsePacket(bits);
        return packet.value();
    }

    record Packet(int version, int typeId, long literalValue, int lengthTypeId, long length, List<Packet> subPackets) {

        long value() {
            switch (typeId) {
                case 0: // sum
                    return subPackets.stream().map(Packet::value).reduce(0L, Long::sum);
                case 1: // product
                    return subPackets.stream().map(Packet::value).reduce(1L, (a, b) -> a * b);
                case 2: // min
                    return subPackets.stream().map(Packet::value).reduce(Long.MAX_VALUE, Long::min);
                case 3: // max
                    return subPackets.stream().map(Packet::value).reduce(Long.MIN_VALUE, Long::max);
                case 4: // literal
                    return literalValue;
                case 5: // greater-than
                    return subPackets().get(0).value() > subPackets().get(1).value() ? 1L : 0L;
                case 6: // less-than
                    return subPackets().get(0).value() < subPackets().get(1).value() ? 1l : 0L;
                case 7: // equal-to
                    return subPackets().get(0).value() == subPackets().get(1).value() ? 1L : 0L;
                default:
                    throw new RuntimeException("unexpected typeId");
            }
        }
    }
}
