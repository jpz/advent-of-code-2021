package org.zavaglia.advent2021;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;


public class Question10 extends Question {

    public long part1() {

        var sumError = 0;
        for (var line : getInputText()) {
            var s = new Stack<Character>();
            for (var ch : line.toCharArray()) {
                if ("([{<".indexOf(ch) != -1) {
                    s.push(ch);
                } else {
                    var ch0 = (char) s.pop();
                    if (ch0 == '(' && ch == ')' ||
                            ch0 == '[' && ch == ']' ||
                            ch0 == '{' && ch == '}' ||
                            ch0 == '<' && ch == '>') {
                        continue;
                    }
                    if (ch == ')')
                        sumError += 3;
                    if (ch == ']')
                        sumError += 57;
                    if (ch == '}')
                        sumError += 1197;
                    if (ch == '>')
                        sumError += 25137;
                    break;
                }
            }
        }
        return sumError;
    }

    public long part2() {
        var errors = new ArrayList<Long>();
        for (var line : getInputText()) {
            var s = new ArrayList<Character>();
            var error = false;
            for (var ch : line.toCharArray()) {
                if ("([{<".indexOf(ch) != -1) {
                    s.add(ch);
                } else {
                    var ch0 = (char) s.remove(s.size() - 1);
                    if (ch0 == '(' && ch == ')' ||
                            ch0 == '[' && ch == ']' ||
                            ch0 == '{' && ch == '}' ||
                            ch0 == '<' && ch == '>') {
                        continue;
                    }
                    error = true;
                    break;
                }
            }
            if (!error) {
                var accum = 0L;
                Collections.reverse(s);
                for (var ch : s) {
                    accum *= 5;
                    if (ch == '(')
                        accum += 1;
                    if (ch == '[')
                        accum += 2;
                    if (ch == '{')
                        accum += 3;
                    if (ch == '<')
                        accum += 4;
                }
                errors.add(accum);
            }
        }
        errors.sort(Long::compare);

        return errors.get(errors.size() / 2);
    }
}
