package org.zavaglia.advent2021;

import java.util.*;
import java.util.stream.Collectors;


public class Question08 extends Question {

    static final Map<Set<Character>, Integer> segmentSetToNumber = new HashMap<>();

    static void initialiseStaticData() {
        if (segmentSetToNumber.size() == 0) {
            segmentSetToNumber.put(Set.of('a', 'b', 'c', 'e', 'f', 'g'), 0);
            segmentSetToNumber.put(Set.of('c', 'f'), 1);
            segmentSetToNumber.put(Set.of('a', 'c', 'd', 'e', 'g'), 2);
            segmentSetToNumber.put(Set.of('a', 'c', 'd', 'f', 'g'), 3);
            segmentSetToNumber.put(Set.of('b', 'c', 'd', 'f'), 4);
            segmentSetToNumber.put(Set.of('a', 'b', 'd', 'f', 'g'), 5);
            segmentSetToNumber.put(Set.of('a', 'b', 'd', 'e', 'f', 'g'), 6);
            segmentSetToNumber.put(Set.of('a', 'c', 'f'), 7);
            segmentSetToNumber.put(Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g'), 8);
            segmentSetToNumber.put(Set.of('a', 'b', 'c', 'd', 'f', 'g'), 9);
        }
    }

    public long part1() {
        return getInputText().stream().map(
                line -> Arrays.stream(line.split("[|]")[1].trim().split(" "))
                        .map(s -> (s.length() == 2 || s.length() == 3 || s.length() == 4 || s.length() == 7) ? 1 : 0)
                        .reduce(0, Integer::sum)
        ).reduce(0, Integer::sum);
    }

    public long part2() {
        initialiseStaticData();

        var sum = 0L;
        for (var line : getInputText()) {
            var randomStrings = Arrays.stream(line.split("[|]")[0].trim().split(" ")).collect(Collectors.toList());
            var encodedDigits = Arrays.stream(line.split("[|]")[1].trim().split(" ")).collect(Collectors.toList());
            final var chars = new HashSet<Character>();
            for (var ch = 'a'; ch <= 'g'; ch++) {
                chars.add(ch);
            }

            // actual character to segment
            final var charToSegment = new HashMap<Character, Character>();
            // segment to actual character
            final var segmentToChar = new HashMap<Character, Character>();


            var strByLen = new HashMap<Integer, List<Set<Character>>>();

            for (var s : randomStrings) {
                var set = s.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
                var list = strByLen.getOrDefault(set.size(), new ArrayList<>());
                list.add(set);
                strByLen.put(set.size(), list);
            }

            // calculate a by subtracting the 2-segment from 3-segment string
            char a = calculateA(strByLen);
            charToSegment.put(a, 'a');
            segmentToChar.put('a', a);

            char g = calculateG(strByLen, a);
            charToSegment.put(g, 'g');
            segmentToChar.put('g', g);
            // known a, g

            char e = calculateE(chars, strByLen, a, g);
            charToSegment.put(e, 'e');
            segmentToChar.put('e', e);
            // known a, e, g

            char b = calculateB(strByLen, a, g, e);
            charToSegment.put(b, 'b');
            segmentToChar.put('b', b);
            // known a, b, e, g

            char d = calculateD(strByLen, a, g);
            charToSegment.put(d, 'd');
            segmentToChar.put('d', d);
            // known a, b, d, e, g

            char c = calculateC(strByLen, a, g, e, d);
            charToSegment.put(c, 'c');
            segmentToChar.put('c', c);
            // known a, b, c, d, e, g

            char f = calculateF(chars, a, g, e, b, d, c);
            charToSegment.put(f, 'f');
            segmentToChar.put('f', f);

            int number = 0;
            for (var encodedDigit : encodedDigits) {
                var segmentSet = encodedDigit.chars()
                        .mapToObj(i -> charToSegment.get((char) i))
                        .collect(Collectors.toSet());
                var digit = segmentSetToNumber.get(segmentSet);
                number *= 10;
                number += digit;
            }

            sum += number;
        }
        return sum;
    }

    private Character calculateF(HashSet<Character> chars, char a, char g, char e, char b, char d, char c) {
        var tmp = new HashSet<>(chars);
        tmp.remove(a);
        tmp.remove(b);
        tmp.remove(c);
        tmp.remove(d);
        tmp.remove(e);
        tmp.remove(g);
        assert tmp.size() == 1;
        var f = tmp.stream().findAny().get();
        return f;
    }

    private char calculateC(HashMap<Integer, List<Set<Character>>> strByLen, char a, char g, char e, char d) {
        var c = '?';
        for (var s : strByLen.get(5)) {
            var tmp = new HashSet<>(s);
            tmp.remove(a);
            tmp.remove(d);
            tmp.remove(e);
            tmp.remove(g);
            if (tmp.size() == 1) {
                c = tmp.stream().findAny().get();
                break;
            }
        }
        assert c != '?';
        return c;
    }

    private char calculateD(HashMap<Integer, List<Set<Character>>> strByLen, char a, char g) {
        var d = '?';
        for (var s : strByLen.get(5)) {
            var tmp = new HashSet<>(s);
            tmp.removeAll(strByLen.get(2).get(0));
            tmp.remove(a);
            tmp.remove(g);
            if (tmp.size() == 1) {
                d = tmp.stream().findAny().get();
                break;
            }
        }
        assert d != '?';
        return d;
    }

    private char calculateB(HashMap<Integer, List<Set<Character>>> strByLen, char a, char g, char e) {
        var b = '?';
        for (var s : strByLen.get(6)) {
            var tmp = new HashSet<>(s);
            tmp.removeAll(strByLen.get(2).get(0));
            tmp.remove(a);
            tmp.remove(e);
            tmp.remove(g);
            if (tmp.size() == 1) {
                b = tmp.stream().findAny().get();
                break;
            }
        }
        assert b != '?';
        return b;
    }

    private char calculateE(HashSet<Character> chars, HashMap<Integer, List<Set<Character>>> strByLen, char a, char g) {
        var setE = new HashSet<>(chars);
        setE.removeAll(strByLen.get(4).get(0));
        setE.remove(a);
        setE.remove(g);
        assert setE.size() == 1;
        var e = (char) setE.stream().findAny().get();
        return e;
    }

    private char calculateG(HashMap<Integer, List<Set<Character>>> strByLen, char a) {
        var g = '?';
        for (var s : strByLen.get(6)) {
            var tmp = new HashSet<>(s);
            tmp.removeAll(strByLen.get(4).get(0));
            tmp.remove(a);
            if (tmp.size() == 1) {
                g = tmp.stream().findAny().get();
                break;
            }
        }
        assert g != '?';
        return g;
    }

    private char calculateA(HashMap<Integer, List<Set<Character>>> strByLen) {
        var setA = strByLen.get(3).get(0);
        setA.removeAll(strByLen.get(2).get(0));

        var a = (char) setA.stream().findFirst().get();
        return a;
    }

}
