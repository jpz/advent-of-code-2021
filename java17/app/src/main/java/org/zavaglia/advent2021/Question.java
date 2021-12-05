package org.zavaglia.advent2021;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Question {
    private List<String> input_;

    public Question() {
    }

    // get the List<String> of for the lines of question input
    public List<String> getInputText() {
        return this.input_;
    }

    public void setInputText(List<String> input) {
        this.input_ = input;
    }

    // get the List<Integer> for the lines of question input
    public List<Integer> getIntegers() {
        return getInputText().stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    // calculate the result of the part 1
    public abstract long part1();

    // calculate the result of the part 2
    public abstract long part2();

}
