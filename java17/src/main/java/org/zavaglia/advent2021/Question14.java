package org.zavaglia.advent2021;

import java.util.HashMap;
import java.util.Map;

public class Question14 extends Question {

    public long part1() {
        // brute force expansion
        var polymer = getPolymerTemplate();
        final var rules = getRules();
        for (var i = 0; i < 10; i++) {
            var newPolymer = polymer;
            var insertions = 0;
            for (var j = 0; j < polymer.length() - 1; j++) {
                var substr = polymer.substring(j, j + 2);
                if (rules.containsKey(substr)) {
                    newPolymer = newPolymer.substring(0, j + insertions + 1) + rules.get(substr) + newPolymer.substring(j + insertions + 1);
                    insertions++;
                }
            }
            polymer = newPolymer;
            System.out.println(polymer.length());
        }

        var map = new HashMap<Character, Long>();

        for (var ch : polymer.toCharArray()) {
            map.put(ch, map.getOrDefault(ch, 0L) + 1);
        }

        var minCount = Long.MAX_VALUE;
        var maxCount = Long.MIN_VALUE;
        for (var ch : map.keySet()) {
            minCount = Math.min(minCount, map.get(ch));
            maxCount = Math.max(maxCount, map.get(ch));
        }

        return maxCount - minCount;
    }

    public long part2() {
        final var rules = getRules();
        final var memo = new HashMap<SubStringAndDepth, Map<Character, Long>>(); // memoise results of getLetterCountForDepths
        final var polymerTemplate = getPolymerTemplate();

        Map<Character, Long> sum = new HashMap<>();

        for (var i = 0; i < polymerTemplate.length() - 1; i++) {
            var substr = polymerTemplate.substring(i, i + 2);
            var counts = getLetterCountForDepth(substr, 40, memo, rules);
            sum = sumCharacterCountMaps(counts, sum);
        }

        // remove double-counted internal characters
        for (var i = 1; i < polymerTemplate.length() - 1; i++) {
            var ch = polymerTemplate.charAt(i);
            sum.put(ch, sum.get(ch) - 1);
        }

        var minCount = Long.MAX_VALUE;
        var maxCount = Long.MIN_VALUE;
        for (var value : sum.values()) {
            minCount = Math.min(minCount, value);
            maxCount = Math.max(maxCount, value);
        }

        return maxCount - minCount;
    }

    // recursive memoising function - returns the number of characters which will expand given
    // a certain depth.  substr is always 2 characters in size.
    public Map<Character, Long> getLetterCountForDepth(String substr, int depth, Map<SubStringAndDepth, Map<Character, Long>> memo, Map<String, String> rules) {
        if (memo.containsKey(new SubStringAndDepth(substr, depth)))
            return memo.get(new SubStringAndDepth(substr, depth));

        if (rules.containsKey(substr) && depth > 0) {
            var middleCharacter = rules.get(substr);
            var substr1 = substr.substring(0, 1).concat(rules.get(substr));
            var substr2 = rules.get(substr).concat(substr.substring(1, 2));

            var counts1 = getLetterCountForDepth(substr1, depth - 1, memo, rules);
            var counts2 = getLetterCountForDepth(substr2, depth - 1, memo, rules);

            var result = sumCharacterCountMaps(counts1, counts2);
            // avoid double-counting middle element
            result.put(middleCharacter.charAt(0), result.get(middleCharacter.charAt(0)) - 1);
            memo.put(new SubStringAndDepth(substr, depth), result);

            return result;
        } else {
            if (substr.charAt(0) == substr.charAt(1))
                return Map.of(substr.charAt(0), 2L);
            else
                return Map.of(substr.charAt(0), 1L, substr.charAt(1), 1L);
        }
    }

    Map<Character, Long> sumCharacterCountMaps(Map<Character, Long> map1, Map<Character, Long> map2) {
        var result = new HashMap<>(map1);

        for (var ch : map2.keySet()) {
            result.put(ch, result.getOrDefault(ch, 0L) + map2.get(ch));
        }
        return result;
    }

    Map<String, String> getRules() {
        var skippingPolymerTemplate = true;
        final var rules = new HashMap<String, String>();
        for (var line : getInputText()) {
            if (skippingPolymerTemplate) {
                if (line.equals("")) {
                    skippingPolymerTemplate = false;
                }
                continue;
            }
            final var arr = line.split(" -> ");
            rules.put(arr[0], arr[1]);
        }
        return rules;
    }

    String getPolymerTemplate() {
        return getInputText().get(0);
    }

    record SubStringAndDepth(String substr, Integer depth) {
    }

}
