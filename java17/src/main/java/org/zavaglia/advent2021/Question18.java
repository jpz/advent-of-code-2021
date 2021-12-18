package org.zavaglia.advent2021;

public class Question18 extends Question {


    public long part1() {

        SnailNumber sum = null;
        for (var line : getInputText()) {
            var sn = new SnailNumber(line, 0);
            if (sum == null) {
                sum = sn;
            } else {
                sum = SnailNumber.add(sum, sn);
            }
        }
        return sum.magnitude();
    }


    public long part2() {
        var maxMagnitude = Long.MIN_VALUE;
        for (var line1 : getInputText()) {
            for (var line2 : getInputText()) {
                var sn1 = new SnailNumber(line1, 0);
                var sn2 = new SnailNumber(line2, 0);
                var sum = SnailNumber.add(sn1, sn2);
                maxMagnitude = Math.max(maxMagnitude, sum.magnitude());
            }
        }
        return maxMagnitude;
    }

}
