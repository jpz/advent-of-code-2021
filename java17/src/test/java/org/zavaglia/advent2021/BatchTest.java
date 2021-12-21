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

        var cons = questionClass.getConstructor();
        q_ = (Question) cons.newInstance();
        q_.setInputText(lines);
        answerPart1_ = answerPart1;
        answerPart2_ = answerPart2;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object> data() {
        return Arrays.asList(
                new Object[]{Question01.class, "q01sample", 7L, 5L},
                new Object[]{Question01.class, "q01", 1393L, 1359L},
                new Object[]{Question02.class, "q02", 1882980L, 1971232560L},
                new Object[]{Question03.class, "q03", 3969000L, 4267809L},
                new Object[]{Question04.class, "q04", 58374L, 11377L},
                new Object[]{Question05.class, "q05sample", 5L, 12L},
                new Object[]{Question05.class, "q05", 5373L, 21514L},
                new Object[]{Question06.class, "q06sample", 5934L, 26984457539L},
                new Object[]{Question06.class, "q06", 362346L, 1639643057051L},
                new Object[]{Question07.class, "q07sample", 37L, 168L},
                new Object[]{Question07.class, "q07", 351901L, 101079875L},
                new Object[]{Question08.class, "q08minisample", 0L, 5353L},
                new Object[]{Question08.class, "q08sample", 26L, 61229L},
                new Object[]{Question08.class, "q08", 495L, 1055164L},
                new Object[]{Question09.class, "q09sample", 15L, 1134L},
                new Object[]{Question09.class, "q09", 594L, 858494L},
                new Object[]{Question10.class, "q10sample", 26397L, 288957L},
                new Object[]{Question10.class, "q10", 399153L, 2995077699L},
                new Object[]{Question11.class, "q11sample", 1656L, 195L},
                new Object[]{Question11.class, "q11", 1659L, 227L},
                new Object[]{Question12.class, "q12sample1", 10L, 36L},
                new Object[]{Question12.class, "q12sample2", 19L, 103L},
                new Object[]{Question12.class, "q12sample3", 226L, 3509L},
                new Object[]{Question12.class, "q12", 4495L, 131254L},
                new Object[]{Question13.class, "q13sample", 17L, 16L},
                new Object[]{Question13.class, "q13", 810L, 103L},
                new Object[]{Question14.class, "q14sample", 1588L, 2188189693529L},
                new Object[]{Question14.class, "q14", 2712L, 8336623059567L},
                new Object[]{Question15.class, "q15sample", 40L, 315L},
                new Object[]{Question15.class, "q15", 687L, 2957L},
                new Object[]{Question16.class, "q16", 940L, 13476220616073L},
                new Object[]{Question17.class, "q17sample", 45L, 112L},
                new Object[]{Question17.class, "q17", 10585L, 5247L},
                new Object[]{Question18.class, "q18sample", 4140L, 3993L},
                new Object[]{Question18.class, "q18", 3725L, 4832L},
                new Object[]{Question19.class, "q19sample", 79L, 3621L},
                new Object[]{Question19.class, "q19", 326L, 10630L},
                new Object[]{Question20.class, "q20sample", 35L, 3351L},
                new Object[]{Question20.class, "q20", 5275L, 16482L},
                new Object[]{Question21.class, "q21sample", 739785L, 444356092776315L},
                new Object[]{Question21.class, "q21", 752745L, 309196008717909L}
        );
    }

    @Test
    public void testPart1() {
        assertEquals(answerPart1_, q_.part1());
    }

    @Test
    public void testPart2() {
        assertEquals(answerPart2_, q_.part2());
    }


}