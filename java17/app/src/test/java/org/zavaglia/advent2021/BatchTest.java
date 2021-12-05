package org.zavaglia.advent2021;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BatchTest extends TestCase {

    Question q_;
    String dataset_;
    long answerPart1_;
    long answerPart2_;

    public BatchTest(Class questionClass, String dataset, Long answerPart1, Long answerPart2) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        dataset_ = dataset;
        var lines = Files.readAllLines(
                Paths.get(String.format("data/%s.txt", dataset_)),
                Charset.defaultCharset());

        var cons = questionClass.getConstructor(null);
        q_ = (Question) cons.newInstance();
        q_.setInputText(lines);
        answerPart1_ = answerPart1;
        answerPart2_ = answerPart2;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object> data() throws IOException {
        return Arrays.asList(
                new Object[]{Question01.class, "q01sample", 7L, 5L},
                new Object[]{Question01.class, "q01", 1393L, 1359L},
                new Object[]{Question02.class, "q02", 1882980L, 1971232560L},
                new Object[]{Question03.class, "q03", 3969000L, 4267809L},
                new Object[]{Question04.class, "q04", 58374L, 11377L},
                new Object[]{Question05.class, "q05sample", 5L, 12L},
                new Object[]{Question05.class, "q05", 5373L, 21514L}
        );
    }

    @Test
    public void testPart1() throws IOException {
        assertEquals(q_.part1(), answerPart1_);
    }

    @Test
    public void testPart2() throws IOException {
        assertEquals(q_.part2(), answerPart2_);
    }


}