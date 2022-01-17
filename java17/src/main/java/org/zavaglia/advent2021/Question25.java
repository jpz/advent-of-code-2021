package org.zavaglia.advent2021;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Question25 extends Question {

    // I did this in a really complicated way, using bit-masks to accelerate the
    // comparison of neighbouring cells and computation after shifts, but could have
    // more easily done this in 5% of the time using a naive approach.

    static BigInteger rotateLeft(BigInteger bits, int width, int n) {
        var mask = BigInteger.valueOf(0);
        for (var i = width-n; i < width; i++) {
            mask = mask.setBit(i);
        }
        return bits.andNot(mask).shiftLeft(n).or(bits.and(mask).shiftRight(width-n));
    }

    static BigInteger rotateRight(BigInteger bits, int width, int n) {
        var mask = BigInteger.valueOf(0);
        for (var i = 0; i < n; i++) {
            mask = mask.setBit(i);
        }
        return bits.shiftRight(n).or(bits.and(mask).shiftLeft(width - n));
    }

    static BigInteger addRightBitsToLeftExtend(BigInteger bits, int width, int n) {
        var mask = BigInteger.valueOf(0);
        for (var i = 0; i < n; i++) {
            mask = mask.setBit(i);
        }
        return bits.or(bits.and(mask).shiftLeft(width));
    }


    record CucumberMap(List<BigInteger> east, List<BigInteger> south, int height, int width) {
        static CucumberMap parseInput(List<String> inputText) {
            var width = inputText.get(0).length();
            var height = inputText.size();
            var east = new ArrayList<BigInteger>(Collections.nCopies(width, BigInteger.ZERO));
            var south = new ArrayList<BigInteger>( Collections.nCopies(height, BigInteger.ZERO));

            for (var row = 0; row < height; row++) {
                var eastAccum = BigInteger.valueOf(0L);
                var southAccum = BigInteger.valueOf(0L);
                var bit = BigInteger.valueOf(1L);
                for (var col = width-1; col >= 0; col--) {
                    if (inputText.get(row).charAt(col) == '>')
                        eastAccum = eastAccum.or(bit);
                    if (inputText.get(row).charAt(col) == 'v')
                        southAccum = southAccum.or(bit);
                    bit = bit.shiftLeft(1);
                }
                east.set(row, eastAccum);
                south.set(row, southAccum);
            }

            return new CucumberMap(east, south, height, width);
        }

        BigInteger moveEast(int row) {
            // we can determine next state on the basis of truth values of prior states
            // truth table in form of state at location-1/state/state at location+1
            // 0  0  0   ->  0
            // 0  0  *   ->  0
            // >  0  0   ->  1
            // >  0  0   ->  1
            // *  >  0   ->  0
            // *  >  *   ->  1
            // eastBits>>1 & (!eastBits & !southBits) | ((eastBits>>1 & (southBits | eastBits))<<1)
            var eastBits = addRightBitsToLeftExtend(east.get(row), width, 1);
            var eastBitsRShift1 = rotateRight(eastBits, width, 1);
            var southBits = addRightBitsToLeftExtend(south.get(row), width, 1);

            var lhs = eastBitsRShift1.andNot(eastBits.or(southBits));
            var rhs = rotateLeft(eastBitsRShift1.and(southBits.or(eastBits)), width, 1);
            var result = lhs.or(rhs);
            // strip extended bit
            result = result.andNot(BigInteger.valueOf(0b1).shiftLeft(width));
            return result;
        }

        BigInteger moveSouth(int row) {
            // we can determine next state on the basis of truth values of prior states
            // truth table in form of state at location-1/state/state at location+1
            // 0  0  0   ->  0
            // 0  0  *   ->  0
            // v  0  0   ->  1
            // v  0  0   ->  1
            // *  v  0   ->  0
            // *  v  *   ->  1
            // this can be encoded as:
            // southBits[-1] & (!southBits[0] & !eastBits[0]) | southBits[0] & (southBits[1] | eastBits[1])
            var idxMinus1 = (row-1+height) % height;
            var idxZero = row;
            var idxPlus1 = (row+1+height) % height;
            var eastBitsZero = east.get(idxZero);
            var eastBitsPlus1 = east.get(idxPlus1);
            var southBitsMinus1 = south.get(idxMinus1);
            var southBitsZero = south.get(idxZero);
            var southBitsPlus1 = south.get(idxPlus1);
            // southBitsMinus1 & (!eastBitsZero & !southBitsZero) | southBitsZero & (southBitsPlus1 | eastBitsPlus1)

            var lhs = southBitsMinus1.andNot(southBitsZero).andNot(eastBitsZero);
            var rhs = southBitsZero.and(southBitsPlus1.or(eastBitsPlus1));
            return  lhs.or(rhs);
        }

        CucumberMap moveEast() {
            var east = IntStream.range(0, height).boxed().map(this::moveEast).collect(Collectors.toList());
            return new CucumberMap(east, south, height, width);
        }
        CucumberMap moveSouth() {
            var south = IntStream.range(0, height).boxed().map(this::moveSouth).collect(Collectors.toList());
            return new CucumberMap(east, south, height, width);
        }

        public String toString() {
            var sb = new StringBuilder();
            for (var row = 0; row < height; row++ ) {
                for (var col = width-1; col >= 0; col--) {
                    if (east.get(row).testBit(col)) {
                        sb.append('>');
                    } else if (south.get(row).testBit(col)) {
                        sb.append('v');
                    } else {
                        sb.append('.');
                    }
                }
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    public long part1() {
        var cucumberMap = CucumberMap.parseInput(getInputText());
        CucumberMap prevMap = null;

        var count = 0;

        while (!cucumberMap.equals(prevMap)) {
            prevMap = cucumberMap;
            cucumberMap = cucumberMap.moveEast().moveSouth();
            count++;
        }

        return count;
    }

    public long part2() {
        var cucumberMap = CucumberMap.parseInput(getInputText());
        return -1;
    }

}

