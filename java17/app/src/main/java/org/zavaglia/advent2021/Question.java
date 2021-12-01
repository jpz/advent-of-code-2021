package org.zavaglia.advent2021;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Question {
    private final int questionNumber_;

    public Question(int questionNumber) {
        this.questionNumber_ = questionNumber;
    }

    public int GetQuestionNumber() {
        return questionNumber_;
    }

    // get the List<String> of for the lines of question input
    public List<String> GetLines() throws IOException {
        return Files.readAllLines(
                Paths.get(String.format("data/q%02d.txt", questionNumber_)),
                Charset.defaultCharset());
    }

    // get the List<Integer> for the lines of question input
    public List<Integer> GetIntegers() throws IOException {
        return GetLines().stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    // calculate the result of the part A question
    public abstract long A() throws IOException;

    // the expected result of the part A question
    public abstract long ExpectedA();

    // calculate the result of the part B question
    public abstract long B() throws IOException;

    // the expected result of the part B question
    public abstract long ExpectedB();
}
